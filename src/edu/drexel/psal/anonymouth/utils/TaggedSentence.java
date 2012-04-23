package edu.drexel.psal.anonymouth.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jgaap.JGAAPConstants;

import edu.stanford.nlp.ling.TaggedWord;
import edu.drexel.psal.anonymouth.suggestors.POS.TheTags;

import edu.drexel.psal.anonymouth.utils.*;

import edu.drexel.psal.jstylo.generics.Logger;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

/**
 * 
 * @author Joe Muoio
 * @author Andrew W.E. McDonald
 */

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
	protected ArrayList<String> functionWords=new ArrayList<String>();
	
	protected List<? extends HasWord> sentenceTokenized;
	protected Tokenizer<? extends HasWord> toke;
	protected TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
	//private MaxentTagger mt = null;	
	//private boolean tagger_ok;
	
	private String[] thirdPersonPronouns={"he","she","him", "her","it","his","hers","its","them","they","their","theirs"};
	private String[] firstPersonPronouns={"I","me","my","mine","we","us","our","ours"};
	private String[] secondPersonPronouns={"you","your","yours"};
	
	public TaggedSentence(String untagged){
		//tagger_ok = initMaxentTagger();
		//Logger.logln("MaxentTagger initialization in TaggedDocument status: "+tagger_ok);
		this.untagged = untagged;
		//tagSentence();
		//setGrammarStats(tagged);
	}
	
	public TaggedSentence(String untagged, ArrayList<TaggedWord> tagged){
		//tagger_ok = initMaxentTagger();
		//Logger.logln("MaxentTagger initialization in TaggedDocument status: "+tagger_ok);
		this.untagged = untagged;
		this.tagged = tagged;
		setGrammarStats();
	}
	
	public boolean setTaggedSentence(ArrayList<TaggedWord> tagged){
		this.tagged = tagged;
		setGrammarStats();
		return true;
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
	/**
	 * sets the ArrayLists, Tense, Pow, and Conj.
	 * @param tagged
	 */
	public void setGrammarStats(){
		FunctionWord fWord=new FunctionWord();
		for (int i=0;i<tagged.size();i++){
			TaggedWord temp=tagged.get(i);
			//System.out.println(temp.tag());
			if(tagged.get(i).word().matches("\\w+")){
				System.out.println(tagged.get(i).word());
				if(fWord.searchListFor(tagged.get(i).word())){
					functionWords.add(tagged.get(i).word());
				}
			}/**///This somehow overwrite the taggedDocument.
			if(temp.tag().startsWith("VB")){
				//it is a verb 
				switch(TheTags.valueOf((temp.tag()))){
				case VB: conj.add(CONJ.SIMPLE);//"Verb, base form";
				case VBD: tense.add(TENSE.PAST);
							conj.add(CONJ.SIMPLE); // "Verb, past tense";
				//case "VBG": // "Verb, gerund or present participle";
				//case "VBN": // "Verb, past participle";
				case VBP: tense.add(TENSE.PRESENT);// "Verb, non-3rd person singular present";
				case VBZ: tense.add(TENSE.PRESENT);// "Verb, 3rd person singular present";
				}
			}
			else if (temp.tag().startsWith("PR")){//this is a pronoun
				String tempWord=temp.word();
				for(int j=0;j<firstPersonPronouns.length;j++){
					if(firstPersonPronouns[j].equalsIgnoreCase(tempWord)){
						if(!pointOfView.contains(POV.FIRST_PERSON))//will not add POVs twice
							pointOfView.add(POV.FIRST_PERSON);
					}
				}
				for(int j=0;j<secondPersonPronouns.length;j++){
					if(secondPersonPronouns[j].equalsIgnoreCase(tempWord)){
						if(!pointOfView.contains(POV.SECOND_PERSON))
							pointOfView.add(POV.SECOND_PERSON);
					}
				}
				for(int j=0;j<thirdPersonPronouns.length;j++){
					if(thirdPersonPronouns[j].equalsIgnoreCase(tempWord)){
						if(!pointOfView.contains(POV.THIRD_PERSON))
							pointOfView.add(POV.THIRD_PERSON);
					}
				}
			}
			/*else if(temp.word().equalsIgnoreCase("shall")||temp.word().equalsIgnoreCase("will")){
				tense.add(TENSE.FUTURE);
			}actually, this is not necessarily true.*/
		}
		
	}
/*	
	public ArrayList<String> findWordsContaining(String thisString){
		
	
	}
*/
	
	public String getUntagged(){
		return untagged;
	}
	
	public String toString(){
		return "[ untagged: "+untagged+" ||| tagged: "+tagged.toString()+" ||| tense: "+tense.toString()+" ||| point of view: "+pointOfView.toString()+" conjugation(s): "+conj.toString()+" ||| functionWords : "+functionWords.toString()+" ]";
	}
	
	public ArrayList<String> getWordsWithTag(TheTags tag){
		wordsToReturn = new ArrayList<String>(tagged.size());// Can't return more words than were tagged
		tagIter = tagged.iterator();
		while (tagIter.hasNext()){
			taggedWord = tagIter.next();
			System.out.println(taggedWord.value());
			System.out.println(taggedWord.tag());
		}
		return wordsToReturn;
	}
	
	
	/*public static void main(String[] args){
		String text1 = "I enjoy coffee, especially in the mornings, because it helps to wake me up.";
		TaggedSentence testDoc = new TaggedSentence(text1);
		testDoc.setGrammarStats();
		System.out.println(testDoc.getUntagged());
		//testDoc.getWordsWithTag(POS.TheTags.VB);
		
	}*/
	
}
