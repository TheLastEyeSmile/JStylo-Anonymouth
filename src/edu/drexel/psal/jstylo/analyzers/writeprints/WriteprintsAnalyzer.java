package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.util.*;

import sun.rmi.runtime.Log;

import com.jgaap.generics.Document;

import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;
import edu.drexel.psal.jstylo.generics.Analyzer;
import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.ProblemSet;
import edu.drexel.psal.jstylo.generics.WekaInstancesBuilder;

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
	private boolean testDocsPreProcessed = false;
	
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
	@Override
	public void preExtraction(ProblemSet ps, CumulativeFeatureDriver cfd) {
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

	@Override
	public void postExtraction(ProblemSet ps, CumulativeFeatureDriver cfd) {
		// skip if no test documents were pre-processed
		if (!testDocsPreProcessed)
			return;
		
		//TODO
	}
	
	@Override
	public List<Map<String, Double>> classify(Instances trainingSet,
			Instances testSet) {
		
		// initialize features, basis and writeprint matrices
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		String authorName;
		AuthorWPData authorData;
		for (int i = 0; i < numAuthors; i++) {
			authorName = classAttribute.value(i);
			authorData = new AuthorWPData(authorName);
			authorData.initFeatureMatrix(trainingSet, averageFeatureVectors);
			if (authorName.startsWith(TEST_AUTHOR_NAME_PREFIX))
				testAuthorData.add(authorData);
			else
				trainAuthorData.add(authorData);
		}
		
		return null;
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
	
	
	
	
	/*
	 * Main for testing.
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}



























