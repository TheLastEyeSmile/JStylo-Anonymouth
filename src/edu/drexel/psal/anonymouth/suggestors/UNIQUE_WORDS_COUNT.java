package edu.drexel.psal.anonymouth.suggestors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Suggestor for UNIQUE_WORDS_COUNT
 * @author Andrew W.E. McDonald
 *
 */
public class UNIQUE_WORDS_COUNT extends TheOracle {

	@Override
	protected void algorithm() {
		//Prepares the string document for tokenizing 
        String theDoc = super.getDocument(); 
        String cleanDoc = theDoc.replaceAll("[\\r\\n\\t.?!\",;()\\[\\]\\\\]", " ");
        
        StringTokenizer tok = new StringTokenizer(cleanDoc);
        
        HashMap<String,Integer> theWords = new HashMap<String,Integer>();
        while( tok.hasMoreTokens() )
        {
            String thisWord = tok.nextToken();
            if( theWords.containsKey(thisWord) ){
            		int temp = theWords.get(thisWord)+1;
            		theWords.put(thisWord, temp+1);
            } 
            else
            		theWords.put(thisWord,1);
        }
        int numWords = theWords.size();
        Set<String> wordSet = theWords.keySet();
        Iterator<String> wsIter = wordSet.iterator();
        UniqueWord[] uw = new UniqueWord[numWords];
        int index = 0;
        while(wsIter.hasNext()){
        		String key = wsIter.next();
        		Integer value = theWords.get(key);
        		uw[index] =new UniqueWord(key,value);
        		index++;
        }
        int sortOrder = 1;
        if(featureTargetValue > featurePresentValue)
        		sortOrder = -1;
        final int theSortOrder = sortOrder;
        Arrays.sort(uw, new Comparator<UniqueWord>(){
			@Override
			public int compare(UniqueWord first, UniqueWord second) {
				// TODO Auto-generated method stub
				return	((theSortOrder)*first.getCount().compareTo(second.getCount())); 
			}
		});
        LinkedList<String> theWordsToHighlight = new LinkedList<String>();
        
        if (featureTargetValue >featurePresentValue){
        		int numUniqueWords = uw.length;
        		int i=0;
        		while(true){
        			UniqueWord tempWord = uw[i];
        			if(tempWord.getCount() > 1)
        				theWordsToHighlight.push(tempWord.getWord());
        			else
        				break;
        			i++;
        		}
        		ArrayList<int[]> theOnesToHighlight = IndexFinder.findIndices(cleanDoc, theWordsToHighlight);
        		highlightMap.put(super.getNextColor(), theOnesToHighlight);
        		setSuggestionHighToLow(sf.uniqueWordsSuggestion);
        	
        }
        else if (featurePresentValue > featureTargetValue){ 
        int numUniqueWords = uw.length;
		int i=0;
		while(true){
			UniqueWord tempWord = uw[i];
			if(tempWord.getCount() == 1)
				theWordsToHighlight.push(tempWord.getWord());
			else
				break;
			i++;
		}
		ArrayList<int[]> theOnesToHighlight = IndexFinder.findIndices(cleanDoc, theWordsToHighlight);
		highlightMap.put(super.getNextColor(), theOnesToHighlight);
		setSuggestionHighToLow(sf.uniqueWordsSuggestion);
        }
        else
        		setNoChangeNeeded(true);
        
        
		

	}
}	


	/**
	 * Holds a single unique word, with number of appearances
	 * @author Andrew W.E. McDonald
	 *
	 */
	class UniqueWord{
		private String word;
		private Integer count;
		
		/**
		 * Constructor
		 * @param word the word
		 * @param count number of appearances
		 */
		UniqueWord(String word, Integer count){
			this.word = word;
			this.count = count;
		}
		
		/**
		 * Returns the word
		 * @return
		 *  the word
		 */
		public String getWord(){
			return word;
		}
		
		/**
		 * Returns the count (number of appearances of the word)
		 * @return
		 *  number of appearances
		 */
		public Integer getCount(){
			return count;
		}
	}

			
		
