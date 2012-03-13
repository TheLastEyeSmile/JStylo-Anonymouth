package edu.drexel.psal.anonymouth.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.trees.Tree;

/**
 * 
 * @author Andrew W.E. McDonald
 *
 */
public class TreeContainer {
	
	//HashMap<String,TreeData> mapOfTrees = new HashMap<String,TreeData>();
	protected HashMap<String,TreeData> processedTrees = new HashMap<String,TreeData>();
	protected static HashMap<String,TreeData> allProcessedTrees = new HashMap<String,TreeData>();
	private ArrayList<TreeData> sortedTreeData;
	
	/**
	 * splits each parse tree up into all of its subtrees, strips the leaves (all 'words'), and saves them into an ArrayList of TreeData Objects. 
	 * One TreeData object per unique subtree.
	 * @param tree
	 * @param minDepth the minimum depth of a subtree to include (if '0', single words will be omitted as they will be a tree of depth '0').
	 * @param addResultToStaticHashMap if true will add result to static HashMap as well
	 */
	public void processTree(Tree tree,int minDepth, boolean addResultToStaticHashMap){
		List<Tree> subTreeList = tree.subTreeList();
		//System.out.println("Sub tree list: \n");
		Iterator<Tree> treeIter = subTreeList.iterator();
		//ArrayList<String> cleanTrees = new ArrayList<String>(subTreeList.size());
		Tree temp;
		String tempLeaves;
		String leaves;
		String treeString;
		Pattern prePat = Pattern.compile("\\([A-Z[.,?!$-:;/&%#@~`'\"]]+\\s");
		Pattern postPat = Pattern.compile("\\)+\\s");
		Matcher matchPre;
		Matcher matchPost;
		boolean foundMatch;
		boolean hasNonLeaf = true;
		int start = 0;
		int tempStart = 0;
		String tempTreeString = "";
		String tempTempLeaves;
		String preString = "";
		while(treeIter.hasNext()){
			tempTreeString = "";
			temp = treeIter.next();
			int treeDepth = temp.depth();
			if(treeDepth <= minDepth)
				continue;
			tempTempLeaves = temp.getLeaves().toString();
			tempLeaves = tempTempLeaves.substring(1,tempTempLeaves.length()-1);
			leaves = tempLeaves.replaceAll(",","");
			//theLeaves.add(leaves);
			treeString = temp.toString()+" "; // add a space at the end for the postPat regex
			//System.out.println("THE TREE: "+treeString);
			matchPre = prePat.matcher(treeString);
			foundMatch = matchPre.find();
			while(foundMatch == true){
				start = matchPre.start();
				tempStart = matchPre.end();
				hasNonLeaf = true;
				while(hasNonLeaf){
					//System.out.println("char at next index: "+treeString.charAt(tempStart));
					if(treeString.charAt(tempStart) == '('){
						//System.out.println(treeString);
						System.out.println((treeString));
						if(matchPre.find(tempStart) == true);
							tempStart = matchPre.end();
					}
					else{
						hasNonLeaf = false;
						preString = treeString.substring(start);
						//System.out.println("preString: "+preString);
						matchPost = postPat.matcher(preString);
						matchPost.find(tempStart-start);
						tempTreeString += preString.substring(0,tempStart-start)+preString.substring(matchPost.start(),matchPost.end());
						//System.out.println("tempTreeString: "+tempTreeString);
					}
				}
				foundMatch = matchPre.find(tempStart);
			}
			
			if(tempTreeString.equals("") == false){
				TreeData td = new TreeData(tempTreeString);
				td.treeDepth = treeDepth;
				//System.out.println(tempTreeString+" ==> "+leaves);
				if(processedTrees.containsKey(tempTreeString))
					processedTrees.get(tempTreeString).addOccurrence(leaves);
				else{
					td.addOccurrence(leaves);
					processedTrees.put(tempTreeString,td);
				}
				if(addResultToStaticHashMap == true){
					if(allProcessedTrees.containsKey(tempTreeString))
							allProcessedTrees.get(tempTreeString).addOccurrence(leaves);
					else
						allProcessedTrees.put(tempTreeString,td);
					
				}
				
			}
		}
		//System.out.println(processedTrees.toString());
	}
	
	public static ArrayList<TreeData> treeDataReverseQuickSort(ArrayList<TreeData> td){
		if (td.size() <= 1)
				return td;
		int tdSize = td.size();
		int pivotIndex = (int)((double)tdSize/2);
		TreeData pivot = td.remove(pivotIndex);
		ArrayList<TreeData> lessThan = new ArrayList<TreeData>(tdSize);
		ArrayList<TreeData> greaterThan = new ArrayList<TreeData>(tdSize);
		for ( TreeData elem : td){
			if (elem.numberOfOccurrences > pivot.numberOfOccurrences)
				greaterThan.add(elem);
			else
				lessThan.add(elem);
		}
		return makeOneArrayList(treeDataReverseQuickSort(lessThan),pivot,treeDataReverseQuickSort(greaterThan));
	}
	
	public static ArrayList<TreeData> makeOneArrayList(ArrayList<TreeData> less, TreeData pivot, ArrayList<TreeData> greater){
		int totalSize = less.size() + greater.size() + 1;
		ArrayList<TreeData> concatted = new ArrayList<TreeData>(totalSize);
		Iterator<TreeData> tdIter = greater.iterator();
		while(tdIter.hasNext())
			concatted.add(tdIter.next());
		concatted.add(pivot);
		tdIter = less.iterator();
		while(tdIter.hasNext())
			concatted.add(tdIter.next());
		return concatted;
		
	}
	
	
	public static ArrayList<TreeData> sortTreeData(HashMap<String,TreeData> processedTrees){
	    Object[] values = processedTrees.values().toArray();
	    int numVals = values.length;
	    ArrayList<TreeData> td = new ArrayList<TreeData>(numVals);
	    for(int i = 0; i < numVals; i++)
	    		td.add((TreeData)values[i]);
		ArrayList<TreeData> sorted = treeDataReverseQuickSort(td);
		//System.out.println(sorted.toString().replaceAll("\\]\\], \\(","\\]\\]\n\\("));
		return sorted;
	}
	
	public static boolean writeTreeDataToCSV(ArrayList<TreeData> sortedTreeData, String authorName) throws IOException{
		FileWriter fw = new FileWriter(new File("/Users/lux/PSAL/grammar_tests/author_"+authorName+"_grammar_data.csv"));
		BufferedWriter buff = new BufferedWriter(fw);
		Iterator<TreeData> tdIter = sortedTreeData.iterator();
		String plusAuthorName = "\"";
		String authorHeader = "";
		if(authorName.equals("ALL_AUTHORS") == false){
			plusAuthorName = authorName+",\"";
			authorHeader = "Author Name,";
		}
		buff.write(authorHeader+"Tree Structure,Tree Depth,Number of Occurrences,Number Uniqe Occurrences,Associated Strings\n");
		
		while(tdIter.hasNext()){
			TreeData temp = tdIter.next();
			//System.out.println(temp.treeStructure.replaceAll(",", "\",\""));
			//in.nextLine();
			buff.write(plusAuthorName+temp.treeStructure+"\","+temp.treeDepth+","+temp.numberOfOccurrences+","+temp.numUnique+","+temp.getOrderedStringsAsString(true)+"\n");
		}
		buff.close();
		return true;
	}

}


		
		
