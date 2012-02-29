package edu.drexel.psal.anonymouth.suggestors;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Suggestion for FUNCTION_WORDS feature
 * @author Andrew W.E. McDonald
 *
 */
public class FUNCTION_WORDS extends TheOracle {

	@Override
	protected void algorithm() {
		
		if(featureTargetValue > featurePresentValue){
			setSuggestionLowToHigh("The function word '"+stringInBraces+"' only appears "+featurePresentValue+" times in your document. Ideally, it would " +
					"appear "+featureTargetValue+" times. The included dictionary may help in finding other words that you have used that are synonymous with '"+stringInBraces+"'.");
		}
		else if (featurePresentValue > featureTargetValue){
			String theDoc = super.getDocument();
			theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");
			ArrayList<int[]> theIndices = IndexFinder.findIndices(theDoc, "\\b"+stringInBraces.replaceAll("\\p{C}", " ")+"\\b{1}+");
			highlightMap.put(getNextColor(), theIndices);
			setSuggestionHighToLow("The highlighted words show occurances of the function word '"+stringInBraces+"'. Right now, there are "+featurePresentValue+" occurances of this word. " +
					"You should try to reduce this number to "+featureTargetValue+" if possible. Using the included dictionary may help in finding synonyms with '"+stringInBraces+"'.");
	}
		else
			setNoChangeNeeded(true);
	
	}
	


}
