package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import edu.drexel.psal.jstylo.generics.Logger;

/**
 * Retrieves sentences from a text and places them into an arraylist.
 * @author Andrew W.E. McDonald
 *
 */
public class SentenceTools {
	
	private int currentStart = 0;
	private int currentStop = 0;
	private static final Pattern EOS_chars = Pattern.compile("([?!]+)|([.]){1}");
	private static final Pattern sentence_quote = Pattern.compile("[.?!]\"\\s+[A-Z]");
	private static final String PERIOD_REPLACEMENT = ""; // XXX: Hopefully it is safe to assume no one sprinkles apple symbols in their paper
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
	private String[] notEndsOfSentence = {"Dr.","Mr.","Mrs.","Ms.","St.","vs.","U.S.","Sr.","Sgt.","R.N.","pt.","mt.","mts.","M.D.","Ltd.","Jr.","Lt.","Hon.","i.e.","e.x.","inc.",
			"et al.","est.","ed.","D.C.","B.C.","B.S.","Ph.D.","B.A.","A.B.","A.D.","A.M.","P.M.","Ln.","fig.","p.","pp.","ref.","r.b.i.","V.P.","yr.","yrs.","etc.","..."};
	
	
	/**
	 * Takes a text (one String representing an entire document), and breaks it up into sentences. Tries to find true ends of sentences: shouldn't break up sentences containing quoted sentences, 
	 * checks for sentences ending in a quoted sentence (e.x. He said, "Hello." ), will not break sentences containing common abbreviations (such as Dr., Mr. U.S., etc.,e.x., i.e., and others), and 
	 * checks for ellipses points. However, It is probably not perfect.
	 * @param text
	 * @return
	 */
	public ArrayList<String> makeSentenceTokens(String text){
		ArrayList<String> sents = new ArrayList<String>(MAX_SENTENCES);
		int currentStart = 1;
		int currentStop = 0;
		int lenText = text.length();
		String temp;
		int openingQuoteIndex = 0;
		int closingQuoteIndex = 0;
		text = text.replaceAll("\u201C","\"");
		text = text.replaceAll("\u201D","\"");
		text = text.replaceAll("\\p{C}"," ");
		int notEOSNumber = 0;
		int numNotEOS = notEndsOfSentence.length;
		String replacementString = "";
		String safeString = "";
		for(notEOSNumber = 0;notEOSNumber<numNotEOS; notEOSNumber++){
			replacementString = notEndsOfSentence[notEOSNumber].replaceAll("\\.",PERIOD_REPLACEMENT);
			//System.out.println("REPLACEMENT: "+replacementString);
			safeString = notEndsOfSentence[notEOSNumber].replaceAll("\\.","\\\\.");
			//System.out.println(safeString);
			text = text.replaceAll("(?i)"+safeString,replacementString);
		}
		Matcher sent = EOS_chars.matcher(text);
		boolean foundEOS = sent.find(currentStart);
		Matcher sentEnd;
		int charNum = 0;
		int lenString = 0;
		int lastQuoteAt = 0;
		boolean foundQuote = false;
		boolean isSentence;
		while (foundEOS == true){
			currentStop = sent.end();
			//System.out.println("Start: "+currentStart+" and Stop: "+currentStop);
			temp = text.substring(currentStart-1,currentStop);
			//System.out.println(temp);
			lenString = temp.length();
			lastQuoteAt = 0;
			foundQuote = false;
			for(charNum =0; charNum <lenString; charNum++){
				if(temp.charAt(charNum) == '\"'){
					lastQuoteAt = charNum;
					if(foundQuote == true){
						foundQuote = false;
						
					}
					else{
						foundQuote = true;
					}
					//System.out.println("Found quote!!! here it is: "+temp.charAt(charNum)+" ... in position: "+lastQuoteAt+" ... foundQuote is: "+foundQuote);
				}
			}
			if(foundQuote == true && ((closingQuoteIndex = temp.indexOf("\"",lastQuoteAt+1)) == -1)){
				if((currentStop = text.indexOf("\"",currentStart +lastQuoteAt+1)) == -1){
					currentStop = text.length();
				}
				else
					currentStop += currentStart+1;
			}
			else
				currentStop += currentStart;
			
			if(currentStop > text.length()) 
				currentStop = text.length();
			
			safeString = text.substring(currentStart-1,currentStop);
			sentEnd = sentence_quote.matcher(safeString);	
			isSentence = sentEnd.find();
			//System.out.println("RESULT OF sentence_quote matching: "+isSentence);
			if(isSentence == true){ // If it seems that the text looks like this: He said, "Hello." Then she said, "Hi." 
				// Then we want to split this up into two sentences (it's possible to have a sentence like this: He said, "Hello.")
				//System.out.println("start: "+sentEnd.start()+" ... end: "+sentEnd.end());
				currentStop = text.indexOf("\"",sentEnd.start()+currentStart)+1;
				safeString = text.substring(currentStart-1,currentStop);
			}
			
			safeString = safeString.replaceAll(PERIOD_REPLACEMENT,".");
			//System.out.println(safeString);
			sents.add(safeString);
			//System.out.println("start minus one: "+(currentStart-1)+" stop: "+currentStop);
			if(currentStart < 0 || currentStop < 0){
				Logger.logln("Something went really wrong making sentence tokens.");
				System.exit(0);
			}
			//System.out.println("The rest of the text: "+text.substring(currentStart));
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
		return sents;
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
//}	
	
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
		String testText = "Hello, Dr., this is my \"test\"ing tex\"t\".\nI need to see if it \"correctly (i.e. nothing goes wrong) ... and finds the first, and every other sentence, etc.. These quotes are silly, and it is 1 A.m. a.m. just for testing purposes.\" No, that isn't a \"real\" \"quote\".";
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