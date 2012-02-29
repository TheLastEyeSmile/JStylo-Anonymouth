package edu.drexel.psal.anonymouth.calculators;

/**
 * Calculator for TOP_LETTER_TRIGRAMS
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_TOP_LETTER_TRIGRAMS extends Computer{

	@Override
	protected void compute() {
		isAvailable = true;
		char[] theText = super.getDocument().getProcessedText();
		int gramLength = stringInBraces.length();
		int textLength = theText.length;
		String thisGram;
		int i;
		int numOfTheseGrams = 0;
		for(i=0;i<=(textLength-gramLength);i++){
			thisGram = new String(theText,i,gramLength);
			if(thisGram.matches(stringInBraces))
				numOfTheseGrams++;
		}
		presentValue = numOfTheseGrams;
		
	}

}
