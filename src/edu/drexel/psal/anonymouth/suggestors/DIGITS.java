package edu.drexel.psal.anonymouth.suggestors;

import java.util.ArrayList;

/**
 * Suggestor for DIGITS
 * @author Andrew W.E. McDonald
 *
 */
public class DIGITS extends TheOracle {

	@Override
	protected void algorithm() {
		// TODO Auto-generated method stub
		
		if(featureTargetValue > featurePresentValue){
			setSuggestionLowToHigh("Your document only has '"+featurePresentValue+"' occurances of the number "+super.stringInBraces+", however, it would " +
					"be ideal to have '"+featureTargetValue+"' occurances of "+super.stringInBraces+". See if there is anywhere that you would reasonably be able to fit one or more "+stringInBraces+" into your document.");
		}
		else if(featurePresentValue > featureTargetValue){
			String stringDoc = super.getDocument();
			ArrayList<int[]> theIndices = IndexFinder.findIndices(stringDoc, super.stringInBraces.replaceAll("\\p{C}", " "));
			highlightMap.put(getNextColor(), theIndices);
			setSuggestionHighToLow("Your document has '"+featurePresentValue+"' occurances of the number "+super.stringInBraces+". It should only have " +
			featureTargetValue+"' occurances of "+super.stringInBraces+". At the very least, try to exchange some numeric formed numbers for their 'word' counterparts.");
	}
		else
			setNoChangeNeeded(true);
		
	}

}
