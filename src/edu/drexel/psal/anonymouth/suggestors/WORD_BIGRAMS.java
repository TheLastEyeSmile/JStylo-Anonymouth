package edu.drexel.psal.anonymouth.suggestors;

/**
 * Suggestor for WORD_BIGRAMS
 * @author Andrew W.E. McDonald
 *
 */
public class WORD_BIGRAMS extends TheOracle{

	@Override
	protected void algorithm() {
		
		String theDoc = super.getDocument();
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");

		String theWords = super.stringInBraces.replaceAll("\\p{C}", " ");;
		System.out.println(theWords);
		int startWord = theWords.indexOf("(")+1;
		int endWord = theWords.indexOf(")"); 
		String wordOne = theWords.substring(startWord,endWord);
		String wordTwo = theWords.substring(theWords.indexOf("(",startWord)+1,theWords.indexOf(")",endWord+1));
		
		if(wordOne.contains("."))
			wordOne = wordOne.replace(".","\\.");
		if(wordOne.contains("?"))
			wordOne = wordOne.replace("?","\\?");
		if(wordTwo.contains("."))
			wordTwo = wordTwo.replace(".","\\.");
		if(wordTwo.contains("?"))
			wordTwo = wordTwo.replace("?","\\?");
		
		if(featureTargetValue > featurePresentValue){
			double numToAdd = Math.floor(100*(featureTargetValue - featurePresentValue)+.5)/100;
			setSuggestionLowToHigh("The number of appearances of the word bigram (pair), '"+wordOne+" "+wordTwo+"' is less than it should be. Currently, this word pair " +
					"appears "+featurePresentValue+" times; however, it should appear "+featureTargetValue+" times. If possible, try to fit this word pair "+numToAdd+" more times " +
							"thoughout your document.");
		}
		else if(featurePresentValue > featureTargetValue){
			double numToRemove = Math.floor(100*(featurePresentValue - featureTargetValue)+.5)/100;
			highlightMap.put(getNextColor(), IndexFinder.findIndices(theDoc,"\\b"+wordOne+"\\s*"+wordTwo+"\\b{1}+"));
			setSuggestionHighToLow("The number of appearances of the word bigram (pair), '"+wordOne+" "+wordTwo+"' is greater than it should be. Currently, this word pair " +
					"appears "+featurePresentValue+" times; however, it should appear "+featureTargetValue+" times. If possible, remove "+numToRemove+" of the highlighted word pairs.");
		}
		else
			setNoChangeNeeded(true);
		
		
	}

}

