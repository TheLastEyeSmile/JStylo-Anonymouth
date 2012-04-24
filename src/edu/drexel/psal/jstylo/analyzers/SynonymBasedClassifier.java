package edu.drexel.psal.jstylo.analyzers;

import java.util.*;

import weka.classifiers.Evaluation;
import weka.core.Instances;
import edu.drexel.psal.jstylo.generics.Analyzer;
import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.ProblemSet;

public class SynonymBasedClassifier extends Analyzer {
	
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
}
