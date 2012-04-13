package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Iterator;

import edu.stanford.nlp.ling.TaggedWord;

public class TaggedDocument {
	
	protected ArrayList<TaggedSentence> taggedSentences;
	protected final int PROBABLE_NUM_SENTENCES = 50;
	protected ArrayList<ArrayList<TENSE>> tenses;
	protected ArrayList<ArrayList<POV>> pointsOfView;
	protected ArrayList<ArrayList<CONJ>> conjugations;
	
	
	public TaggedDocument(){
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
	}
	public ArrayList<TaggedSentence> getTaggedSentences(){
		return taggedSentences;
	}
	
}

enum TENSE {PAST,PRESENT,FUTURE};

enum POV {FIRST_PERSON,SECOND_PERSON,THIRD_PERSON};

enum CONJ {SIMPLE,PROGRESSIVE,PERFECT,PERFECT_PROGRESSIVE};
	

class TaggedSentence{
	
	protected String untagged;
	protected ArrayList<TaggedWord> tagged;
	protected Iterator<TaggedWord> tagIter;
	protected TaggedWord taggedWord;
	protected ArrayList<String> wordsToReturn;
	protected ArrayList<TENSE> tense;
	protected ArrayList<POV> pointOfView;
	protected ArrayList<CONJ> conj;
	
	
	public TaggedSentence(String untagged, ArrayList<TaggedWord> tagged){
		this.untagged = untagged;
		this.tagged = tagged;
		update();
	}
	public void update(){
		
	}
	
	public ArrayList<TENSE> getTense(){
		return tense;
	}
	public ArrayList<POV> getPov(){
		return pointOfView;
	}
	public ArrayList<CONJ> getConj(){
		return conj;
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

class IndependentClause{
	
	
	
	
}