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
	private boolean averageFeatureVectors = true;
	
	/**
	 * Whether to reduce the feature space.
	 */
	private boolean reduceFeatureSpace = false;
	
	/**
	 * The value to reduce the number of all gram-based features to.
	 */
	private static int featureReductionThreshold = 50;
	
	/**
	 * Local logger
	 */
	protected static MultiplePrintStream log = new MultiplePrintStream();
	
	
	/* ============
	 * Constructors
	 * ============
	 */
	
	/**
	 * Default constructor for WriteprintsAnalyzer.
	 */
	public WriteprintsAnalyzer() {
		// default constructor
	}
	
	/**
	 * Constructor for WriteprintsAnalyzer.
	 * @param averageFeatureVectors
	 * 		Whether to average all feature vectors into one. Increases performance.
	 * @param reduceFeatureSpace
	 * 		Whether to apply feature space reduction for large feature classes (e.g.
	 * 		word bigrams) by information gain.
	 */
	public WriteprintsAnalyzer(boolean averageFeatureVectors,
			boolean reduceFeatureSpace) {
		this.averageFeatureVectors = averageFeatureVectors;
		this.reduceFeatureSpace = reduceFeatureSpace;
	}
	
	
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
		
		// reduce feature space by info-gain
		if (reduceFeatureSpace)
			reduceFeatures(trainingSet, testSet);
		double[] IG = null;
		// calculate information-gain over only training authors
		// (after reduction)
		log.println("Calculating information gain over training authors data");
		int numFeatures = trainingSet.numAttributes() - 1;
		try {
			IG = calcInfoGain(trainingSet, numFeatures);
		} catch (Exception e) {
			System.err.println("Error evaluating information gain.");
			e.printStackTrace();
			return null;
		}

		trainAuthorData.clear();
		testAuthorData.clear();
		
		// initialize features, basis and writeprint matrices
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		String authorName;
		AuthorWPData authorData;
		int authorsInRow = 5;
		// training set
		log.println("Initializing training authors data:");
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			authorData = new AuthorWPData(authorName);
			log.print(authorName + "  ");
			authorData.initFeatureMatrix(trainingSet, averageFeatureVectors);
			trainAuthorData.add(authorData);
			authorData.initBasisAndWriteprintMatrices();
			if ((i + 1) % authorsInRow == 0)
				log.println();
		}
		log.println();
		// test set
		int numTestInstances = testSet.numInstances();
		// train-test mode
		if (unknownDocs != null) {
			log.println("Initializing test authors data (author per test document):");
			for (int i = 0; i < numTestInstances; i++) {
				authorName =
						TEST_AUTHOR_NAME_PREFIX +
						String.format("%03d", i) + "_" +
						unknownDocs.get(i).getTitle();
				authorData = new AuthorWPData(authorName);
				log.print(authorName + "  ");
				authorData.initFeatureMatrix(testSet, i, averageFeatureVectors);
				testAuthorData.add(authorData);				
				authorData.initBasisAndWriteprintMatrices();
				if ((i + 1) % authorsInRow == 0)
					log.println();
			}
		}
		// CV mode
		else {
			log.println("Initializing test authors data (CV mode):");
			for (int i = 0; i < numAuthors; i++) {
				authorName = classAttribute.value(i);
				authorData = new AuthorWPData(authorName);
				log.print(authorName + "  ");
				authorData.initFeatureMatrix(testSet, averageFeatureVectors);
				testAuthorData.add(authorData);
				authorData.initBasisAndWriteprintMatrices();
				if ((i + 1) % authorsInRow == 0)
					log.println();
			}
		}
		log.println();
		
		// initialize result set
		results = new HashMap<String,Map<String,Double>>(trainAuthorData.size());
		
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
		int count = 0;
		authorsInRow = 12;
		for (AuthorWPData testData: testAuthorData) {
			Map<String,Double> testRes = new HashMap<String,Double>();
			log.print(testData.authorName + "  ");
			count++;
			if (count % authorsInRow == 0)
				log.println();
			for (AuthorWPData trainData: trainAuthorData) {
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
				
				// save the inverse to maintain the smallest distance as the best fit
				totalDist = - (dist1 + dist2);
				testRes.put(trainData.authorName, totalDist);
				//log.println("- " + trainData.authorName + ": " + totalDist);
			}
			results.put(testData.authorName,testRes);
		}
		log.println(">>> classify finished");
		return results;
	}

	@Override
	public String runCrossValidation(Instances data, int folds,
			long randSeed) {
		log.println(">>> runCrossValidation started");
		
		// setup
		data.setClass(data.attribute("authorName"));
		Instances randData = new Instances(data);
		Random rand = new Random(randSeed);
		randData.randomize(rand);
		randData.stratify(folds);
		
		// prepare folds
		Instances[] foldData = new Instances[folds];
		for (int i = 0; i < folds; i ++)
			foldData[i] = randData.testCV(folds, i);
		int half = (folds / 2) + (folds % 2);
		
		// run CV - use half the folds for training, half for testing
		// E.g. for 10 folds, use 1-5 for training, 6-10 for testing; 2-6 for training, 1 + 7-10 for testing, etc.
		Instances train, test;
		Instances tmp;
		int tmpSize;
		Map<String,Map<String,Double>> results;
		Map<String,Double> instResults;
		double success;
		double total = 0;
		double max;
		String selected;
		for (int i = 0; i < folds; i ++) {
			log.println("Running experiment " + (i + 1) + " out of " + folds);
			
			// initialize
			train = new Instances(data,0);
			test = new Instances(data,0);
			
			// prepare training set
			for (int j = i; j < i + half; j++) {
				tmp = foldData[j % folds];
				tmpSize = tmp.numInstances();
				for (int k = 0; k < tmpSize; k++)
					train.add(tmp.instance(k));
			}
			// prepare test set
			for (int j = i + half; j < i + folds; j++) {
				tmp = foldData[j % folds];
				tmpSize = tmp.numInstances();
				for (int k = 0; k < tmpSize; k++)
					test.add(tmp.instance(k));
			}
			
			// classify
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
				if (testInstAuthor.equals(selected))
					success++;
			}
			success = 100 * success / results.size();
			log.printf("- Accuracy for experiment %d: %.2f\n", (i + 1), success);
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
	
	/**
	 * Used to identify features that should be
	 * reduced by info-gain
	 */
	private static String[] toReduceFeatures = {
		"Top-Letter-bigrams",
		"Top-Letter-trigrams",
		"Two-Digit-Numbers",
		"Three-Digit-Numbers",
		"Function-Words",
		"POS-Bigrams",
		"POS-Trigrams",
		"Words",
		"Word-Bigrams",
		"Word-Trigrams",
		"Misspelled-Words"
	};
	
	/**
	 * Reduces the feature space by removing the lowest information-gain n-gram
	 * features (e.g. POS bigrams, word trigrams etc.) and misspelled words,
	 * as defined in <code>toReduceFeatures</code>.<br>
	 * The reduction is based on the information gain extracted from the given
	 * training set only. 
	 * @param train
	 * @param test
	 */
	private static void reduceFeatures(Instances train, Instances test) {
		log.println("Reducing feature space");
		log.println("Number of attributes before: " + train.numAttributes());
		// calculate information-gain over only training authors
		log.println("Calculating information gain over training authors data");
		double[] IG = null;
		int numFeatures = train.numAttributes() - 1;
		try {
			IG = calcInfoGain(train, numFeatures);
		} catch (Exception e) {
			System.err.println("Error evaluating information gain.");
			e.printStackTrace();
			return;
		}
		
		// find feature indices ranges
		Map<String,int[]> featureRanges =
				new HashMap<String,int[]>(toReduceFeatures.length);
		int[] range;
		boolean foundFirst, foundSecond;
		for (String feature: toReduceFeatures) {
			foundFirst = false;
			foundSecond = false;
			range = new int[2];
			for (int i = 0; i < numFeatures; i++) {
				if (train.attribute(i).name().startsWith(feature)) {
					if (!foundFirst) {
						range[0] = i;
						foundFirst = true;
					}
				}
				else {
					if (foundFirst) {
						range[1] = i - 1;
						foundSecond = true;
						break;
					}
				}
			}
			if (!foundSecond)
				range[1] = numFeatures - 1;
			log.println(feature + ": [" + range[0] + "," + range[1] + "]");
			if (range[1] - range[0] + 1 > featureReductionThreshold)
				featureRanges.put(feature, range);
		}
		
		// mark indices of features to remove
		Comparator<double[]> IGcomp = new Comparator<double[]>() {
			@Override
			public int compare(double[] arg0, double[] arg1) {
				double diff = arg0[1] - arg1[1];
				if (diff < 0) return -1;
				if (diff > 0) return 1;
				return 0;
			}
		};
		List<Integer> indicesToRemove = new ArrayList<Integer>();
		double[][] featureIndicesIG;
		int featureIGLen;
		int count;
		log.println("calculating features to be removed");
		for (String feature: toReduceFeatures) {
			range = featureRanges.get(feature);
			if (range == null)
				continue;
			log.print(feature + ": ");
			featureIGLen = range[1] - range[0] + 1;
			featureIndicesIG = new double[featureIGLen][2];
			log.print("initializing... ");
			for (int i = 0; i < featureIGLen; i++) {
				featureIndicesIG[i][0] = i;
				featureIndicesIG[i][1] = IG[i + range[0]];
			}
			log.print("sorting... ");
			Arrays.sort(featureIndicesIG, IGcomp);
			log.print("adding feature indices to be removed... ");
			count = 0;
			for (int i = featureReductionThreshold; i < featureIGLen; i++) {
				indicesToRemove.add((int) featureIndicesIG[i][0]);
				count++;
			}
			log.println("done! total features to remove: " + count);
		}
		log.print("sorting all features to be removed by indices... ");
		Collections.sort(indicesToRemove);
		log.println("done!");
		
		// remove selected features
		int indexToRemove;
		log.println("removing selected features");
		count = 0;
		for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
			indexToRemove = indicesToRemove.get(i);
			train.deleteAttributeAt(indexToRemove);
			test.deleteAttributeAt(indexToRemove);
			count++;
			if (count % 1000 == 0)
				log.println("removed " +count);
		}
		log.println("Number of attributes after: " + train.numAttributes());
	}
	
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
		log.println("Calculating synonym count for word-based features:");
		
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
		/*
		log.println("Total comparisons for distance: " +
				numACols + " x " + numBCols + " = " + total);
		*/
		return sum;
	}
	
	/**
	 * Setter for the local logger.
	 * @param log
	 * 		The logger to set to.
	 */
	public static void setLogger(MultiplePrintStream log) {
		WriteprintsAnalyzer.log = log;
	}
	
	/**
	 * Getter for the local logger.
	 * @return
	 * 		The local logger.
	 */
	public static MultiplePrintStream getLogger() {
		return log;
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
		
		//ProblemSet ps = new ProblemSet(JSANConstants.JSAN_PROBLEMSETS_PREFIX + "drexel_1_train_test.xml");
		//ProblemSet ps = new ProblemSet(JSANConstants.JSAN_PROBLEMSETS_PREFIX + "drexel_1.xml");
		//ProblemSet ps = new ProblemSet(JSANConstants.JSAN_PROBLEMSETS_PREFIX + "amt.xml");
		
		/*
		CumulativeFeatureDriver cfd =
				//new CumulativeFeatureDriver(JSANConstants.JSAN_FEATURESETS_PREFIX + "writeprints_feature_set_limited.xml");
				new CumulativeFeatureDriver(JSANConstants.JSAN_FEATURESETS_PREFIX + "writeprints_feature_set.xml");
		WekaInstancesBuilder wib = new WekaInstancesBuilder(false);
		List<Document> trainingDocs = ps.getAllTrainDocs();
		List<Document> testDocs = ps.getTestDocs();
		int numTrainDocs = trainingDocs.size();
		int numTestDocs = testDocs.size();
		
		// extract features
		System.out.println("feature extraction");
		wib.prepareTrainingSet(trainingDocs, cfd);
		System.out.println("feature post extraction");
		Instances trainingSet = wib.getTrainingSet();
		System.out.println("done!");
		
		Instances train = wib.getTrainingSet();
		Instances test = wib.getTestSet();
		WekaInstancesBuilder.writeSetToARFF("d:/tmp/drexel_1_all_train.arff", train);
		//WekaInstancesBuilder.writeSetToARFF("d:/tmp/drexel_1_tt_test.arff", test);
		System.exit(0);
		*/
		
		Instances train = new Instances(new FileReader(new File("d:/tmp/amt_limited_100_train.arff")));
		train.setClassIndex(train.numAttributes() - 1);
		//Instances test = new Instances(new FileReader(new File("d:/tmp/drexel_1_tt_test.arff")));
		//test.setClassIndex(test.numAttributes() - 1);
		
		// cross-validation
		wa.runCrossValidation(train, 10, 0);
	}
}
