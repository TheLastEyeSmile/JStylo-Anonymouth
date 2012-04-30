package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.HashMap;

import edu.drexel.psal.anonymouth.projectDev.Attribute;
import edu.drexel.psal.anonymouth.projectDev.FeatureList;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * 
 * @author Andrew W.E. McDonald
 * @author Joe Muoio
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
	private HashMap<String,Word>wordsInDocToMod;
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
		wordsInDocToMod=new HashMap<String,Word>();
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
	
	public void findWordsToAdd(){//Only loops through otherSampleTaggedDocs.
		// TODO I think this should take a global (to this function) hashmap of String -> Word objects, and run through all features in the 'toAdd' list, checking them 
		// against each TaggedWord words in otherSampleTaggedWords. When it finds that one of the TaggedWord words contains the feature its checking, it should
		// find out how many times that feature appears in the TaggedWord word, and then:
			// If the hashmap contains the Word, read the value from the map, adjustVals, and replace it
			// else, create new entry in hashmap
		int taggedDocsIndex, toAddIndex;
		
		for(toAddIndex=0;toAddIndex<toAdd.size();toAddIndex++){//loops through toAddTriple
			if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_BIGRAMS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getWordBigrams(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getWordBigrams(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_TRIGRAMS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getWordTrigrams(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getWordTrigrams(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORDS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getWords(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getWords(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.POS_BIGRAMS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getPOSBigrams(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getPOSBigrams(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.POS_TRIGRAMS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getPOSTrigrams(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getPOSTrigrams(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.POS_TAGS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getPOS(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getPOS(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.LETTERS)){//what is letterNGrams?
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getLetters(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getLetters(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if(toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.TOP_LETTER_BIGRAMS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getLetterBigrams(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getLetterBigrams(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.TOP_LETTER_TRIGRAMS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getLetterTrigrams(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getLetterTrigrams(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.FUNCTION_WORDS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getFunctionWords(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getFunctionWords(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.PUNCTUATION)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getPunctuation(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getPunctuation(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.WORD_LENGTHS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
				//	findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getWordLengths(),toAdd.get(toAddIndex),wordsToAdd);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.DIGITS)){
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getDigits(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getDigits(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.MISSPELLED_WORDS)){
				//findMisspelledWords();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getMisspelledWords(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getMisspelledWords(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			else if (toAdd.get(toAddIndex).getFeatureName().equals(FeatureList.SPECIAL_CHARACTERS)){
				//findSpecialChars();
				for(taggedDocsIndex=0;taggedDocsIndex<otherSampleTaggedDocs.size();taggedDocsIndex++){
					findAttribute(otherSampleTaggedDocs.get(taggedDocsIndex).getSpecialChars(),toAdd.get(toAddIndex),wordsToAdd);
					findAttribute(toModifyTaggedDocs.get(taggedDocsIndex).getSpecialChars(),toAdd.get(toAddIndex),wordsInDocToMod);
				}
			}
			
		}
				
	}
	private void findAttribute(HashMap<String,Integer> hashMap,Triple toAddTriple,HashMap<String,Word> listToAddTo) {
		while(hashMap.keySet().iterator().hasNext()){
			Word newWord=new Word(hashMap.keySet().iterator().next());
			newWord.adjustVals(hashMap.get(newWord.word).intValue(), toAddTriple.getInfoGain());
			//newWord.rank=hashMap.get(newWord.word).intValue();
			//newWord.infoGainSum=toAddTriple.getInfoGain();
			addToHashMap(listToAddTo,newWord);
		}
	}
	/*private void findAttribute(HashMap<Integer,Integer> hashMap) {
		while(hashMap.keySet().iterator().hasNext()){
			Word newWord=new Word(hashMap.keySet().iterator().next());
			newWord.rank=hashMap.get(newWord.word).intValue();
			addToHashMap(wordsToAdd,newWord);
		}
	}*/

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
	
	
	//Note: removed a lot of old code.
	
	public void findWordsToRemove(){
		//TODO Should do the same as above, but ONLY with the toModifyTaggedDocs -- obviously in a separate hashmap.
	}
	
	

}
