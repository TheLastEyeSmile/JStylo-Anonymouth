package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for LETTERS_PERCENTAGE
 * @author Andrew W.E. McDonald
 *
 */
public class LETTERS_PERCENTAGE extends TheOracle {

	@Override
	protected void algorithm() {
		// TODO Auto-generated method stub
		if(super.featurePresentValue > (100*super.featureTargetValue)){
			super.setSuggestionHighToLow("The ratio of letter characters to non-letter characters is too high in your document. Presently, your document consists of "+
					super.featurePresentValue+"% letters. Ideally, it would be only made up of "+(100*super.featureTargetValue)+"% letters. An easy way to change this is to " +
							"change any words that represent numbers, to their digit equivalent. (e.g seven -> 7).");
		}
		else if (super.featurePresentValue < (100*super.featureTargetValue)){
			super.setSuggestionLowToHigh("The ratio of letter characters to non-letter characters is too low in your document. Presently, your document consists of only "+
					super.featurePresentValue+"% letters. Ideally, it would be made up of "+(100*super.featureTargetValue)+"% letters. An easy way to change this is to " +
							"change any digits to their 'word' equivalent. (e.g 8 -> eight).");
		}
		else 
			super.setNoChangeNeeded(true);
	}

}
