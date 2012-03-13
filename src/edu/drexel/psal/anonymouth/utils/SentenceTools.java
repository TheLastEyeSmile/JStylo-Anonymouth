package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import edu.drexel.psal.jstylo.generics.Logger;

/**
 * Retrieves sentences from a text and places them into an arraylist. Each sentence is presented in order, and the 
 * edited version of that sentence is appended onto the end of a new string, creating an edited version of the input
 * text. 
 * @author Andrew W.E. McDonald
 *
 */
public class SentenceTools {
	
	private int currentStart = 0;
	private int currentStop = 0;
	private static final Pattern EOS_chars = Pattern.compile("[.?!]");
	private static int MAX_SENTENCES = 500;
	private int numSentences;
	private boolean shouldInitialize = true;
	private ArrayList<String> sentsToEdit = new ArrayList<String>(MAX_SENTENCES);
	private ArrayList<String> editedSents = new ArrayList<String>(MAX_SENTENCES);
	private int currentSentence =0;
	private int nextSentence = 0;
	private Iterator<Sentence> sentenceIterator;
	private String editedText = "" ;
	private int sentNumber = 0;
	private int totalSentences = 0;
	private boolean mustAddToIndex = false;
	
	
	public void makeSentenceTokens(String text){
		ArrayList<String> sents = new ArrayList<String>(MAX_SENTENCES);
		Matcher sent = EOS_chars.matcher(text);
		int currentStart = 1;
		int currentStop = 0;
		int lenText = text.length();
		boolean foundEOS = sent.find(currentStart);
		while (foundEOS == true){
			currentStop = sent.end();
			//System.out.println("Start: "+currentStart+" and Stop: "+currentStop);
			sents.add(text.substring(currentStart-1,currentStop));
			currentStart = currentStop+1;
			if(currentStart >= lenText){
				foundEOS = false;
				continue;
			}
			foundEOS = sent.find(currentStart);
		}
		totalSentences = sents.size();
		sentNumber = 0;
		//sentenceIterator = sentsToEdit.iterator();
		sentsToEdit = sents;
	}
	
	
	/**
	 * Checks whether or not there are more unchecked sentences from the intial input text or not. True if there are,
	 * false if not.
	 * @return
	 * 	True if there are more sentences to check, false if not.
	 */
	public boolean moreToCheck(){
		if(sentNumber < totalSentences)
			return true;
		else
			return false;
	}
	
	/**
	 * gets the next sentence
	 * @return
	 */
	public String getNext(){
		if(sentNumber <totalSentences){
			sentNumber++;
			mustAddToIndex = false;
			return sentsToEdit.get(sentNumber-1);
		}
		else
			return null;
	}
	
	public String getLast(){
		if(sentNumber >=0){
			sentNumber--;
			mustAddToIndex = true;
			return sentsToEdit.get(sentNumber +1);
		}
		else
			return null;
	}
	
	public void replaceCurrentSentence(String s){
		int index;
		if(mustAddToIndex == true)
			index = sentNumber +1;
		else
			index = sentNumber -1;
		sentsToEdit.remove(index);
		sentsToEdit.add(index,s);
	}	
	
	public String getFullDoc(){
		Iterator<String> sentIter = sentsToEdit.iterator();
		String fullDoc = "";
		while(sentIter.hasNext()){
			fullDoc += sentIter.next()+" ";
		}
		return fullDoc;
	}
	
	public ArrayList<String> getSentenceTokens(){
		return sentsToEdit;
	}
	
	/*
	public String editBySentence(){
		while(moreToCheck()){
			String editedSentence = JOptionPane.showInputDialog("Edit this: ",getNext().getSentence());
			editedText += editedSentence+" ";
		}
		editedText = editedText.substring(0,editedText.length()-1);
		return editedText;
	}
*/		
	public static void main(String[] args){
		SentenceTools ss = new SentenceTools();
		String testText = "Hello, this is my testing text. I need to see if it correctly finds the first, and every other sentence.";
		ss.makeSentenceTokens(testText);
		//System.out.println(ss.editBySentence());
	}
	
}
/*
class Sentence {
	
	private int absStart;
	private int absStop;
	private String sentenceText;
	
	public Sentence(String sentenceText, int absStart, int absStop){
		this.absStart = absStart;
		this.absStop = absStop;
		this.sentenceText = sentenceText;
	}
		
	public String getSentText(){
		return sentenceText;
	}
	
	public int[] getAbsBounds(){
		return new int[]{absStart,absStop};
	}
	
}
*/