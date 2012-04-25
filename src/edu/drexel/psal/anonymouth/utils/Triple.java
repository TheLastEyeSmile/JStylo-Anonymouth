package edu.drexel.psal.anonymouth.utils;

import edu.drexel.psal.anonymouth.projectDev.FeatureList;

/**
 *	Holds: string in braces, infogain, and featureName  
 * @author Andrew W.E. McDonald
 * @author Joe Muoio
 *
 */
public class Triple {
	
	
	protected String stringInBraces;
	//protected double percentChangeNeeded;
	protected FeatureList featureName;
	protected double infoGain;
	
	public Triple(String stringInBraces, FeatureList featureName, double infoGain){
		this.stringInBraces = stringInBraces;
		//this.percentChangeNeeded = percentChangeNeeded;
		this.featureName=featureName;
		this.infoGain = infoGain;
	}
	
	public FeatureList getFeatureName(){
		return featureName;
	}
	public double getInfoGain(){
		return infoGain;
	}
	public String getStringInBraces(){
		return stringInBraces;
	}

}
