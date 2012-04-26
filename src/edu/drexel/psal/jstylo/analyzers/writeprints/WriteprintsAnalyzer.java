package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import Jama.Matrix;

import com.jgaap.JGAAPConstants;
import com.jgaap.generics.*;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.*;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.drexel.psal.JSANConstants;
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
	 * all not. Increases performance but may reduce accuracy.
	 */
	private boolean averageFeatureVectors = true;
	
	/**
	 * Indicates whether pre-processing has changed the problem-set.
	 */
	private static boolean testDocsPreProcessed = false;
	
	/* ==========
	 * operations
	 * ==========
	 */
	
	/**
	 * Feature extraction pre-processing for the Writeprints analyzer.
	 * Places all test documents as training documents under temporary author names
	 * based on the document titles.
	 * Necessary for the test document feature extraction to be independent of the
	 * training set features, as applied in {@link WekaInstancesBuilder}.
	 */
	public static void preExtraction(ProblemSet ps) {
		if (ps.hasTestDocs()) {
			testDocsPreProcessed = true;
			List<Document> testDocs = ps.getTestDocs();
			int numTestDocs = testDocs.size();
			Document doc;
			String authorName;
			for (int i = numTestDocs - 1; i >= 0; i--) {
				doc = testDocs.remove(i);
				authorName = TEST_AUTHOR_NAME_PREFIX + doc.getTitle().replaceAll("\\.\\S+$","");
				doc.setAuthor(authorName);
				ps.addTrainDoc(authorName, doc);
			}
		}
	}
	
	public static void postExtraction(WekaInstancesBuilder wib) {
		// skip if no test documents were pre-processed
		if (!testDocsPreProcessed)
			return;
		
		// move all test instances to the test set
		Instances trainingSet = wib.getTrainingSet();
		Instances testSet = new Instances(trainingSet,0);
		testSet.setClassIndex(trainingSet.classIndex());
		int numInstances = trainingSet.numInstances();
		Attribute classAttribute = trainingSet.classAttribute();
		for (int i = numInstances - 1; i >= 0; i--)
			if (classAttribute.value((int)trainingSet.instance(i).
					classValue()).startsWith(TEST_AUTHOR_NAME_PREFIX)) {
				testSet.add(trainingSet.instance(i));
				trainingSet.delete(i);
			}
		wib.setTestSet(testSet);
	}
	
	@Override
	public Map<String,Map<String, Double>> classify(Instances trainingSet,
			Instances testSet, List<Document> unknownDocs) {
		
		/* ========
		 * TRAINING
		 * ========
		 */
		
		// initialize features, basis and writeprint matrices
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		String authorName;
		AuthorWPData authorData;
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			authorData = new AuthorWPData(authorName);
			//authorData.initFeatureProbabilities();
			if (authorName.startsWith(TEST_AUTHOR_NAME_PREFIX)) {
				authorData.initFeatureMatrix(testSet, averageFeatureVectors);
				testAuthorData.add(authorData);				
			}
			else {
				authorData.initFeatureMatrix(trainingSet, averageFeatureVectors);
				trainAuthorData.add(authorData);
			}
			authorData.initBasisAndWriteprintMatrices();
		}
		
		// initialize the posterior probability p(c|j)
		// for the training authors only
		/*
		double[] totalProbability = new double[numFeatures];
		for (int j = 0; j < numFeatures; j++)
			for (AuthorWPData ad: trainAuthorData)
				totalProbability[j] += ad.featureProbabilities[j];
		for (AuthorWPData ad: trainAuthorData)
			ad.initPosteriorProbabilities(totalProbability);
		
		// calculate information-gain
		double[] IG = calcInfoGain(numFeatures, trainAuthorData);
		*/
		
		// initialize result set
		results = new HashMap<String,Map<String,Double>>(trainAuthorData.size());
		
		// calculate information-gain over only the training authors set
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
		Map<Integer,Integer> wordsSynCount = calcSynonymCount(trainingSet,numFeatures);
		
		
		/* =======
		 * TESTING
		 * =======
		 */
		
		Matrix testPattern, trainPattern;
		double dist1, dist2;
		for (AuthorWPData testData: testAuthorData) {
			Map<String,Double> testRes = new HashMap<String,Double>();
			for (AuthorWPData trainData: trainAuthorData) {
				// initialize zero-frequency features
				testData.initBasisMatrix();
				trainData.initBasisMatrix();
				
				// compute pattern matrices BEFORE adding pattern disruption
				testPattern = AuthorWPData.generatePattern(trainData, testData);
				trainPattern = AuthorWPData.generatePattern(testData, trainData);
				
				// add pattern disruptions
				testData.addPatternDisruption(trainData, IG, wordsSynCount, trainPattern);
				trainData.addPatternDisruption(testData, IG, wordsSynCount, testPattern);
				testData.addPatternDisruption(trainData, IG, wordsSynCount, trainPattern);
				
				// compute pattern matrices AFTER adding pattern disruption
				testPattern = AuthorWPData.generatePattern(trainData, testData);
				trainPattern = AuthorWPData.generatePattern(testData, trainData);
				
				// compute distances
				dist1 = sumEuclideanDistance(testPattern, trainData.writeprint);
				dist2 = sumEuclideanDistance(trainPattern, testData.writeprint);
				
				// save the inverse to maintain the smallest distance as the best fit
				testRes.put(trainData.authorName, -(dist1 + dist2));
			}
			results.put(testData.authorName,testRes);
		}
		return results;
	}

	@Override
	public Evaluation runCrossValidation(Instances data, int folds,
			long randSeed) {
		// TODO Auto-generated method stub
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
	 * Calculates the Information-Gain for each feature, defined as
	 * <code>IG(c,j) = H(c) - H(c|j)</code> where
	 * <code>H(c)</code> and <code>H(c|j)</code> are the overall entropy across
	 * author classes and the conditional entropy for feature <code>j</code>,
	 * respectively.
	 * @param numFeatures
	 * 		The total number of features <code>j</code>.
	 * @param trainAuthorData
	 * 		The training author data.
	 * @return
	 * 		The vector of Information-Gain per feature.
	 */
	/*
	private static double[] calcInfoGain(int numFeatures, List<AuthorWPData> trainAuthorData) {
		double[] infoGain = new double[numFeatures];
		int numAuthors = trainAuthorData.size();

		// total entropy
		double p = 1 / numAuthors;
		double totalEntropy = - (numAuthors * p * log2(p));

		double conditionalEntropy, f;
		for (int j = 0; j < numFeatures; j++) {
			// conditional entropy
			conditionalEntropy = 0;
			for (AuthorWPData ad: trainAuthorData) {
				f = ad.posteriorProbabilities[j];
				conditionalEntropy -= f * log2(f);
			}
			
			// information gain
			infoGain[j] = totalEntropy - conditionalEntropy;
		}
		
		return infoGain;
	}
	*/
	
	/**
	 * Calculates and returns the information gain vector for all features
	 * based only on the training authors data in the given training set
	 * (i.e. excluding test authors data).
	 * @param trainingSet
	 * 		The training set (containing both training and test authors data).
	 * @param numFeatures
	 * 		The number of features.
	 * @return
	 * 		The information gain vector for all features based only on the
	 * 		the training authors data in the given training set.
	 * @throws Exception
	 * 		If an error is encountered during information gain evaluation.
	 */
	private static double[] calcInfoGain(Instances trainingSet, int numFeatures) throws Exception {		
		Instances train = getTrainOnlyData(trainingSet, TEST_AUTHOR_NAME_PREFIX);
		InfoGainAttributeEval ig = new InfoGainAttributeEval();
		ig.buildEvaluator(train);
		double[] IG = new double[numFeatures];
		for (int j = 0; j < numFeatures; j++)
			IG[j] = ig.evaluateAttribute(j);
		return IG;
	}

	/**
	 * Removes all test data (classes and instances) from the given training set,
	 * by removing all data of authors with name beginning with the given prefix.
	 * @param trainingSet
	 * 		The training data to filter.
	 * @param testAuthorsNamePrefix
	 * 		Test authors name prefix.
	 * @return
	 * 		The filtered data, containing only training authors and instances.
	 */
	private static Instances getTrainOnlyData(Instances trainingSet, String testAuthorsNamePrefix) {
		// attributes
		FastVector newAttributes = new FastVector(trainingSet.numAttributes());
		int numAttributes = trainingSet.numAttributes();
		for (int i = 0; i < numAttributes - 1; i++)
			newAttributes.addElement(trainingSet.attribute(i));
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		FastVector newClassVector = new FastVector();
		String authorName;
		Set<Double> testAuthorIndices = new HashSet<Double>();
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			if (authorName.startsWith(testAuthorsNamePrefix))
				testAuthorIndices.add(new Double(i));
			else
				newClassVector.addElement(authorName);
		}
		Attribute newClassAttribute = new Attribute(classAttribute.name(), newClassVector);
		newAttributes.addElement(newClassAttribute);
		Instances train = new Instances(
				trainingSet.relationName(),
				newAttributes,
				0);
		train.setClassIndex(train.numAttributes() - 1);
		// instances
		int numInstances = trainingSet.numInstances();
		Instance originalInst;
		double[] originalValues;
		int newClassIndex;
		for (int i = 0; i < numInstances; i++) {
			originalInst = trainingSet.instance(i);
			if (testAuthorIndices.contains(originalInst.classValue()))
				continue; // skip test authors instances
			originalValues = originalInst.toDoubleArray();
			newClassIndex = newClassAttribute.indexOfValue(
					classAttribute.value((int)(originalValues[numAttributes - 1])));
			originalValues[numAttributes - 1] = newClassIndex;
			train.add(new Instance(originalInst.weight(), originalValues));
		}

		return train;
	}
	
	/*
	/**
	 * Calculates the logarithm base 2 of the given number.
	 * @param x
	 * 		The input number.
	 * @return
	 * 		The logarithm base 2 of the given number.
	 */
	/*
	private static double log2(double x) {
		return Math.log10(x)/Math.log10(2);
	}
	*/
	
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
		WriteprintsAnalyzer wa = new WriteprintsAnalyzer();
		ProblemSet ps = new ProblemSet(JSANConstants.JSAN_PROBLEMSETS_PREFIX + "drexel_1_train_test.xml");
		CumulativeFeatureDriver cfd =
				new CumulativeFeatureDriver(JSANConstants.JSAN_FEATURESETS_PREFIX + "writeprints_feature_set_limited.xml");
		WekaInstancesBuilder wib = new WekaInstancesBuilder(false);
		
		// extract features
		System.out.println("feature pre extraction");
		WriteprintsAnalyzer.preExtraction(ps);
		System.out.println("feature extraction");
		wib.prepareTrainingSet(ps.getAllTrainDocs(), cfd);
		System.out.println("feature post extraction");
		WriteprintsAnalyzer.postExtraction(wib);
		System.out.println("done!");
		
		Instances train = wib.getTrainingSet();
		Instances test = wib.getTestSet();
		
		// classify
		System.out.println("classification");
		Map<String,Map<String, Double>> res = wa.classify(train, test, ps.getTestDocs());
		System.out.println("done!");
		Map<String,Double> docMap;
		String selectedAuthor = null;
		double maxValue;
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
		}
	}
}
