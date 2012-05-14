package edu.drexel.psal.anonymouth.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.drexel.psal.anonymouth.suggestors.POS;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class TaggedDocument {
	
	protected ArrayList<TaggedSentence> taggedSentences;
	protected final int PROBABLE_NUM_SENTENCES = 50;
	
	public TaggedDocument(){
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
	}
	
	public static void main(String[] args){
		MaxentTagger mt = null;
		try {
			mt = new MaxentTagger("./external/MaxentTagger/left3words-wsj-0-18.tagger");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String text1 = "I enjoy coffee, especially in the mornings, because it helps to wake me up.";
		String text2 = "My dog is fairly small, but she seems not to realize it when she is around bigger dogs.";
		String text3 = "This is my third testing sentence.";
		ArrayList<String> sentenceTokens = new ArrayList<String>(3);
		sentenceTokens.add(text1);
		sentenceTokens.add(text2);
		sentenceTokens.add(text3);
		int numSentences = sentenceTokens.size();
		Iterator<String> sentIter = sentenceTokens.iterator();
		List<List<? extends HasWord>> sentencesPreTagging = new ArrayList<List<? extends HasWord>>();
		String tempSent;
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		while(sentIter.hasNext()){
			tempSent = sentIter.next();
			Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(tempSent));
			List<? extends HasWord> sentenceTokenized = toke.tokenize();
			sentencesPreTagging.add(sentenceTokenized);
		}
		//while...
		//ArrayList<TaggedWord> beenTagged = mt.tagString(testText);
		
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