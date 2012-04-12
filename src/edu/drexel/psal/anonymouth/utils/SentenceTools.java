package edu.drexel.psal.anonymouth.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

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
	private static final String PERIOD_REPLACEMENT = "ï£¿"; // XXX: Hopefully it is safe to assume no one sprinkles apple symbols in their paper
	private static int MAX_SENTENCES = 500;
	private int numSentences;
	private boolean shouldInitialize = true;
	private ArrayList<String> sentsToEdit = new ArrayList<String>(MAX_SENTENCES);
	private ArrayList<String> editedSents = new ArrayList<String>(MAX_SENTENCES);
	private int currentSentence =0;
	private int nextSentence = 0;
	private Iterator<Sentence> sentenceIterator;
	private String editedText = "" ;
	private static int sentNumber = -1;
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
		boolean merge1=false, mergeFinal=false;
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
				else{
					currentStop +=1;
					merge1=true;
				}
			}
			safeString = text.substring(currentStart-1,currentStop);
			//if(text.substring(currentStop, currentStop+1)=="\n"||text.substring(currentStop, currentStop+2)==" \n"){
				//safeString+="\n";
			//}
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
			if(mergeFinal){
				mergeFinal=false;
				String prev=sents.remove(sents.size()-1);
				safeString=prev+safeString;
			}
			if (merge1){//makes so that the merge happens on the next pass through
				merge1=false;
				mergeFinal=true;
			}
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
		
		return sents;
	}
	
	public static int getSentNumb(){
		return sentNumber;
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
		if(sentNumber <totalSentences-1){
			sentNumber++;
			mustAddToIndex = false;
			return sentsToEdit.get(sentNumber);
		}
		else{
			System.out.print("RETURN NULL getNext");
			return null;
		}
	}
	/*
	 * gets the previous sentence.
	 * @return the string of the previous sentence 
	 */
	
	public String getLast(){
		if(sentNumber >0){
			sentNumber--;
			mustAddToIndex = true;
			return sentsToEdit.get(sentNumber);
		}
		else{
			System.out.print("RETURN NULL getLast");
			return null;
		}
	}
	
	/*
	 * adds the next sentence to the current one.
	 * @retrun the concatenation of the current sentence and the next sentence.
	 */
	public String addNextSent() {
		if(sentNumber <totalSentences-1){
			totalSentences--;
			String tempSent=sentsToEdit.remove(sentNumber+1);
			String newSent=sentsToEdit.get(sentNumber)+tempSent;
			replaceCurrentSentence(newSent);
			return newSent;
		}
		return sentsToEdit.get(sentNumber);
		
	}
	/*
	 * makes sure the text in the edit sentence box is just one sentence.
	 * In the case of multiple sentences, it updates the list so that each sentence has its own index.
	 * @return a list of the sentences in the box 
	 */
	public ArrayList<String> checkNumSent(String editText){
		
		ArrayList<String> sentences= makeSentenceTokens(editText);
		int i=0;
		replaceCurrentSentence(sentences.get(i));
		Logger.logln(sentences.get(i));
		for (i=1; i<sentences.size();i++){
			sentNumber++;
			totalSentences++;
			sentsToEdit.add(sentNumber,sentences.get(i));
		}
		
		return sentences;
	}
	
	
	public void replaceCurrentSentence(String s){
		int index;
		index = sentNumber;
		sentsToEdit.remove(index);
		sentsToEdit.add(index,s);
	}	
	
	public String getFullDoc(){
		
		String fullDoc = "";
		for(int i=0;i<totalSentences;i++){
			fullDoc += sentsToEdit.get(i);
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
		String testText = "There are many issues with the concept of intelligence and the way it is tested in people. As stated by David Myers, intelligence is the “mental quality consisting of the ability. to learn from experience”, solve problems, and use knowledge “to adapt. to new situations” (2010). Is there really just one intelligence? According to many psychologists, there exists numerous intelligences. One such psychologist, Sternberg, believes there are three: Analytical Intelligence, Creative Intelligence, and Practical Intelligence. Analytical Intelligence is the intelligence assessed by intelligence tests which presents well-defined problems with set answers and predicts school grades reasonably well and to a lesser extent, job success. Creative Intelligence is demonstrated by the way one reacts to certain unforeseen situations in “new” ways. The last of the three is Practical intelligence which is the type of intelligence required for everyday tasks. This is what is used by business managers and the like to manage and motivate people, promote themselves, and delegate tasks efficiently. In contrast to this idea of 3 separate intelligences is the idea of just one intelligence started by Charles Spearman. He thought we had just one intelligence that he called “General Intelligence” which is many times shortened to just: “G”. This G factor was an underlying factor in all areas of our intelligence. Spearman was the one who also developed factor analysis which is a statistics method which allowed him to track different clusters of topics being tested in an intelligence test which showed that those who score higher in one area are more likely to score higher in another. This is the reason why he believed in this concept of G.";
		//testText = "Hello, Dr., this is my \"test\"ing tex\"t\".\nI need to see if it \"correctly (i.e. nothing goes wrong) ... and finds the first, and every other sentence, etc.. These quotes are silly, and it is 1 A.m. a.m. just for testing purposes.\" No, that isn't a \"real\" \"quote\".";
		ArrayList<String> Stok=ss.makeSentenceTokens(testText);
		Object[] arr = Stok.toArray();
		for (int i = 0; i<arr.length; i++){
			System.out.println(arr[i]);
		}
		System.out.println("End");
		
	}

	public void setSentsToEdit(ArrayList<String> tokens) {
		// used in backend interface.
		sentsToEdit=tokens;
	}

	public void setNumberSentences(int size) {
		// used in backend interface
		totalSentences=size;
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