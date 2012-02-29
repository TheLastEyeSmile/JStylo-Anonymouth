package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for LETTER_SPACE
 * @author Andrew W.E. McDonald
 *
 */
public class LETTER_SPACE extends TheOracle {

	@Override
	protected void algorithm() {
		if(featureTargetValue > featurePresentValue){
			setSuggestionLowToHigh(sf.letterSpaceSuggestion);
		}
		else if (featurePresentValue > featureTargetValue){
			setSuggestionHighToLow(sf.letterSpaceSuggestion);
	}
		else
			setNoChangeNeeded(true);
		
	}

}
