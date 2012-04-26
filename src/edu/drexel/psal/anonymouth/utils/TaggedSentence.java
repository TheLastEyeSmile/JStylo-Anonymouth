package edu.drexel.psal.anonymouth.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.jgaap.JGAAPConstants;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;

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
	private TaggedWord taggedWord;
	protected ArrayList<String> wordsToReturn;
	private int PROBABLE_MAX = 3;
	protected ArrayList<TENSE> tense = new ArrayList<TENSE>(PROBABLE_MAX);
	protected ArrayList<POV> pointOfView = new ArrayList<POV>(PROBABLE_MAX);
	protected ArrayList<CONJ> conj = new ArrayList<CONJ>(PROBABLE_MAX);
	protected ArrayList<String> functionWords=new ArrayList<String>(PROBABLE_MAX);//not sure if should have put PROBABLE_MAX
	protected ArrayList<String> misspelledWords=new ArrayList<String>(PROBABLE_MAX);
	protected ArrayList<String> punctuation =new ArrayList<String>(PROBABLE_MAX);
	protected ArrayList<String> digits =new ArrayList<String>(PROBABLE_MAX);
	protected ArrayList<Integer> wordLengths=new ArrayList<Integer>(PROBABLE_MAX);
	
	private static final Pattern punctuationRegex=Pattern.compile("[.?!,\'\";:]{1}");
	private static final Pattern digit=Pattern.compile("[\\d]{1,}");
	
	protected List<? extends HasWord> sentenceTokenized;
	protected Tokenizer<? extends HasWord> toke;
	protected TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
	
	private String[] thirdPersonPronouns={"he","she","him", "her","it","his","hers","its","them","they","their","theirs"};
	private String[] firstPersonPronouns={"I","me","my","mine","we","us","our","ours"};
	private String[] secondPersonPronouns={"you","your","yours"};
	
	public TaggedSentence(String untagged){
		this.untagged = untagged;
	}
	
	public TaggedSentence(String untagged, ArrayList<TaggedWord> tagged){
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
		MisspelledWords mWord=new MisspelledWords();
		for (int i=0;i<tagged.size();i++){
			TaggedWord temp=tagged.get(i);
			//System.out.println(temp.tag());
			if(temp.word().matches("[\\w&&\\D]+")){//fixes the error with sentenceAppend button
				if(fWord.searchListFor(temp.word())){
					functionWords.add(temp.word());
				}
				else if(mWord.searchListFor(temp.word())){
					misspelledWords.add(temp.word());
				}
				else{
					java.util.regex.Matcher wordToSearch=punctuationRegex.matcher(temp.word());
					if(wordToSearch.find()){
						punctuation.add(temp.word().substring(wordToSearch.start(), wordToSearch.end()));
					}
					else{//adds digits
						wordToSearch=digit.matcher(temp.word());
						if(wordToSearch.find()){
							String digitSubStr=temp.word().substring(wordToSearch.start(), wordToSearch.end());
							for (int count=0;count<digitSubStr.length();count++){
								if(count-2>=0){
									digits.add(digitSubStr.substring(count-2, count));
								}
								if(count-1>=0){
									digits.add(digitSubStr.substring(count-1, count));
								}
								digits.add(digitSubStr.substring(count, count));
							}	
						}	
					}
				}	/**///This somehow overwrite the taggedDocument.
				
				wordLengths.add(temp.word().length());
				
			}
			/*Stuff for tenses
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
	
}
