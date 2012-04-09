package edu.drexel.psal.anonymouth.utils;

/**
 *	Holds: string in braces, percent change needed,  
 * @author Andrew W.E. McDonald
 *
 */
public class Triple {
	
	
	protected String stringInBraces;
	protected double percentChangeNeeded;
	protected double infoGain;
	
	public Triple(String stringInBraces, double percentChangeNeeded, double infoGain){
		this.stringInBraces = stringInBraces;
		this.percentChangeNeeded = percentChangeNeeded;
		this.infoGain = infoGain;
	}
	
	

}
