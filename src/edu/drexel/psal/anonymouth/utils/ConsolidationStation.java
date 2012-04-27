package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.HashMap;

import edu.drexel.psal.anonymouth.projectDev.Attribute;
import edu.drexel.psal.anonymouth.projectDev.FeatureList;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * 
 * @author Andrew W.E. McDonald
 *
 */
public class ConsolidationStation {
	
	Attribute[] attribs;
	HashMap<String,ArrayList<TreeData>> parsed;
	ArrayList<Triple> toAdd;
	ArrayList<Triple> toRemove;
	public static ArrayList<TaggedDocument> otherSampleTaggedDocs;
	public static ArrayList<TaggedDocument> authorSampleTaggedDocs;
	public static ArrayList<TaggedDocument> toModifyTaggedDocs;
	private static boolean allDocsTagged = false;
	
	private HashMap<String,Word>wordsToAdd;
	private HashMap<String,Word>wordsToRemove;
	
	/**
	 * constructor for ConsolidationStation. Depends on target values, and should not be called until they have been selected.
	 * @param attribs
	 * @param parsed
	 */
	public ConsolidationStation(Attribute[] attribs){
		this.attribs = attribs;
		this.parsed = parsed;
		toAdd = new ArrayList<Triple>(400);
		toRemove = new ArrayList<Triple>(400);
		wordsToAdd=new HashMap<String,Word>();
		wordsToRemove=new HashMap<String,Word>();
	}
	
	
	/**
	 * Starts the consolidation process
	 */
	public void beginConsolidation(){
		
		
	}
	
	public static void setAllDocsTagged(boolean allDocsTagged){
		ConsolidationStation.allDocsTagged = allDocsTagged;
	}
	
	/**
	 * runs through all attributes in attribs and pulls out the stringInBraces if it is there, and the percent (positive and negative)  
	 * change needed
	 * 
	 */
	public void getStringsFromAttribs(){
		for(Attribute attrib:attribs){
			if (attrib.getCalcHist() == false)
				continue; // ignore single valued features
			String tempID;
			FeatureList feature;
			double tempInfoGain;
			feature = attrib.getGenericName();
			tempID = attrib.getStringInBraces();
			tempInfoGain = attrib.getInfoGain();
			/*if (tempPercentChange > 0){
				Triple trip = new Triple(tempID,tempPercentChange,tempInfoGain);
				toAdd.add(trip);
			}
			else if(tempPercentChange < 0){
				Triple trip = new Triple(tempID,tempPercentChange,tempInfoGain);
				toRemove.add(trip);
			}*/
		}			
	}
	
	public void findWordsToAdd(){
		// TODO I think this should take a global (to this function) hashmap of String -> Word objects, and run through all features in the 'toAdd' list, checking them 
		// against each TaggedWord words in otherSampleTaggedWords. When it finds that one of the TaggedWord words contains the feature its checking, it should
		// find out how many times that feature appears in the TaggedWord word, and then:
			// If the hashmap contains the Word, read the value from the map, adjustVals, and replace it
			// else, create new entry in hashmap
		boolean letterGrams=false,functionWords=false, posGrams=false,wordGrams=false;//maybe make jsut one list with all the rnaking andweeights on it???
		int taggedDocsIndex;
		int toAddIndex;//ISSUE WITH THIS STRATEGY: what if multiple grams are unescessary? FInd Another Way than making 20 functions.
		for(toAddIndex=0;toAddIndex<toAdd.size();toAddIndex++){//loops through toAddTriple
			if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_BIGRAMS)||toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_TRIGRAMS)){
				findWordGrams();
				wordGrams=true;
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.POS_BIGRAMS)||toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.POS_TRIGRAMS)||
					toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.POS_TAGS)){
				findPOSGrams();
				posGrams=true;
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.TOP_LETTER_BIGRAMS)||toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.TOP_LETTER_TRIGRAMS)){
				findLetterGrams();
				letterGrams=true;
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.FUNCTION_WORDS)){
				//findFunctionWords();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getFunctionWords());
				}
				functionWords=true;
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.PUNCTUATION)){
				//findPunctuation();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getPunctuation());
				}
				//punctuation=true;
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_LENGTHS)){
				//findWordLengths();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
				//	findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getWordLengths());
				}
				//wordLength=true;
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.DIGITS)){
				//findDigits();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getDigits());
				}
				//digits=true;
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.MISSPELLED_WORDS)){
				//findMisspelledWords();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getMisspelledWords());
				}
				//misspelled=true;
			}
			
		}
				
	}
	private void findAttribute(HashMap<String,Integer> hashMap) {
		while(hashMap.keySet().iterator().hasNext()){
			Word newWord=new Word(hashMap.keySet().iterator().next());
			newWord.rank=hashMap.get(newWord.word).intValue();
			addToHashMap(wordsToAdd,newWord);
		}
	}
	/*private void findAttribute(HashMap<Integer,Integer> hashMap) {
		while(hashMap.keySet().iterator().hasNext()){
			Word newWord=new Word(hashMap.keySet().iterator().next());
			newWord.rank=hashMap.get(newWord.word).intValue();
			addToHashMap(wordsToAdd,newWord);
		}
	}*/
	private void findFunctionWords() {
		int attribIndex,taggedDocsIndex,sentenceIndex;
		HashMap<String,Integer>functionWords;
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			functionWords=otherSampleTaggedDocs.get(taggedDocsIndex).getFunctionWords();
			while(functionWords.keySet().iterator().hasNext()){
				Word newWord=new Word(functionWords.keySet().iterator().next());
				newWord.rank=functionWords.get(newWord.word).intValue();
				addToHashMap(wordsToAdd,newWord);
			}
		}
	}
	private void findPunctuation() {
		int attribIndex,taggedDocsIndex,sentenceIndex;
		HashMap<String,Integer>punctuation;
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			punctuation=otherSampleTaggedDocs.get(taggedDocsIndex).getPunctuation();
			while(punctuation.keySet().iterator().hasNext()){
				Word newWord=new Word(punctuation.keySet().iterator().next());
				newWord.rank=punctuation.get(newWord.word).intValue();
				addToHashMap(wordsToAdd,newWord);
			}
		}
	}


	private void findWordGrams(){
		int attribIndex,taggedDocsIndex,sentenceIndex,wordIndex;
		//This will parse through the individual sentences
		
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			ArrayList<TaggedSentence> tagSentences=otherSampleTaggedDocs.get(taggedDocsIndex).getTaggedSentences();
			for(sentenceIndex=0;sentenceIndex<tagSentences.size();sentenceIndex++){//loopsThrough sentences in each doc
				TaggedSentence tagged=tagSentences.get(sentenceIndex);
				for(wordIndex=1;wordIndex<tagged.tagged.size();wordIndex++){//loops through words in sentences
					Word wordBigram=makeWordBigram(tagged.tagged.get(wordIndex-1).word(),tagged.tagged.get(wordIndex).word());
					addToHashMap(wordsToAdd,wordBigram);
					if(wordIndex-2>=0){
						Word wordTrigram=makeWordTrigram(tagged.tagged.get(wordIndex-2).word(),tagged.tagged.get(wordIndex-1).word(),tagged.tagged.get(wordIndex).word());
						addToHashMap(wordsToAdd,wordTrigram);
					}
					else {
						Word wordGram=makeWordBigram(tagged.tagged.get(wordIndex-1).tag(),"");
						addToHashMap(wordsToAdd,wordGram);
					}
					Word wordGram=makeWordBigram(tagged.tagged.get(wordIndex).tag(),"");
					addToHashMap(wordsToAdd,wordGram);
				}
			}
		}
	}
	private void findPOSGrams(){
		int attribIndex,taggedDocsIndex,sentenceIndex,wordIndex;
		//This will parse through the individual sentences
		
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			ArrayList<TaggedSentence> tagSentences=otherSampleTaggedDocs.get(taggedDocsIndex).getTaggedSentences();
			for(sentenceIndex=0;sentenceIndex<tagSentences.size();sentenceIndex++){//loopsThrough sentences in each doc
				TaggedSentence tagged=tagSentences.get(sentenceIndex);
				for(wordIndex=1;wordIndex<tagged.tagged.size();wordIndex++){//loops through words in sentences
					Word wordBigram=makeWordBigram(tagged.tagged.get(wordIndex-1).tag(),tagged.tagged.get(wordIndex).tag());
					addToHashMap(wordsToAdd,wordBigram);
					if(wordIndex-2>=0){
						Word wordTrigram=makeWordTrigram(tagged.tagged.get(wordIndex-2).tag(),tagged.tagged.get(wordIndex-1).tag(),tagged.tagged.get(wordIndex).tag());
						addToHashMap(wordsToAdd,wordTrigram);
					}
					else{
						Word wordGram=makeWordBigram(tagged.tagged.get(wordIndex-1).tag(),"");
						addToHashMap(wordsToAdd,wordGram);
					}
					Word wordGram=makeWordBigram(tagged.tagged.get(wordIndex).tag(),"");
					addToHashMap(wordsToAdd,wordGram);
				}
			}
		}
	}
	private void findLetterGrams(){
		int taggedDocsIndex,sentenceIndex,wordIndex,letterIndex;
		//This will parse through the individual sentences
		
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			ArrayList<TaggedSentence> tagSentences=otherSampleTaggedDocs.get(taggedDocsIndex).getTaggedSentences();
			for(sentenceIndex=0;sentenceIndex<tagSentences.size();sentenceIndex++){//loopsThrough sentences in each doc
				TaggedSentence tagged=tagSentences.get(sentenceIndex);
				for(wordIndex=0;wordIndex<tagged.tagged.size();wordIndex++){//loops through words in sentences
					char[] untaggedWord=tagged.tagged.get(wordIndex).word().toCharArray();
					for(letterIndex=1;letterIndex<untaggedWord.length;letterIndex++){
						Word letterBigram=makeWordBigram(untaggedWord[letterIndex-1]+"",untaggedWord[letterIndex]+"");
						addToHashMap(wordsToAdd,letterBigram);
						if(letterIndex-2>=0){
							Word wordTrigram=makeWordTrigram(untaggedWord[letterIndex-2]+"",untaggedWord[letterIndex-1]+"",untaggedWord[letterIndex]+"");
							addToHashMap(wordsToAdd,wordTrigram);
						}
					}
				}
			}
		}
	}
	
	


	/**
	 * Checks to see if the wordToAdd exists in the map. If it doesn't then it adds it. Otherwise it updates the 
	 * word at that location to have more weight
	 * @param hashMap the hashmap to add the feature to.
	 * @param wordToAdd the word that is added
	 * 
	 */
	
	public void addToHashMap(HashMap <String,Word> hashMap, Word wordToAdd){
		
		if (hashMap.containsKey(wordToAdd.word)){
			hashMap.get(wordToAdd.word).adjustVals(wordToAdd.rank, wordToAdd.infoGainSum);
		}
		else{
			hashMap.put(wordToAdd.word, wordToAdd);
		}		
	}
	
	
	/**
	 * 
	 * @param str1 the first string to make into a word
	 * @param str2 the second string to make into a word
	 * @return the newWord that represents the bigram
	 */
	private Word makeWordBigram(String str1, String str2) {
		Word newWord = new Word(str1+str2);
		newWord.infoGainSum=1;
		newWord.rank=1;//I think this needs to be initialized to 1
		newWord.numFeaturesIncluded=1;// I do not know what this is supposed to be
		return newWord;
	}
	/**
	 * 
	 * @param str1 the first string to make into a word
	 * @param str2 the second string to make into a word
	 * @param str3 the third string to make into a word
	 * @return the new word that represents the trigram
	 */
	private Word makeWordTrigram(String str1, String str2,String str3) {
		Word newWord = new Word(str1+str2+str3);
		newWord.infoGainSum=1;
		newWord.rank=1;//I htink this needs to be initialized to 1
		newWord.numFeaturesIncluded=1;// I do not know what this is supposed to be
		return newWord;
	}
	
	/**
	 * Concatenates 2 words
	 * @param word1 the first word
	 * @param word2 the word concatenating onto the first
	 * @return word1
	 */
	
	private Word concatWords(Word word1, Word word2){
		 word1.word=word1.word+word2.word;
		 word1.numFeaturesIncluded+=word2.numFeaturesIncluded;
		 word1.infoGainSum+=word2.infoGainSum;
		 word1.rank+=word2.rank;
		 return word1;
	}
	
	public void findWordsToRemove(){
		//TODO Should do the same as above, but ONLY with the toModifyTaggedDocs -- obviously in a separate hashmap.
	}
	
	
	// TODO something to do with finding function words, or playing with tenses or something.. I'm not sure.. give it some thought.

}
