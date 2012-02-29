package edu.drexel.psal.anonymouth.suggestors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Suggestion for TOP_LETTER_BIGRAMS
 * @author Andrew W.E. McDonald
 *
 */
public class TOP_LETTER_BIGRAMS extends TheOracle{

	ArrayList<int[]> toHighlight; 
	
	@Override
	protected void algorithm() {
		
		if(featurePresentValue == featureTargetValue){
			setNoChangeNeeded(true);
		}
		else{
			toHighlight = new ArrayList<int[]>();
			findIndices();
			Color highlightColor = super.getNextColor();
			super.highlightMap.put(highlightColor,toHighlight);
			double difference = super.featurePresentValue - super.featureTargetValue;
			int numToChange = (int) Math.floor(Math.abs(difference));
			//System.out.println("Difference: "+difference+" NumToChange: "+numToChange+" present value: "+super.featurePresentValue+" target value: "+super.featureTargetValue);
			if(difference >0){
				String suggestionHighToLow = "Your document has '"+featurePresentValue+"' occurances of '"+stringInBraces+"'. '"+numToChange+"' of the highlighted values should be changed in order to bring the number of '"+
				super.stringInBraces+"' character grams down to the target value of '"+featureTargetValue+"'. The included dictionary may help to find synonyms for words you presently have.";
			setSuggestionHighToLow(suggestionHighToLow);
			}
			else if (difference < 0){
				String suggestionLowToHigh = "The count of '"+super.stringInBraces+"' in your doucment is '"+featurePresentValue+"', which is too low by'"+numToChange+"'. If possible, add '"+numToChange+
						"' of these in the document. Clicking on additional features may aid in identifying appropriate places to make these changes. You can use the included dictionary to search for words that contain specific strings of letters, " +
						"such as 'ns'.";
				setSuggestionLowToHigh(suggestionLowToHigh);
			}
			else
				super.setNoChangeNeeded(true);
		}
	}
	
	/**
	 * Finds indices of letter grams
	 */
	public void findIndices(){	
		String stringToTest=super.getDocument();
		String  theRegEx =super.stringInBraces.replaceAll("\\p{C}", " ");;
		int lengthRegEx = theRegEx.length();
		Pattern p = Pattern.compile(theRegEx);
		Matcher m = p.matcher(stringToTest);
		int startIndex=-1;
		while(true){
		boolean found = m.find(startIndex+1);
		if(found == true){
			int index = m.start();
			System.out.println(index);
			toHighlight.add(new int[]{index,index+lengthRegEx});
			startIndex=index;
			m.reset();
		}
		else
			break;
		}
		
	}
	
	

	

}
