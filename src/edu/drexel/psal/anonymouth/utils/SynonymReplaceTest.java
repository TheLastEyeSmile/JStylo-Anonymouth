package edu.drexel.psal.anonymouth.utils;

import java.util.Scanner;

import edu.drexel.psal.anonymouth.gooie.DictionaryBinding;
import edu.drexel.psal.anonymouth.gooie.EditorInnerTabSpawner;
import edu.drexel.psal.jstylo.generics.Logger;

public class SynonymReplaceTest {

	
	/*
	 * This function is for the study of replacing words with their highest ranked synonyms.
	 */
	public static void replaceWords(EditorInnerTabSpawner eits){
		String currentSent=eits.getSentenceEditPane().getText();
		String newSentence="";
		Scanner parser=new Scanner(currentSent);
		String currentWord, synonyms[];
		Word currentSynonym,topSynonym;
		while(parser.hasNext()){//loops through every word in the sentence
			currentWord=parser.next();
			synonyms=DictionaryBinding.getSynonyms(currentWord);
			topSynonym=new Word(currentWord);
			//Double d=topSynonym.getAnonymityIndex();
			ConsolidationStation.setWordFeatures(topSynonym);
			for(String s:synonyms){//loops through every synonym
				currentSynonym=new Word(s);
				ConsolidationStation.setWordFeatures(currentSynonym);
				if(topSynonym.getAnonymityIndex()<currentSynonym.getAnonymityIndex()){
					topSynonym=currentSynonym;
					Logger.logln("Replaced the TopSynonym!"+topSynonym.getUntagged());
				}
			}
			if(topSynonym.getUntagged()!=""){//Then replace the word with this
				newSentence+=topSynonym.getUntagged()+ " ";
			}
			else{
				newSentence+=currentWord+" ";
			}
		}
		Logger.logln(newSentence,Logger.LogOut.STDERR);
		eits.getSentenceEditPane().setText(newSentence);
		
	}
}
