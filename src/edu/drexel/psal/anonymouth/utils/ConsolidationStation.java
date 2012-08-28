package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import edu.drexel.psal.anonymouth.projectDev.Attribute;
import edu.drexel.psal.anonymouth.projectDev.DataAnalyzer;
import edu.drexel.psal.anonymouth.projectDev.FeatureList;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * 
 * @author Andrew W.E. McDonald
 * @author Joe Muoio
 *
 */
public class ConsolidationStation {
	
	static HashMap<String,ArrayList<TreeData>> parsed;
	static ArrayList<Triple> toAdd=new ArrayList<Triple>(400);
	static ArrayList<Triple> toRemove=new ArrayList<Triple>(400);
	public static ArrayList<TaggedDocument> otherSampleTaggedDocs;//initialized in backendInterfaces.
	public static ArrayList<TaggedDocument> authorSampleTaggedDocs;
	public static ArrayList<TaggedDocument> toModifyTaggedDocs;//init in editor Tab Driver
	private static boolean allDocsTagged = false;
	public static FunctionWord functionWords=new FunctionWord();
	
	private HashMap<String,Word>wordsToAdd;
	private HashMap<String,Word>newWordsToAdd;
	private HashMap<String,Word>wordsInDocToMod;
	private HashMap<String,Word>wordsToRemove;
	
	/**
	 * constructor for ConsolidationStation. Depends on target values, and should not be called until they have been selected.
	 * @param attribs
	 * @param parsed
	 */
	public ConsolidationStation(){
		this.parsed = parsed;
		toAdd = new ArrayList<Triple>(400);
		toRemove = new ArrayList<Triple>(400);
		wordsToAdd=new HashMap<String,Word>();
		newWordsToAdd=new HashMap<String,Word>();
		wordsInDocToMod=new HashMap<String,Word>();
		wordsToRemove=new HashMap<String,Word>();
		
	}
	
	
	
	public static void setAllDocsTagged(boolean allDocsTagged){
		ConsolidationStation.allDocsTagged = allDocsTagged;
	}
	
	/**
	 * Adds the features present to each word in the taggedSentence
	 * @param taggedSent the tagged Sentence with the Word List to update.
	 * @return the taggedSentence passed in.
	 */
	public static TaggedSentence featurePacker(TaggedSentence taggedSent){
		for(Word word:taggedSent.wordsInSentence){
			setWordFeatures(word);
		}
		return taggedSent;
	}
	
	
	/**
	 * Adds Reference objects to each Word objects' SparseReferences indicating which features were found in each word, and how many times that feature was found
	 * @param word
	 */
	public static void setWordFeatures(Word word){
		String wordString=word.word;
		int strSize=wordString.length(), tempNumber;
		int attribLen=DataAnalyzer.lengthTopAttributes;
		//for (Attribute attrib:attribs){
		System.exit(0);
		for(int i=0;i<attribLen;i++){
			String stringInBrace=DataAnalyzer.topAttributes[i].getStringInBraces();
			int toAddLength=stringInBrace.length();
			if(toAddLength==0){
				//Logger.logln("THIS IS BAD",Logger.LogOut.STDERR);
			}
			else if(toAddLength<=strSize){//checks for a possible match
				tempNumber=0;
				for(int j=0;j<strSize-toAddLength;j++){
					if(wordString.substring(j, j+toAddLength).equals(stringInBrace)){
						tempNumber++;
					}
				}
				if(tempNumber>0){
					//add the feature to the word and have it appearing tempNumber times.
					//Logger.logln("AddNewReference from ConsolStation.featurePacker");
					//Logger.logln("Value i: "+i+" Value indexOf Attrib: "+DataAnalyzer.topAttributes[i].getIndexNumber()+" Attribute: "+DataAnalyzer.topAttributes[i].getFullName()+" the word: "+wordString);
					word.wordLevelFeaturesFound.addNewReference(i, tempNumber);
					//Logger.logln("Added a feature: "+word.wordLevelFeaturesFound.toString());
				}
			}
		}
	}
	
	
	/**
	 * Same as {@link #setWordFeatures(Word word)}, except on the sentence level. 
	 * 
	 * NOTE: Should be called AFTER {@link #setWordFeatures(Word word)}
	 *  
	 * @param word
	 */
	public static void setSentenceFeatures(TaggedSentence sent){
		// TODO -- We already found the 'word' level features, and they are stored differently/independently... so, we start with word bigrams, and move up (trigrams, possibly POS bi/trigrams, and punctutation)
		String sentString = sent.untagged;
		int strSize = sentString.length(); 
		int tempNumber;
		int attribLen = DataAnalyzer.lengthTopAttributes;
		//for (Attribute attrib:attribs){
		for(int i=0;i<attribLen;i++){
			String stringInBrace=DataAnalyzer.topAttributes[i].getStringInBraces();
			int toAddLength=stringInBrace.length();
			if(toAddLength==0){
				// ???
			}
			else if(toAddLength<=strSize){//checks for a possible match
				tempNumber=0;
				for(int j=0;j<strSize-toAddLength;j++){
					if(wordString.substring(j, j+toAddLength).equals(stringInBrace)){
						tempNumber++;
					}
				}
				if(tempNumber>0){
					//add the feature to the word and have it appearing tempNumber times.
					//Logger.logln("AddNewReference from ConsolStation.featurePacker");
					//Logger.logln("Value i: "+i+" Value indexOf Attrib: "+DataAnalyzer.topAttributes[i].getIndexNumber()+" Attribute: "+DataAnalyzer.topAttributes[i].getFullName()+" the word: "+wordString);
					word.wordLevelFeaturesFound.addNewReference(i, tempNumber);
					//Logger.logln("Added a feature: "+word.wordLevelFeaturesFound.toString());
				}
			}
		}
	}
	
	/**
	 * Goes through all Words in all TaggedSentences in all TaggedDocuments, sorts them from least to greatest in terms of Anonymity Index, and returns either the lowest ranked or
	 * highest ranked percent as strings.
	 * NOTE:
	 * 	* percentToReturn, the percent of highest or lowest ranked words (String) to return, should be a number between 0 and 1.
	 * * if findTopToRemove is false, the highest ranked Words will be returned (as Strings) (these would then be the most important words to ADD to the documentToAnonymize)
	 * * if findTopToRemove is true, the lowest ranked Words will be returned (as String) (these would then be the most important words to REMOVE from the documentToAnonymize)
	 * @param docsToConsider the TaggedDocuments to extract Words from
	 * @param findTopToRemove true to find the top words to remove, false to find the top words to add
	 * @param percentToReturn the percent of words found to return (should probably be MUCH smaller if finding top words to add, because this will look at all otherSampleDocuments
	 * @return
	 */
	public static ArrayList<String> getPriorityWords(ArrayList<TaggedDocument> docsToConsider, boolean findTopToRemove, double percentToReturn){
		int totalWords = 0;
		ArrayList<Word> words = new ArrayList<Word>(totalWords);
		for(TaggedDocument td:docsToConsider){
			totalWords += td.getWordCount();
			words.addAll(td.getWords());
		}
		//System.out.println("-----------------------Printing word list-------------------------");
		/*for(Word w:words){
			System.out.println(w.toString());
		}*/
		
		int numToReturn = (int)(totalWords*percentToReturn);
		ArrayList<String> toReturn = new ArrayList<String>(numToReturn);
		words = removeDuplicateWords(words);
		
		Collections.sort(words);// sort the words in INCREASING anonymityIndex
		
		int mergedNumWords = words.size();
		if (mergedNumWords <= numToReturn){
			Logger.logln("The number of priority words to return is greater than the number of words available. Only returning what is available");
			numToReturn = mergedNumWords;
		}
		Word tempWord;
		if(findTopToRemove){ // then start from index 0, and go up to index (numToReturn-1) words (inclusive)]
			for(int i = 0; i<numToReturn; i++){
				System.out.println(words.get(i).word+" "+words.get(i).getAnonymityIndex()); 	
				if((tempWord=words.get(i)).getAnonymityIndex()<0)
					toReturn.add(tempWord.word);//+" ("+tempWord.getAnonymityIndex()+")");
				else 
					break;
			}
		}
		else{ // start at the END of the list, and go down to (END-numToReturn) (inclusive)
			System.out.println("GOt here");
			int startIndex = mergedNumWords - 1;
			int stopIndex = startIndex - numToReturn;
			for(int i = startIndex; i> stopIndex; i--){
				//System.out.println("Got here..");
				//System.out.println(words.get(i).word+" "+words.get(i).getAnonymityIndex());
				if((tempWord=words.get(i)).getAnonymityIndex()>0)
					toReturn.add(tempWord.word);//+" ("+tempWord.getAnonymityIndex()+")");
				else 
					break;
			}	
		}
		System.out.println(toReturn);
		return toReturn;
	}
	
	
	public static ArrayList<Word> removeDuplicateWords(ArrayList<Word> unMerged){
		HashMap<String,Word> mergingMap = new HashMap<String,Word>((unMerged.size()));//Guessing there will be at least an average of 3 duplicate words per word -> 1/3 of the size is needed
		for(Word w: unMerged){
			if(mergingMap.containsKey(w.word) == true){
				//Word temp = mergingMap.get(w.word);
				//temp.mergeWords(w);
				//mergingMap.put(w.word,temp);
				if(w.equals(mergingMap.get(w.word))){
					//check is sparse ref the same
					if(!w.wordLevelFeaturesFound.equals(mergingMap.get(w.word).wordLevelFeaturesFound)){
						Logger.logln("The wordLevelFeaturesFound in the words are not equal.",Logger.LogOut.STDERR);
					}
				}
				else{
					Logger.logln("Problem in mergeWords--Words objects not equal",Logger.LogOut.STDERR);
				}
				
			}
			else{
				mergingMap.put(w.word,new Word(w));
			}
		}
		Set<String> mergedWordKeys = mergingMap.keySet();
		ArrayList<Word> mergedWords = new ArrayList<Word>(mergedWordKeys.size());
		for(String s: mergedWordKeys){
			mergedWords.add(mergingMap.get(s));
		}
		return mergedWords;
	}
	
	
	public static Word getWordFromString(String str){
		Word newWord=new Word(str);
		for (int i=0;i<toAdd.size();i++){//toaddList loop
			Logger.logln("TOADD: "+toAdd.get(i).getStringInBraces());
			int toAddLength=toAdd.get(i).getStringInBraces().length();
			if(toAddLength<=str.length()){//checks if it can be a possible match
				int tempNumber=0;
				double featureInfoGain=toAdd.get(i).getInfoGain();
				double featurePercentChange = toAdd.get(i).getInfoGain();
				for(int j=0;j<str.length()-toAddLength;j++){//loops through word to check if/howManyTimes the stringInBraces is found in the word.
					if(str.substring(j, j+toAddLength).equals((String)toAdd.get(i).stringInBraces)){
						tempNumber++;
					}
				}
				//newWord.adjustVals(tempNumber, featureInfoGain,featurePercentChange);
			}
		}
		for (int i=0;i<toRemove.size();i++){//toaddList loop
			Logger.logln("TOREMOVE: "+toRemove.get(i).getStringInBraces());
			int toAddLength=toRemove.get(i).getStringInBraces().length();
			if(toAddLength<=str.length()){//checks if it can be a possible match
				int tempNumber=0;
				double featureInfoGain=toRemove.get(i).getInfoGain();
				double featurePercentChange = toRemove.get(i).getInfoGain();
				for(int j=0;j<str.length()-toAddLength;j++){//loops through word to check if/howManyTimes the stringInBraces is found in the word.
					if(str.substring(j, j+toAddLength).equals((String)toRemove.get(i).stringInBraces)){
						tempNumber++;
					}
				}
				//newWord.adjustVals(tempNumber, featureInfoGain, featurePercentChange);//respresents a word to remove, so it should be negative
			}
		}
	//	Logger.logln("NEW WORD"+newWord.toString());
		return newWord;
	}
	

	
}
