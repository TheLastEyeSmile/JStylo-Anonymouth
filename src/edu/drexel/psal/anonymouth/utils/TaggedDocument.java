package edu.drexel.psal.anonymouth.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jgaap.JGAAPConstants;

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
 *
 */
public class TaggedDocument{
	
	protected ArrayList<TaggedSentence> taggedSentences;
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
	MaxentTagger mt = null;	
	protected SentenceTools jigsaw;
	protected Iterator<String> strIter;
	private boolean tagger_ok;
	private static int sentNumber = -1;
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
	 
	public void setTitle(String title){
		documentTitle = title;
	}
	
	public void setAuthor(String author){
		documentAuthor = author;
	}
	
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
		ArrayList<String> untaggedSentences = jigsaw.makeSentenceTokens(untagged);
		totalSentences=untaggedSentences.size();
		ArrayList<TaggedSentence> taggedSentences = new ArrayList<TaggedSentence>(untaggedSentences.size());
		//sentencesPreTagging = new ArrayList<List<? extends HasWord>>();
		strIter = untaggedSentences.iterator();
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
			for(i=0;i<len;i++)
				this.taggedSentences.add(taggedSentences.get(i)); 
		}
		return taggedSentences;
	}
	
	public ArrayList<TaggedSentence> getTaggedDocument(){
		return taggedSentences;
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
			Logger.logln("ERROR: SentNumber cannot exceed the total sentences.");
			return null;
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
	 * @return the concatenation of the current sentence and the next sentence.
	 */
	public String addNextSentence() {
		if(sentNumber <totalSentences-1||sentNumber>=0){
			totalSentences--;
			String tempSent=taggedSentences.remove(sentNumber+1).getUntagged();
			String newSent=taggedSentences.get(sentNumber).getUntagged()+tempSent;
			replaceCurrentSentence(newSent);
			return newSent;
		}
		if(sentNumber<0){
			sentNumber=0;
		}
		return taggedSentences.get(sentNumber).getUntagged();
		
	}
	
	private void replaceCurrentSentence(String newSent) {
		// TODO Auto-generated method stub
		taggedSentences.remove(sentNumber).getUntagged();
		taggedSentences.get(sentNumber).getUntagged();
	}

	public static int getSentNumber(){
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
	
	public void removeAndReplace(int indexToRemove, int placeToAdd, String sentsToAdd){
		removeTaggedSentence(indexToRemove);
		ArrayList<TaggedSentence> taggedSentsToAdd = makeAndTagSentences(sentsToAdd,false);
		int i = 0;
		int currentAddingIndex = placeToAdd;
		int len = taggedSentsToAdd.size();
		for(i=0;i<len;i++){
			addTaggedSentence(taggedSentsToAdd.get(i),currentAddingIndex);
			currentAddingIndex++;
		}
		
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
			String text1 = "I enjoy coffee, especially in the mornings, because it helps to wake me up. My dog is fairly small, but she seems not to realize it when she is around bigger dogs. This is my third testing sentence. I hope this works well.";
			TaggedDocument testDoc = new TaggedDocument(text1);
			System.out.println(testDoc.toString());
			
		}
	
}
	
