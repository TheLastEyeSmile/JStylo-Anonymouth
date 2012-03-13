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

import edu.drexel.psal.anonymouth.gooie.DocsTabDriver.ExtFilter;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.ui.TreeJPanel;

class ParserDemo2 {
	
	
	static String[] authorNames;// = new String[]{"aa","cc","p","q","r","x","y","z"};

	/** Usage: ParserDemo2 [[grammar] textFile] */
	public static void main(String[] args) throws IOException {
		String grammar =  "./jsan_resources/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "120", "-retainTmpSubcategories" };
		LexicalizedParser lp = new LexicalizedParser(grammar, options);
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		Iterable<List<? extends HasWord>> sentences;

		//String pathToDoc = "/Users/lux/repos/Anonymouth_user_study_documents/Anonymouth_user_study_user_2/documentToAnonymize/part_1.txt";
		//String pathToBallDoc = "/Users/lux/PSAL/paraphrasing/kickingTheBall.txt";
		//ArrayList<String> sentenceTokens = getDocs(new String[]{pathToDoc});
		
		HashMap<String,ArrayList<String>> everything = docPathFinder();
		Set<String> allAuthors = everything.keySet();
		Iterator<String> strIter = allAuthors.iterator();
		String currentAuthor;
		ArrayList<String> sentenceTokens;
		while(strIter.hasNext()){
			currentAuthor = strIter.next();
			sentenceTokens = everything.get(currentAuthor);
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
		TreeContainer tc = new TreeContainer();
		//int numDone = 0;
		for (List<? extends HasWord> sentence : sentences) {
			Tree parse = lp.apply(sentence);
			//parse.pennPrint();
			//System.out.println(parse.treeSkeletonCopy().toString());
			//System.out.println(parse.taggedYield());
			//System.out.println();
			//printSubTrees(parse);
			//TreeContainer.recurseTree(parse,"breadth");
			tc.processTree(parse, 0, false);
			//System.out.println(tc.processedTrees.toString().replaceAll("\\]\\], \\(","\\]\\]\n\\("));
			//numDone++;
			//System.out.println("sent "+numDone+" of "+numSentences+" done ");
			//System.out.println(tc.processedTrees.toString());
			//in.nextLine();
			//TreeContainer.recurseTree(parse, "depth");
			//in.nextLine();
			//addTree(parse);
			//GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);//TODO: LOOK AT THIS
			//Collection tdl = gs.typedDependenciesCCprocessed(true);
			//System.out.println(tdl);
			//System.out.println();
		}
		//System.out.println("After all sents: ");
		//System.out.println(tc.processedTrees.toString().replaceAll("\\]\\], \\(","\\]\\]\n\\("));
		//String sent3 = "This is one last test!";
		//Tree parse3 = lp.apply(sent3);
		//parse3.pennPrint();
		ArrayList<TreeData> sortedTD = TreeContainer.sortTreeData(tc.processedTrees);
		TreeContainer.writeTreeDataToCSV(sortedTD,currentAuthor);
		//System.out.println("After sorting and writing:");
		//System.out.println(tc.processedTrees.toString().replaceAll("\\]\\], \\(","\\]\\]\n\\("));
		//Scanner in = new Scanner(System.in);
		//System.out.println("First one done.");
		//in.nextLine();
		//viewTrees();
	}
	
	//ArrayList<TreeData> sortedTD = TreeContainer.sortTreeData(TreeContainer.allProcessedTrees);
	//TreeContainer.writeTreeDataToCSV(sortedTD,"ALL_AUTHORS");
}
	


	//HashMap<Tree,HashMap<Integer,HashMap<String,Integer>>> 

	public static void printSubTrees(Tree tree){
		List<Tree> subTrees = tree.subTreeList();
		int i = 0;
		for(i=0; i < subTrees.size();i++){
			System.out.println(subTrees.get(i).toString());
		}
	}

	private static JFrame treeFrame = new JFrame();
	private static JPanel treesPanel = new JPanel();
	private static JScrollPane scrollingPane = new JScrollPane();
	private static int numSentences;
	private static JPanel[] treePanels;
	private static int counter = 0;
	private static int panelNumber = 0;
	private static int numJPanels;

	public static void initialize(int numSentences){

		numJPanels = (int) Math.ceil(((double)numSentences/3));
		treePanels = new JPanel[numJPanels];
		for(int i = 0; i< numJPanels;i++){
			treePanels[i] = new JPanel();
		}
	}

	public static void viewTrees(){
		GridLayout gl = new GridLayout(numJPanels,1);
		treesPanel.setLayout(gl);
		for(int i =0; i < numJPanels; i++){
			treesPanel.add(treePanels[i]);
		}
		scrollingPane.setViewportView(treesPanel);
		scrollingPane.setVisible(true);
		treeFrame.getContentPane().add(scrollingPane,BorderLayout.CENTER);
		treeFrame.pack();
		treeFrame.setVisible(true);
		treesPanel.setVisible(true);
		Scanner in = new Scanner(System.in);
		while(treeFrame.isVisible()){
			String trash = in.nextLine();
			treeFrame.setVisible(false);
		}

	}

	public static void addTree(Tree tree){
		if(counter >= 3){
			counter = 0;
			panelNumber++;
		}
		TreeJPanel tjp = new TreeJPanel();
		// String ptbTreeString1 = "(ROOT (S (NP (DT This)) (VP (VBZ is) (NP (DT a) (NN test))) (. .)))";
		//String ptbTreeString = "(ROOT (S (NP (NNP Interactive_Tregex)) (VP (VBZ works)) (PP (IN for) (PRP me)) (. !))))";
		//if (args.length > 0) {
		//ptbTreeString = args[0];
		//}
		//Tree tree = (new PennTreeReader(new StringReader(ptbTreeString), new LabeledScoredTreeFactory(new StringLabelFactory()))).readTree();
		tjp.setTree(tree);
		tjp.setBackground(Color.white);
		treePanels[panelNumber].add(tjp);
		counter++;
		//JFrame frame = new JFrame();
		//frame.getContentPane().add(tjp, BorderLayout.CENTER);
		//frame.addWindowListener(new WindowAdapter() {
		//@Override
		//public void windowClosing(WindowEvent e) {
		//System.exit(0);
		//}
		//});
		//frame.pack();
		//frame.setVisible(true);
		//Scanner in = new Scanner(System.in);
		//while(frame.isVisible()){
		//	String trash = in.nextLine();
		//	frame.setVisible(false);
		//}
	}


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

}