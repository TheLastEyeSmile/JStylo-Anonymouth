package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for CHARACTER_SPACE
 * @author Andrew W.E. McDonald
 *
 */
public class CHARACTER_SPACE extends TheOracle {

	@Override
	protected void algorithm() {
		shouldNotHighlight = true;
		if(super.featurePresentValue > super.featureTargetValue){
			double amountToCut = super.featurePresentValue - super.featureTargetValue;
			setSuggestionHighToLow(sf.charSpaceSuggestion);   
		}
		else if(super.featurePresentValue < super.featureTargetValue){
			double amountToAdd = super.featureTargetValue - super.featurePresentValue;
			setSuggestionLowToHigh(sf.charSpaceSuggestion);   
		}
		else
			super.setNoChangeNeeded(true);
		
	}

}
