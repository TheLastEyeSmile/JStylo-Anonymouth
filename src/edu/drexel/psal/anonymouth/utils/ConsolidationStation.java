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
		int toAddIndex;
		for(toAddIndex=0;toAddIndex<toAdd.size();toAddIndex++){//loops through toAddTriple
			if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_BIGRAMS)){
				findWordBigrams();
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.FUNCTION_WORDS)){
				findFunctionWords();
			}
		}
				
	}
	
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


	private void findWordBigrams(){
		int attribIndex,taggedDocsIndex,sentenceIndex,wordIndex, previousIndex;
		//This will parse through the individual sentences
		
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			ArrayList<TaggedSentence> tagSentences=otherSampleTaggedDocs.get(taggedDocsIndex).getTaggedSentences();
			for(sentenceIndex=0;sentenceIndex<tagSentences.size();sentenceIndex++){//loopsThrough sentences in each doc
				TaggedSentence tagged=tagSentences.get(sentenceIndex);
				previousIndex=0;
				for(wordIndex=1;wordIndex<tagged.tagged.size();wordIndex++){//loops through words in sentences
					Word wordBigram=makeWordBigram(tagged.tagged.get(previousIndex),tagged.tagged.get(wordIndex));
					addToHashMap(wordsToAdd,wordBigram);
					previousIndex=wordIndex;
				}
			}
		}
	}
	private void findPOSBigrams(){//fix this
		int attribIndex,taggedDocsIndex,sentenceIndex,wordIndex, previousIndex;
		//This will parse through the individual sentences
		
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			ArrayList<TaggedSentence> tagSentences=otherSampleTaggedDocs.get(taggedDocsIndex).getTaggedSentences();
			for(sentenceIndex=0;sentenceIndex<tagSentences.size();sentenceIndex++){//loopsThrough sentences in each doc
				TaggedSentence tagged=tagSentences.get(sentenceIndex);
				previousIndex=0;
				for(wordIndex=1;wordIndex<tagged.tagged.size();wordIndex++){//loops through words in sentences
					Word wordBigram=makeWordBigram(tagged.tagged.get(previousIndex),tagged.tagged.get(wordIndex));//here
					addToHashMap(wordsToAdd,wordBigram);
					previousIndex=wordIndex;
				}
			}
		}
	}
	private void findLetterBigrams(){
		int attribIndex,taggedDocsIndex,sentenceIndex,wordIndex, previousIndex,letterIndex;
		//This will parse through the individual sentences
		
		for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){//loops through documents
			ArrayList<TaggedSentence> tagSentences=otherSampleTaggedDocs.get(taggedDocsIndex).getTaggedSentences();
			for(sentenceIndex=0;sentenceIndex<tagSentences.size();sentenceIndex++){//loopsThrough sentences in each doc
				TaggedSentence tagged=tagSentences.get(sentenceIndex);
				for(wordIndex=0;wordIndex<tagged.tagged.size();wordIndex++){//loops through words in sentences
					String untaggedWord=tagged.tagged.get(wordIndex).word();
					for(letterIndex=1;letterIndex<untaggedWord.length();letterIndex++){
						previousIndex=0;
						Word letterBigram=makeWordBigram(tagged.tagged.get(previousIndex),tagged.tagged.get(wordIndex));
						addToHashMap(wordsToAdd,letterBigram);
						previousIndex=letterIndex;
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
			hashMap.get(wordToAdd.word).adjustVals(1, wordToAdd.infoGainSum);
		}
		else{
			hashMap.put(wordToAdd.word, wordToAdd);
		}		
	}
	/**
	 * 
	 * @param taggedWord the first tagged word to make into a word
	 * @param taggedWord2
	 * @return
	 */
	private Word makeWordBigram(TaggedWord taggedWord1, TaggedWord taggedWord2) {
		Word newWord = new Word(taggedWord1.word()+taggedWord2.word());
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
