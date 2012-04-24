package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.HashMap;

import edu.drexel.psal.anonymouth.projectDev.Attribute;

/**
 * 
 * @author Andrew W.E. McDonald
 *
 */
public class ConsolidationStation {
	
	Attribute[] attribs;
	HashMap<String,ArrayList<TreeData>> parsed;
	ArrayList<Triple> toAdd;
	ArrayList<Triple> toRemove;
	public static DocumentTagger otherSampleTagger;
	public static DocumentTagger authorSampleTagger;
	public static DocumentTagger toModifyTagger;
	
	/**
	 * constructor for ConsolidationStation. Depends on target values, and should not be called until they have been selected.
	 * @param attribs
	 * @param parsed
	 */
	public ConsolidationStation(Attribute[] attribs){
		this.attribs = attribs;
		this.parsed = parsed;
		toAdd = new ArrayList<Triple>(400);
		toRemove = new ArrayList<Triple>(400);
	}
	
	/**
	 * Starts the consolidation process
	 */
	public void beginConsolidation(){
		
		
	}
	
	/**
	 * runs through all attributes in attribs and pulls out the stringInBraces if it is there, and the percent (positive and negative)  
	 * change needed
	 * 
	 */
	public void getStringsFromAttribs(){
		for(Attribute attrib:attribs){
			if (attrib.getCalcHist() == false)
				continue; // ignore single valued features
			String tempID;
			double tempPercentChange;
			double tempInfoGain;
			tempPercentChange = attrib.getPercentChangeNeeded();
			tempID = attrib.getStringInBraces();
			tempInfoGain = attrib.getInfoGain();
			if (tempPercentChange > 0){
				Triple trip = new Triple(tempID,tempPercentChange,tempInfoGain);
				toAdd.add(trip);
			}
			else if(tempPercentChange < 0){
				Triple trip = new Triple(tempID,tempPercentChange,tempInfoGain);
				toRemove.add(trip);
			}
		}			
	}
	
	

}
