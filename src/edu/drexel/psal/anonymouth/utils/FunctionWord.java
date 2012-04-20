package edu.drexel.psal.anonymouth.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.anonymouth.utils.Trie;

/**
 * 
 * @author Joe Muoio
 * Keeps track of the list of function words and implements the Trie to search them.
 * 
 */

public class FunctionWord {
	
	protected ArrayList<String> functionWordList;
	private static String filePath="src/edu/drexel/psal/resources/koppel_function_words.txt";
	//private Trie node;

	public FunctionWord() {
		//node = new Trie();
		functionWordList=readFunctionWords();
	}
	
	
	public boolean searchListFor(String str){
		return functionWordList.contains(str);
	}
	
	public String getWordAt(int index){
		return functionWordList.get(index);
	}
	
	
	public static ArrayList<String> readFunctionWords(){
		ArrayList<String> functionWords=new ArrayList<String>();
		
		 try {
			BufferedReader readIn  = new BufferedReader(new FileReader(filePath));
			String newLine;
			try {
				while((newLine=readIn.readLine())!=null){
					if(newLine.length()>1){
						functionWords.add(newLine);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.logln("Error opening reader: "+e.getMessage());
		}
		
		return functionWords;
	}
	
	public static void main(String[] args){//times the execution of the search on the list of function words.
		//ArrayList<String> strings=;
		//TaggedDocument doc=new TaggedDocument(strings.toString());
		FunctionWord fWord=new FunctionWord();
		
		//System.out.print(doc.toString());
		ArrayList<String> strList=fWord.functionWordList;
		String findStr;
		Random randomGen = new Random(); 
		int num;
		long startTime = System.currentTimeMillis();
		for(int i=0;i<strList.size();i++){
			num=Math.abs(randomGen.nextInt()%strList.size());
			findStr=strList.get(num);
			fWord.searchListFor(findStr);
			
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time ArrList: " + (endTime-startTime));//strList.size());		
		startTime = System.currentTimeMillis();
		for(int i=0;i<strList.size();i++){
			num=Math.abs(randomGen.nextInt()%strList.size());
			findStr=strList.get(num);			
		}
		endTime = System.currentTimeMillis();
		System.out.println("OverHead Time: " + (endTime-startTime));//strList.size());		
		
	}
}
