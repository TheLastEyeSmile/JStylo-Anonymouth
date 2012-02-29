package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for SENTENCE_COUNT
 * @author Andrew W.E. McDonald
 *
 */
public class SENTENCE_COUNT extends TheOracle{

	@Override
	protected void algorithm() {
		super.setSuggestionHighToLow(sf.sentenceCountSuggestion);
		super.setSuggestionLowToHigh(sf.sentenceCountSuggestion);
		
	}

}
