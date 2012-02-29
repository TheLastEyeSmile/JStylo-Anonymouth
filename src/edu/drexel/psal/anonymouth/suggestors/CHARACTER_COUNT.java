package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for CHARACTER_COUNT
 * @author Andrew W.E. McDonald
 *
 */
public class CHARACTER_COUNT extends TheOracle {

	@Override
	protected void algorithm() {
		if(super.featurePresentValue > super.featureTargetValue){
			super.setSuggestionHighToLow("The present number of characters in your document is greater than it should be. If general document length is an" +
					" issue to you, see if it is possible to shorten your document. Currently, you have "+super.featurePresentValue+" characters in your document. " +
							"Ideally, your document would have closer to "+super.featureTargetValue+" characters.");
		}
		else if (super.featurePresentValue < super.featureTargetValue){
			super.setSuggestionLowToHigh("The present number of characters in your document is smaller than it should be. If general document length is an" +
					" issue to you, see if it is possible to lengthen your document. Currently, you have "+super.featurePresentValue+" characters in your document. " +
					"Ideally, your document would have closer to "+super.featureTargetValue+" characters.");
		}
		else
			super.setNoChangeNeeded(true);
		
	}

}
