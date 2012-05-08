package edu.drexel.psal.anonymouth.utils;

import edu.drexel.psal.anonymouth.utils.POS.TheTags;
import edu.drexel.psal.jstylo.generics.Logger;

/**
 * Holds a 'word' as a String, and retains its rank and the collective information gain of all features that have been found within the 'word'
 * Can also hold two or three 'words' in 'word' field. 
 * @author Andrew W.E. McDonald
 *
 */
public class Word {
	
	protected String word;
	protected double anonymityRank = 0; // start at neutral
	protected double infoGainSum = 0;//weka calc//want the avg info gain. (I htink)
	protected double numFeaturesIncluded = 0;//
	protected double percentChangeNeededSum = 0;
	protected String partOfSpeech;
	
	/**
	 * constructor for Word
	 * @param word the word to construct a Word for
	 */
	public Word(String word){
		this.word = word;
	}
	
	public Word(Integer integer) {
		// TODO Auto-generated constructor stub
		word=integer.toString();
	}


	public void setPOS(String string){
		partOfSpeech=string;
	}
	
	/**
	 * Computes the AnonymityRank of a word: the average information gain of a word * the average percent change needed. These numbers are
	 * determined by the percent change needed and information gain for each feature (and each instance of each feature) found in the "Word"
	 * @return
	 */
	public double getAnonymityRank(){
		anonymityRank = ((infoGainSum/numFeaturesIncluded)*(percentChangeNeededSum/numFeaturesIncluded));
		return anonymityRank;
	}
	
	/**
	 * Merges two words, provided that the 'word' (string) inside are equivalent (case sensitive), and that both 'word' strings have been determined to be of 
	 * the same part of speech.
	 * @param newWord
	 */
	public void mergeWords(Word newWord){
		if(newWord.equals(this)){
			this.infoGainSum += newWord.infoGainSum;
			this.percentChangeNeededSum += newWord.percentChangeNeededSum;
			this.numFeaturesIncluded += newWord.numFeaturesIncluded;
		}
		else
			Logger.logln("The Words did not match");
	}
	/**
	 * the method to use to add or subtract from a Word's rank
	 * @param numTimesFeatureSeen the number of times a feature appears in the Word's String
	 * @param featureInfoGain the information gain for the feature modifying the rank of the word.
	 */
	public void adjustVals(int numTimesFeatureSeen, double featureInfoGain, double percentChangeNeeded){
		int numAppearancesOfFeature = Math.abs(numTimesFeatureSeen);
		//rank += numTimesFeatureSeen;
		infoGainSum += numAppearancesOfFeature*featureInfoGain;
		percentChangeNeededSum += numAppearancesOfFeature*percentChangeNeeded;
		numFeaturesIncluded += numAppearancesOfFeature;
	}
	
	/**
	 * defines two Word objects to be equal if they contain the same 'word' String object.
	 * @return
	 * 	true if equal
	 */
	public boolean equals(Object obj){
			if(word.equals(((Word)obj).word))
				if(((Word)obj).partOfSpeech.equals(this.partOfSpeech))
					return true;
				else 
					return false;
			else
				return false;
	}
	
	/**
	 * generates a hashcode for Word, modulus 987643211 (an arbitrary large prime number) to mitigate risk of integer overflow. Multiplier is 31,
	 * hash value starts at 7, and iteratively multiplies itself by the product of all preceding characters in 'word'.
	 * @return
	 * 	hashcode
	 */
	public int hashCode(){
		final int thePrime = 31;
		final int arbitraryLargePrime = 987643211;
		long longHash = 7;
		int i = 0;
		if(word != null){
			char[] theWord = word.toCharArray();
			int len = theWord.length;
			for(i=0; i < len; i++){
				longHash = longHash*theWord[i]*thePrime;
				longHash = longHash % arbitraryLargePrime;// to eliminate wrap-around / overflow
			}
		}
		int hash = (int)longHash;
		return hash;
	}
	
	

	/**
	 * toString method
	 */
	public String toString(){
		return "[ WORD: "+word+" ||| RANK: "+anonymityRank+" ||| AVG. INFO GAIN: "+(infoGainSum/numFeaturesIncluded)+"]";
	}
	
	
}
