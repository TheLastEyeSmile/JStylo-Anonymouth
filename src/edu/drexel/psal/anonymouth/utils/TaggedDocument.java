package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Iterator;

import edu.drexel.psal.anonymouth.suggestors.POS;
import edu.stanford.nlp.ling.TaggedWord;

public class TaggedDocument {
	
	protected ArrayList<TaggedSentence> taggedSentences;
	protected final int PROBABLE_NUM_SENTENCES = 50;
	
	public TaggedDocument(){
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
	}
	
}

class TaggedSentence{
	
	protected String untagged;
	protected ArrayList<TaggedWord> tagged;
	protected Iterator<TaggedWord> tagIter;
	protected TaggedWord taggedWord;
	protected ArrayList<String> wordsToReturn;
	
	public TaggedSentence(String untagged, ArrayList<TaggedWord> tagged){
		this.untagged = untagged;
		this.tagged = tagged;
		
	}
	
	public ArrayList<String> getWordsWithTag(POS tag){
		wordsToReturn = new ArrayList<String>(tagged.size());// Can't return more words than were tagged
		tagIter = tagged.iterator();
		while (tagIter.hasNext()){
			taggedWord = tagIter.next();
			System.out.println(taggedWord.value());
			System.out.println(taggedWord.tag());
		}
		return wordsToReturn;
	}
	
}