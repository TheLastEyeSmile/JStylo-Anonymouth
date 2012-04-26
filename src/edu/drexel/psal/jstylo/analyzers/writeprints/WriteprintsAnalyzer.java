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
	
	public static void postExtraction(ProblemSet ps) {
		// skip if no test documents were pre-processed
		if (!testDocsPreProcessed)
			return;
		
		//TODO
	}
	
	@Override
	public Map<String,Map<String, Double>> classify(Instances trainingSet,
			Instances testSet, List<Document> unknownDocs) {
		
		/* ========
		 * TRAINING
		 * ========
		 */
		
		// initialize features, basis and writeprint matrices
		// and feature probabilities per author p(j|c)
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		String authorName;
		AuthorWPData authorData;
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			authorData = new AuthorWPData(authorName);
			authorData.initFeatureMatrix(trainingSet, averageFeatureVectors);
			authorData.initBasisAndWriteprintMatrices();
			//authorData.initFeatureProbabilities();
			if (authorName.startsWith(TEST_AUTHOR_NAME_PREFIX))
				testAuthorData.add(authorData);
			else
				trainAuthorData.add(authorData);
		}
		
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
		
		// initialize synonym count mapping
		Map<String,Integer> wordsSynCount = calcSynonymCount(trainingSet,numFeatures);
		
		
		/* =======
		 * TESTING
		 * =======
		 */
		
		System.out.println(trainAuthorData);
		System.out.println(testAuthorData);
		
		Matrix testPattern, trainPattern;
		double dist1, dist2;
		for (AuthorWPData testData: testAuthorData) {
			Map<String,Double> testRes = new HashMap<String,Double>();
			for (AuthorWPData trainData: trainAuthorData) {
				testPattern = AuthorWPData.generatePattern(trainData, testData);
				trainPattern = AuthorWPData.generatePattern(testData, trainData);
				dist1 = sumEuclideanDistance(testPattern, trainData.writeprint);
				dist2 = sumEuclideanDistance(trainPattern, testData.writeprint);
				
				// save the inverse to maintain the smallest distance as the best fit
				testRes.put(trainData.authorName, -((dist1 + dist2) / 2));
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
	 * Constructs a mapping from all word-based features to the number of their synonyms.
	 * The synonym counted are only those belonging to synsets of the most common part-of-speech
	 * synset-type. If the feature is an n-gram feature, the synonym count is the multiplication
	 * of synonym count values of each word in the n-gram.
	 * @param trainingSet
	 * 		The training set from which to extract the features.
	 * @param numFeatures
	 * 		The number of features.
	 * @return
	 * 		A mapping from the word features of the given training set to the synonym count.
	 */
	private static Map<String,Integer> calcSynonymCount(Instances trainingSet, int numFeatures) {
		
		// initialize
		Map<String,Integer> synCountMap = new HashMap<String,Integer>(numFeatures);
		if (wndb == null)
			initWordnetDB();
		
		boolean isWordFeature;
		Attribute feature;
		String featureName;
		String[] words;
		List<String> featureNames = new ArrayList<String>();
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
			
			// extract word
			featureNames.add(featureName);
		}
		
		// find synonym count for all word features
		// multiply synonym-count for n-gram features
		int numWords = featureNames.size();
		Synset[] synsets, tmpSynsets;
		SynsetType[] allTypes = SynsetType.ALL_TYPES;
		Set<String> synonyms;
		int synCount;
		for (int i = 0; i < numWords; i++) {
			synCount = 1;
			featureName = featureNames.get(i);
			words = getWordsFromFeatureName(featureName);
			
			for (String word: words) {
				// find the SynsetType with the maximum number of synsets
				synsets = wndb.getSynsets(word, allTypes[0]);
				for (int j = 1; j < allTypes.length; j++) {
					tmpSynsets = wndb.getSynsets(word, allTypes[j]);
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
			synCountMap.put(featureName, synCount);
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
	 * Returns the sum of the Euclidean distances between every 
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
		int numFeatures = a.getRowDimension();
		for (int i = 0; i < numACols; i++) {
			for (int j = 0; j < numBCols; j++) {
				colsDiff = 0;
				for (int k = 0; k < numFeatures; k++) {
					tmp = a.get(k,i) - b.get(k, j);
					colsDiff += tmp * tmp;
				}
				sum += Math.sqrt(colsDiff);
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
		WriteprintsAnalyzer.preExtraction(ps);
		CumulativeFeatureDriver cfd =
				new CumulativeFeatureDriver(JSANConstants.JSAN_FEATURESETS_PREFIX + "writeprints_feature_set_limited.xml");
		WekaInstancesBuilder wib = new WekaInstancesBuilder(false);
		
		// extract features
		wib.prepareTrainingSet(ps.getAllTrainDocs(), cfd);
		Instances insts = wib.getTrainingSet();
		System.out.println(insts);
		
		// classify
		Map<String,Map<String, Double>> res = wa.classify(insts, null, ps.getTestDocs());
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
