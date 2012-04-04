package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.HashMap;

import edu.drexel.psal.anonymouth.projectDev.Attribute;

public class ConsolidationStation {
	
	Attribute[] attribs;
	HashMap<String,ArrayList<TreeData>> parsed;
	
	/**
	 * constructor for ConsolidationStation. Depends on target values, and should not be called until they have been selected.
	 * @param attribs
	 * @param parsed
	 */
	public ConsolidationStation(Attribute[] attribs, HashMap<String,ArrayList<TreeData>> parsed){
		this.attribs = attribs;
		this.parsed = parsed;
	}
	
	/**
	 * runs through all attributes in attribs and pulls out the stringInBraces if it is there, and the percent (positive and negative)  
	 * change needed
	 * 
	 */
	public void getStringsFromAttribs(){
		
	}

}
