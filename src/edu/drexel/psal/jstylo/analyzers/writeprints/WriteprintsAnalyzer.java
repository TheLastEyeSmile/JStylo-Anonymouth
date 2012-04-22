package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.util.List;
import java.util.Map;

import weka.classifiers.Evaluation;
import weka.core.Instances;
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

	/**
	 * Main for testing.
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	
	@Override
	public List<Map<String, Double>> classify(Instances trainingSet,
			Instances testSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Evaluation runCrossValidation(Instances data, int folds,
			long randSeed) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/* ===============
	 * Utility methods
	 * ===============
	 */
	
}



























