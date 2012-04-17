package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Iterator;

import edu.stanford.nlp.ling.TaggedWord;

public class TaggedSentence {

	protected String untagged;
	protected ArrayList<TaggedWord> tagged;
	protected Iterator<TaggedWord> tagIter;
	protected TaggedWord taggedWord;
	protected ArrayList<String> wordsToReturn;
	private int PROBABLE_MAX = 3;
	protected ArrayList<TENSE> tense = new ArrayList<TENSE>(PROBABLE_MAX);
	protected ArrayList<POV> pointOfView = new ArrayList<POV>(PROBABLE_MAX);
	protected ArrayList<CONJ> conj = new ArrayList<CONJ>(PROBABLE_MAX);
	
	public TaggedSentence(String untagged){
		this.untagged = untagged;
		
	}
	
	public TaggedSentence(String untagged, ArrayList<TaggedWord> tagged){
		this.untagged = untagged;
		this.tagged = tagged;
		setGrammarStats(this.tagged);
	}
	
	public boolean setTaggedSentence(ArrayList<TaggedWord> tagged){
		this.tagged = tagged;
		setGrammarStats(this.tagged);
		return true;
	}
	
	public void setGrammarStats(ArrayList<TaggedWord> tagged){
		
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
/*	
	public ArrayList<String> findWordsContaining(String thisString){
		
		
	}
*/
	public String toString(){
		return "[ untagged: "+untagged+" ||| tagged: "+tagged.toString()+" ||| tense: "+tense.toString()+" ||| point of view: "+pointOfView.toString()+" conjugation(s): "+conj.toString()+" ]";
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