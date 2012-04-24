package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.util.*;

import com.jgaap.generics.*;

import weka.classifiers.*;
import weka.core.*;
import edu.drexel.psal.jstylo.generics.*;

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
	 * Whether to average all feature vectors per author ending up with one feature vector
	 * all not. Increases performance but may reduce accuracy.
	 */
	private boolean averageFeatureVectors = true;
	
	/**
	 * The list of training author data, including feature, basis and writeprint matrices.
	 */
	private List<AuthorWPData> trainAuthorData = new ArrayList<AuthorWPData>();
	private List<AuthorWPData> testAuthorData = new ArrayList<AuthorWPData>();
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
			for (int i = numTestDocs - 1; i >= 0; i--) {
				doc = testDocs.remove(i);
				ps.addTrainDoc(TEST_AUTHOR_NAME_PREFIX + doc.getTitle().replaceAll("\\.\\S+$",""), doc);
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
	public List<Map<String, Double>> classify(Instances trainingSet,
			Instances testSet) {
		
		/* ========
		 * TRAINING
		 * ========
		 */
		
		// initialize features, basis and writeprint matrices
		// and feature probabilities per author p(j|c)
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		int numFeatures = trainingSet.numAttributes() - 1; // exclude class attribute
		String authorName;
		AuthorWPData authorData;
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			authorData = new AuthorWPData(authorName);
			authorData.initFeatureMatrix(trainingSet, averageFeatureVectors);
			authorData.initFeatureProbabilities();
			if (authorName.startsWith(TEST_AUTHOR_NAME_PREFIX))
				testAuthorData.add(authorData);
			else
				trainAuthorData.add(authorData);
		}
		
		// initialize the posterior probability p(c|j)
		// for the training authors only
		double[] totalProbability = new double[numFeatures];
		for (int j = 0; j < numFeatures; j++)
			for (AuthorWPData ad: trainAuthorData)
				totalProbability[j] += ad.featureProbabilities[j];
		for (AuthorWPData ad: trainAuthorData)
			ad.initPosteriorProbabilities(totalProbability);
		
		// calculate information-gain
		double[] IG = calcInfoGain(numFeatures, trainAuthorData);
		
		// initialize result set
		List<Map<String, Double>> res =
				new ArrayList<Map<String,Double>>(trainAuthorData.size());
		
		// initialize synonym count mapping
		Map<Integer,Integer> calcSynonymCount = calcSynonymCount(trainingSet,numFeatures);
		
		/* =======
		 * TESTING
		 * =======
		 */
		
		
		
		return res;
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
	
	/**
	 * Counts the number of synonyms per feature that is word-based, and constructs
	 * a map from feature indices to their synonym count. Features that are not word
	 * based are mapped to 0.
	 * @param trainingSet
	 * 		The training set from which to extract the features.
	 * @param numFeatures
	 * 		The number of features.
	 * @return
	 * 		A mapping from the features of the given training set to the synonym count.
	 */
	private static Map<Integer,Integer> calcSynonymCount(Instances trainingSet, int numFeatures) {
		Map<Integer,Integer> synCountMap = new HashMap<Integer,Integer>(numFeatures);
		
		for (int j = 0; j < numFeatures; j ++) {
			
		}
		
		return synCountMap;
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
	
	/**
	 * Calculates the logarithm base 2 of the given number.
	 * @param x
	 * 		The input number.
	 * @return
	 * 		The logarithm base 2 of the given number.
	 */
	private static double log2(double x) {
		return Math.log10(x)/Math.log10(2);
	}
	
	/*
	 * Main for testing.
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}



























