package edu.drexel.psal.anonymouth.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jgaap.JGAAPConstants;

import edu.drexel.psal.anonymouth.gooie.ErrorHandler;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

enum TENSE {PAST,PRESENT,FUTURE};

enum POV {FIRST_PERSON,SECOND_PERSON,THIRD_PERSON};

enum CONJ {SIMPLE,PROGRESSIVE,PERFECT,PERFECT_PROGRESSIVE};

/**
 * 
 * @author Andrew W.E. McDonald
 * @author Joe Muoio
 * 
 */
public class TaggedDocument {
	
	protected ArrayList<TaggedSentence> taggedSentences;
	//protected ArrayList<String> untaggedSentences;
	private static final Pattern EOS_chars = Pattern.compile("([?!]+)|([.]){1}\\s*");
	
	protected String documentTitle = "None";
	protected String documentAuthor = "None";
	protected ArrayList<ArrayList<TENSE>> tenses;
	protected ArrayList<ArrayList<POV>> pointsOfView;
	protected ArrayList<ArrayList<CONJ>> conjugations;
	protected List<List<? extends HasWord>> sentencesPreTagging;
	protected Iterator<List<? extends HasWord>> preTagIterator;
	protected TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
	protected List<? extends HasWord> sentenceTokenized;
	protected Tokenizer<? extends HasWord> toke;
	protected final int PROBABLE_NUM_SENTENCES = 50;
	private MaxentTagger mt = null;	
	protected SentenceTools jigsaw;
	protected Iterator<String> strIter;
	private boolean tagger_ok;
	private static int sentNumber = -1;
	private String ID; 
	private int totalSentences=0;
	
	/**
	 * Constructor for TaggedDocument
	 */
	public TaggedDocument(){
		tagger_ok = initMaxentTagger();
		Logger.logln("MaxentTagger initialization in TaggedDocument status: "+tagger_ok);
		jigsaw = new SentenceTools();
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
	}
	
	/**
	 * Constructor for TaggedDocument, accepts an untagged string, and calls addSentences method.
	 * @param untaggedDocument
	 */
	public TaggedDocument(String untaggedDocument){
		tagger_ok = initMaxentTagger();
		 Logger.logln("MaxentTagger initialization in TaggedDocument status: "+tagger_ok);
		jigsaw = new SentenceTools();
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
		
		if(tagger_ok == true)
			makeAndTagSentences(untaggedDocument, true);
	}
	 
	/**
	 * 
	 * @param untaggedDocument
	 * @param docTitle
	 * @param author
	 */
	public TaggedDocument(String untaggedDocument, String docTitle, String author){
		tagger_ok = initMaxentTagger();
		this.documentTitle = docTitle;
		this.documentAuthor = author;
		this.ID = documentTitle+"_"+documentAuthor;
		Logger.logln("TaggedDocument ID: "+ID);
		Logger.logln("MaxentTagger initialization in TaggedDocument status: "+tagger_ok);
		jigsaw = new SentenceTools();
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
		if(tagger_ok == true)
			makeAndTagSentences(untaggedDocument, true);
	}
	/*
	public boolean writeSerializedSelf(String directory){
		return ObjectIO.writeObject(this, ID, directory);
	}
	*/
	
	public void setTitle(String title){
		documentTitle = title;
	}
	
	public void setAuthor(String author){
		documentAuthor = author;
	}
	
	/*public void setTaggedSentences(ArrayList<TaggedSentence> taggedSentences){
		int i = 0;
		int len = taggedSentences.size();
		for(i=0;i<len;i++)
			totalSentences++;
			this.taggedSentences.add(taggedSentences.get(i)); 
	}*/
	
	/**
	 * Returns the status of the MaxentTagger
	 * @return true if tagger is okay (has been properly initialized), false otherwise
	 */
	public boolean getTaggerStatus(){
		return tagger_ok;
	}
	
	
	/**
	 * Initializes MaxentTagger
	 * @return true if successful, false otherwise
	 */
	public boolean initMaxentTagger(){
		try {
			mt = new MaxentTagger("."+JGAAPConstants.JGAAP_RESOURCE_PACKAGE+"models/postagger/english-left3words-distsim.tagger");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Takes a String of sentences (can be an entire document), breaks it up into individual sentences (sentence tokens), breaks those up into tokens, and then tags them (via MaxentTagger).
	 * Each tagged sentence is saved into a TaggedSentence object, along with its untagged counterpart.
	 * @param untagged String containing sentences to tag
	 * @param appendTaggedSentencesToGlobalArrayList if true, appends the TaggedSentence objects to the TaggedDocument's arraylist of TaggedSentences
	 * @return the TaggedSentences
	 */
	public ArrayList<TaggedSentence> makeAndTagSentences(String untagged, boolean appendTaggedSentencesToGlobalArrayList){
		ArrayList<String> untaggedSent = jigsaw.makeSentenceTokens(untagged);
		ArrayList<TaggedSentence> taggedSentences = new ArrayList<TaggedSentence>(untaggedSent.size());
		//sentencesPreTagging = new ArrayList<List<? extends HasWord>>();
		strIter = untaggedSent.iterator();
		String tempSent;
		while(strIter.hasNext()){
			tempSent = strIter.next();
			TaggedSentence taggedSentence = new TaggedSentence(tempSent);
			toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(tempSent));
			sentenceTokenized = toke.tokenize();
			
			taggedSentence.setTaggedSentence(mt.tagSentence(sentenceTokenized));
			taggedSentence.setGrammarStats();
			taggedSentences.add(taggedSentence); 
			
		}
		if(appendTaggedSentencesToGlobalArrayList == true){
			int i = 0;
			int len = taggedSentences.size();
			for(i=0;i<len;i++){
				totalSentences++;
				this.taggedSentences.add(taggedSentences.get(i)); 
			}
		}
		
		return taggedSentences;
	}
	
	public ArrayList<TaggedSentence> getTaggedDocument(){
		return taggedSentences;
	}
		

	public ArrayList<String> getUntaggedSentences(){
		ArrayList<String> sentences = new ArrayList<String>();
		for (int i=0;i<taggedSentences.size();i++){
			sentences.add(taggedSentences.get(i).getUntagged());
		}
		return sentences;
	}
	
	
	/**
	 * gets the next sentence
	 * @return
	 */
	public String getNextSentence(){
		if(sentNumber <totalSentences-1){
			sentNumber++;
			return taggedSentences.get(sentNumber).getUntagged();
		}
		else{
			sentNumber=totalSentences-1;
			return taggedSentences.get(sentNumber).getUntagged();
		}
	}
	
	
	/**
	 * gets the previous sentence.
	 * @return the string of the previous sentence 
	 */
	public String getLastSentence(){
		if(sentNumber >0){
			sentNumber--;
			return taggedSentences.get(sentNumber).getUntagged();
		}
		else{
			Logger.logln("Returned first sentence");
			sentNumber=0;
			return taggedSentences.get(0).getUntagged();
		}
	}
	
	/**
	 * adds the next sentence to the current one.
	 * @param The sentenceEditBox text
	 * @return the concatenation of the current sentence and the next sentence.
	 */
	public String addNextSentence(String boxText) {
		if(sentNumber <totalSentences-1 && sentNumber>=0){
			totalSentences--;
			TaggedSentence newSent= new TaggedSentence(boxText);
			int position=0;
			while(position<boxText.length()){
				Matcher sent = EOS_chars.matcher(boxText);
				if(!sent.find(position)){//checks to see if there is a lack of an end of sentence character.
					Logger.logln("User tried submitting an incomplete sentence.");//THIS DOES NOT KEEP TAGS. 
					//--------------------This is because you cannot pass in incomplete sent to parser
					TaggedSentence tagSentNext=removeTaggedSentence(sentNumber+1);
					removeTaggedSentence(sentNumber);
					newSent.untagged=newSent.getUntagged()+tagSentNext.getUntagged();
					addTaggedSentence(newSent,sentNumber);//--------possible improvement needed to parser?-----
					//ErrorHandler.incompleteSentence();
					return newSent.getUntagged();
				}
				position=sent.end();
			}
			ArrayList<TaggedSentence> taggedSents=makeAndTagSentences(boxText,false);
			TaggedSentence nextSent=taggedSentences.remove(sentNumber+1);
			taggedSents.add(nextSent);
			taggedSentences.remove(sentNumber);
			newSent=concatSentences(taggedSents);
			taggedSentences.add(sentNumber, newSent);
			return newSent.getUntagged();
		}
		if(sentNumber<0){
			sentNumber=0;
		}
		return taggedSentences.get(sentNumber).getUntagged();
		
	}
	/**
	 * 
	 * @param taggedList takes a list of tagged sentences.
	 * @return returns a single tagged sentences with the properties of all the sentences in the list.
	 */
	private TaggedSentence concatSentences(ArrayList<TaggedSentence> taggedList){
		TaggedSentence newSent=new TaggedSentence(taggedList.get(0).getUntagged());//+taggedList.get(1).getUntagged());
		newSent.setTaggedSentence(taggedList.get(0).tagged);
		for (int i=1;i<taggedList.size();i++){
			newSent.untagged=newSent.getUntagged()+taggedList.get(i).getUntagged();
			for(int j = 0; j<taggedList.get(i).tagged.size();j++){
				newSent.tagged.add(taggedList.get(i).tagged.get(j));
			}
		}
		
		return newSent;
	}
	
	public int getSentNumber(){
		return sentNumber;
	}
	
	public static void setSentenceCounter(int sentNumber){
		TaggedDocument.sentNumber = sentNumber;
	}

	public void addTaggedSentence(TaggedSentence sentToAdd, int placeToAdd){
		taggedSentences.add(placeToAdd,sentToAdd);
		
	}
	
	public TaggedSentence removeTaggedSentence(int indexToRemove){
		return taggedSentences.remove(indexToRemove);
	}
	/**
	 * 
	 * @param sentsToAdd a String representing the sentence(s) from the editBox
	 * @return 1 for everything worked as expected. 0 for user deleted a sentence. -1 for user submitted an incomplete sentence
	 */
	public int removeAndReplace(String sentsToAdd){//, int indexToRemove, int placeToAdd){
		if(sentsToAdd.matches("\\s*")){//checks to see if the user deleted the current sentence
			removeTaggedSentence(sentNumber);
			Logger.logln("User deleted a sentence.");
			totalSentences--;
			sentNumber--;
			return 0;
		}
		int position=0;
		while(position<sentsToAdd.length()){
			Matcher sent = EOS_chars.matcher(sentsToAdd);
			if(!sent.find(position)){//checks to see if there is a lack of an end of sentence character.
				Logger.logln("User tried submitting an incomplete sentence.");
				TaggedSentence newSent= new TaggedSentence(sentsToAdd);
				removeTaggedSentence(sentNumber);
				addTaggedSentence(newSent,sentNumber);
				ErrorHandler.incompleteSentence();
				return -1;
			}
			position=sent.end();
		}
		ArrayList<TaggedSentence> taggedSentsToAdd = makeAndTagSentences(sentsToAdd,false);
		removeTaggedSentence(sentNumber);
		addTaggedSentence(taggedSentsToAdd.get(0),sentNumber);
		int i ;
		int len = taggedSentsToAdd.size();
		for(i=1;i<len;i++){
			sentNumber++;
			addTaggedSentence(taggedSentsToAdd.get(i),sentNumber);
			totalSentences++;
		}
		return 1;
		
	}
	
	public ArrayList<TaggedSentence> getTaggedSentences(){
		return taggedSentences;
	}
	public String getUntaggedDocument(){
		String str = "";
		for (int i=0;i<totalSentences;i++){
			str+=taggedSentences.get(i).getUntagged();
		}
		return str;
	}
	
	
	public String toString(){
		String toReturn = "Document Title: "+documentTitle+" Author: "+documentAuthor+"\n";
		int len = taggedSentences.size();
		int i =0;
		for(i=0;i<len;i++){
			toReturn += taggedSentences.get(i).toString()+"\n";
		}
		return toReturn;
	}
	
	
		public static void main(String[] args){
			//String text1 = "I enjoy coffee, especially in the mornings, because it helps to wake me up. My dog is fairly small, but she seems not to realize it when she is around bigger dogs. This is my third testing sentence. I hope this works well.";
			////TaggedDocument testDoc = new TaggedDocument(text1);
			//System.out.println(testDoc.toString());			
			
			
		}
	
}
	
