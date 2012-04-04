package edu.drexel.psal.anonymouth.utils;

import edu.drexel.psal.anonymouth.projectDev.Attribute;

/**
 * Consolidates Strings in ArrayLists. 
 * @author Andrew W.E. McDonald
 *
 */
public class ConsolidationStation {
	
	/*
	 * TODO:
	 * read through entire documentToModify, find all indicies of all features that need to be removed.
	 * 		- any indices within a range are just ingored, as they are already highlighted
	 * 
	 * when the user clicks on a sentence, everything that should be removed from that sentence gets highlighted,
	 * 		- the user is presented with other ways to express that idea using examples from other places in the text
	 * 
	 * 
	 */
	
	public ConsolidationStation(Attribute[] attribs){
		String specificFeature ="";
		for(Attribute a:attribs){
			specificFeature = a.getStringInBraces();
			if(specificFeature.equals(""))
				continue;// TODO: dont just ignore single numeric features - (e.x. letters percentage)....
			
			
			
		}
		
	}

}
