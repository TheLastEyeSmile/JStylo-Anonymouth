package edu.drexel.psal.anonymouth.suggestors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.jgaap.eventDrivers.SentenceEventDriver;
import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;
import com.jgaap.generics.EventSet;

import edu.drexel.psal.jstylo.eventDrivers.WordCounterEventDriver;

/**
 * Suggestor for AVERAGE_SENTENCE_LENGTH 
 * @author Andrew W.E. McDonald
 *
 */
public class AVERAGE_SENTENCE_LENGTH extends TheOracle {

	@Override
	protected void algorithm() throws EventGenerationException {
		
		String theDoc = super.getDocument();
		Document doc = new Document();
		doc.setText(theDoc.toCharArray());	
		SentenceEventDriver sentenceEventDriver = new SentenceEventDriver();
	    EventSet sentenceSet = sentenceEventDriver.createEventSet(doc);  
	    ArrayList<String> allSentences = new ArrayList<String>();
	    
	    
	    
	    for (int i = 0; i < sentenceSet.size(); i++){
	    	allSentences.add( sentenceSet.eventAt(i).getEvent() );
	    	//System.out.println(allSentences.get(i).toString());
	    }
	    
	    
	    int numSentences = allSentences.size();
	    ArrayList<Double> sentenceWordCount = new ArrayList<Double>(numSentences);
	    for(int i=0;i<numSentences;i++){
	    		Document justASentence = new Document();
	    		justASentence.setText(allSentences.get(i).toCharArray());
		    WordCounterEventDriver  wordEventDriver = new WordCounterEventDriver();
		    double wordCount = wordEventDriver.getValue(justASentence);
		    sentenceWordCount.add(i,wordCount);
		    //System.out.println(sentenceWordCount.get(i));
	    } 
	    ArrayList<int[]> shortIndices = new ArrayList<int[]>();
		ArrayList<int[]> longIndices = new ArrayList<int[]>();	    
		LinkedList<String> sentencesList = new LinkedList<String>(allSentences);
		
		ArrayList<int[]> allIndices = indexFinder( theDoc, sentencesList );
		
		for (int i = 0; i < allIndices.size(); i++)
		{
			int[] temp = allIndices.get(i);
			String sentence = allSentences.get(i);
			Double wordsInSentence = sentenceWordCount.get(i);
			//System.out.print("words: "+wordsInSentence+"    "+"target: "+featureTargetValue+"      ");
			if ( wordsInSentence < super.featureTargetValue ){
				shortIndices.add(new int[] {temp[0],temp[1]});
				
				
			} else if ( wordsInSentence > super.featureTargetValue ){
					longIndices.add(new int[] {temp[0],temp[1]});
					
			}
			
			
		}
		if(super.featurePresentValue >super.featureTargetValue){
			highlightMap.put( super.getNextColor(),longIndices );
			setSuggestionHighToLow("Your average sentence length is '" + super.featurePresentValue + "', which is higher than it should be." +
					" Ideally, it should be at '" + super.featureTargetValue + "'. The highlighted sentences are all longer than the target value," +
							" and if possible, should be shortened. Look below to see your average sentence count updated as you fix your document.");
		}
		else if(super.featurePresentValue < super.featureTargetValue){
			highlightMap.put( super.getNextColor(),shortIndices );
			setSuggestionLowToHigh("Your average sentence length is '" + super.featurePresentValue + "', which is shorter than it should be." +
					" Ideally, it should be at '" + super.featureTargetValue + "'. The highlighted sentences are all shorter than the target value," +
							" and if possible, should be lengthend. Look below to see your average sentence count updated as you fix your document.");
		}
		else
			super.setNoChangeNeeded(true);
	
	}

	public ArrayList<int[]> indexFinder(String theDoc, LinkedList<String> theList)
	{
		ArrayList<int[]> theIndices = new ArrayList<int[]>();
		int listLength = theList.size();
		int i;
		int j;
		int start;
		int end;
		String temp;
		String spaces=" ";
		for(i=0;i<listLength;i++){
			temp = theList.pop().replaceAll("\\p{C}", " ");
			//System.out.println("POPPED: "+temp);
			if(temp == null)
				break;
			start = theDoc.indexOf(temp);
			if(start == -1)
				continue;
			end = start+temp.length();
			spaces = " ";
			for(j=1;j<temp.length();j++)
				spaces = spaces +" ";
			String theDocOne = theDoc.substring(0,start);
			String theDocTwo = theDoc.substring(end);
			theDoc = theDocOne+spaces+theDocTwo;
			//System.out.println("FOUND: start at: "+start+" end at: "+end);
			theIndices.add(new int[]{start,end});
		}
		return theIndices;
	}
}
