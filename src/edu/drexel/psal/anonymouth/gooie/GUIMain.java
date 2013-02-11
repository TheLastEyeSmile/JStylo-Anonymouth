package edu.drexel.psal.anonymouth.gooie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import edu.drexel.psal.JSANConstants;
import edu.drexel.psal.jstylo.generics.*;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.drexel.psal.anonymouth.gooie.Translation;
import edu.drexel.psal.anonymouth.gooie.DocsTabDriver.ExtFilter;
import edu.drexel.psal.anonymouth.utils.ConsolidationStation;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.*;
import javax.swing.tree.*;

import net.miginfocom.swing.MigLayout;

import com.jgaap.generics.Document;

import weka.classifiers.*;

import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * JStylo main GUI class.
 * 
 * @author Andrew W.E. McDonald
 */
//This is a comment from Joe Muoio to see if he can commit changes.
public class GUIMain extends javax.swing.JFrame 
{
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		/*InfoNodeLookAndFeel info = new InfoNodeLookAndFeel();
		try {
			UIManager.setLookAndFeel(info);
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}


	// main instance
	public static GUIMain inst;
	protected JPanel mainPanel;

	// ------------------------

	// data
	protected ProblemSet ps;
	protected CumulativeFeatureDriver cfd;
	protected List<CumulativeFeatureDriver> presetCFDs;
	protected WekaInstancesBuilder wib;
	protected WekaAnalyzer wad;
	protected List<Classifier> classifiers;
	protected Thread analysisThread;
	protected List<String> results;
	
	protected PreProcessSettingsFrame PPSP;

	protected String defaultTrainDocsTreeName = "Authors"; 
	protected Font defaultLabelFont = new Font("Verdana",0,16);
	protected static int cellPadding = 5;
	
	// possible color scheme
	protected final Color lightblue = new Color(126, 181, 214);
	protected final Color medblue = new Color(42, 117, 169);
	protected final Color darkblue = new Color(39, 66, 87);
	protected final Color lightbrown = new Color(223, 193, 132);
	protected final Color medbrown = new Color(143, 96, 72);
	protected final Color darkbrown = new Color(100, 68, 54);
	
	protected final Color ready = new Color(0,255,128);
	protected final Color notReady = new Color(255,102,102);
	protected final Color tan = new Color(252,242,206);
	protected final Color optionsColor = tan;

	// tabs
	protected JTabbedPane mainJTabbedPane;
	protected JPanel docsTab;
	protected JPanel featuresTab;
	protected JPanel classTab;
	protected JPanel editorTab;
	
	// documents tab
	protected String propFileName = "jsan_resources/anonymouth_prop.prop";
	File propFile = new File(propFileName);
	Properties prop = new Properties();
	protected JFileChooser load = new JFileChooser();
	protected JFileChooser save = new JFileChooser();
	
	protected JLabel testDocsJLabel;
	protected JButton trainDocPreviewJButton;
	protected JButton testDocPreviewJButton;
	protected JButton trainNameJButton;
	protected JLabel docPreviewJLabel;
	protected JButton newProblemSetJButton;
	protected JTextPane docPreviewJTextPane;
	protected JScrollPane docPreviewJScrollPane;
	protected JButton loadProblemSetJButton;
	protected JButton saveProblemSetJButton;
	protected JButton docTabNextJButton;
	protected JButton removeAuthorJButton;
	protected JButton removeTrainDocsJButton;
	protected JButton addTrainDocsJButton;
	
	protected JTable testDocsJTable;
	protected DefaultTableModel testDocsTableModel;
	protected JLabel featuresToolsJLabel;
	protected JLabel docPreviewNameJLabel;
	protected JLabel corpusJLabel;
	protected JButton removeTestDocJButton;
	protected JButton addAuthorJButton;
	protected JButton addTestDocJButton;
	protected JButton clearDocPreviewJButton;
	protected JButton docsAboutJButton;
	protected JTable userSampleDocsJTable;
	protected DefaultTableModel userSampleDocsTableModel;
	protected JLabel userSampleDocsJLabel;
	protected JPanel buttons;
	protected JButton adduserSampleDocJButton;
	protected JButton removeuserSampleDocJButton;
	protected JButton userSampleDocPreviewJButton;

//	// features tab
//	protected JButton featuresNextJButton;
//	protected JButton featuresBackJButton;
//	protected JLabel featuresFeatureConfigJLabel;
//	protected JLabel featuresFactorContentJLabel;
//	protected JLabel featuresFeatureExtractorContentJLabel;
//	protected JScrollPane featuresFeatureExtractorJScrollPane;
//	protected JLabel featuresNormContentJLabel;
//	protected JScrollPane featuresFeatureExtractorConfigJScrollPane;
//	protected JScrollPane featuresCullConfigJScrollPane;
//	protected JScrollPane featuresCanonConfigJScrollPane;
//	protected JList featuresCullJList;
//	protected DefaultComboBoxModel featuresCullJListModel;
//
//	protected JScrollPane featuresCullListJScrollPane;
//	protected JScrollPane featuresCanonListJScrollPane;
//	protected JList featuresCanonJList;
//	protected DefaultComboBoxModel featuresCanonJListModel;
//	protected JScrollPane featuresFeatureDescJScrollPane;
//	protected JTextPane featuresFeatureDescJTextPane;
//	protected JLabel featuresFeatureExtractorJLabel;
//	protected JLabel featuresFactorJLabel;
//	protected JLabel featuresNormJLabel;
//	protected JLabel featuresFeatureDescJLabel;
//	protected JTextField featuresFeatureNameJTextField;
//	protected JLabel featuresFeatureNameJLabel;
//	protected JLabel featuresCullJLabel;
//	protected JLabel featuresCanonJLabel;
//	protected JButton featuresEditJButton;
//	protected JButton featuresRemoveJButton;
//	protected JButton featuresAddJButton;
//	protected JList featuresJList;
//	protected DefaultComboBoxModel featuresJListModel;
//	protected JLabel featuresFeaturesJLabel;
//	protected JTextPane featuresSetDescJTextPane;
//	protected JScrollPane featuresSetDescJScrollPane;
//	protected JLabel featuresSetDescJLabel;
//	protected JTextField featuresSetNameJTextField;
//	protected JLabel featuresSetNameJLabel;
//	protected JButton featuresNewSetJButton;
//	protected JButton featuresSaveSetJButton;
//	protected JButton featuresLoadSetFromFileJButton;
//	protected JButton featuresAddSetJButton;
//	protected JComboBox featuresSetJComboBox;
//	protected DefaultComboBoxModel featuresSetJComboBoxModel;
//	protected JLabel featuresSetJLabel;
//	protected JButton featuresAboutJButton;

	// Calssifiers tab
	protected JTextField classAvClassArgsJTextField;
	protected JLabel classAvClassArgsJLabel;
	protected JComboBox classClassJComboBox;
	protected JLabel classAvClassJLabel;
	protected JButton classAddJButton;
	
	protected JTextField classSelClassArgsJTextField;
	protected JLabel classSelClassArgsJLabel;
	protected JScrollPane classSelClassJScrollPane;
	protected DefaultComboBoxModel classSelClassJListModel;
	protected JScrollPane classTreeScrollPane;
	protected JScrollPane classDescJScrollPane;
	protected JTextPane classDescJTextPane;
	protected JLabel classDescJLabel;
	protected JButton classBackJButton;
	protected JButton classNextJButton;
	protected JLabel classSelClassJLabel;
	protected JButton classRemoveJButton;
	protected JButton classAboutJButton;
	
	// Editor tab
	
	
	protected JScrollPane theEditorScrollPane;
	protected JTable suggestionTable;
	protected JPanel editorRowTwoButtonBufferPanel;
	protected JPanel buttonBufferJPanel;
	protected JPanel editorBottomRowButtonPanel;
	protected JPanel editorTopRowButtonsPanel;
	protected JPanel editorButtonJPanel;
	protected JPanel editorInteractionWestPanel;
	protected JPanel editorInteractionJPanel;
	protected JPanel jPanel2;
	protected JPanel dummyPanelUpdatorLeftSide;
	protected JPanel elementsToAddBoxLabelJPanel;
	protected JPanel suggestionBoxLabelJPanel;
	protected JPanel jPanel1;
	protected JPanel valueLabelJPanel;
	protected JPanel valueBoxPanel;
	protected JPanel updaterJPanel;
	//-------------- HELP TAB PANE STUFF ---------
	protected JTabbedPane editorHelpTabPane;
	
	protected JPanel editorHelpPrepPanel;
	protected JButton prepAdvButton;
		protected JPanel prepDocumentsPanel;
			protected JPanel prepMainDocPanel;
				protected JLabel prepDocLabel;
				protected JLabel mainLabel;
				protected JList prepMainDocList;
				protected JButton clearProblemSetJButton;
				protected JScrollPane prepMainDocScrollPane;
			protected JPanel prepSampleDocsPanel;
				protected JLabel sampleLabel;
				protected JList prepSampleDocsList;
				protected JScrollPane prepSampleDocsScrollPane;
			protected JPanel prepTrainDocsPanel;
				protected JLabel trainLabel;
				protected JTree trainCorpusJTree;
				protected JScrollPane trainCorpusJTreeScrollPane;
		protected JPanel prepFeaturesPanel;
			protected JLabel prepFeatLabel;
			protected JComboBox featuresSetJComboBox;
			protected DefaultComboBoxModel featuresSetJComboBoxModel;
		protected JPanel prepClassifiersPanel;
			protected JLabel prepClassLabel;
			protected JPanel prepAvailableClassPanel;
				protected JTree classJTree;
				protected JScrollPane prepAvailableClassScrollPane;
			protected JPanel prepSelectedClassPanel;
				protected JList classJList;
				protected JScrollPane prepSelectedClassScrollPane;
	
	protected JPanel editorHelpSugPanel;
		protected JPanel elementsPanel;
		protected JPanel elementsToAddPanel;
		protected JLabel elementsToAddLabel;
		protected JTextPane elementsToAddPane;
		protected JScrollPane elementsToAddScrollPane;
		protected JPanel elementsToRemovePanel;
		protected JLabel elementsToRemoveLabel;
		protected JTextPane elementsToRemovePane;
		protected JScrollPane elementsToRemoveScrollPane;
		
	protected JPanel editorHelpTransPanel;
		protected JPanel translationsPanel;
		protected JLabel translationsLabel;
		protected JTable translationsTable;
		protected JScrollPane translationsScrollPane;
		protected JComboBox translationsComboBox;
	
	protected JPanel editorHelpInfoPanel;
		protected JLabel sentenceEditorLabel;
		protected JLabel documentViewerLabel;
		protected JLabel classificationResultsLabel;
		protected JTextPane descriptionPane;
		
		protected JPanel instructionsPanel;
		protected JLabel instructionsLabel;
		protected JTextPane instructionsPane;
		protected JScrollPane instructionsScrollPane;
		protected JPanel synonymsPanel;
		protected JLabel synonymsLabel;
		protected JTextPane synonymsPane;
		protected JScrollPane synonymsScrollPane;
	//--------------------------------------------
		
	//--------------- Editor Tab Pane stuff ----------------------
		protected JTabbedPane editorTabPane;
		protected JPanel editBoxPanel;
		protected JPanel sentenceAndDocumentPanel;
		protected JPanel sentenceLabelPanel;
		
		protected JPanel sentenceEditingPanel;
		protected JPanel documentPanel;
		protected JPanel resultsPanel;
		protected JPanel documentOptionsPanel;
		
		protected JScrollPane sentencePane;
		protected JPanel sentenceOptionsPanel;
		protected JPanel translationOptionsPanel;
		protected JButton removeWordsButton;
		protected JButton shuffleButton;
		protected JButton SaveChangesButton;
		protected JButton copyToSentenceButton;
		private JPanel spacer1;
		protected JButton restoreSentenceButton;
		protected JLabel classificationLabel;
		protected JPanel resultsBoxPanel_InnerBottomPanel;
		protected JTable resultsTable;
		protected JScrollPane resultsTablePane;
		protected JPanel resultsBoxPanel;
		protected JLabel resultsTableLabel;
		protected JPanel resultsTableLabelPanel;
		protected JPanel resultsBoxAndResultsLabelPanel;
		protected JTextPane editorBox;
		protected JScrollPane editBox;
		protected JPanel editorBoxPanel;
		protected JLabel editBoxLabel;
		protected JPanel editBoxLabelPanel;
		protected JPanel editBoxAndEditLabelPanel;
		public JTextPane sentenceEditPane; //============================================ PUBLIC
		protected JLabel sentenceBoxLabel;
		protected JPanel sentencePanel;
		protected JPanel sentenceAndSentenceLabelPanel;
		protected JLabel translationsBoxLabel;
		protected JScrollPane translationPane;
		protected JTextPane translationEditPane;
		
		private boolean tabMade = false;
		protected int resultsMaxIndex;
		protected String chosenAuthor;
		
		protected JButton dictButton;
		protected JButton appendSentenceButton;
		protected JButton saveButton;
		protected JButton processButton;
		protected JButton nextSentenceButton;
		protected JButton prevSentenceButton;
		protected JButton transButton;

		private String oldEditorBoxDoc = " ";
		private TableModel oldResultsTableModel = null;
		private TableCellRenderer tcr = new DefaultTableCellRenderer();
		
		protected JScrollPane clusterScrollPane;
		protected JPanel holderPanel;
		protected JPanel topPanel;
		protected JButton reClusterAllButton;
		protected JButton refreshButton;
		protected JButton selectClusterConfiguration;
		protected JPanel secondPanel;
	//--------------------------------------------------------------------
	
	protected JPanel editorInfoJPanel;
	protected JScrollPane editorInteractionScrollPane;
	protected JScrollPane EditorInfoScrollPane;
	protected JTabbedPane editTP;
	
	protected JScrollPane wordsToAddPane;
	protected JTextField searchInputBox;
	protected JComboBox highlightSelectionBox;
	protected JLabel highlightLabel;
	protected JPanel jPanel_IL3;
	protected JButton clearHighlightingButton;
	protected JLabel featureNameLabel;
	protected JLabel targetValueLabel;
	protected JLabel presentValueLabel;
	protected JTextField targetValueField;
	protected JTextField presentValueField;
	protected JLabel suggestionListLabel;
	protected JButton verboseButton;
	protected JScrollPane suggestionListPane;
	
	// Analysis tab
	protected JCheckBox analysisOutputAccByClassJCheckBox;
	protected JCheckBox analysisOutputConfusionMatrixJCheckBox;
	protected ButtonGroup analysisTypeButtonGroup;
	
	protected static ImageIcon iconNO;
	protected static ImageIcon iconFINISHED;
	public static ImageIcon icon;
	
	protected Translation GUITranslator = new Translation();
	
	protected boolean docPPIsShowing = true;
	protected boolean featPPIsShowing = true;
	protected boolean classPPIsShowing = true;
	
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void startGooie() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Logger.initLogFile();
				try {
					icon = new ImageIcon(getClass().getResource(JSANConstants.JSAN_GRAPHICS_PREFIX+"Anonymouth_LOGO.png"),"logo");
					iconNO = new ImageIcon(getClass().getResource(JSANConstants.JSAN_GRAPHICS_PREFIX+"Anonymouth_NO.png"), "my 'no' icon");
					iconFINISHED = new ImageIcon(getClass().getResource(JSANConstants.JSAN_GRAPHICS_PREFIX+"Anonymouth_FINISHED.png"), "my 'finished' icon");
					//javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					System.err.println("Look-and-Feel error!");
				}
				inst = new GUIMain();
				inst.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public GUIMain() {
		super();
		initData();
		initGUI();
		
	}

	private void initData() {
		ProblemSet.setDummyAuthor("~* you *~");
		ps = new ProblemSet();
		ps.setTrainCorpusName(defaultTrainDocsTreeName);
		cfd = new CumulativeFeatureDriver();
		FeaturesTabDriver.initPresetCFDs(this);
		FeatureWizardDriver.populateAll();
		classifiers = new ArrayList<Classifier>();
		wib = new WekaInstancesBuilder(true);
		results = new ArrayList<String>();
		
		// properties file -----------------------------------
		BufferedReader propReader = null;
		
		if (!propFile.exists())
		{
			try {propFile.createNewFile();} 
			catch (IOException e1) {e1.printStackTrace();}
		}
		
		try {propReader = new BufferedReader (new FileReader(propFileName));} 
		catch (FileNotFoundException e) {e.printStackTrace();}
		
		try {prop.load(propReader);}
		catch (IOException e) {e.printStackTrace();}
	}

	private void initGUI() {
		try 
		{
			setExtendedState(MAXIMIZED_BOTH);
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setSize(new Dimension((int)(screensize.width*.75), (int)(screensize.height*.75)));
			this.setTitle("Anonymouth");
			this.setIconImage(new ImageIcon(getClass().getResource(JSANConstants.JSAN_GRAPHICS_PREFIX+"Anonymouth_LOGO.png")).getImage());
			
			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			JMenuItem printMenuItem = new JMenuItem("Print...");
			JMenu settingsMenu = new JMenu("Settings");
			JMenuItem settingsAdvancedMenuItem = new JMenuItem("Advanced...");
			JMenu helpMenu = new JMenu("Help");
			JMenuItem aboutMenuItem = new JMenuItem("About Anonymouth");
			
			menuBar.add(fileMenu);
			fileMenu.add(printMenuItem);
			menuBar.add(settingsMenu);
			settingsMenu.addSeparator();
			settingsMenu.add(settingsAdvancedMenuItem);
			menuBar.add(helpMenu);
			helpMenu.add(aboutMenuItem);
			this.setJMenuBar(menuBar);
			
			getContentPane().setLayout(new MigLayout(
					"fill", // layout constraints
					"[grow 20, fill][grow 80, growprio 110, fill]", // column constraints
					"[fill]")); // row constraints)
			
			editorHelpTabPane = new JTabbedPane();
			{
				editorHelpTabPane.addTab("Pre-Process", createPPTab());
				editorHelpTabPane.addTab("Information", createInfoTab());
				editorHelpTabPane.addTab("Suggestions", createSugTab());
				editorHelpTabPane.addTab("Translations", createTransTab());
			}
			
			editorTabPane = new JTabbedPane();
			{
				editorTabPane.addTab("Document", createDocumentTab());
				editorTabPane.addTab("Clusters", createClustersTab());
			}
			
			
			
			getContentPane().add(editorHelpTabPane, "width 300!");
			getContentPane().add(editorTabPane, "width 600::");
			
			
			
			// final property settings
			
			EditorTabDriver.setAllDocTabUseable(false, this);
			
			// init all settings panes
			
			PPSP = new PreProcessSettingsFrame(this);
			
			// initialize listeners - except for EditorTabDriver!
			
			MainDriver.initListeners(this);
			DocsTabDriver.initListeners(this);
			FeaturesTabDriver.initListeners(this);
			ClassTabDriver.initListeners(this);
			EditorTabDriver.initListeners(this);
			//EditorTabDriver.initEditorInnerTabListeners(this);
			ClusterViewerDriver.initListeners(this);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean documentsAreReady()
	{
		boolean ready = true;
		try {
			if (!mainDocReady())
				ready = false;
			if (!sampleDocsReady())
				ready = false;
			if (!trainDocsReady())
				ready = false;
		}
		catch (Exception e){
			return false;
		}
		
		return ready;
	}
	
	public boolean mainDocReady()
	{
		if (inst.ps.hasTestDocs())
			return true;
		else
			return false;
	}
	
	public boolean sampleDocsReady()
	{
		if (!inst.ps.getTrainDocs(ProblemSet.getDummyAuthor()).isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean trainDocsReady()
	{
		if (inst.ps.hasTestDocs())
			return true;
		else
			return false;
	}
	
	public boolean featuresAreReady()
	{
		boolean ready = true;
		
		try {
			if (inst.cfd.numOfFeatureDrivers() == 0)
				ready = false;
		}
		catch (Exception e){
			return false;
		}
		
		return ready;
	}
	
	public boolean classifiersAreReady()
	{
		boolean ready = true;
		
		try {
			if (inst.classifiers.isEmpty())
				ready = false;
		}
		catch (Exception e){
			return false;
		}
		
		return ready;
	}
	
	/**
	 * Creates a Pre-Process panel that can be added to the "help area".
	 * @return editorHelpSettingsPanel
	 */
	protected JPanel createPPTab()
	{
		editorHelpPrepPanel = new JPanel();
		//editorHelpPrepPanel.setMaximumSize(editorHelpPrepPanel.getPreferredSize());
		MigLayout settingsLayout = new MigLayout(
				"fill, wrap 1",
				"fill, grow",
				"fill, grow");
		editorHelpPrepPanel.setLayout(settingsLayout);
		prepDocumentsPanel = new JPanel();
		MigLayout documentsLayout = new MigLayout(
				"fill, wrap 4",
				"grow 25, fill, center");
		prepDocumentsPanel.setLayout(documentsLayout);
		//prepDocumentsPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
		{
			// Advanced Button
			prepAdvButton = new JButton("Advanced");
			prepDocumentsPanel.add(prepAdvButton, "span 2, skip 1");
			
			// Documents Label
			prepDocLabel = new JLabel("Documents:");
			prepDocLabel.setFont(new Font("Ariel", Font.BOLD, 12));
			prepDocLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepDocLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			prepDocLabel.setOpaque(true);
			prepDocLabel.setBackground(notReady);
			prepDocumentsPanel.add(prepDocLabel, "skip 1, span, h 20!");
			
			// Save Problem Set button
			saveProblemSetJButton = new JButton("Save");
			prepDocumentsPanel.add(saveProblemSetJButton, "span 4, split 3");
			
			// load problem set button
			loadProblemSetJButton = new JButton("Load");
			prepDocumentsPanel.add(loadProblemSetJButton);
			
			// Save Problem Set button
			clearProblemSetJButton = new JButton("Clear");
			prepDocumentsPanel.add(clearProblemSetJButton);
			
			// main label
			mainLabel = new JLabel("Main:");
			mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepDocumentsPanel.add(mainLabel, "span 2");
			
			// sample label
			sampleLabel = new JLabel("Sample:");
			sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepDocumentsPanel.add(sampleLabel, "span 2");
			
			// main documents list
			DefaultListModel mainDocListModel = new DefaultListModel();
			prepMainDocList = new JList(mainDocListModel);
			prepMainDocList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			prepMainDocScrollPane = new JScrollPane(prepMainDocList);
			prepDocumentsPanel.add(prepMainDocScrollPane, "span 2, growy, h 60::180");
			
			// sample documents list
			DefaultListModel sampleDocsListModel = new DefaultListModel();
			prepSampleDocsList = new JList(sampleDocsListModel);
			prepSampleDocsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			prepSampleDocsScrollPane = new JScrollPane(prepSampleDocsList);
			prepDocumentsPanel.add(prepSampleDocsScrollPane, "span 2, growy, h 60::180");
			
			// main add button
			addTestDocJButton = new JButton("Add");
			prepDocumentsPanel.add(addTestDocJButton);
			
			// main delete button
			removeTestDocJButton = new JButton("Delete");
			prepDocumentsPanel.add(removeTestDocJButton);
			
			// sample add button
			adduserSampleDocJButton = new JButton("Add");
			prepDocumentsPanel.add(adduserSampleDocJButton);
			
			// sample delete button
			removeuserSampleDocJButton = new JButton("Delete");
			prepDocumentsPanel.add(removeuserSampleDocJButton);
			
			// train label
			trainLabel = new JLabel("Other Authors:");
			trainLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepDocumentsPanel.add(trainLabel, "span");
			
			// train tree
			DefaultMutableTreeNode top = new DefaultMutableTreeNode(ps.getTrainCorpusName());
			trainCorpusJTree = new JTree(top);
			trainCorpusJTreeScrollPane = new JScrollPane(trainCorpusJTree);
			prepDocumentsPanel.add(trainCorpusJTreeScrollPane, "span, growy, h 120::, shrinkprio 110");
			
			// train add button
			addTrainDocsJButton = new JButton("Add");
			prepDocumentsPanel.add(addTrainDocsJButton, "span 2");
			
			// train delete button
			removeTrainDocsJButton = new JButton("Delete");
			prepDocumentsPanel.add(removeTrainDocsJButton, "span 2");
		}
		editorHelpPrepPanel.add(prepDocumentsPanel, "growx, shrinkprio 110");
		
		prepFeaturesPanel = new JPanel();
		MigLayout featuresLayout = new MigLayout(
				"fill, wrap 2",
				"fill");
		prepFeaturesPanel.setLayout(featuresLayout);
		{
			prepFeatLabel = new JLabel("Features:");
			prepFeatLabel.setOpaque(true);
			prepFeatLabel.setFont(new Font("Ariel", Font.BOLD, 12));
			prepFeatLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepFeatLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			prepFeatLabel.setBackground(notReady);
			prepFeaturesPanel.add(prepFeatLabel, "span 2, h 20!");
			
			JLabel label = new JLabel("Feature Set:");
			prepFeaturesPanel.add(label);
			
			String[] presetCFDsNames = new String[presetCFDs.size() + 1];
			presetCFDsNames[0] = "Select A Feature Set...";
			for (int i=0; i<presetCFDs.size(); i++)
				presetCFDsNames[i+1] = presetCFDs.get(i).getName();
			
			featuresSetJComboBoxModel = new DefaultComboBoxModel(presetCFDsNames);
			featuresSetJComboBox = new JComboBox();
			featuresSetJComboBox.setModel(featuresSetJComboBoxModel);
			prepFeaturesPanel.add(featuresSetJComboBox, "growx");
		}
		editorHelpPrepPanel.add(prepFeaturesPanel, "growx");
		
		prepClassifiersPanel = new JPanel();
		MigLayout classLayout = new MigLayout(
				"fill, wrap 2",
				"center, fill, grow",
				"grow, fill");
		prepClassifiersPanel.setLayout(classLayout);
		{
			prepClassLabel = new JLabel("Classifiers:");
			prepClassLabel.setOpaque(true);
			prepClassLabel.setFont(new Font("Ariel", Font.BOLD, 12));
			prepClassLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepClassLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			prepClassLabel.setBackground(notReady);
			prepClassifiersPanel.add(prepClassLabel, "span 2, h 20!");
			
			JLabel availLabel = new JLabel("Available:");
			availLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepClassifiersPanel.add(availLabel);
			
			JLabel selectedLabel = new JLabel("Selected:");
			selectedLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepClassifiersPanel.add(selectedLabel);
			
			classJTree = new JTree();
			classJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			prepAvailableClassScrollPane = new JScrollPane(classJTree);
			ClassTabDriver.initMainWekaClassifiersTree(this);
			prepClassifiersPanel.add(prepAvailableClassScrollPane, "grow, h 150:360:, w 50%:60%:75%, gapbottom 0");
			
			DefaultListModel selectedListModel = new DefaultListModel();
			classJList = new JList(selectedListModel);
			classJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			prepSelectedClassScrollPane = new JScrollPane(classJList);
			prepClassifiersPanel.add(prepSelectedClassScrollPane, "grow, h 150:360:, w 25%:40%:50%, gapbottom 0");
			
			classAddJButton = new JButton("Select");
			prepClassifiersPanel.add(classAddJButton, "gaptop 0, growy 0");
			
			classRemoveJButton = new JButton("Remove");
			prepClassifiersPanel.add(classRemoveJButton, "gaptop 0, growy 0");
		}
		editorHelpPrepPanel.add(prepClassifiersPanel, "growx");
		return editorHelpPrepPanel;
	}
	
	private JPanel createInfoTab()
	{
		//for word wrapping, generally width in style should be 100 less than width of component
		String html1 = "<html><body style='width: ";
        String html2 = "px'>";
		
		editorHelpInfoPanel = new JPanel();
		editorHelpInfoPanel.setPreferredSize(new java.awt.Dimension(290, 660));
		
		GridBagLayout infoLayout = new GridBagLayout();
		editorHelpInfoPanel.setLayout(infoLayout);
		GridBagConstraints IPConst = new GridBagConstraints();
		
		editorHelpTabPane.addTab("Information", editorHelpInfoPanel);
		{ //=========== Information Tab ====================
			//---------- Instructions Panel ----------------------
			instructionsPanel = new JPanel();
			instructionsPanel.setPreferredSize(new java.awt.Dimension(290, 660));
			
			GridBagLayout instructionsLayout = new GridBagLayout();
			editorHelpInfoPanel.setLayout(instructionsLayout);
			
			IPConst.gridx = 0;
			IPConst.gridy = 0;
			IPConst.gridheight = 1;
			IPConst.gridwidth = 1;
			editorHelpInfoPanel.add(instructionsPanel, IPConst);
			Font titleFont = new Font("Ariel", Font.BOLD, 12);
			Font answerFont = new Font("Ariel", Font.PLAIN, 11);
			{// ---------- Question One ----------------------
				JLabel questionOneTitle = new JLabel();
				questionOneTitle.setText("What is this tab?");
				questionOneTitle.setFont(titleFont);
				questionOneTitle.setPreferredSize(new java.awt.Dimension(290, 20));
				IPConst.gridx = 0;
				IPConst.gridy = 0;
				IPConst.gridheight = 1;
				IPConst.gridwidth = 3;
				instructionsPanel.add(questionOneTitle, IPConst);
			}
			{
				JLabel questionOneAnswer = new JLabel();
				String s = "This is the <b>\"Editor Tab.\"</b> Here is where you edit the document you wish to anonymize. The goal is to edit your document to a point where it is not recognized as your writing, and Anonymouth is here to help you acheive that.";
				questionOneAnswer.setText(html1+"170"+html2+s);
				questionOneAnswer.setFont(answerFont);
				questionOneAnswer.setVerticalAlignment(SwingConstants.TOP);
				
				questionOneAnswer.setPreferredSize(new java.awt.Dimension(270, 60));
				IPConst.gridx = 0;
				IPConst.gridy = 1;
				IPConst.gridheight = 1;
				IPConst.gridwidth = 2;
				instructionsPanel.add(questionOneAnswer, IPConst);
			}
			{// ---------- Question Two ----------------------
				JLabel questionTwoTitle = new JLabel();
				questionTwoTitle.setText("What should I do first?");
				questionTwoTitle.setFont(titleFont);
				questionTwoTitle.setPreferredSize(new java.awt.Dimension(290, 20));
				IPConst.gridx = 0;
				IPConst.gridy = 2;
				IPConst.gridheight = 1;
				IPConst.gridwidth = 3;
				instructionsPanel.add(questionTwoTitle, IPConst);
			}
			{
				JLabel questionTwoAnswer = new JLabel();
				String s = "If you have not processed your document yet, do so now by pressing the <b>\"Process\"</b> button. This will let us figure out how anonymous your document currently is.";
				questionTwoAnswer.setText(html1+"170"+html2+s);
				questionTwoAnswer.setFont(answerFont);
				questionTwoAnswer.setVerticalAlignment(SwingConstants.TOP);
				
				questionTwoAnswer.setPreferredSize(new java.awt.Dimension(270, 60));
				IPConst.gridx = 0;
				IPConst.gridy = 3;
				IPConst.gridheight = 1;
				IPConst.gridwidth = 2;
				instructionsPanel.add(questionTwoAnswer, IPConst);
			}
			{// ---------- Question Three ----------------------
				JLabel questionThreeTitle = new JLabel();
				questionThreeTitle.setText("How do I go about editing my document?");
				questionThreeTitle.setFont(titleFont);
				questionThreeTitle.setPreferredSize(new java.awt.Dimension(290, 20));
				IPConst.gridx = 0;
				IPConst.gridy = 4;
				IPConst.gridheight = 1;
				IPConst.gridwidth = 3;
				instructionsPanel.add(questionThreeTitle, IPConst);
			}
			{
				JLabel questionThreeAnswer = new JLabel();
				String s = "<p>After you've processed the document, the first sentence should be highlighted and will appear in the <b>\"Sentence\"</b> box."
						+ " Edit each sentence one by one, saving your changes as you go. Use the arrow buttons for navigation.</p>"
						+ "<br><p>Once you are satisfied with your changes, process the document to see how the anonymity has been affected.</p>";
				questionThreeAnswer.setText(html1+"170"+html2+s);
				questionThreeAnswer.setFont(answerFont);
				questionThreeAnswer.setVerticalAlignment(SwingConstants.TOP);
				
				questionThreeAnswer.setPreferredSize(new java.awt.Dimension(270, 120));
				IPConst.gridx = 0;
				IPConst.gridy = 5;
				IPConst.gridheight = 1;
				IPConst.gridwidth = 2;
				instructionsPanel.add(questionThreeAnswer, IPConst);
			}
			return editorHelpInfoPanel;
		} // =========== End Information Tab ==================
	}
	
	private JPanel createSugTab()
	{
		editorHelpSugPanel = new JPanel();
		editorHelpSugPanel.setPreferredSize(new java.awt.Dimension(320, 610));
		BorderLayout sugLayout = new BorderLayout(); 
		editorHelpSugPanel.setLayout(sugLayout);
		editorHelpTabPane.addTab("Suggestions", editorHelpSugPanel);
		{//================ Suggestions Tab =====================
			//--------- Elements Panel ------------------
			elementsPanel = new JPanel();
			elementsPanel.setPreferredSize(new java.awt.Dimension(310, 300));
			BorderLayout eleLayout = new BorderLayout(); 
			elementsPanel.setLayout(eleLayout);
			editorHelpSugPanel.add(elementsPanel, BorderLayout.SOUTH); // center of suggestions tab
			{
				//--------- Elements to Add Panel ------------------
				elementsToAddPanel = new JPanel();
				elementsToAddPanel.setPreferredSize(new java.awt.Dimension(150, 300));
				BorderLayout eleALayout = new BorderLayout(); 
				elementsToAddPanel.setLayout(eleALayout);
				elementsPanel.add(elementsToAddPanel, BorderLayout.WEST); // west of the center
				{
					//--------- Elements to Add Label ------------------
					elementsToAddLabel = new JLabel("Elements To Add:");
					elementsToAddLabel.setHorizontalAlignment(SwingConstants.CENTER);
					elementsToAddLabel.setPreferredSize(new java.awt.Dimension(150, 20));
					elementsToAddPanel.add(elementsToAddLabel, BorderLayout.NORTH); // north of the west
					
					//--------- Elements to Add Text Pane ------------------
					elementsToAddScrollPane = new JScrollPane();
					elementsToAddScrollPane.setPreferredSize(new java.awt.Dimension(150, 280));
					elementsToAddPane = new JTextPane();
					elementsToAddScrollPane.setViewportView(elementsToAddPane);
					elementsToAddPane.setText("This is where the words to Add to the curent sentence will go.");
					elementsToAddPane.setPreferredSize(new java.awt.Dimension(150, 280));
					elementsToAddPanel.add(elementsToAddScrollPane, BorderLayout.SOUTH); // south of the west
				}
				
				//--------- Elements to Remove Panel ------------------
				elementsToRemovePanel = new JPanel();
				elementsToRemovePanel.setPreferredSize(new java.awt.Dimension(150, 300));
				BorderLayout eleRLayout = new BorderLayout(); 
				elementsToRemovePanel.setLayout(eleRLayout);
				elementsPanel.add(elementsToRemovePanel, BorderLayout.EAST); // east of the center
				{
					//--------- Elements to Remove Label  ------------------
					elementsToRemoveLabel = new JLabel("Elements To Remove:");
					elementsToRemoveLabel.setHorizontalAlignment(SwingConstants.CENTER);
					elementsToRemoveLabel.setPreferredSize(new java.awt.Dimension(150, 20));
					elementsToRemovePanel.add(elementsToRemoveLabel, BorderLayout.NORTH); // north of the west
					
					//--------- Elements to Remove Text Pane ------------------
					elementsToRemoveScrollPane = new JScrollPane();
					elementsToRemoveScrollPane.setPreferredSize(new java.awt.Dimension(150, 280));
					elementsToRemovePane = new JTextPane();
					elementsToRemoveScrollPane.setViewportView(elementsToRemovePane);
					elementsToRemovePane.setText("This is where the words to Remove from the curent sentence will go.");
					elementsToRemovePane.setPreferredSize(new java.awt.Dimension(150, 280));
					elementsToRemovePanel.add(elementsToRemoveScrollPane, BorderLayout.SOUTH); // south of the west
				}
			}
		}//============ End Suggestions Tab =================
	return editorHelpSugPanel;
	}
	
	private JPanel createTransTab()
	{
		editorHelpTransPanel = new JPanel();
		editorHelpTransPanel.setPreferredSize(new java.awt.Dimension(300, 660));
		BorderLayout transLayout = new BorderLayout(); 
		editorHelpTransPanel.setLayout(transLayout);
		editorHelpTabPane.addTab("Translations", editorHelpTransPanel);
		{//================= Translations Tab ==============
			//--------- translationsPanel ------------------
			translationsPanel = new JPanel();
			translationsPanel.setPreferredSize(new java.awt.Dimension(290, 650));
			editorHelpTransPanel.add(translationsPanel, BorderLayout.NORTH);
			
			//--------- translationsLabel ------------------
			translationsLabel = new JLabel("Translations:");
			translationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
			translationsLabel.setPreferredSize(new java.awt.Dimension(290, 20));
			translationsPanel.add(translationsLabel, BorderLayout.NORTH);
			
			//--------- TranslationTable and scroll pane ------------------
			translationsTable = new JTable()
			{	
				//http://blog.marcnuri.com/blog/defaul/2007/03/15/JTable-Row-Alternate-Row-Background
			    public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			    {
			        Component returnComp = super.prepareRenderer(renderer, row, column);
			        Color alternateColor = optionsColor;
			        Color whiteColor = Color.WHITE;
			        if (!returnComp.getBackground().equals(getSelectionBackground())){
			            Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
			            returnComp .setBackground(bg);
			            bg = null;
			        }
			        return returnComp;
			    }
			};
			translationsScrollPane = new JScrollPane();
			translationsScrollPane.setPreferredSize(new java.awt.Dimension(280, 650));
            translationsScrollPane.setViewportView(translationsTable);
            
          //--------- TranslationsTable model ------------------
        	String[][] tableFiller = new String[GUITranslator.getUsedLangs().length][1];
        	for (int i = 0; i < GUITranslator.getUsedLangs().length; i++)
        	{
        		String name = GUITranslator.getName(GUITranslator.getUsedLangs()[i]);
        		String[] temp = {"", name};
        		tableFiller[i] = temp;
        	}
        	String[] tableHeaderFiller = {"Translation:", "Language:"};
            DefaultTableModel translationTableModel = new DefaultTableModel(tableFiller, tableHeaderFiller)
            {
            	@Override
                public boolean isCellEditable(int row, int column) {
                   //all cells false
                   return false;
                }
            };
            ListSelectionListener selListener = new ListSelectionListener()
            {
            	public void valueChanged(ListSelectionEvent e) 
            	{
            		int row = translationsTable.getSelectedRow();
    				String sentence = ConsolidationStation.toModifyTaggedDocs.get(0).getCurrentSentence().getTranslations().get(row).getUntagged();
    				inst.translationEditPane.setText(sentence);
                }
            };
            translationsTable.getSelectionModel().addListSelectionListener(selListener);
            //--------- TranslationsTable properties ------------------
            translationsTable.setModel(translationTableModel);
            translationsTable.setPreferredSize(null); // allows it to fit to the number and size of the entries
            translationsTable.setRowHeight(16);
            translationsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            translationsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            translationsPanel.add(translationsScrollPane, BorderLayout.SOUTH);
		}//================= End Translations Tab ==============
	return editorHelpTransPanel;
	}
	
	private JPanel createDocumentTab()
	{
		Logger.logln("Creating Documents Tab...");
		if(tabMade == false)
		{
			Font normalFont = new Font("Ariel", Font.PLAIN, 11);
			Font titleFont = new Font("Ariel", Font.BOLD, 11);
			
			editBoxPanel = new JPanel();
			MigLayout EBPLayout = new MigLayout(
					"fillx, wrap 3, ins 20 20 0 20",
					"[70]0[grow, fill]0[140]",
					"[70]0[70]10[fill]0[grow, fill]10[fill]0[]0[]");
            editBoxPanel.setLayout(EBPLayout);
			{
            	sentenceBoxLabel = new JLabel("Sentence:");
                sentenceBoxLabel.setFont(titleFont);
                editBoxPanel.add(sentenceBoxLabel, "width 70!, height 70!");
                
                sentencePane = new JScrollPane();
                sentenceEditPane = new JTextPane();
                sentenceEditPane.setText("Current Sentence.");
                sentenceEditPane.setFont(normalFont);
                sentenceEditPane.setEditable(true);
                sentencePane.setViewportView(sentenceEditPane);
                editBoxPanel.add(sentencePane, "growx, height 70!");
                
                sentenceOptionsPanel = new JPanel();
            	sentenceOptionsPanel.setBackground(optionsColor);
            	sentenceOptionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            	MigLayout sentOptLayout = new MigLayout(
            			"fill, wrap 1, gap 0 0, ins 0 n 0 n",
            			"fill",
            			"10:20:20");
            	sentenceOptionsPanel.setLayout(sentOptLayout);
            	editBoxPanel.add(sentenceOptionsPanel, "width 140!, height 70!, gapleft 0");
            	{
                 	JLabel sentOptionsLabel = new JLabel("Sentence Options:");
                 	sentOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                 	sentOptionsLabel.setFont(titleFont);
                 	sentenceOptionsPanel.add(sentOptionsLabel);
                   
                 	restoreSentenceButton = new JButton("Restore");
 					restoreSentenceButton.setToolTipText("Restores the sentence in the \"Current Sentence Box\"" +
 														" back to what is highlighted in the document below, reverting any changes.");
 					sentenceOptionsPanel.add(restoreSentenceButton);
 					
                 	SaveChangesButton = new JButton("Save Changes");
                 	SaveChangesButton.setToolTipText("Saves what is in the \"Current Sentence Box\" to the document below.");
                 	sentenceOptionsPanel.add(SaveChangesButton);
                }
                
                translationsBoxLabel = new JLabel("Translation:");
                translationsBoxLabel.setFont(titleFont);
                editBoxPanel.add(translationsBoxLabel, "width 70!, height 70!");
                
                translationPane = new JScrollPane();
                translationEditPane = new JTextPane();
                translationEditPane.setText("Current Translation.");
                translationEditPane.setFont(normalFont);
                translationEditPane.setEditable(true);
                translationPane.setViewportView(translationEditPane);
                editBoxPanel.add(translationPane, "height 70!");
            	
            	translationOptionsPanel = new JPanel();
            	translationOptionsPanel.setBackground(optionsColor);
            	translationOptionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            	MigLayout transOptLayout = new MigLayout(
            			"fill, wrap 1, gap 0 0, ins 0 n 0 n",
            			"fill",
            			"10:20:20");
            	translationOptionsPanel.setLayout(transOptLayout);
            	editBoxPanel.add(translationOptionsPanel, "width 140!, height 70!, gapleft 0");
            	{
                    JLabel transOptionsLabel = new JLabel("Translation Options:");
                    transOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    transOptionsLabel.setFont(new Font("Ariel", Font.BOLD, 11));
                    translationOptionsPanel.add(transOptionsLabel);
                    
                	copyToSentenceButton = new JButton("Copy To Sentence");
                	copyToSentenceButton.setToolTipText("Copies the translation in the \"Translation Box\"" +
														" to the \"Current Sentence Box\". Press the \"Restore\" button to undo this.");
                    translationOptionsPanel.add(copyToSentenceButton);
                    
                    JLabel filler = new JLabel();
                    translationOptionsPanel.add(filler);
                }
            	
                editBoxLabel = new JLabel("Document:");
                editBoxLabel.setFont(titleFont);
                editBoxLabel.setHorizontalAlignment(SwingConstants.CENTER);
                editBoxPanel.add(editBoxLabel, "span, growx");
                
                editBox = new JScrollPane();
                editorBox = new JTextPane();
                editorBox.setText("This is where the latest version of your document will be.");
                editorBox.setFont(normalFont);
                editorBox.setEnabled(true);
                editorBox.setEditable(false);
                editBox.setViewportView(editorBox);
                editBoxPanel.add(editBox, "span 2, grow, shrink 90");
                
                documentOptionsPanel = new JPanel();
                documentOptionsPanel.setBackground(optionsColor);
                documentOptionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                MigLayout DOPLayout = new MigLayout(
            			"fill, wrap 1",
            			"fill",
            			"[20][20][20][20][20][][20]");
            	documentOptionsPanel.setLayout(DOPLayout);
            	editBoxPanel.add(documentOptionsPanel, "width 140!, growy, right, shrink 90");
        		{
                    JLabel docOptionsLabel = new JLabel("Document Options:");
                    docOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    docOptionsLabel.setFont(titleFont);
                    documentOptionsPanel.add(docOptionsLabel);
                    
                	transButton = new JButton("Translate");
                	transButton.setToolTipText("Translates the currently highlighted sentence.");
                	documentOptionsPanel.add(transButton);
                	
                	appendSentenceButton = new JButton("Append Next");
                	appendSentenceButton.setToolTipText("Appends the next sentence onto the current sentence.");
                	documentOptionsPanel.add(appendSentenceButton);
                	
                	dictButton = new JButton("Synonym Dictionary");
                	dictButton.setToolTipText("Phrase and Synonym Dictionary.");
                	documentOptionsPanel.add(dictButton);
                	
                	saveButton = new JButton("Save To File");
                	saveButton.setToolTipText("Saves what is in the document view to it's source file.");
                	documentOptionsPanel.add(saveButton);
                	
                	processButton = new JButton("Process");
                	processButton.setToolTipText("Processes the document.");
                	documentOptionsPanel.add(processButton, "pushy, bottom, h 40!");
                    
        			prevSentenceButton = new JButton("<--");
        			prevSentenceButton.setHorizontalTextPosition(SwingConstants.CENTER);
        			documentOptionsPanel.add(prevSentenceButton, "split 2");
                    
                	nextSentenceButton = new JButton("-->");
                	nextSentenceButton.setHorizontalTextPosition(SwingConstants.CENTER);
                    documentOptionsPanel.add(nextSentenceButton);
        		}
    		
        		resultsTableLabel = new JLabel("Classification Results:");
            	resultsTableLabel.setFont(titleFont);
            	resultsTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	editBoxPanel.add(resultsTableLabel, "span, height 20!, growx");
            	
            	resultsTablePane = new JScrollPane();
                TableModel resultsTableModel = 
				new DefaultTableModel(
                                      new String[][] { { "", "" }, { "", "" } },
                                      new String[] { "", "" });
                resultsTable = new JTable();
                resultsTablePane.setViewportView(resultsTable);
                resultsTable.setModel(resultsTableModel);
                editBoxPanel.add(resultsTablePane, "span, height 60!, growx");
                
            	classificationLabel = new JLabel("");
            	classificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	editBoxPanel.add(classificationLabel, "span, height 20!");
			}
            tabMade = true;
		}
		return editBoxPanel;
	}
	
	private JScrollPane createClustersTab()
	{
		clusterScrollPane = new JScrollPane();
		clusterScrollPane.setOpaque(true);
		clusterScrollPane.setSize(900, 600);
		{
			holderPanel = new JPanel();
			BoxLayout holderPanelLayout = new BoxLayout(holderPanel, javax.swing.BoxLayout.Y_AXIS);
			//holderPanel.setPreferredSize(new java.awt.Dimension(775,528));
			holderPanel.setAutoscrolls(true);
			holderPanel.setOpaque(true);
			clusterScrollPane.setViewportView(holderPanel);
			holderPanel.setLayout(holderPanelLayout);
			{
				topPanel = new JPanel();
				{
					reClusterAllButton = new JButton();
					topPanel.add(reClusterAllButton);
					reClusterAllButton.setText("Re-Cluster");
					reClusterAllButton.setRolloverEnabled(true);
				}
				{
					refreshButton = new JButton();
					topPanel.add(refreshButton);
					refreshButton.setText("show me where my black dot is right now");
					refreshButton.setEnabled(true);
					refreshButton.setVisible(true);
				}
				secondPanel = new JPanel();
				holderPanel.add(secondPanel);
				holderPanel.add(topPanel);
				/*
				{
					ComboBoxModel clusterConfigurationBoxModel = 
							new DefaultComboBoxModel(
									new String[] { " ", " " });
					clusterConfigurationBox = new JComboBox();
					secondPanel.add(clusterConfigurationBox);
					clusterConfigurationBox.setModel(clusterConfigurationBoxModel);
					
					
				}
				*/
				{
					selectClusterConfiguration = new JButton();
					secondPanel.add(selectClusterConfiguration);
					selectClusterConfiguration.setText("Tag Document");
				}
				
			}
			{
				LegendPanel legend = new LegendPanel();
				legend.setPreferredSize(new Dimension(800,50));
				holderPanel.add(legend);
				
				
			}
		}
		return clusterScrollPane;
	}
	
	/**
	 * Class for resizing table columns to fit the data/header.
	 * Call ColumnsAutoSizer.sizeColumnsToFit(table); in tabledChanged when adding a TableModelListener.
	 * @author Jeff
	 *
	 */
	
	//http://bosmeeuw.wordpress.com/2011/08/07/java-swing-automatically-resize-table-columns-to-their-contents/
	public static class ColumnsAutoSizer 
	{
	    public static void sizeColumnsToFit(JTable table) 
	    {
	        sizeColumnsToFit(table, 5);
	    }
	    
	    public static void sizeColumnsToFit(JTable table, int columnMargin) 
	    {
	        JTableHeader tableHeader = table.getTableHeader();
	        if(tableHeader == null) 
	        {
	            // can't auto size a table without a header
	            return;
	        }
	        FontMetrics headerFontMetrics = tableHeader.getFontMetrics(tableHeader.getFont());
	        int[] minWidths = new int[table.getColumnCount()];
	        int[] maxWidths = new int[table.getColumnCount()];
	        for(int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) 
	        {
	            int headerWidth = headerFontMetrics.stringWidth(table.getColumnName(columnIndex));
	            minWidths[columnIndex] = headerWidth + columnMargin;
	            int maxWidth = getMaximalRequiredColumnWidth(table, columnIndex, headerWidth);
	            maxWidths[columnIndex] = Math.max(maxWidth, minWidths[columnIndex]) + columnMargin;
	        }
	        adjustMaximumWidths(table, minWidths, maxWidths);
	        for(int i = 0; i < minWidths.length; i++) 
	        {
	            if(minWidths[i] > 0) 
	            {
	                table.getColumnModel().getColumn(i).setMinWidth(minWidths[i]);
	            }
	            if(maxWidths[i] > 0) 
	            {
	                table.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);
	                table.getColumnModel().getColumn(i).setWidth(maxWidths[i]);
	            }
	        }
	    }
	    private static void adjustMaximumWidths(JTable table, int[] minWidths, int[] maxWidths) 
	    {
	        if(table.getWidth() > 0) {
	            // to prevent infinite loops in exceptional situations
	            int breaker = 0;
	            // keep stealing one pixel of the maximum width of the highest column until we can fit in the width of the table
	            while(sum(maxWidths) > table.getWidth() && breaker < 10000) 
	            {
	                int highestWidthIndex = findLargestIndex(maxWidths);
	                maxWidths[highestWidthIndex] -= 1;
	                maxWidths[highestWidthIndex] = Math.max(maxWidths[highestWidthIndex], minWidths[highestWidthIndex]);
	                breaker++;
	            }
	        }
	    }
	    private static int getMaximalRequiredColumnWidth(JTable table, int columnIndex, int headerWidth) 
	    {
	        int maxWidth = headerWidth;
	        TableColumn column = table.getColumnModel().getColumn(columnIndex);
	        TableCellRenderer cellRenderer = column.getCellRenderer();
	        if(cellRenderer == null) 
	        {
	            cellRenderer = new DefaultTableCellRenderer();
	        }
	        for(int row = 0; row < table.getModel().getRowCount(); row++) 
	        {
	            Component rendererComponent = cellRenderer.getTableCellRendererComponent(table,
	                table.getModel().getValueAt(row, columnIndex),
	                false,
	                false,
	                row,
	                columnIndex);
	            double valueWidth = rendererComponent.getPreferredSize().getWidth();
	            maxWidth = (int) Math.max(maxWidth, valueWidth);
	        }
	        return maxWidth;
	    }
	    private static int findLargestIndex(int[] widths) 
	    {
	        int largestIndex = 0;
	        int largestValue = 0;
	        for(int i = 0; i < widths.length; i++) 
	        {
	            if(widths[i] > largestValue) 
	            {
	                largestIndex = i;
	                largestValue = widths[i];
	            }
	        }
	        return largestIndex;
	    }

	    private static int sum(int[] widths) 
	    {
	        int sum = 0;
	        for(int width : widths) 
	        {
	            sum += width;
	        }
	        return sum;
	    }
	}

}
