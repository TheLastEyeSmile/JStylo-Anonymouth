package edu.drexel.psal.jstylo.analyzers.writeprints;

import java.util.*;

import weka.core.*;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Representation of an author (identity) data required for Writeprints.
 * Includes the feature matrix, basis matrix, writeprint matrix etc.
 * 
 * @author Ariel Stolerman
 *
 */
public class AuthorWPData {

	// fields
	protected String authorName;
	protected Matrix featureMatrix;
	protected List<Integer> zeroFeatures;
	protected Matrix basisMatrix;
	protected Matrix writeprint;
	
	// constructor
	
	/**
	 * Constructs a new author Writeprints data object with the given author name.
	 * @param authorName
	 * 		The name of the author.
	 */
	public AuthorWPData(String authorName) {
		this.authorName = authorName;
	}
	
	
	// methods
	
	/**
	 * Extracts the feature matrix from the given training data (set of all
	 * extracted features) for this author, identified by the
	 * <code>authorName</code> value.<br>
	 * If the <code>average</code> parameter is <code>true</code>, sets the
	 * matrix to be a single vector which is the average values across all
	 * the author's feature vectors.<br>
	 * In addition records the list of features that have 0 frequency.
	 * @param trainingData
	 * 		The ARFF training data representing feature vectors of various
	 * 		authors, including the current author, to extract the feature
	 * 		values from.
	 * @param average
	 * 		Whether to save only one feature vector, which will be the average
	 * 		of all extracted feature vectors.
	 */
	public void initFeatureMatrix(Instances trainingData, boolean average) {
		// isolate only relevant feature-vectors in a new Instances object
		int numInstances = trainingData.numInstances();
		int classIndex = trainingData.classIndex();
		Instances data = new Instances(trainingData, 0);
		for (int i = 0; i < numInstances; i++)
			if (trainingData.instance(i).stringValue(classIndex).equals(authorName))
				data.add(trainingData.instance(i));
		numInstances = data.numInstances();
		int numFeatures = data.numAttributes() - 1; // exclude class attribute
		
		/*
		 * initialize a matrix of features (each row represents an instance, each
		 * column represents a feature).
		 */
		double[][] matrix = new double[numInstances][numFeatures];
		Instance inst;
		for (int i = 0; i < numInstances; i++) {
			inst = data.instance(i);
			for (int j = 0; j < numFeatures; j++)
				matrix[i][j] = inst.value(j);
		}
		
		// save feature matrix
		if (average) {
			// calculate average
			double[][] avg = new double[1][numFeatures];
			for (int j = 0; j < numFeatures; j++) {
				for (int i = 0; i < numInstances; i++)
					avg[0][j] += matrix[i][j];
				avg[0][j] /= numInstances;
			}
			featureMatrix = new Matrix(avg);
		}
		else {
			// just save feature matrix
			featureMatrix = new Matrix(matrix);
		}
		
		// record zero-frequency features for author
		zeroFeatures = new ArrayList<Integer>();
		boolean isZero;
		for (int j = 0; j < numFeatures; j++) {
			isZero = true;
			for (int i = 0; i < numInstances; i++)
				if (matrix[i][j] != 0) {
					isZero = false;
					break;
				}
			if (isZero)
				zeroFeatures.add(j);
		}
	}
	
	/**
	 * Derives the basis matrix (set of eigenvectors) from feature usage
	 * covariance matrix using Karhunen-Loeve transform (PCA) as described in
	 * {@link http://isa.umh.es/asignaturas/cscs/PR/3%20-%20Feature%20extraction.pdf}.
	 * In addition computes the author's writeprint pattern.
	 */
	public void initBasisAndWriteprintMatrices() {
		int numInstances = featureMatrix.getRowDimension();
		int numFeatures = featureMatrix.getColumnDimension();
		
		/* (1) calculate the covariance matrix */
		
		// calculate X, the (#features)x(#instances) matrix
		Matrix X = featureMatrix.transpose();
		// calculate MU, the (#features)x(#instances) matrix of feature means
		// where each cell i,j equals mean(feature_i)
		double[] MU_values = new double[numFeatures];
		for (int j = 0; j < numFeatures; j++) {
			MU_values[j] = 0;
			for (int i = 0; i < numInstances; i++)
				MU_values[j] += featureMatrix.get(i,j);
			MU_values[j] /= numInstances;
		}
		double[][] MU_matrix_values = new double[numFeatures][numInstances];
		for (int i = 0; i < numFeatures; i++)
			for (int j = 0; j < numInstances; j++)
				MU_matrix_values[i][j] = MU_values[i];
		Matrix MU = new Matrix(MU_matrix_values);
		// calculate X - MU
		Matrix X_minus_MU = X.minus(MU);
		// finally, calculate the covariance matrix
		Matrix COV = X_minus_MU.times(X_minus_MU.transpose()).times(1 / ((double) numFeatures));
		
		/* (2)	calculate eigenvalues followed by eigenvectors - the basis matrix,
		 * 		and calculate the principal component matrix - the author's writeprint*/
		EigenvalueDecomposition eigenvalues = COV.eig();
		basisMatrix = eigenvalues.getV();
		writeprint = basisMatrix.transpose().times(X_minus_MU);
	}
	
	public static void main(String[] args) {
		AuthorWPData a = new AuthorWPData("a");
		double[][] d = new double[][]{
				{1,2,3},
				{4,5,6}
		};
		a.featureMatrix = new Matrix(d);
		a.initBasisAndWriteprintMatrices();
	}
}





























