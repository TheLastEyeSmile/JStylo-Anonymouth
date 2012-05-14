package edu.drexel.psal.anonymouth.suggestors;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import edu.drexel.psal.anonymouth.utils.POS;
import edu.drexel.psal.anonymouth.utils.POS.TheTags;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Suggestor for POS_TAGS feature
 * @author Andrew W.E. McDonald
 *
 */
public class POS_TAGS extends TheOracle {

	@Override
	protected void algorithm() {
		if (featureTargetValue == featurePresentValue){
			setNoChangeNeeded(true);
		}
		else{
			String theDoc = super.getDocument();
			LinkedList<String> wordsToHighlight = new LinkedList<String>();
			/*
			MaxentTagger mt= null;
			try {
				mt = new MaxentTagger("./external/MaxentTagger/left3words-wsj-0-18.tagger");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			*/
			
			String beenTagged = mt.tagString(theDoc);
			//System.out.println(beenTagged);
			String theTag = super.stringInBraces.replaceAll("\\p{C}", " ");;
			String safeTag = "";
			if(theTag.contains("$"))
				safeTag = theTag.replace("$", "\\$");
			else
				safeTag= theTag;
			
			System.out.println("The POS tag is: "+theTag);
			int start = -1;
			Pattern posTag = Pattern.compile("(\\s|\\b)[^\\s]+/"+safeTag+"(\\b|\\s)");
			Matcher wordFinder = posTag.matcher(beenTagged);
			double numFound = 0;
			boolean isFound;
			while(true){
				isFound = wordFinder.find(start+1);
				if(isFound == false)
					break;
				numFound+= 1;
				if(wordFinder.group(1).matches("\\s"))
					start=wordFinder.start()+1;
				else
					start=wordFinder.start();
				//System.out.println(beenTagged.substring(start,beenTagged.indexOf("/"+theTag,start)));
				wordsToHighlight.push(beenTagged.substring(start,beenTagged.indexOf("/"+theTag,start)));
				//TheTags thisTag = POSmap.TheTags.valueOf(theTag);
				//System.out.print("    ---> "+POSmap.tagToDescription(thisTag));
				
			}	
			attrib.setToModifyValue(numFound);
			featurePresentValue = numFound;
			
			String tagDef;
			try{
				tagDef= POS.tagToDescription(TheTags.valueOf(theTag));
			} catch(IllegalArgumentException iae){
				tagDef = "punctuation/symbol";
			}
			if(tagDef.equals("punctuation/symbol"))
				highlightMap.put(getNextColor(), IndexFinder.findSymbolIndices(theDoc, wordsToHighlight));
			else
				highlightMap.put(getNextColor(),IndexFinder.findIndices(theDoc, wordsToHighlight));
			
			
			if(featureTargetValue > featurePresentValue){
				double numToAdd = Math.floor(100*(featureTargetValue - featurePresentValue)+.5)/100;
				setSuggestionLowToHigh("The description of the part of speech tag '"+theTag+"' is: "+tagDef+". Currently, you have too few of these types of words " +
						"in your document. You have '"+featurePresentValue+"', while you should have '"+featureTargetValue+"'. The highlighted words are examples of words that " +
								"would be/ are tagged as "+theTag+" in the context that they are used. Try to use these words in their context as a guideline " +
										"to add "+numToAdd+" words that would be tagged as '"+theTag+"'.");
			}
			else if(featurePresentValue > featureTargetValue){
				double numToRemove = Math.floor(100*(featurePresentValue - featureTargetValue)+.5)/100;
				setSuggestionHighToLow("The description of the part of speech tag '"+theTag+"' is: "+tagDef+". Currently, you have too many of these types of words " +
						"in your document. You have '"+featurePresentValue+"', while you should have '"+featureTargetValue+"'. The highlighted words " +
								"have been tagged as "+theTag+" in the context that they are used. Try to " +
										"eliminate "+numToRemove+" of these words - though be careful to avoid simply substituting these words for different ones that fall into the same part of speec catagory.");
			}
			else
				setNoChangeNeeded(true); // safety measure... although this should NEVER be executed.
		}
	}

}
