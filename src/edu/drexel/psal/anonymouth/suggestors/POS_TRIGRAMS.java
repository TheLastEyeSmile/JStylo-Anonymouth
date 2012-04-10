package edu.drexel.psal.anonymouth.suggestors;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.drexel.psal.anonymouth.suggestors.POS.TheTags;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Suggestor for POS_TRIGRAMS 
 * @author Andrew W.E. McDonald
 *
 */
public class POS_TRIGRAMS extends TheOracle {

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
			int startTagTwo = theTags.indexOf("(",startTag)+1;
			int endTagTwo = theTags.indexOf(")",endTag+1);
			String tagTwo = theTags.substring(startTagTwo,endTagTwo);
			String tagThree = theTags.substring(theTags.indexOf("(",startTagTwo)+1,theTags.indexOf(")",endTagTwo+1));
			String safeTagOne=tagOne;
			String safeTagTwo=tagTwo;
			String safeTagThree=tagThree;
			if(safeTagOne.contains("$"))
				safeTagOne = safeTagOne.replace("$", "\\$");
			if(safeTagOne.contains("."))
				safeTagOne = safeTagOne.replace(".", "\\.");
			if(safeTagTwo.contains("$"))
				safeTagTwo = safeTagTwo.replace("$","\\$");
			if(safeTagTwo.contains("."))
				safeTagTwo = safeTagTwo.replace(".", "\\.");
			if(safeTagThree.contains("$"))
				safeTagThree = safeTagThree.replace("$","\\$");
			if(safeTagThree.contains("."))
				safeTagThree = safeTagThree.replace(".", "\\.");
			
			//System.out.println("The POS tags are: "+tagOne+" (safe is: "+safeTagOne+") and "+tagTwo+" (safe is: "+safeTagTwo+") and "+tagThree+" safe is: "+safeTagThree+")");
			
			int start = -1;
			Pattern posTag = Pattern.compile("(\\s|\\b)[^\\s]+/"+safeTagOne+"(\\b|\\s)[^\\s]+/"+safeTagTwo+"(\\b|\\s)[^\\s]+/"+safeTagThree+"(\\b|\\s)");
			Matcher wordFinder = posTag.matcher(beenTagged);
			int secondIndex;
			int thirdIndex;
			double numFound = 0;
			boolean isFound;
			while(true){
				isFound = wordFinder.find(start+1);
				if(isFound == false)
					break;
				numFound += 1;
				if(wordFinder.group(1).matches("\\s"))
					start=wordFinder.start()+1;
				else
					start=wordFinder.start();
				
				secondIndex = beenTagged.indexOf("/"+tagOne,start);
				String wordOne = beenTagged.substring(start,secondIndex);
				
				thirdIndex = beenTagged.indexOf("/"+tagTwo,secondIndex);
				String wordTwo = beenTagged.substring(secondIndex+2+tagOne.length(),thirdIndex);
				
				String wordThree = beenTagged.substring(thirdIndex+2+tagTwo.length(),beenTagged.indexOf("/"+tagThree,thirdIndex));
				
				System.out.println(wordOne+" & "+wordTwo+" & "+wordThree);
				wordsToHighlight.push(wordOne);
				wordsToHighlight.push(wordTwo);
				wordsToHighlight.push(wordThree);
				//TheTags thisTag = POSmap.TheTags.valueOf(theTag);
				//System.out.print("    ---> "+POSmap.tagToDescription(thisTag));
				
			}	
			
			attrib.setToModifyValue(numFound);
			featurePresentValue = numFound;
			
			String tagOneDef;
			String tagTwoDef;
			String tagThreeDef;
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
			try{
				tagThreeDef= POS.tagToDescription(TheTags.valueOf(tagThree));
			} catch(IllegalArgumentException iae){
				tagThreeDef = "punctuation/symbol";
			}
			
			
			highlightMap.put(getNextColor(),IndexFinder.findWordTrigramIndices(theDoc, wordsToHighlight));
			
			
			if(featureTargetValue > featurePresentValue){
				double numToAdd = Math.floor(100*(featureTargetValue - featurePresentValue)+.5)/100;
				setSuggestionLowToHigh("The description of the first part of speech tag '"+tagOne+"' is: "+tagOneDef+", the description of the second tag '"+tagTwo+"' is: "+tagTwoDef+", and the description of the third tag '"+tagThree+"' is: "+
				tagThreeDef+". Currently, you have too few of these part of speech trigrams (triplets) " +
						"in your document. You have '"+featurePresentValue+"', while you should have '"+featureTargetValue+"'. The highlighted words are examples of word triplets that " +
								"would be/ are tagged as "+stringInBraces+" in the context that they are used. Try to use these words in their context as a guideline " +
										"to add "+numToAdd+" word triplets that would be tagged as '"+stringInBraces+"'.");
			}
			else if(featurePresentValue > featureTargetValue){
				double numToRemove = Math.floor(100*(featurePresentValue - featureTargetValue)+.5)/100;
				setSuggestionHighToLow("The description of the first part of speech tag '"+tagOne+"' is: "+tagOneDef+", the description of the second tag '"+tagTwo+"' is: "+tagTwoDef+", and the description of the third tag '"+tagThree+"' is: "+
				tagThreeDef+". Currently, you have too many of these part of speech trigrams (triplets) " +
						"in your document. You have '"+featurePresentValue+"', while you should have '"+featureTargetValue+"'. The highlighted word triplets " +
								"have been tagged as "+stringInBraces+" in the context that they are used. Try to " +
										"eliminate "+numToRemove+" of these word triplets - though be careful to avoid simply substituting these words for different ones that fall into the same part of speech catagory.");
			}
			else
				setNoChangeNeeded(true); // safety measure... although this should NEVER be executed.
		}
	}
		

}
