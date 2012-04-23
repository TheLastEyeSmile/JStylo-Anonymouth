package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.util.*;

import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.NominalToString;
import edu.drexel.psal.jstylo.generics.Analyzer;

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
	
	List<AuthorWPData> trainAuthorData = new ArrayList<AuthorWPData>();
	
	
	/* ==========
	 * operations
	 * ==========
	 */
	
	@Override
	public void preExtraction(Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postExtraction(Object... args) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Map<String, Double>> classify(Instances trainingSet,
			Instances testSet) {
		
		// initialize training data features, basis and writeprint matrices
		Attribute classAttribute = trainingSet.classAttribute();
		int numAuthors = classAttribute.numValues();
		for (int i = 0; i < numAuthors; i++) {
			trainAuthorData.add(new AuthorWPData(classAttribute.value(i)));
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



























