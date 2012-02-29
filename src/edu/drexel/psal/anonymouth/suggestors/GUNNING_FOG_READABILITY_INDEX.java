package edu.drexel.psal.anonymouth.suggestors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventSet;

import edu.drexel.psal.jstylo.eventDrivers.SentenceCounterEventDriver;

/**
 * Suggestor to alter GUNNING_FOG_READABILITY INDEX
 * @author Andrew W.E. McDonald 
 *
 */
public class GUNNING_FOG_READABILITY_INDEX extends TheOracle{

	//@SuppressWarnings("unchecked")
	//@Override
	protected void algorithm() {
		
		/*
		LinkedList<String> eightPlusSylls = new LinkedList<String>();
		LinkedList<String> sevenSylls = new LinkedList<String>();
		LinkedList<String> sixSylls = new LinkedList<String>();
		LinkedList<String> fiveSylls = new LinkedList<String>();
		LinkedList<String> fourSylls = new LinkedList<String>();
		LinkedList<String> threeSylls = new LinkedList<String>();
		LinkedList<String> twoSylls = new LinkedList<String>();
		LinkedList<String> oneSyll = new LinkedList<String>();
		
		String theVowels = "aeiouyAEIOUY";

		String theDoc = super.getDocument();
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");

		int i=0;
		Document doc = new Document();
		doc.setText(super.getDocument().toCharArray());
		int numSentences = (int)new SentenceCounterEventDriver().getValue(doc); 
		
		@SuppressWarnings("unused")
		int numComplex =0;
		StringTokenizer st = new StringTokenizer(theDoc);
		String word;
		int numWords=0;
		while(st.hasMoreTokens() == true){
			word = st.nextToken();
			numWords++;
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
            		break;
            case 2: twoSylls.push(word);
		    		break;
            case 3: threeSylls.push(word);
            		numComplex++;
		    		break;
            case 4: fourSylls.push(word);
	            numComplex++;
		    		break;
            case 5: fiveSylls.push(word);
	            numComplex++;
		    		break;
            case 6: sixSylls.push(word);
	            numComplex++;		
	            break;
            case 7: sevenSylls.push(word);
	            numComplex++;		
	            break;
		    	default: eightPlusSylls.push(word);
			    	numComplex++;
			    	break;
            }
        }
		
		//System.out.println("number of words: "+numWords);
		//System.out.println("number of sentences: "+numSentences);
		//double numComplexWordsDesired =( numWords * ( (super.featureTargetValue - (0.4*(numWords/numSentences)))/100));
	
		if (super.featureTargetValue < super.featurePresentValue){
			LinkedList<String>[] compilation =(LinkedList<String>[]) new LinkedList[]{threeSylls,fourSylls,fiveSylls,sixSylls,sevenSylls,eightPlusSylls}; 
			LinkedList<String> everyone = listConcatenator(compilation);
			highlightMap.put(super.getNextColor(),IndexFinder.findIndices(theDoc,everyone));
			int numToRemove = (int) (featurePresentValue-featureTargetValue);
			String suggestionHighToLow ="Your document appears to be too complex. It has a FOG index of '"+super.featurePresentValue+"', however" +
					"it would ideally have a FOG index of '"+featureTargetValue+"'. Try decreasing the number of complex words (3 or more syllables), as well as the length" +
							" of your sentences (i.e. make some complex or compound sentences simple ones - insead of ';' or ',' simply create a new sentence). The highlighted words" +
							"identify all words with three or more syllables. ";
			setSuggestionHighToLow(suggestionHighToLow);
		}
		else if(super.featureTargetValue > super.featurePresentValue){
			int numToAdd = (int) (featureTargetValue-featurePresentValue);
			highlightMap.put(super.getNextColor(),IndexFinder.findIndices(theDoc,twoSylls));
			String suggestionLowToHigh ="Your document appears to be too easy to read (simple). It has a FOG index of '"+super.featurePresentValue+"', however" +
					"it would ideally have a FOG index of '"+featureTargetValue+"'. Try increasing the number of complex words (3 or more syllables), as well as the length" +
					" of your sentences (i.e. make some simple sentences either complex or compound by using ';' to link two complete thoughts, or by using more descriptive" +
					" words: 'large' could be replaced by 'muscular', 'overweight','enormous','voluminous', etc.). The highlighted words" +
					"identify all words with one or two syllables. ";
			setSuggestionLowToHigh(suggestionLowToHigh);
			}
		else
			super.setNoChangeNeeded(true);
		}

			 
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
	*/
	}
		  
			 
}		 
			 

	

	
	