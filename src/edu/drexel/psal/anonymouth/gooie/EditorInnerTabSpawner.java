package edu.drexel.psal.anonymouth.gooie;

import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.ProblemSet;
import edu.drexel.psal.jstylo.generics.WekaInstancesBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import weka.classifiers.Classifier;
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
 * Creates new inner panel for editor tab, broken up into three parts: get information from last clicked pane, create pane/set values, set main objects
 * @author Andrew W.E. McDonald
 *
 */
public class EditorInnerTabSpawner {
	
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
			protected JTextPane sentenceEditPane;
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
			//private String oldResultsTableLabelString = " ";
			private String oldClassificationLabel = " ";
			private TableCellRenderer tcr = new DefaultTableCellRenderer();
	
			public EditorInnerTabSpawner(){
				
			}
			
			public EditorInnerTabSpawner(EditorInnerTabSpawner eits){
				oldEditorBoxDoc = eits.editorBox.getText().substring(0);
				oldResultsTableModel = eits.resultsTable.getModel();
				tcr = eits.resultsTable.getDefaultRenderer(Object.class);
				EditorTabDriver.numEdits++;
				Logger.logln("EditorInnerTabSpawner created.");
			}
			
			private void color()
			{
				Color background = new Color(200, 200, 200);
				editBoxPanel.setBackground(background);
				sentenceEditingPanel.setBackground(background);
				sentenceBoxLabel.setBackground(background);
				translationsBoxLabel.setBackground(background);
				documentPanel.setBackground(background);
                editBoxLabel.setBackground(background);
                resultsPanel.setBackground(background);
            	resultsTableLabel.setBackground(background);
            	classificationLabel.setBackground(background);
			}

			public EditorInnerTabSpawner spawnTab(){
				Logger.logln("EditorInnerTabSpawner spawning tab");
				if(tabMade == false)
				{
					
					editBoxPanel = new JPanel();
					
					
					GridBagLayout EBPLayout = new GridBagLayout();
                    editBoxPanel.setLayout(EBPLayout);
                    GridBagConstraints EBPConst = new GridBagConstraints();
                    
                    int prefX = 850;
                    int prefY = 600;
                    editBoxPanel.setPreferredSize(new java.awt.Dimension(prefX, prefY));
                    {
                        sentenceEditingPanel = new JPanel();
                        GridBagLayout SEPLayout = new GridBagLayout();
                        sentenceEditingPanel.setLayout(SEPLayout);
                        GridBagConstraints SEPConst = new GridBagConstraints();
                        EBPConst.gridx = 0;
                        EBPConst.gridheight = 1;
                        EBPConst.gridy = 0;
                        EBPConst.gridwidth = 1;
                        editBoxPanel.add(sentenceEditingPanel, EBPConst);
                        sentenceEditingPanel.setPreferredSize(new java.awt.Dimension(prefX, 140));
                        {
                            sentenceBoxLabel = new JLabel();
                            sentenceBoxLabel.setText("Sentence:");
                    		sentenceBoxLabel.setPreferredSize(new java.awt.Dimension(70, 60));
                    		SEPConst.gridx = 0;
                    		SEPConst.gridheight = 1;
                    		SEPConst.gridy = 0;
                    		SEPConst.gridwidth = 1;
                    		sentenceEditingPanel.add(sentenceBoxLabel, SEPConst);
                        }
                        {
                            translationsBoxLabel = new JLabel();
                            translationsBoxLabel.setText("Translation:");
                            translationsBoxLabel.setPreferredSize(new java.awt.Dimension(70, 60));
                            SEPConst.gridx = 0;
                            SEPConst.gridheight = 1;
                            SEPConst.gridy = 1;
                            SEPConst.gridwidth = 1;
                            sentenceEditingPanel.add(translationsBoxLabel, SEPConst);
                        }
                        Font editBoxFont = new Font("Verdana", Font.PLAIN, 10);
                        {
                            sentencePane = new JScrollPane();
                            sentencePane.setPreferredSize(new java.awt.Dimension(640, 60));
                            sentenceEditPane = new JTextPane();
                            sentencePane.setViewportView(sentenceEditPane);
                            sentenceEditPane.setText("Current Sentence.");
                            sentenceEditPane.setFont(editBoxFont);
                            sentenceEditPane.setEditable(true);
                            sentenceEditPane.setPreferredSize(new java.awt.Dimension(640, 60));
                            SEPConst.gridx = 1;
                            SEPConst.gridheight = 1;
                    		SEPConst.gridy = 0;
                            SEPConst.gridwidth = 1;
                            sentenceEditingPanel.add(sentencePane, SEPConst);
                        }
                        {
                            translationPane = new JScrollPane();
                            translationPane.setPreferredSize(new java.awt.Dimension(640, 60));
                            translationEditPane = new JTextPane();
                            translationPane.setViewportView(translationEditPane);
                            translationEditPane.setText("Current Translation.");
                            translationEditPane.setFont(editBoxFont);
                            translationEditPane.setEditable(true);
                            translationEditPane.setPreferredSize(new java.awt.Dimension(640, 60));
                            SEPConst.gridx = 1;
                            SEPConst.gridheight = 1;
                    		SEPConst.gridy = 1;
                            SEPConst.gridwidth = 1;
                            sentenceEditingPanel.add(translationPane, SEPConst);
                        }
                        {
                        	sentenceOptionsPanel = new JPanel();
                        	sentenceOptionsPanel.setPreferredSize(new java.awt.Dimension(140, 60));
                        	//sentenceButtonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.DARK_GRAY));
                        	sentenceOptionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                        	GridBagLayout layout = new GridBagLayout();
                        	sentenceOptionsPanel.setLayout(layout);
                        	SEPConst.gridx = 2;
                        	SEPConst.gridheight = 1;
                        	SEPConst.gridy = 0;
                            SEPConst.gridwidth = 1;
                            sentenceEditingPanel.add(sentenceOptionsPanel, SEPConst);
                        	
                        	 {
                             	JLabel sentOptionsLabel = new JLabel("Sentence Options:");
                             	sentOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                             	sentOptionsLabel.setFont(new Font("Ariel", Font.BOLD, 11));
                             	sentOptionsLabel.setPreferredSize(new java.awt.Dimension(130, 18));
                             	SEPConst.gridx = 0;
                             	SEPConst.gridheight = 1;
                             	SEPConst.gridy = 0;
 	                            SEPConst.gridwidth = 1;
 	                           sentenceOptionsPanel.add(sentOptionsLabel, SEPConst);
                             }
                             {
                             	restoreSentenceButton = new JButton();
             					restoreSentenceButton.setText("Restore");
             					restoreSentenceButton.setToolTipText("Restores the sentence in the \"Current Sentence Box\"" +
             														" back to what is highlighted in the document below, reverting any changes.");
             					restoreSentenceButton.setPreferredSize(new java.awt.Dimension(130, 18));
                             	SEPConst.gridx = 0;
                             	SEPConst.gridheight = 1;
                             	SEPConst.gridy = 1;
 	                            SEPConst.gridwidth = 1;
 	                           sentenceOptionsPanel.add(restoreSentenceButton, SEPConst);
                             }
                             {
                             	SaveChangesButton = new JButton();
                             	SaveChangesButton.setText("Save Changes");
                             	SaveChangesButton.setToolTipText("Saves what is in the \"Current Sentence Box\" to the document below.");
                             	SaveChangesButton.setPreferredSize(new java.awt.Dimension(130, 18));
                             	SEPConst.gridx = 0;
                             	SEPConst.gridheight = 1;
 	                    		SEPConst.gridy = 2;
 	                            SEPConst.gridwidth = 1;
 	                           sentenceOptionsPanel.add(SaveChangesButton, SEPConst);
                             }
                        }
                        {
                        	translationOptionsPanel = new JPanel();
                        	translationOptionsPanel.setPreferredSize(new java.awt.Dimension(140, 60));
                        	//translationButtonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.DARK_GRAY));
                        	translationOptionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                        	GridBagLayout layout = new GridBagLayout();
                        	translationOptionsPanel.setLayout(layout);
                        	SEPConst.gridx = 2;
                        	SEPConst.gridheight = 1;
                        	SEPConst.gridy = 1;
                            SEPConst.gridwidth = 1;
                            sentenceEditingPanel.add(translationOptionsPanel, SEPConst);
                        	
                        	{
	                            JLabel transOptionsLabel = new JLabel("Translation Options:");
	                            transOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	                            transOptionsLabel.setFont(new Font("Ariel", Font.BOLD, 11));
	                            transOptionsLabel.setPreferredSize(new java.awt.Dimension(130, 18));
	                        	SEPConst.gridx = 0;
	                        	SEPConst.gridheight = 1;
	                        	SEPConst.gridy = 0;
	                            SEPConst.gridwidth = 1;
	                            translationOptionsPanel.add(transOptionsLabel, SEPConst);
                            }
                            {
                            	copyToSentenceButton = new JButton();
                            	copyToSentenceButton.setText("Copy To Sentence");
                            	copyToSentenceButton.setToolTipText("Copies the translation in the \"Translation Box\"" +
            														" to the \"Current Sentence Box\". Press the \"Restore\" button to undo this.");
                            	copyToSentenceButton.setPreferredSize(new java.awt.Dimension(130, 18));
                            	SEPConst.gridx = 0;
                            	SEPConst.gridheight = 1;
                            	SEPConst.gridy = 1;
	                            SEPConst.gridwidth = 1;
	                            translationOptionsPanel.add(copyToSentenceButton, SEPConst);
                            }
                            {
                            	JLabel filler = new JLabel();
                            	filler.setPreferredSize(new java.awt.Dimension(130, 18));
                            	SEPConst.gridx = 0;
                            	SEPConst.gridheight = 1;
                            	SEPConst.gridy = 2;
	                            SEPConst.gridwidth = 1;
	                            translationOptionsPanel.add(filler, SEPConst);
                            }
                        }
                    }
                    { //------- Document ---------------
                        documentPanel = new JPanel();
                        
                        GridBagLayout DPLayout = new GridBagLayout();
                        documentPanel.setLayout(DPLayout);
                        GridBagConstraints DPConst = new GridBagConstraints();
                        
                        EBPConst.gridx = 0;
                        EBPConst.gridheight = 1;
                        EBPConst.gridy = 1;
                        EBPConst.gridwidth = 1;
                        
                        editBoxPanel.add(documentPanel, EBPConst);
                        documentPanel.setPreferredSize(new java.awt.Dimension(prefX, 400));
                        {//------- Document Label ---------------
                            editBoxLabel = new JLabel();
                            editBoxLabel.setText("Document:");
                            editBoxLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            editBoxLabel.setPreferredSize(new java.awt.Dimension(prefX, 20));
                            DPConst.gridx = 0;
                            DPConst.gridheight = 1;
                            DPConst.gridy = 0;
                            DPConst.gridwidth = 2;
                            documentPanel.add(editBoxLabel, DPConst);
                        }
                        {//------- Document Text Area ---------------
                            editBox = new JScrollPane();
                            editBox.setPreferredSize(new java.awt.Dimension(710, 370));
                            DPConst.gridx = 0;
                            DPConst.gridheight = 1;
                            DPConst.gridy = 1;
                            DPConst.gridwidth = 1;
                            documentPanel.add(editBox, DPConst);
                            {
                                editorBox = new JTextPane();
                                editBox.setViewportView(editorBox);
                                editorBox.setText("This is where the latest version of your document will be.");
                                editorBox.setEnabled(true);
                                editorBox.setEditable(false);
                            }
                        }
                        {//------- Document Options ---------------
	                        documentOptionsPanel = new JPanel();
	                        documentOptionsPanel.setPreferredSize(new java.awt.Dimension(140, 370));
	                    	//translationButtonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.DARK_GRAY));
	                        documentOptionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	                    	GridBagLayout DOPLayout = new GridBagLayout();
	                    	documentOptionsPanel.setLayout(DOPLayout);
	                    	DPConst.gridx = 1;
	                    	DPConst.gridheight = 1;
	                    	DPConst.gridy = 1;
	                    	DPConst.gridwidth = 1;
	                    	int x = 130;
	                    	int y = 20;
	                    	documentPanel.add(documentOptionsPanel, DPConst);
                    		{
	                            JLabel docOptionsLabel = new JLabel("Document Options:");
	                            docOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	                            docOptionsLabel.setFont(new Font("Ariel", Font.BOLD, 11));
	                            docOptionsLabel.setPreferredSize(new java.awt.Dimension(x, y));
	                            DPConst.gridx = 0;
	                            DPConst.gridheight = 1;
	                            DPConst.gridy = 0;
	                            DPConst.gridwidth = 1;
	                            documentOptionsPanel.add(docOptionsLabel, DPConst);
                            }
                            {
                            	transButton = new JButton();
                            	transButton.setText("Translate");
                            	transButton.setToolTipText("Translates the currently highlighted sentence.");
                            	transButton.setPreferredSize(new java.awt.Dimension(x, y));
                            	DPConst.gridx = 0;
                            	DPConst.gridheight = 1;
                            	DPConst.gridy = 1;
                            	DPConst.gridwidth = 1;
                            	documentOptionsPanel.add(transButton, DPConst);
                            }
                            {
                            	appendSentenceButton = new JButton();
                            	appendSentenceButton.setText("Append Next");
                            	appendSentenceButton.setToolTipText("Appends the next sentence onto the current sentence.");
                            	appendSentenceButton.setPreferredSize(new java.awt.Dimension(x, y));
                            	DPConst.gridx = 0;
                            	DPConst.gridheight = 1;
                            	DPConst.gridy = 2;
                            	DPConst.gridwidth = 1;
                            	documentOptionsPanel.add(appendSentenceButton, DPConst);
                            }
                            {
                            	dictButton = new JButton();
                            	dictButton.setText("Synonym Dictionary");
                            	dictButton.setToolTipText("Phrase and Synonym Dictionary.");
                            	dictButton.setPreferredSize(new java.awt.Dimension(x, y));
                            	DPConst.gridx = 0;
                            	DPConst.gridheight = 1;
                            	DPConst.gridy = 3;
                            	DPConst.gridwidth = 1;
                            	documentOptionsPanel.add(dictButton, DPConst);
                            }
                            {
                            	saveButton = new JButton();
                            	saveButton.setText("Save To File");
                            	saveButton.setToolTipText("Saves what is in the document view to it's source file.");
                            	saveButton.setPreferredSize(new java.awt.Dimension(x, y));
                            	DPConst.gridx = 0;
                            	DPConst.gridheight = 1;
                            	DPConst.gridy = 4;
                            	DPConst.gridwidth = 1;
                            	documentOptionsPanel.add(saveButton, DPConst);
                            }
                            {
                            	JLabel filler = new JLabel();
                            	filler.setPreferredSize(new java.awt.Dimension(x, 200));
                            	DPConst.gridx = 0;
                            	DPConst.gridheight = 1;
                            	DPConst.gridy = 5;
                            	DPConst.gridwidth = 1;
                            	documentOptionsPanel.add(filler, DPConst);
                            }
                            {
                            	processButton = new JButton();
                            	processButton.setText("Process");
                            	//processButton.setBackground(new Color(163, 255, 160)); //not working
                            	processButton.setToolTipText("Processes the document.");
                            	processButton.setPreferredSize(new java.awt.Dimension(x, y*2));
                            	DPConst.gridx = 0;
                            	DPConst.gridheight = 1;
                            	DPConst.gridy = 6;
                            	DPConst.gridwidth = 1;
                            	documentOptionsPanel.add(processButton, DPConst);
                            }
                            {
	                            JPanel navPanel = new JPanel();
	                            GridBagLayout NPLayout = new GridBagLayout();
	                            navPanel.setLayout(NPLayout);
	                            navPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
	                            navPanel.setPreferredSize(new java.awt.Dimension(x, y));
	                            DPConst.gridx = 0;
	                            DPConst.gridheight = 1;
	                            DPConst.gridy = 7;
	                            DPConst.gridwidth = 1;
	                            documentOptionsPanel.add(navPanel, DPConst);
	                            {
	                    			prevSentenceButton = new JButton();
	                    			prevSentenceButton.setPreferredSize(new java.awt.Dimension(x/2, y-1));
	                    			prevSentenceButton.setHorizontalTextPosition(SwingConstants.CENTER);
	                    			prevSentenceButton.setText("<--");
		                            DPConst.gridx = 0;
		                            DPConst.gridheight = 1;
		                            DPConst.gridy = 0;
		                            DPConst.gridwidth = 1;
		                            navPanel.add(prevSentenceButton, DPConst);
	                            }
	                            {
	                            	nextSentenceButton = new JButton();
	                            	nextSentenceButton.setPreferredSize(new java.awt.Dimension(x/2, y-1));
	                            	nextSentenceButton.setHorizontalTextPosition(SwingConstants.CENTER);
	                            	nextSentenceButton.setText("-->");
		                            DPConst.gridx = 1;
		                            DPConst.gridheight = 1;
		                            DPConst.gridy = 0;
		                            DPConst.gridwidth = 1;
		                            navPanel.add(nextSentenceButton, DPConst);
	                            }
                            }
                        }
                    } //------- END Document ---------------
                    {   
                        resultsPanel = new JPanel();
                        
                        GridBagLayout RPLayout = new GridBagLayout();
                        resultsPanel.setLayout(RPLayout);
                        GridBagConstraints RPConst = new GridBagConstraints();
                        
                        EBPConst.gridx = 0;
                        EBPConst.gridheight = 1;
                        EBPConst.gridy = 2;
                        EBPConst.gridwidth = 1;
                        
                        editBoxPanel.add(resultsPanel, EBPConst);
                        resultsPanel.setPreferredSize(new java.awt.Dimension(prefX, 120));
                        {
                        	resultsTableLabel = new JLabel("Below are the results of your document's classification");
                        	resultsTableLabel.setPreferredSize(new java.awt.Dimension(prefX, 20));
                        	resultsTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        	RPConst.gridx = 0;
                        	RPConst.gridheight = 1;
                        	RPConst.gridy = 0;
                        	RPConst.gridwidth = 1;
                        	resultsPanel.add(resultsTableLabel, RPConst);
                        }
                        {
                        	resultsTablePane = new JScrollPane();
                        	resultsTablePane.setPreferredSize(new java.awt.Dimension(prefX, 60));
                        	RPConst.gridx = 0;
                        	RPConst.gridheight = 1;
                        	RPConst.gridy = 1;
                        	RPConst.gridwidth = 1;
                        	resultsPanel.add(resultsTablePane, RPConst);
                        	{
                            	
                                TableModel resultsTableModel = 
								new DefaultTableModel(
                                                      new String[][] { { "One", "Two" }, { "Three", "Four" } },
                                                      new String[] { "Column 1", "Column 2" });
                                if (oldResultsTableModel!=null) {
                                	resultsTableModel=oldResultsTableModel; 
                                }
                                resultsTable = new JTable();
                                resultsTablePane.setViewportView(resultsTable);
                                resultsTable.setModel(resultsTableModel);
                                resultsTable.setPreferredSize(new java.awt.Dimension(prefX-3, 32));
                               
                            }
                        }
                        {
                        	classificationLabel = new JLabel("Below are the results of your document's classification");
                        	classificationLabel.setPreferredSize(new java.awt.Dimension(prefX, 20));
                        	resultsTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        	RPConst.gridx = 0;
                        	RPConst.gridheight = 1;
                        	RPConst.gridy = 2;
                        	RPConst.gridwidth = 1;
                        	resultsPanel.add(classificationLabel, RPConst);
                        }
                    }	
                    tabMade = true;
				}
				return this;
			}
			
			public JScrollPane getSentencePane() {
				if(sentencePane == null) {
					sentencePane = new JScrollPane();
					sentencePane.setViewportView(getSentenceEditPane());
				}
				return sentencePane;
			}
			
			public JTextPane getSentenceEditPane() {
				if(sentenceEditPane == null) {
					sentenceEditPane = new JTextPane();
					sentenceEditPane.setText("Edit here");
					sentenceEditPane.setVisible(true);
				}
				return sentenceEditPane;
			}
			
			public JButton getRestoreSentenceButton() {
				if(restoreSentenceButton == null) {
					restoreSentenceButton = new JButton();
					restoreSentenceButton.setText("Restore Sentence");
					restoreSentenceButton.setEnabled(false);
				}
				return restoreSentenceButton;
			}
			
			public JPanel getSpacer1() {
				if(spacer1 == null) {
					spacer1 = new JPanel();
					spacer1.setPreferredSize(new java.awt.Dimension(76, 10));
				}
				return spacer1;
			}
			
			private JButton getShuffleButton() {
				if(shuffleButton == null) {
					shuffleButton = new JButton();
					shuffleButton.setText("Shuffle!");
					shuffleButton.setPreferredSize(new java.awt.Dimension(100, 23));
					shuffleButton.setEnabled(false);
				}
				return shuffleButton;
			}
			
			private JButton getRemoveWordsButton() {
				if(removeWordsButton == null) {
					removeWordsButton = new JButton();
					removeWordsButton.setText("Remove Red Words");
					shuffleButton.setEnabled(false);
				}
				return removeWordsButton;
			}

}

