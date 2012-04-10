package edu.drexel.psal.anonymouth.suggestors;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.drexel.psal.anonymouth.suggestors.POS.TheTags;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Suggestor for POS_BIGRAMS
 * @author Andrew W.E. McDonald
 *
 */
public class POS_BIGRAMS extends TheOracle{

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
			String theTags = super.stringInBraces.replaceAll("\\p{C}", " ");;
			//System.out.println(theTags);
			int startTag = theTags.indexOf("(")+1;
			int endTag = theTags.indexOf(")"); 
			String tagOne = theTags.substring(startTag,endTag);
			String tagTwo = theTags.substring(theTags.indexOf("(",startTag)+1,theTags.indexOf(")",endTag+1));
			String safeTagOne=tagOne;
			String safeTagTwo=tagTwo;
			if(safeTagOne.contains("$"))
				safeTagOne = safeTagOne.replace("$", "\\$");
			if(safeTagOne.contains("."))
				safeTagOne = safeTagOne.replace(".", "\\.");
			if(safeTagTwo.contains("$"))
				safeTagTwo = safeTagTwo.replace("$","\\$");
			if(safeTagTwo.contains("."))
				safeTagTwo = safeTagTwo.replace(".", "\\.");
			
			//System.out.println("The POS tags are: "+tagOne+" (safe is: "+safeTagOne+") and "+tagTwo+" (safe is: "+safeTagTwo+")");
			int start = -1;
			Pattern posTag = Pattern.compile("(\\s|\\b)[^\\s]+/"+safeTagOne+"(\\b|\\s)[^\\s]+/"+safeTagTwo+"(\\b|\\s)");
			Matcher wordFinder = posTag.matcher(beenTagged);
			int secondIndex;
			double numFound = 0;
			boolean isFound;
			while(true){
				isFound = wordFinder.find(start+1);
				if(isFound == false)
					break;
				numFound += 1;
				if(wordFinder.group(1).matches("\\s")){
					start=wordFinder.start()+1;
					//System.out.println("MATCH 1");
				}else{
					//System.out.println("MATCH 2");
					start=wordFinder.start();
				}
				secondIndex = beenTagged.indexOf("/"+tagOne,start);
				String wordOne = beenTagged.substring(start,secondIndex);
				String wordTwo = beenTagged.substring(secondIndex+2+tagOne.length(),beenTagged.indexOf("/"+tagTwo,secondIndex));
				//System.out.println(wordOne+" & "+wordTwo);
				wordsToHighlight.push(wordOne);
				wordsToHighlight.push(wordTwo);
				//TheTags thisTag = POSmap.TheTags.valueOf(theTag);
				//System.out.print("    ---> "+POSmap.tagToDescription(thisTag));
				
			}	
			
			attrib.setToModifyValue(numFound);
			featurePresentValue = numFound;
			
			String tagOneDef;
			String tagTwoDef;
			try{
				tagOneDef= POS.tagToDescription(TheTags.valueOf(tagOne));
			} catch(IllegalArgumentException iae){
				tagOneDef = "punctuation/symbol";
			}
			try{
				tagTwoDef= POS.tagToDescription(TheTags.valueOf(tagTwo));
			} catch(IllegalArgumentException iae){
				tagTwoDef = "punctuation/symbol";
			}
			
			
			highlightMap.put(getNextColor(),IndexFinder.findWordBigramIndices(theDoc, wordsToHighlight));
			
			
			if(featureTargetValue > featurePresentValue){
				double numToAdd = Math.floor(100*(featureTargetValue - featurePresentValue)+.5)/100;
				setSuggestionLowToHigh("The description of the part of speech tag '"+tagOne+"' is: "+tagOneDef+", and the description of '"+tagTwo+"' is: "+tagTwoDef+". Currently, you have too few of these part of speech bigrams (pairs) " +
						"in your document. You have '"+featurePresentValue+"', while you should have '"+featureTargetValue+"'. The highlighted words are examples of word pairs that " +
								"would be/ are tagged as "+stringInBraces+" in the context that they are used. Try to use these words in their context as a guideline " +
										"to add "+numToAdd+" word pairs that would be tagged as '"+stringInBraces+"'.");
			}
			else if(featurePresentValue > featureTargetValue){
				double numToRemove = Math.floor(100*(featurePresentValue - featureTargetValue)+.5)/100;
				setSuggestionHighToLow("The description of the part of speech tag '"+tagOne+"' is: "+tagOneDef+", and the description of '"+tagTwo+"' is: "+tagTwoDef+". Currently, you have too many of these part of speech bigrams (pairs) " +
						"in your document. You have '"+featurePresentValue+"', while you should have '"+featureTargetValue+"'. The highlighted word pairs " +
								"have been tagged as "+stringInBraces+" in the context that they are used. Try to " +
										"eliminate "+numToRemove+" of these word pairs - though be careful to avoid simply substituting these words for different ones that fall into the same part of speech catagory.");
			}
			else
				setNoChangeNeeded(true); // safety measure... although this should NEVER be executed.
		}
	}

	

}
