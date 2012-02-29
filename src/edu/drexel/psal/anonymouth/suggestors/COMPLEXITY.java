package edu.drexel.psal.anonymouth.suggestors;


/**
 * Suggestor for COMPLEXITY
 * @author Andrew W.E. McDonald
 *
 */
public class COMPLEXITY extends TheOracle {

	protected void algorithm() {
		doesNotGetSuggestion = true;
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
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");

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
	
	
		
		if (super.featureTargetValue < super.featurePresentValue){
			
			LinkedList<String>[] compilation =(LinkedList<String>[]) new LinkedList[]{threeSylls,fourSylls,fiveSylls,sixSylls,sevenSylls,eightPlusSylls}; 
			LinkedList<String> everyone = listConcatenator(compilation);
			highlightMap.put(super.getNextColor(),IndexFinder.findIndices(theDoc,everyone));
			int numToRemove =  (int) (featurePresentValue-featureTargetValue);
			
			String suggestionHighToLow ="";
			setSuggestionHighToLow(suggestionHighToLow);
		}
		else if(super.featureTargetValue > super.featurePresentValue){
			int numToAdd = (int) (featureTargetValue-featurePresentValue);
			LinkedList<String>[] compilation =(LinkedList<String>[]) new LinkedList[]{oneSyll,twoSylls}; 
			LinkedList<String> everyone = listConcatenator(compilation);
			highlightMap.put(super.getNextColor(),IndexFinder.findIndices(theDoc,everyone));
			String suggestionLowToHigh ="Your document has too few complex words, It should have '"+featureTargetValue+"' complex words, however" +
					"it presently has '"+featurePresentValue+"' complex words. A complex word is defined as having 3 or more syllables. The highlighted words" +
							"identify all words with either one or two syllables. Ideally, you should add approximately '"+numToAdd+
							"' complex words to you document by substituting shorter 'simple' words for longer words containing at least 3 syllables.";
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
