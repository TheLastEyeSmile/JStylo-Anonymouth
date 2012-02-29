package edu.drexel.psal.anonymouth.suggestors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Suggestion for AVERAGE_SYLLABLES_IN_WORD
 * @author Andrew W.E. McDonald
 *
 */
public class AVERAGE_SYLLABLES_IN_WORD extends TheOracle{

	//@SuppressWarnings("unchecked")
	//@Override
	protected void algorithm() {
		// TODO Auto-generated method stub
		/*
		LinkedList<String> eightPlusSylls = new LinkedList<String>();
		LinkedList<String> sevenSylls = new LinkedList<String>();
		LinkedList<String> sixSylls = new LinkedList<String>();
		LinkedList<String> fiveSylls = new LinkedList<String>();
		LinkedList<String> fourSylls = new LinkedList<String>();
		LinkedList<String> threeSylls = new LinkedList<String>();
		LinkedList<String> twoSylls = new LinkedList<String>();
		LinkedList<String> oneSyll = new LinkedList<String>();
		int[][] wordCountBySyllGroup = new int[8][2];// int[0] <=> word count for syllable group && int[1] <=> syllable group 
		int i=0;
		for(i=0; i<wordCountBySyllGroup.length; i++){
			wordCountBySyllGroup[i][0]=0;
			wordCountBySyllGroup[i][1]=i;
		}
		
		String theVowels = "aeiouyAEIOUY";

		String theDoc = super.getDocument();
        //theDoc = theDoc.replaceAll("[\\r\\n\\t.?!\":\\-,;()\\[\\]\\\\]", " ");
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");
		
		StringTokenizer st = new StringTokenizer(theDoc);
		String word;
		while(st.hasMoreTokens() == true){
			word = st.nextToken();
            int sylls = 0; //NOTE: this whole loop came from  JGAAP's source code.
            for (int j = 0; j < word.length(); j++) {
                    if ((theVowels.indexOf(word.charAt(j)) != -1)
                                    && ((j == word.length() - 1) || (theVowels.indexOf(word.charAt(j + 1)) == -1))) {
                           sylls++;
                    }
            }
            if (sylls == 0) {
                    sylls = 1; // handle words like "Dr" by setting to 1
            }
            // here is where we will collect the number of syllables in each word. 
            switch (sylls){
            case 1: oneSyll.push(word);
            		wordCountBySyllGroup[0][0]++;
            		break;
            case 2: twoSylls.push(word);
            		wordCountBySyllGroup[1][0]++;
		    		break;
            case 3: threeSylls.push(word);
            		wordCountBySyllGroup[2][0]++;
		    		break;
            case 4: fourSylls.push(word);
            		wordCountBySyllGroup[3][0]++;
		    		break;
            case 5: fiveSylls.push(word);
            		wordCountBySyllGroup[4][0]++;
		    		break;
            case 6: sixSylls.push(word);
            		wordCountBySyllGroup[5][0]++;
	            break;
            case 7: sevenSylls.push(word);
            		wordCountBySyllGroup[6][0]++;
	            break;
		    	default: eightPlusSylls.push(word);
            		wordCountBySyllGroup[7][0]++;
			    	break;
            }
        }
		// sort the int[][] array in decreasing order (multiply result of compareTo() by '-1'  just because it makes life easier)
		Arrays.sort(wordCountBySyllGroup, new Comparator<int[]>(){
			@Override
			public int compare(int[] first, int[] second) {
				return (-1)*((Integer)first[0]).compareTo((Integer)second[0]);
			}
		});
		
		
		LinkedList<String>[] compilation =(LinkedList<String>[]) new LinkedList[]{oneSyll,twoSylls,threeSylls,fourSylls,fiveSylls,sixSylls,sevenSylls,eightPlusSylls}; 
		//System.out.println("passed everything in average syllables in word up to feature target vs feature present comparision");
		if (super.featureTargetValue < super.featurePresentValue){
			int roundedPresent = (int) Math.ceil(super.featurePresentValue); // round all the way down
			
			
			//take top 2 (greatest word count) LinkedLists with syllable count >= floor(presentValue) .... then, change as many as possible because I dont have a better suggestion, and i doubt it will be easy 
			boolean secondFound = false;
			int[] firstList = wordCountBySyllGroup[0];
			int[] secondList = wordCountBySyllGroup[1];
			for(i=2;i<wordCountBySyllGroup.length;i++){
				if(firstList[1]<roundedPresent){
					firstList = secondList;
					secondList = wordCountBySyllGroup[i];
				}
				else if(secondList[1]<roundedPresent){
					secondList=wordCountBySyllGroup[i];
				}
				else
					break;
			}
			if(secondList[1]<roundedPresent)
				secondFound = false;
			
		
			// Now that firstList and secondList have been selected (presumably), the actual lists containing the words are selected.	
			LinkedList<String> concatedList;
			if (secondFound == true)
				concatedList = listConcatenator((LinkedList<String>[]) new LinkedList[]{compilation[firstList[1]],compilation[secondList[1]]});
			else
				concatedList = listConcatenator((LinkedList<String>[]) new LinkedList[]{compilation[firstList[1]]});
			highlightMap.put(super.getNextColor(),IndexFinder.findIndices(theDoc,concatedList));
			*/
			String suggestionHighToLow = sf.averageSyllablesSuggestion; 
			setSuggestionHighToLow(suggestionHighToLow);
			/*
		}
		else if(super.featureTargetValue > super.featurePresentValue){
			int roundedPresent = (int) Math.ceil(super.featurePresentValue); // round  all the way up
			// take top 2 (greatest word count) LinkedLists with syllable count <= ceil(presentValue)... and then (again), change as many as possible - at least for the time being. 
			boolean secondFound = false;
			int[] firstList = wordCountBySyllGroup[0];
			int[] secondList = wordCountBySyllGroup[1];
			for(i=2;i<wordCountBySyllGroup.length;i++){
				if(firstList[1]>roundedPresent){
					firstList = secondList;
					secondList = wordCountBySyllGroup[i];
				}
				else if(secondList[1]>roundedPresent){
					secondList=wordCountBySyllGroup[i];
				}
				else
					break;
			}
			if(secondList[1]>roundedPresent)
				secondFound = false;
			
			
			LinkedList<String> concatedList;
			if(secondFound == true)
				concatedList = listConcatenator((LinkedList<String>[]) new LinkedList[]{compilation[firstList[1]],compilation[secondList[1]]});
			else
				concatedList = listConcatenator((LinkedList<String>[]) new LinkedList[]{compilation[firstList[1]]});
			highlightMap.put(super.getNextColor(),IndexFinder.findIndices(theDoc,concatedList));
			//System.out.println("Suggestion should be about to be added..");
			 
			 */
			String suggestionLowToHigh =sf.averageSyllablesSuggestion; 
			setSuggestionLowToHigh(suggestionLowToHigh);
			/*
			//System.out.println("SUGGESTION:"+suggestionLowToHigh);
			}
			*/
		}

	/*		 
	public LinkedList<String> listConcatenator(LinkedList<String>[] manyLists){
		int numLists = manyLists.length;
		LinkedList<String> singleList = new LinkedList<String>();
		int i;
		int j;
		for(i=0;i<numLists;i++){
			int currentListLength = manyLists[i].size();
			for(j=0;j< currentListLength;j++){
				singleList.push(manyLists[i].pop());
			}
		}
		return singleList;
	}
	*/
	
	

}
