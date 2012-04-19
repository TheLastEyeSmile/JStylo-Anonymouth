package edu.drexel.psal.anonymouth.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import com.jgaap.generics.Document;

import edu.drexel.psal.anonymouth.gooie.DocsTabDriver.ExtFilter;
import edu.drexel.psal.anonymouth.gooie.ErrorHandler;
import edu.drexel.psal.anonymouth.gooie.ThePresident;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;

/**
 * Parses documents.....
 * @author Andrew W.E. McDonald
 *
 */
public class DocumentTagger implements Runnable{
	
	
	//static String[] authorNames;// = new String[]{"aa","cc","p","q","r","x","y","z"};
	//private static Document dummy_doc = new Document();
	//private static ArrayList<TaggedDocument> taggedOtherSampleDocs;
	//private static ArrayList<TaggedDocument> taggedAuthorSampleDocs;
	//private static ArrayList<TaggedDocument> taggedToModifyDoc;
	//private static TreeProcessor[] allTreeProcessors = new TreeProcessor[3];
	//private static HashMap<String,ArrayList<TreeData>> allParsedAndOrdered = new HashMap<String,ArrayList<TreeData>>(3);
	//private MaxentTagger mt = null;
	private List<Document> toTag;
	private boolean loadIfExists;
	private ArrayList<TaggedDocument> tagged;
	private boolean finishedTagging = false;
	
	public DocumentTagger(){

	}

	public void setDocList(List<Document> toTag, boolean loadIfExists){
		this.toTag = toTag;
		this.loadIfExists = loadIfExists;
		Logger.logln("Set document to tag.");
	}
	
	
	/*
	public static void setDocs(List<Document> otherSample, List<Document> authorSample, List<Document> toModify) throws Exception{
		dummy_doc.setAuthor("Dummy author. This author should absolutley never be seen. If it is seen, and it is the last author in one of the lists, those documents won't process, and bad things will follow.");
		Logger.logln("Starting otherSample in DocumentParser... size: "+otherSample.size());
		taggedOtherSampleDocs = getDocs(otherSample, false);
		dummy_doc.setAuthor("Dummy author. This author should absolutley never be seen. If it is seen, and it is the last author in one of the lists, those documents won't process, and bad things will follow.");
		Logger.logln("Starting authorSample in DocumentParser... size: "+authorSample.size());
		taggedAuthorSampleDocs = getDocs(authorSample, false);
		dummy_doc.setAuthor("Dummy author. This author should absolutley never be seen. If it is seen, and it is the last author in one of the lists, those documents won't process, and bad things will follow.");
		Logger.logln("Starting toModify in DocumentParser... size: "+toModify.size());
		taggedToModifyDoc = getDocs(toModify, true);
		System.out.println(taggedToModifyDoc.get(0).toString());
	}
	*/
	
	public void run() {
		try {
			tagged = tagDocs(toTag,loadIfExists);
			finishedTagging = true;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logln("ERROR! Tagging documets failed!");
			ErrorHandler.fatalError();
		}
	}
		
	public ArrayList<TaggedDocument>tagDocs(List<Document> docs, boolean isToModify) throws Exception{
		String currentAuthor;
		String docTitle;
		String fullDoc = "";
		ArrayList<TaggedDocument> outMap = new ArrayList<TaggedDocument>();
		for(Document d:docs){
			currentAuthor = docs.get(0).getAuthor();
			docTitle = docs.get(0).getTitle();
			TaggedDocument td = null;
			if(ObjectIO.objectExists(currentAuthor+"_"+docTitle,ThePresident.GRAMMAR_DIR) == true && !isToModify){
				td = ObjectIO.readTaggedDocument(docTitle+"_"+currentAuthor, ThePresident.GRAMMAR_DIR, false);
			}
			else{
				d.load();
				fullDoc = d.stringify();//.replaceAll("\\p{C}"," ");// get rid of unicode control chars (causes parse errors).
				td = new TaggedDocument(fullDoc,docTitle,currentAuthor);
				if (ThePresident.SAVE_TAGGED_DOCUMENTS == true)
					td.writeSerializedSelf(ThePresident.GRAMMAR_DIR);
			}
			outMap.add(td);
			
		}
		return outMap;
	}
/*
	public HashMap<String,ArrayList<TreeData>> tagAllDocs() throws IOException{ 
		ArrayList<HashMap<String,ArrayList<String>>> everything = new ArrayList<HashMap<String,ArrayList<String>>>(3); 
		everything.add(0,otherSampleStrings);
		everything.add(1,authorSampleStrings);
		everything.add(2,toModifyStrings);
		Iterator<HashMap<String,ArrayList<String>>> everythingIter = everything.iterator();
		int docTypeNumber = -1; // 0 for otherSampleStrings, 1 for authorSampleStrings, 2 for toModifyStrings
		int numLoaded = 0;
		while(everythingIter.hasNext()){
			docTypeNumber++;
			Set<String> currentDocStrings = currentSampleStrings.keySet();
			Iterator<String> docStrIter = currentDocStrings.iterator();
			String docID;
			ArrayList<String> sentenceTokens;
			allTreeProcessors[docTypeNumber]  = new TreeProcessor();
			allTreeProcessors[docTypeNumber].clearLoadedTreeDataMaps();
			numLoaded=0;
			while(docStrIter.hasNext()){
				docID = docStrIter.next();
				sentenceTokens = currentSampleStrings.get(docID);
				if(sentenceTokens == null){
					allTreeProcessors[docTypeNumber].loadTreeDataMap(docID, GRAMMAR_DIR, false);
					numLoaded++;
					continue;
				}
				//System.out.println(sentenceTokens.size()+", strIter.hasNext? -> "+strIter.hasNext());

				numSentences = sentenceTokens.size();
				//initialize(numSentences);
				Iterator<String> sentIter = sentenceTokens.iterator();
				List<List<? extends HasWord>> tmp = new ArrayList<List<? extends HasWord>>();
				String tempSent;
				while(sentIter.hasNext()){
					tempSent = sentIter.next();
					Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(tempSent));
					List<? extends HasWord> sentenceTokenized = toke.tokenize();
					tmp.add(sentenceTokenized);
				}
				sentences = tmp;
				//int numDone = 0;
				TreeProcessor.singleDocMap.clear();
				boolean willSaveResults = true;
				for (List<? extends HasWord> sentence : sentences) {
					ArrayList<TaggedWord> taggedWords = mt.tagSentence(sentence);
				}
				if(willSaveResults == true)
					ObjectIO.writeObject(TreeProcessor.singleDocMap,docID, GRAMMAR_DIR);
			}
			
			//TreeProcessor.writeTreeDataToCSV(sortedTD,docID);
			allTreeProcessors[docTypeNumber].unmergedMaps = new ArrayList<HashMap<String,TreeData>>(numLoaded+1);
			
		}	
		
		
		int i= 0;
		allParsedAndOrdered.clear();
		String[] docTypes = new String[]{"otherSample","authorSample","toModify"};
		for(i=0; i < 3; i++){
			allTreeProcessors[i].unmergedMaps.add(allTreeProcessors[i].processedTrees);
			allTreeProcessors[i].unmergedMaps.addAll(allTreeProcessors[i].loadedTreeDataMaps);
			allTreeProcessors[i].mergeTreeDataLists(allTreeProcessors[i].unmergedMaps);
			allParsedAndOrdered.put(docTypes[i],allTreeProcessors[i].sortTreeData(allTreeProcessors[i].mergedMap));
			
		}
		
		//ArrayList<TreeData> sortedTD = TreeContainer.sortTreeData(TreeContainer.allProcessedTrees);
		//TreeContainer.writeTreeDataToCSV(sortedTD,"ALL_AUTHORS");
		
		return allParsedAndOrdered;
	}
*/
/*
	public static HashMap<String,ArrayList<String>> docPathFinder() throws IOException{
		HashMap<String,ArrayList<String>> everything = new HashMap<String,ArrayList<String>>();
		JFileChooser open = new JFileChooser();
		open.setMultiSelectionEnabled(true);
		open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
		open.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int answer = open.showOpenDialog(null);
		int num = 0;
		String[] paths;
		if (answer == JFileChooser.APPROVE_OPTION) {
			File[] files = open.getSelectedFiles();
			authorNames = new String[files.length];
			int numFiles = files.length;
			for (File file: files) {
				if(file.isDirectory()){
					String[] theDocsInTheDir = file.list();
					int numDocs = theDocsInTheDir.length;
					authorNames[num] = file.getName();
					String pathFirstHalf = file.getAbsolutePath();
					paths = new String[numDocs];
					int innerNum =0;
					for (String otherFile: theDocsInTheDir){
						File newFile = new File(otherFile);	
						String path = pathFirstHalf+File.separator+otherFile;
						if(path.contains(".svn") || path.contains("imitation") || path.contains("verification") || path.contains("obfuscation") || path.contains("demographics"))
							continue;
						paths[innerNum] = path;
						innerNum++;
					}
					ArrayList<String> authorsSentenceTokens = getDocs(paths);
					everything.put(authorNames[num],authorsSentenceTokens);
					num++;
				}
			}
		}
		return everything;

	}


	public static ArrayList<String> getDocs(String[] paths) throws IOException{
		String fullDoc = "";
		for(String s:paths){
			if (s != null){
				FileReader fr = new FileReader(new File(s));
				BufferedReader buff = new BufferedReader(fr);
				String tempDoc = "";
				while((tempDoc = buff.readLine()) != null){
					fullDoc += tempDoc;
				}
			}
		}
		SentenceTools st = new SentenceTools();
		st.makeSentenceTokens(fullDoc);
		ArrayList<String> sentenceTokens = st.getSentenceTokens();
		return sentenceTokens;


	}
	
	public static void main(String[] args){
		DocumentTagger tagger=new DocumentTagger();
		String test="This is a test string that I want to see tagged. Actually, it will be two sentences.";
		
	}
*/
}