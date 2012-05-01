package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

import Jama.Matrix;

import com.jgaap.JGAAPConstants;
import com.jgaap.generics.*;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.*;
import weka.core.Attribute;
import weka.core.Instances;
import edu.drexel.psal.jstylo.generics.*;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * Implementation of the Writeprints method (supervised).
 * For more details see:<br>
 * Abbasi, A., Chen, H. (2008). Writeprints: A stylometric approach to identity-level identification
 * and similarity detection in cyberspace. ACM Trans. Inf. Syst., 26(2), 129.
 * 
 * @author Ariel Stolerman
 *
 */
public class WriteprintsAnalyzer extends Analyzer {
	
	/* ======
	 * fields
	 * ======
	 */
	
	/**
	 * The prefix given to any author of a test document.
	 */
	public static final String TEST_AUTHOR_NAME_PREFIX = "_test_";
	
	/**
	 * The list of training author data, including feature, basis and writeprint matrices.
	 */
	private List<AuthorWPData> trainAuthorData = new ArrayList<AuthorWPData>();
	
	/**
	 * The list of training author data, including feature, basis and writeprint matrices.
	 */
	private List<AuthorWPData> testAuthorData = new ArrayList<AuthorWPData>();
	
	/**
	 * Whether to average all feature vectors per author ending up with one feature vector
	 * or not. Increases performance but may reduce accuracy.
	 */
	private boolean averageFeatureVectors = false;
	
	/**
	 * Local logger
	 */
	protected static MultiplePrintStream log = new MultiplePrintStream();
	
	/* ==========
	 * operations
	 * ==========
	 */
	
	@Override
	public Map<String,Map<String, Double>> classify(Instances trainingSet,
			Instances testSet, List<Document> unknownDocs) {
		log.println(">>> classify started");
		
		/* ========
		 * LEARNING
		 * ========
		 */
		log.println("> Learning");
		
		// initialize features, basis and writeprint matrices
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		String authorName;
		AuthorWPData authorData;
		// training set
		log.println("Initializing training authors data:");
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			authorData = new AuthorWPData(authorName);
			log.println("- " + authorName);
			authorData.initFeatureMatrix(trainingSet, averageFeatureVectors);
			trainAuthorData.add(authorData);
			authorData.initBasisAndWriteprintMatrices();
		}
		// test set
		int numTestInstances = testSet.numInstances();
		log.println("Initializing test authors data (author per test document):");
		for (int i = 0; i < numTestInstances; i++) {
			authorName = TEST_AUTHOR_NAME_PREFIX +
					String.format("%03d", i) + "_" +
					(unknownDocs == null ?
					testSet.instance(i).stringValue(classAttribute) :
					unknownDocs.get(i).getTitle());
			authorData = new AuthorWPData(authorName);
			log.println("- " + authorName);
			authorData.initFeatureMatrix(testSet, i, averageFeatureVectors);
			testAuthorData.add(authorData);				
			authorData.initBasisAndWriteprintMatrices();
		}
		
		// initialize result set
		results = new HashMap<String,Map<String,Double>>(trainAuthorData.size());
		
		// calculate information-gain over only training authors
		log.println("Calculating information gain over training authors data");
		double[] IG = null;
		int numFeatures = trainingSet.numAttributes() - 1;
		try {
			IG = calcInfoGain(trainingSet, numFeatures);
		} catch (Exception e) {
			System.err.println("Error evaluating information gain.");
			e.printStackTrace();
			return null;
		}
		
		// initialize synonym count mapping
		log.println("Initializing word synonym count");
		Map<Integer,Integer> wordsSynCount = calcSynonymCount(trainingSet,numFeatures);
		
		
		/* =======
		 * TESTING
		 * =======
		 */
		log.println("> Testing");
		
		Matrix testPattern, trainPattern;
		double dist1, dist2, totalDist;
		AuthorWPData testDataCopy, trainDataCopy;
		for (AuthorWPData testData: testAuthorData) {
			Map<String,Double> testRes = new HashMap<String,Double>();
			log.println("Test author: " + testData.authorName + ":");
			for (AuthorWPData trainData: trainAuthorData) {
				// initialize zero-frequency features
				//testData.initBasisAndWriteprintMatrix();
				//trainData.initBasisAndWriteprintMatrix();
				
				testDataCopy = testData.halfClone();
				trainDataCopy = trainData.halfClone();
				
				// compute pattern matrices BEFORE adding pattern disruption
				testPattern = AuthorWPData.generatePattern(trainData, testData);
				trainPattern = AuthorWPData.generatePattern(testData, trainData);
				
				// add pattern disruptions
				testDataCopy.addPatternDisruption(trainData, IG, wordsSynCount, trainPattern);
				trainDataCopy.addPatternDisruption(testData, IG, wordsSynCount, testPattern);
				
				// compute pattern matrices AFTER adding pattern disruption
				testPattern = AuthorWPData.generatePattern(trainDataCopy, testDataCopy);
				trainPattern = AuthorWPData.generatePattern(testDataCopy, trainDataCopy);
				
				// compute distances
				dist1 = sumEuclideanDistance(testPattern, trainDataCopy.writeprint);
				dist2 = sumEuclideanDistance(trainPattern, testDataCopy.writeprint);
				totalDist = - (dist1 + dist2);
				// save the inverse to maintain the smallest distance as the best fit
				testRes.put(trainData.authorName, totalDist);
				log.println("- " + trainData.authorName + ": " + totalDist);
			}
			results.put(testData.authorName,testRes);
		}
		log.println(">>> classify finished");
		return results;
	}

	@Override
	public Evaluation runCrossValidation(Instances data, int folds,
			long randSeed) {
		log.println(">>> runCrossValidation started");
		
		// setup
		data.setClass(data.attribute("authorName"));
		Instances randData = new Instances(data);
		Random rand = new Random(randSeed);
		randData.randomize(rand);
		randData.stratify(folds);

		// run CV
		Map<String,Map<String,Double>> results;
		Map<String,Double> instResults;
		double success;
		double total = 0;
		double max;
		String selected;
		for (int n = 0; n < folds; n++) {
			log.println("Running fold " + (n + 1) + " out of " + folds);
			Instances train = randData.trainCV(folds, n);
			Instances test = randData.testCV(folds, n);
			// build and evaluate classifier
			results = classify(train, test, null);
			success = 0;
			selected = null;
			for (String testInstAuthor: results.keySet()) {
				max = Double.NEGATIVE_INFINITY;
				instResults = results.get(testInstAuthor);
				for (String key: instResults.keySet()) {
					if (max < instResults.get(key)) {
						max = instResults.get(key);
						selected = key;
					}
				}
				log.println(testInstAuthor + ": " + selected);
				if (testInstAuthor.replaceFirst(TEST_AUTHOR_NAME_PREFIX + "\\d+_", "").equals(selected))
					success++;
			}
			success = 100 * success / results.size();
			log.printf("- Accuracy for fold %d: %.2f\n", n, success);
			total += success;
		}
		total /= folds;
		log.println("========================");
		log.printf("Total Accuracy: %.2f\n", total);
		
		log.println(">>> runCrossValidation finished");
		return null;
	}
	
	
	/* ===============
	 * utility methods
	 * ===============
	 */
	
	private static WordNetDatabase wndb = null;
	
	/**
	 * Initializes the Wordnet database.
	 * @throws IOException 
	 */
	private static void initWordnetDB() {
		URL url = Thread.currentThread().getClass().getResource(
				JGAAPConstants.JGAAP_RESOURCE_PACKAGE+"wordnet");
		System.setProperty("wordnet.database.dir", url.getPath());
		wndb = WordNetDatabase.getFileInstance();
	}

	/**
	 * Used to identify word-based features.
	 */
	private static String[] wordFeatures = {
		"Function-Words",
		"Words",
		"Word-Bigrams",
		"Word-Trigrams",
		"Misspelled-Words"
	};
	
	/**
	 * Constructs a mapping from all word-based feature indices to the number of their synonyms.
	 * The synonym counted are only those belonging to synsets of the most common part-of-speech
	 * synset-type. If the feature is an n-gram feature, the synonym count is the multiplication
	 * of synonym count values of each word in the n-gram.
	 * @param trainingSet
	 * 		The training set from which to extract the features.
	 * @param numFeatures
	 * 		The number of features.
	 * @return
	 * 		A mapping from the word feature indices of the given training set to the synonym count.
	 */
	private static Map<Integer,Integer> calcSynonymCount(Instances trainingSet, int numFeatures) {
		
		// initialize
		Map<Integer,Integer> synCountMap = new HashMap<Integer,Integer>(numFeatures);
		if (wndb == null)
			initWordnetDB();
		
		boolean isWordFeature;
		Attribute feature;
		String featureName;
		String[] words;
		int synCount;
		Synset[] synsets, tmpSynsets;
		SynsetType[] allTypes = SynsetType.ALL_TYPES;
		Set<String> synonyms;
		for (int j = 0; j < numFeatures; j ++) {
			feature = trainingSet.attribute(j);
			featureName = feature.name();
			
			// check whether it is a word feature, else continue
			isWordFeature = false;
			for (String wordFeature: wordFeatures)
				if (featureName.startsWith(wordFeature)) {
					isWordFeature = true;
					break;
				}
			if (!isWordFeature)
				continue;
			
			// find synonym count for all word features
			// multiply synonym-count for n-gram features
			synCount = 1;
			words = getWordsFromFeatureName(featureName);
			for (String word: words) {
				// find the SynsetType with the maximum number of synsets
				synsets = wndb.getSynsets(word, allTypes[0]);
				for (int i = 1; i < allTypes.length; i++) {
					tmpSynsets = wndb.getSynsets(word, allTypes[i]);
					if (tmpSynsets.length > synsets.length)
						synsets = tmpSynsets;
				}

				// count synonyms
				synonyms = new HashSet<String>();
				for (Synset synset: synsets)
					synonyms.addAll(Arrays.asList(synset.getWordForms()));
				if (!synonyms.isEmpty())
					synCount *= synonyms.size();
			}
			synCountMap.put(j, synCount);
		}

		return synCountMap;
	}
	
	/**
	 * Extracts the words from the given feature name and returns them in an
	 * array.
	 * @param featureName
	 * 		The feature name, of the form <code>FEATURE-TYPE-{WORDS}</code>.
	 * @return
	 */
	private static String[] getWordsFromFeatureName(String featureName) {
		String content = featureName.replaceAll(".*\\{", "").replace("}", "");
		if (!featureName.contains("grams"))
			return new String[]{content};
		else {
			content = content.substring(1, content.length() - 1);
			return content.split("\\)-\\(");
		}
	}
	
	/**
	 * Calculates and returns the information gain vector for all features
	 * based on the given training set.
	 * @param trainingSet
	 * 		The training set to calculate information gain on.
	 * @param numFeatures
	 * 		The number of features.
	 * @return
	 * 		The information gain vector for all features based on the given
	 * 		training set.
	 * @throws Exception
	 * 		If an error is encountered during information gain evaluation.
	 */
	private static double[] calcInfoGain(Instances trainingSet, int numFeatures) throws Exception {		
		InfoGainAttributeEval ig = new InfoGainAttributeEval();
		ig.buildEvaluator(trainingSet);
		double[] IG = new double[numFeatures];
		for (int j = 0; j < numFeatures; j++)
			IG[j] = ig.evaluateAttribute(j);
		return IG;
	}
	
	/**
	 * Returns the average of the Euclidean distance between every 
	 * pair of columns (corresponding to document feature values)
	 * of the given matrices.
	 * @param a
	 * 		The first matrix.
	 * @param b
	 * 		The second matrix.
	 * @return
	 */
	private static double sumEuclideanDistance(Matrix a, Matrix b) {
		double sum = 0;
		double colsDiff, tmp;
		int numACols = a.getColumnDimension();
		int numBCols = b.getColumnDimension();
		int total = numACols * numBCols;
		int numFeatures = a.getRowDimension();
		for (int i = 0; i < numACols; i++) {
			for (int j = 0; j < numBCols; j++) {
				colsDiff = 0;
				for (int k = 0; k < numFeatures; k++) {
					tmp = a.get(k,i) - b.get(k, j);
					colsDiff += tmp * tmp;
				}
				sum += Math.sqrt(colsDiff) / total;
			}
		}
		return sum;
	}
	
	
	// ============================================================================================
	// ============================================================================================
	
	
	/**
	 * Main for testing.
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// initialize log
		PrintStream logPS = new PrintStream(new File("./log/" + MultiplePrintStream.getLogFilename()));
		log = new MultiplePrintStream(System.out, logPS);
		
		WriteprintsAnalyzer wa = new WriteprintsAnalyzer();
		/*
		ProblemSet ps = new ProblemSet(JSANConstants.JSAN_PROBLEMSETS_PREFIX + "drexel_1.xml");
		//ProblemSet ps = new ProblemSet(JSANConstants.JSAN_PROBLEMSETS_PREFIX + "amt.xml");
		CumulativeFeatureDriver cfd =
				new CumulativeFeatureDriver(JSANConstants.JSAN_FEATURESETS_PREFIX + "writeprints_feature_set_limited.xml");
		WekaInstancesBuilder wib = new WekaInstancesBuilder(false);
		List<Document> trainingDocs = ps.getAllTrainDocs();
		List<Document> testDocs = ps.getTestDocs();
		int numTrainDocs = trainingDocs.size();
		int numTestDocs = testDocs.size();
		
		// extract features
		System.out.println("feature pre extraction");
		trainingDocs.addAll(testDocs);
		System.out.println("feature extraction");
		wib.prepareTrainingSet(trainingDocs, cfd);
		System.out.println("feature post extraction");
		Instances trainingSet = wib.getTrainingSet();
		Instances testSet = new Instances(
				trainingSet,
				numTrainDocs,
				numTestDocs);
		wib.setTestSet(testSet);
		int total = numTrainDocs + numTestDocs;
		for (int i = total - 1; i >= numTrainDocs; i--)
			trainingSet.delete(i);
		System.out.println("done!");
		
		Instances train = wib.getTrainingSet();
		Instances test = wib.getTestSet();
		WekaInstancesBuilder.writeSetToARFF("d:/tmp/drexel_1_train.arff", train);
		System.exit(0);
		*/
		Instances train = new Instances(new FileReader(new File("d:/tmp/drexel_1_train.arff")));
		
		// classify
		/*
		System.out.println("classification");
		Map<String,Map<String, Double>> res = wa.classify(train, test, ps.getTestDocs());
		System.out.println("done!");
		Map<String,Double> docMap;
		String selectedAuthor = null;
		double maxValue;
		double success = 0;
		for (String doc: res.keySet()) {
			maxValue = Double.NEGATIVE_INFINITY;
			docMap = res.get(doc);
			System.out.println(doc+":");
			for (String key: docMap.keySet())
				if (maxValue < docMap.get(key)) {
					selectedAuthor = key;
					maxValue = docMap.get(key);
				}
			System.out.println("- "+selectedAuthor+": "+maxValue);
			success += doc.replaceFirst(TEST_AUTHOR_NAME_PREFIX, "").startsWith(selectedAuthor) ? 1 : 0;
		}
		success = 100 * success / res.size();
		System.out.printf("Total accuracy: %.2f\n",success);
		*/
		
		// cross-validation
		wa.runCrossValidation(train, 10, 0);
	}
}
