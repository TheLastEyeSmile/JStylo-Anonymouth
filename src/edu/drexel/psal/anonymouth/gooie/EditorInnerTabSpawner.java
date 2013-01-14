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
			protected JScrollPane sentencePane;
			protected JPanel sentenceButtonPanel;
			protected JPanel translationButtonPanel;
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

			public EditorInnerTabSpawner spawnTab(){
				Logger.logln("EditorInnerTabSpawner spawning tab");
				if(tabMade == false){
					editBoxPanel = new JPanel();
                    BorderLayout thisLayout = new BorderLayout();
                    editBoxPanel.setLayout(thisLayout);
                    editBoxPanel.setPreferredSize(new java.awt.Dimension(744, 569));
                    editBoxPanel.setMaximumSize(new java.awt.Dimension(1000, 569));
                    {
                        sentenceAndDocumentPanel = new JPanel();
                        BorderLayout sentenceAndDocumentPanelLayout = new BorderLayout();
                        sentenceAndDocumentPanel.setLayout(sentenceAndDocumentPanelLayout);
                        editBoxPanel.add(sentenceAndDocumentPanel, BorderLayout.NORTH);
                        sentenceAndDocumentPanel.setPreferredSize(new java.awt.Dimension(744, 435));
                        {
                            sentenceAndSentenceLabelPanel = new JPanel();
                            GridBagLayout sentenceAndSentenceLabelPanelLayout = new GridBagLayout();
                            sentenceAndSentenceLabelPanel.setLayout(sentenceAndSentenceLabelPanelLayout);
                            sentenceAndDocumentPanel.add(sentenceAndSentenceLabelPanel, BorderLayout.NORTH);
                            sentenceAndSentenceLabelPanel.setPreferredSize(new java.awt.Dimension(744, 140));
                            GridBagConstraints c = new GridBagConstraints();
                            int totalHeight = 6; // this is dependent on how many components need to fit on one column
                            {
	                            sentenceBoxLabel = new JLabel();
	                            sentenceBoxLabel.setText("Sentence:");
	                    		sentenceBoxLabel.setPreferredSize(new java.awt.Dimension(70, 60));
	                    		c.gridx = 0;
	                    		c.gridheight = totalHeight/2;
	                    		c.gridy = 0;
	                            c.gridwidth = 1;
	                            sentenceAndSentenceLabelPanel.add(sentenceBoxLabel, c);
                            }
                            {
	                            translationsBoxLabel = new JLabel();
	                            translationsBoxLabel.setText("Translation:");
	                            translationsBoxLabel.setPreferredSize(new java.awt.Dimension(70, 60));
	                    		c.gridx = 0;
	                    		c.gridheight = totalHeight/2;
	                    		c.gridy = 3;
	                            c.gridwidth = 1;
	                            sentenceAndSentenceLabelPanel.add(translationsBoxLabel, c);
                            }
                            Font editBoxFont = new Font("Verdana", Font.PLAIN, 10);
                            {
	                            sentencePane = new JScrollPane();
	                            sentencePane.setPreferredSize(new java.awt.Dimension(650, 60));
	                            sentenceEditPane = new JTextPane();
	                            sentencePane.setViewportView(sentenceEditPane);
	                            sentenceEditPane.setText("Current Sentence.");
	                            sentenceEditPane.setFont(editBoxFont);
	                            sentenceEditPane.setEditable(true);
	                            sentenceEditPane.setPreferredSize(new java.awt.Dimension(650, 60));
	                            c.gridx = 1;
	                            c.gridheight = totalHeight/2;
	                    		c.gridy = 0;
	                            c.gridwidth = 5;
	                            sentenceAndSentenceLabelPanel.add(sentencePane, c);
                            }
                            {
	                            translationPane = new JScrollPane();
	                            translationPane.setPreferredSize(new java.awt.Dimension(650, 60));
	                            translationEditPane = new JTextPane();
	                            translationPane.setViewportView(translationEditPane);
	                            translationEditPane.setText("Current Translation.");
	                            translationEditPane.setFont(editBoxFont);
	                            translationEditPane.setEditable(true);
	                            translationEditPane.setPreferredSize(new java.awt.Dimension(650, 60));
	                            c.gridx = 1;
	                            c.gridheight = totalHeight/2;
	                    		c.gridy = 3;
	                            c.gridwidth = 5;
	                            sentenceAndSentenceLabelPanel.add(translationPane, c);
                            }
                            {
                            	sentenceButtonPanel = new JPanel();
                            	sentenceButtonPanel.setPreferredSize(new java.awt.Dimension(140, 60));
                            	//sentenceButtonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.DARK_GRAY));
                            	sentenceButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                            	GridBagLayout layout = new GridBagLayout();
                            	sentenceButtonPanel.setLayout(layout);
                            	c.gridx = 6;
                            	c.gridheight = 3;
                            	c.gridy = 0;
	                            c.gridwidth = 1;
                            	sentenceAndSentenceLabelPanel.add(sentenceButtonPanel, c);
                            	
	                        	 {
	                             	JLabel sentOptionsLabel = new JLabel("Sentence Options:");
	                             	sentOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	                             	sentOptionsLabel.setFont(new Font("Ariel", Font.BOLD, 11));
	                             	sentOptionsLabel.setPreferredSize(new java.awt.Dimension(130, 18));
	                             	c.gridx = 0;
	                             	c.gridheight = 1;
	                             	c.gridy = 0;
	 	                            c.gridwidth = 1;
	 	                           sentenceButtonPanel.add(sentOptionsLabel, c);
	                             }
	                             {
	                             	restoreSentenceButton = new JButton();
	             					restoreSentenceButton.setText("Restore");
	             					restoreSentenceButton.setToolTipText("Restores the sentence in the \"Current Sentence Box\"" +
	             														" back to what is highlighted in the document below, reverting any changes.");
	             					restoreSentenceButton.setPreferredSize(new java.awt.Dimension(130, 18));
	                             	c.gridx = 0;
	                             	c.gridheight = 1;
	                             	c.gridy = 1;
	 	                            c.gridwidth = 1;
	 	                           sentenceButtonPanel.add(restoreSentenceButton, c);
	                             }
	                             {
	                             	SaveChangesButton = new JButton();
	                             	SaveChangesButton.setText("Save Changes");
	                             	SaveChangesButton.setToolTipText("Saves what is in the \"Current Sentence Box\" to the document below.");
	                             	SaveChangesButton.setPreferredSize(new java.awt.Dimension(130, 18));
	                             	c.gridx = 0;
	                             	c.gridheight = 1;
	 	                    		c.gridy = 2;
	 	                            c.gridwidth = 1;
	 	                           sentenceButtonPanel.add(SaveChangesButton, c);
	                             }
                            }
                            {
                            	translationButtonPanel = new JPanel();
                            	translationButtonPanel.setPreferredSize(new java.awt.Dimension(140, 60));
                            	//translationButtonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.DARK_GRAY));
                            	translationButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                            	GridBagLayout layout = new GridBagLayout();
                            	translationButtonPanel.setLayout(layout);
                            	c.gridx = 6;
                            	c.gridheight = 3;
                            	c.gridy = 3;
	                            c.gridwidth = 1;
                            	sentenceAndSentenceLabelPanel.add(translationButtonPanel, c);
                            	
                            	{
    	                            JLabel transOptionsLabel = new JLabel("Translation Options:");
    	                            transOptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	                            transOptionsLabel.setFont(new Font("Ariel", Font.BOLD, 11));
    	                            transOptionsLabel.setPreferredSize(new java.awt.Dimension(130, 18));
    	                        	c.gridx = 0;
    	                        	c.gridheight = 1;
    	                        	c.gridy = 0;
    	                            c.gridwidth = 1;
    	                            translationButtonPanel.add(transOptionsLabel, c);
                                }
                                {
                                	copyToSentenceButton = new JButton();
                                	copyToSentenceButton.setText("Copy To Sentence");
                                	copyToSentenceButton.setToolTipText("Copies the translation in the \"Translation Box\"" +
                														" to the \"Current Sentence Box\". Press the \"Restore\" button to undo this.");
                                	copyToSentenceButton.setPreferredSize(new java.awt.Dimension(130, 18));
                                	c.gridx = 0;
                                	c.gridheight = 1;
                                	c.gridy = 1;
    	                            c.gridwidth = 1;
    	                            translationButtonPanel.add(copyToSentenceButton, c);
                                }
                                {
                                	JLabel filler = new JLabel();
                                	filler.setPreferredSize(new java.awt.Dimension(130, 18));
                                	c.gridx = 0;
                                	c.gridheight = 1;
                                	c.gridy = 2;
    	                            c.gridwidth = 1;
    	                            translationButtonPanel.add(filler, c);
                                }
                            }
                            /*{
                            	c.gridx = 4;
	                    		c.gridy = 2;
	                            c.gridwidth = 1;
                            	sentenceAndSentenceLabelPanel.add(getRemoveWordsButton());
                            }*/

                            /*
                            button = new JButton("5");
                            c.fill = GridBagConstraints.HORIZONTAL;
                            c.ipady = 0;       //reset to default
                            c.weighty = 1.0;   //request any extra vertical space
                            c.anchor = GridBagConstraints.PAGE_END; //bottom of space
                            c.insets = new Insets(10,0,0,0);  //top padding
                            c.gridx = 0;       //aligned with button 2
                            c.gridwidth = 1;   //2 columns wide
                            c.gridy = 0;       //third row
                            pane.add(button, c);
                            {
                                sentencePanel = new JPanel();
                                BorderLayout sentencePanelLayout = new BorderLayout();
                                sentencePanel.setLayout(sentencePanelLayout);
                                sentenceAndSentenceLabelPanel.add(sentencePanel, BorderLayout.SOUTH);
                                sentencePanel.setPreferredSize(new java.awt.Dimension(744, 70));
                                {
                                    sentencePane = new JScrollPane();
                                    sentencePanel.add(sentencePane, BorderLayout.WEST);
                                    sentencePane.setPreferredSize(new java.awt.Dimension(744, 30));
                                    {
                                    	//StyleContext sc = new StyleContext();
                                        //final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
                                        sentenceEditPane = new JTextPane();
                                        sentencePane.setViewportView(sentenceEditPane);
                                        //makes font size 24
                                        //Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
                                        //Style editBoxStyle = sc.addStyle("EditBoxStyle", defaultStyle);
                                        //StyleConstants.setFontSize(editBoxStyle, 14);
                                        sentenceEditPane.setText("Current Sentence.");
                                        Font font = new Font("Verdana", Font.PLAIN, 10);
                                        sentenceEditPane.setFont(font);
                                        sentenceEditPane.setEditable(true);
                                        sentenceEditPane.setPreferredSize(new java.awt.Dimension(740, 30));
                                        //doc.setLogicalStyle(0, editBoxStyle);
                                    }
                                    translationPane = new JScrollPane();
                                    sentencePanel.add(translationPane, BorderLayout.SOUTH);
                                    translationPane.setPreferredSize(new java.awt.Dimension(744, 30));
                                    {
                                        translationEditPane = new JTextPane();
                                        translationPane.setViewportView(translationEditPane);
                                        translationEditPane.setText("Current Translation.");
                                        Font font = new Font("Verdana", Font.PLAIN, 10);
                                        translationEditPane.setFont(font);
                                        translationEditPane.setEditable(true);
                                        translationEditPane.setPreferredSize(new java.awt.Dimension(740, 30));
                                    }
                                }
                            }
                            {
                            	sentenceLabelPanel = new JPanel();
                            	sentenceAndSentenceLabelPanel.add(sentenceLabelPanel, BorderLayout.NORTH);
                            	sentenceLabelPanel.setPreferredSize(new java.awt.Dimension(744, 28));
                            	{
                            		sentenceBoxLabel = new JLabel();
                            		sentenceLabelPanel.add(sentenceBoxLabel);
                            		sentenceLabelPanel.add(getSpacer1());
                            		sentenceLabelPanel.add(getRestoreSentenceButton());
                            		sentenceLabelPanel.add(getShuffleButton());
                            		sentenceLabelPanel.add(getRemoveWordsButton());
                            		sentenceBoxLabel.setText("Sentence You are Currently Editing:");
                            		sentenceBoxLabel.setPreferredSize(new java.awt.Dimension(196, 15));
                            	}
                            }*/
                        }
                        {
                            editBoxAndEditLabelPanel = new JPanel();
                            BorderLayout editBoxAndEditLabelPanelLayout = new BorderLayout();
                            editBoxAndEditLabelPanel.setLayout(editBoxAndEditLabelPanelLayout);
                            sentenceAndDocumentPanel.add(editBoxAndEditLabelPanel, BorderLayout.SOUTH);
                            editBoxAndEditLabelPanel.setPreferredSize(new java.awt.Dimension(744, 300));
                            {
                                editBoxLabelPanel = new JPanel();
                                editBoxAndEditLabelPanel.add(editBoxLabelPanel, BorderLayout.NORTH);
                                editBoxLabelPanel.setPreferredSize(new java.awt.Dimension(744, 29));
                                {
                                    editBoxLabel = new JLabel();
                                    editBoxLabelPanel.add(editBoxLabel);
                                    editBoxLabel.setText("The Latest Version of Your \"Document to Anonymize\":");
                                }
                            }
                            {
                                editorBoxPanel = new JPanel();
                                BorderLayout editorBoxPanelLayout = new BorderLayout();
                                editorBoxPanel.setLayout(editorBoxPanelLayout);
                                editBoxAndEditLabelPanel.add(editorBoxPanel, BorderLayout.SOUTH);
                                editorBoxPanel.setPreferredSize(new java.awt.Dimension(744, 270));
                                {
                                    editBox = new JScrollPane();
                                    editorBoxPanel.add(editBox, BorderLayout.CENTER);
                                    {
                                        editorBox = new JTextPane();
                                        editorBox.setPreferredSize(new java.awt.Dimension(744, 270));
                                        editBox.setViewportView(editorBox);
                                        editorBox.setText("This is where the latest version of your document will be.");
                                        editorBox.setEnabled(true);
                                    }
                                }
                            }
                        }
                    }
                    {
                        resultsBoxAndResultsLabelPanel = new JPanel();
                        BorderLayout resultsBoxAndResultsLabelPanelLayout = new BorderLayout();
                        resultsBoxAndResultsLabelPanel.setLayout(resultsBoxAndResultsLabelPanelLayout);
                        editBoxPanel.add(resultsBoxAndResultsLabelPanel, BorderLayout.SOUTH);
                        resultsBoxAndResultsLabelPanel.setPreferredSize(new java.awt.Dimension(744, 121));
                        {
                            resultsTableLabelPanel = new JPanel();
                            resultsBoxAndResultsLabelPanel.add(resultsTableLabelPanel, BorderLayout.NORTH);
                            resultsTableLabelPanel.setPreferredSize(new java.awt.Dimension(744, 27));
                            {
                                resultsTableLabel = new JLabel();
                                resultsTableLabelPanel.add(resultsTableLabel);
                                resultsTableLabel.setText("Below are the results of your document's classification");
                            }
                        }
                        {
                            resultsBoxPanel = new JPanel();
                            BorderLayout resultsBoxPanelLayout = new BorderLayout();
                            resultsBoxPanel.setLayout(resultsBoxPanelLayout);
                            resultsBoxAndResultsLabelPanel.add(resultsBoxPanel, BorderLayout.SOUTH);
                            resultsBoxPanel.setPreferredSize(new java.awt.Dimension(744, 91));
                            {
                                resultsTablePane = new JScrollPane();
                                resultsBoxPanel.add(resultsTablePane, BorderLayout.NORTH);
                                resultsTablePane.setPreferredSize(new java.awt.Dimension(744, 60));
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
                                    resultsTable.setPreferredSize(new java.awt.Dimension(741, 32));
                                   
                                }
                            }
                            {
                                resultsBoxPanel_InnerBottomPanel = new JPanel();
                                resultsBoxPanel.add(resultsBoxPanel_InnerBottomPanel, BorderLayout.SOUTH);
                                resultsBoxPanel_InnerBottomPanel.setPreferredSize(new java.awt.Dimension(744, 35));
                                {
                                    classificationLabel = new JLabel();
                                    resultsBoxPanel_InnerBottomPanel.add(classificationLabel);
                                    classificationLabel.setText("Your document's classification will be shown here");
                                }
                            }
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

