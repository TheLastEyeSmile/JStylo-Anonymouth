package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for WORDS feature
 * @author Andrew W.E. McDonald
 *
 */
public class WORDS extends TheOracle{

	@Override
	protected void algorithm() {
		
		if(featureTargetValue > featurePresentValue){
			setSuggestionLowToHigh("The word '"+stringInBraces+"' appears "+featurePresentValue+" times in your document. It would be helpful to search out " +
					"additional places throughout your document that you could fit '"+stringInBraces+"' in order to bring its total number of appearances to "+featureTargetValue+".");
		}
		else if (featurePresentValue > featureTargetValue){
			String theDoc = super.getDocument();
			highlightMap.put(getNextColor(), IndexFinder.findIndices(theDoc,"\\b"+stringInBraces.replaceAll("\\p{C}", " ")+"\\b"));
			double numToReplace = featurePresentValue - featureTargetValue;
			setSuggestionHighToLow("The word '"+stringInBraces+"' appears "+featurePresentValue+" times in your document. It would be helpful to remove/replace this word in "+numToReplace+
					" places across your document in order to lower its total number of appearances to "+featureTargetValue+".");
		}
		else
			setNoChangeNeeded(true);
	
	}

}
