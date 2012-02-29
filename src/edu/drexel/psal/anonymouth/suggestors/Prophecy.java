package edu.drexel.psal.anonymouth.suggestors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Prophecy class receives the output of each suggestor (subclass of TheOracle), and allows it to be neatly passed along
 * @author Andrew W.E. McDonald
 *
 */
public class Prophecy {

	private String featureName;
	private String theSuggestion;
	private HashMap<Color,ArrayList<int[]>> highlightMap;
	private boolean noChangeNeeded;
	
	/**
	 * Constructor for Prophecy
	 * @param featureName  String, name of feature
	 * @param theSuggestion  String, the suggestion itself
	 * @param highlightMap  HashMap<Color,ArrayList<int[]>, mapping of colors to various strings within the document that will be modified
	 * @param noChangeNeeded false if change is needed
	 */
	public Prophecy(String featureName, String theSuggestion, HashMap<Color,ArrayList<int[]>> highlightMap, boolean noChangeNeeded){
		//System.out.println("Suggestion inside prophecy: "+theSuggestion);
		this.featureName = featureName;
		this.theSuggestion = theSuggestion;
		this.highlightMap = highlightMap;
		this.noChangeNeeded = noChangeNeeded;
		
	}
	
	/**
	 * Returns String representation of the feature name
	 * @return
	 * 	String, feature name
	 */
	public String getName(){
		return featureName;
	}
	
	/**
	 * Returns String representation of suggestion
	 * @return
	 */
	public String getSuggestion(){
		return theSuggestion;
	}
	
	/**
	 * Returns mapping of colors to indices for highlighting in the GUI
	 * @return
	 * 	mapping of colors to indices
	 */
	public HashMap<Color,ArrayList<int[]>> getHighlightMap(){
		return highlightMap;
	}
	
	/**
	 * False if change is needed in the feature's present value
	 * @return
	 * 	 False if change is needed, true if not.
	 */
	public boolean getNoChangeNeeded(){
		return noChangeNeeded;
	}
	
}
