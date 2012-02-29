package edu.drexel.psal.anonymouth.suggestors;


/**
 * Suggestor for WORD_TRIGRAMS
 * @author Andrew W.E. McDonald
 *
 */
public class WORD_TRIGRAMS extends TheOracle {

	@Override
	protected void algorithm() {

		
		String theDoc = super.getDocument();
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");

		String theWords = super.stringInBraces.replaceAll("\\p{C}", " ");;
		//System.out.println(theWords);
		int startWord = theWords.indexOf("(")+1;
		int endWord = theWords.indexOf(")"); 
		String wordOne = theWords.substring(startWord,endWord);
		int startWordTwo = theWords.indexOf("(",startWord)+1;
		int endWordTwo = theWords.indexOf(")",endWord+1);
		String wordTwo = theWords.substring(startWordTwo,endWordTwo);
		String wordThree = theWords.substring(theWords.indexOf("(",startWordTwo)+1,theWords.indexOf(")",endWordTwo+1));
		if(wordOne.contains("."))
			wordOne = wordOne.replace(".","\\.");
		if(wordOne.contains("?"))
			wordOne = wordOne.replace("?","\\?");
		if(wordTwo.contains("."))
			wordTwo = wordTwo.replace(".","\\.");
		if(wordTwo.contains("?"))
			wordTwo = wordTwo.replace("?","\\?");
		if(wordThree.contains("."))
			wordThree = wordThree.replace(".","\\.");
		if(wordThree.contains("."))
			wordThree = wordThree.replace("?","\\?");
		
		
		if(featureTargetValue > featurePresentValue){
			double numToAdd = Math.floor(100*(featureTargetValue - featurePresentValue)+.5)/100;
			setSuggestionLowToHigh("The number of appearances of the word trigram (triplet), '"+wordOne+" "+wordTwo+" "+wordThree+"' is less than it should be. Currently, this word trigram " +
					"appears "+featurePresentValue+" times; however, it should appear "+featureTargetValue+" times. If possible, try to fit this word trigram "+numToAdd+" more times " +
							"thoughout your document.");
		}
		else if(featurePresentValue > featureTargetValue){
			double numToRemove = Math.floor(100*(featurePresentValue - featureTargetValue)+.5)/100;
			highlightMap.put(getNextColor(), IndexFinder.findIndices(theDoc,"\\b"+wordOne+"\\s*"+wordTwo+"\\s*"+wordThree+"\\b{1}+"));
			setSuggestionHighToLow("The number of appearances of the word trigram (triplet), '"+wordOne+" "+wordTwo+" "+wordThree+"' is greater than it should be. Currently, this word triplet " +
					"appears "+featurePresentValue+" times; however, it should appear "+featureTargetValue+" times. If possible, remove "+numToRemove+" of the highlighted word trigrams.");
		}
		else
			setNoChangeNeeded(true);
		
		
	}

}
