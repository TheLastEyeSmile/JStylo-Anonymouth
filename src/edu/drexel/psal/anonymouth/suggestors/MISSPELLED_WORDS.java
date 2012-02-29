package edu.drexel.psal.anonymouth.suggestors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Suggestor for MISSPELLED_WORDS
 * @author Andrew W.E. McDonald
 *
 */
public class MISSPELLED_WORDS extends TheOracle{

	@Override
	protected void algorithm() {
		String theDoc = super.getDocument();
		if(featureTargetValue > featurePresentValue){
			setSuggestionLowToHigh("It appears as if you spell the word '"+stringInBraces+"' suspiciously well. If you can make out what the correct spelling is of the misspelled word ("+stringInBraces+"), " +
					"it might not be a bad idea to find it in your document and alter the spelling (to '"+stringInBraces+"')");
		}
		else if (featurePresentValue > featureTargetValue){
			highlightMap.put(getNextColor(), IndexFinder.findIndices(theDoc, stringInBraces.replaceAll("\\p{C}", " ")));
			setSuggestionHighToLow("The (incorrectly spelled) word, '"+stringInBraces+"', has been found in your document. Its appearance(s) has/have been highlighted. If you can, modify the spelling " +
					"so that '"+stringInBraces+"' is spelled correctly.");
		}
		else
			setNoChangeNeeded(true);
		
		
	}
	
	

}
