package edu.drexel.psal.jstylo.generics;

import java.util.*;
import weka.core.*;
import com.jgaap.generics.*;

/**
 * Abstract class for analyzers - classification routines to be applied on test sets, given a training set.
 * The data representation is based on Weka's Instances object.
 * 
 * @author Ariel Stolerman
 */
public abstract class Analyzer {
	
	/* ======
	 * fields
	 * ======
	 */
	
	/**
	 * The Weka Instances dataset to hold the extracted training data.
	 */
	protected Instances trainingSet;
	
	/**
	 * The Weka Instances dataset to hold the extracted test data.
	 */
	protected Instances testSet;
	
	/**
	 * List of distribution classification results for each unknown document.
	 */
	protected List<Map<String,Double>> results;
	
	/**
	 * List of authors.
	 */
	protected List<String> authors;
	
	
	/* ============
	 * constructors
	 * ============
	 */
	
	// -- none --
	
	/* ==========
	 * operations
	 * ==========
	 */
	
	/**
	 * Classifies the given test set based on the given training set. Should update the following fields along the classification:
	 * trainingSet, testSet, results and authors.
	 * Returns list of distributions of classification probabilities per instance.
	 * @param trainingSet
	 * 		The Weka Instances dataset of the training instances.
	 * @param testSet
	 * 		The Weka Instances dataset of the test instances.
	 * @return
	 * 		The list of distributions of classification probabilities per instance, or null if prepare was
	 * 		not previously called. Each result in the list is a mapping from the author to its corresponding
	 * 		classification probability.
	 */
	public abstract List<Map<String,Double>> classify(Instances trainingSet, Instances testSet);
	
	
	/* =======
	 * getters
	 * =======
	 */
	
	/**
	 * Returns the string representation of the last classification results.
	 * @param unknownDocs
	 * 		The list of the unknown documents on which the classification was applied.
	 * @return
	 * 		The string representation of the classification results.
	 */
	public String getLastStringResults(List<Document> unknownDocs) {
		// if there are no results yet
		if (results == null)
			return "No results!";
		
		String[] docs = new String[unknownDocs.size()];
		String[] unknownAuthors = new String[unknownDocs.size()];
		for (int i=0; i<docs.length; i++) {
			docs[i] = unknownDocs.get(i).getTitle();
			unknownAuthors[i] = unknownDocs.get(i).getAuthor();
		}
		
		String res = "";
		
		Formatter f = new Formatter();
		f.format("%-14s |", "doc \\ author");
		
		List<String> actualAuthors = new ArrayList<String>(authors);
		actualAuthors.remove(WekaInstancesBuilder.getDummy());
		actualAuthors.remove(ProblemSet.getDummyAuthor());
		
		for (String author: actualAuthors)
			f.format(" %-14s |",author);
		res += f.toString()+"\n";
		for (int i=0; i<actualAuthors.size(); i++)
			res += "-----------------";
		res += "----------------\n";
		
		//int correctlyClassified = 0;
		for (int i=0; i<results.size(); i++) {
			//String currAuthor = unknownAuthors[i];
			f = new Formatter();
			f.format("%-14s |",docs[i]);
			Map<String,Double> currRes = results.get(i);
			
			String resAuthor = "";
			double maxProb = 0, oldMaxProb;
			for (String author: currRes.keySet()) {
				oldMaxProb = maxProb;
				maxProb = Math.max(maxProb, currRes.get(author).doubleValue());
				if (maxProb > oldMaxProb)
					resAuthor = author;
			}
			
			for (String author: actualAuthors) {
				char c;
				if (author.equals(resAuthor))
					/*
					if (resAuthor.equals(currAuthor)){
						correctlyClassified++;
						c = '+';
					} else
						c = '-';
					*/
					c = '+';
				else c = ' ';
				f.format(" %2.6f %c     |",currRes.get(author).doubleValue(),c);
			}
			res += f.toString()+"\n";
		}
		
		res += "\n";
		/*
		f = new Formatter();
		f.format("%3.4f %%",((double)correctlyClassified)*100/unknownDocs.size());
		res += "Total accuracy: "+correctlyClassified+"/"+unknownDocs.size()+" ("+f.toString()+")\n";
		*/
		return res;
	}
	
	/**
	 * Returns the fraction of rightly classified instances in the test case, or -1 if no prior
	 * classification was applied.
	 * @param unknownDocs
	 * 		The list of the unknown documents on which the classification was applied. 
	 * @return
	 * 		The fraction of rightly classified instances in the test case, or -1 if no prior
	 * 		classification was applied.
	 */
	public double getClassificationAccuracy(List<Document> unknownDocs) {
		// if there are no results yet
		if (results == null) return -1;
		
		int numOfInstances = testSet.numInstances();
		String[] unknownDocAuthors = new String[numOfInstances];
		for (int i=0; i<unknownDocAuthors.length; i++)
			unknownDocAuthors[i] = unknownDocs.get(i).getAuthor();
		
		int rightClassifications = 0;
		double maxProb;
		Iterator<Double> currRes;
		String currAuthor;
		
		for (int i=0; i<numOfInstances; i++) {
			currAuthor = unknownDocAuthors[i];

			// get the highest probability of all authors for current document
			maxProb = 0;
			currRes = results.get(i).values().iterator();
			for (;currRes.hasNext();)
				maxProb = Math.max(maxProb, currRes.next());
			
			// increase rightly classified count by 1 when true
			rightClassifications += (results.get(i).get(currAuthor) == maxProb) ? 1 : 0;
		}
		
		return ((double) rightClassifications)/numOfInstances;
	}
	
	/**
	 * Returns the last training Weka Instances set that was used for classification.
	 * @return
	 * 		The last training Weka Instances set that was used for classification.
	 */
	public Instances getLastTrainingSet() {
		return trainingSet;
	}
	
	/**
	 * Returns the last test Weka Instances set that was used for classification.
	 * @return
	 * 		The last test Weka Instances set that was used for classification.
	 */
	public Instances getLastTestSet() {
		return testSet;
	}
	
	/**
	 * Returns the entire data in one Weka Instances set.
	 * @return
	 * 		The entire data in one Weka Instances set.
	 */
	public Instances getAllInstances() {
		Instances all = new Instances(trainingSet);
		for (int i=0; i<testSet.numInstances(); i++) {
			all.add(testSet.instance(i));
		}
		return all;
	}
	
	/**
	 * Returns the last list of author names.
	 * @return
	 * 		The last list of author names.
	 */
	public List<String> getLastAuthors() {
		return authors;
	}
	
	/**
	 * Returns the last classification results or null if no classification was applied.
	 * @return
	 * 		The classification results or null if no classification was applied.
	 */
	public List<Map<String,Double>> getLastResults() {
		return results;
	}
}
