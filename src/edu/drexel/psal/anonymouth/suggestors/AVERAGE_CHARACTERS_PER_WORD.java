package edu.drexel.psal.anonymouth.suggestors;

import java.util.LinkedList;
import java.util.StringTokenizer;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.SentenceCounterEventDriver;

/**
 * Suggestor for AVERAGE_CHARACTERS_PER_WORD
 * @author Andrew W.E. McDonald
 *
 */
public class AVERAGE_CHARACTERS_PER_WORD extends TheOracle {

	@Override
	protected void algorithm() throws EventGenerationException {
		LinkedList<String> greaterThan = new LinkedList<String>();
		LinkedList<String> lessThan = new LinkedList<String>();
		String theDoc = super.getDocument();
		theDoc = theDoc.replaceAll("[“”‘’„˚˙‚’‘`*$%@#~\\r\\n\\t.?!\",;:()\\[\\]\\\\]"," ");

		int i=0;
		Document doc = new Document();
		doc.setText(super.getDocument().toCharArray());
		int numSentences = (int)new SentenceCounterEventDriver().getValue(doc); 
		
		
		@SuppressWarnings("unused")
		int numWords =0;
		StringTokenizer st = new StringTokenizer(theDoc);
		String word;
		while(st.hasMoreTokens() == true){
			numWords++;
			word = st.nextToken();
			if(word.length() > featureTargetValue)
				greaterThan.push(word);
			else if(word.length() < featureTargetValue)
				lessThan.push(word);
		}   
		
		if(featureTargetValue > featurePresentValue){
			highlightMap.put(getNextColor(), IndexFinder.findIndices(theDoc, lessThan));
			setSuggestionLowToHigh("The average number of characters per word is less than it should be in your document. Currently, your document has '" +
					featurePresentValue+"' characters per word, however, it should have '"+featureTargetValue+"' characters per word. The highlighted words all have" +
							" fewer than "+featureTargetValue+" characters per word; it would be helpful to eliminate some of the shorter words in favor of longer ones in order " +
									"to bring your average characters per word up to its target value.");
		}
		else if (featurePresentValue > featureTargetValue){
			highlightMap.put(getNextColor(), IndexFinder.findIndices(theDoc, greaterThan));
			setSuggestionHighToLow("The average number of characters per word is greater than it should be in your document. Currently, your document has '" +
					featurePresentValue+"' characters per word, however, it should have '"+featureTargetValue+"' characters per word. The highlighted words all have" +
					" more than "+featureTargetValue+" characters per word; it would be helpful to replace these longer words with one or more shorter ones to " +
							"to bring your average characters per word down to its target value.");
		}
		else
			setNoChangeNeeded(true);
		
	}

}
