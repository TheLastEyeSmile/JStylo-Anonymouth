package edu.drexel.psal.anonymouth.suggestors;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Suggestor for DIGITS_PERCENTAGE
 * @author Andrew W.E. McDonald
 *
 */
public class DIGITS_PERCENTAGE extends TheOracle{

	@Override
	protected void algorithm() {
		if(featureTargetValue > featurePresentValue){
			super.setSuggestionLowToHigh("Your document contains '"+featurePresentValue+"%' digits. In order to fit into the optimal target cluster, it should have '"+
					featureTargetValue+"%' digits. If there is any way to introduce/sprinkle a few digits into your document, it would be helpful in anoymizing this document. " +
							"A good way to do this would be to find word representations of numbers, and swap them for their numeric equivalents. You may also find it helpful to " +
							"decrease the number of non-digit characters.");
		}
		else if(featurePresentValue > featureTargetValue){
			String stringDoc = super.getDocument();
			String regEx = "\\d{1}+";
			ArrayList<int[]> theIndices = IndexFinder.findIndices(stringDoc,regEx);
			highlightMap.put(getNextColor(), theIndices);
			super.setSuggestionHighToLow("Your document contains '"+featurePresentValue+"%' digits. In order to fit into the optimal target cluster, it should have '"+
					featureTargetValue+"%' digits. The highlighted characters are all of the digits that appear in your document. Try to replace some of these numbers for their " +
							"'word' equivalents (e.g 200 years -> two hundred years). You may also consider increasing the number of non-digit characters.");
	}
		else
			setNoChangeNeeded(true);
	
	
	}
}
