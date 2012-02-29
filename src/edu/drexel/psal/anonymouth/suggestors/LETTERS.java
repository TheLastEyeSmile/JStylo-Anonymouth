package edu.drexel.psal.anonymouth.suggestors;

import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Suggestor for LETTERS
 * @author Andrew W.E. McDonald
 *
 */
public class LETTERS extends TheOracle {

	@Override
	protected void algorithm() {
		String theDoc = super.getDocument();
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");
		theDoc = theDoc.toLowerCase();
		StringTokenizer st = new StringTokenizer(theDoc);
		
		
		LinkedList<String> yesLetter = new LinkedList<String>();
		LinkedList<String> noLetter = new LinkedList<String>();
		
		String word;
		while(st.hasMoreTokens()){
			word = st.nextToken();
			if(word.contains(stringInBraces.replaceAll("\\p{C}", " ")))
				yesLetter.push(word);
			else
				noLetter.push(word);
			
		}
		
		if(featureTargetValue > featurePresentValue){
			highlightMap.put(getNextColor(),IndexFinder.findIndices(theDoc, noLetter));
			setSuggestionLowToHigh("The the letter '"+stringInBraces+"' appears only "+featurePresentValue+" times. However, it would be benificial to increase this number to '"+featureTargetValue+"'." +
					" The highlighted words do NOT contain the letter '"+stringInBraces+"' - it may be helpful to search for synonyms for some of the highlighted words, as some synonyms may contain '"+stringInBraces+"'."); 
		}
		else if (featurePresentValue > featureTargetValue){
			highlightMap.put(getNextColor(),IndexFinder.findIndices(theDoc, yesLetter));
			setSuggestionHighToLow("The the letter '"+stringInBraces+"' appears "+featurePresentValue+" times. However, it would be benificial to decrease this number to '"+featureTargetValue+"'." +
					" The highlighted words DO contain the letter '"+stringInBraces+"' - it may be helpful to search for synonyms for some of the highlighted words, as some synonyms may not contain '"+stringInBraces+"'."); 
		}
		else
			setNoChangeNeeded(true);
		
	}
}
