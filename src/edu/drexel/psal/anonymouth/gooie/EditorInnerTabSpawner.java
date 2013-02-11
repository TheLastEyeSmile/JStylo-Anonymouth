package edu.drexel.psal.anonymouth.gooie;

import edu.drexel.psal.jstylo.generics.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;



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
			private TableCellRenderer tcr = new DefaultTableCellRenderer();
			
			protected JScrollPane clusterScrollPane;
			protected JPanel holderPanel;
			protected JPanel topPanel;
			protected JButton reClusterAllButton;
			protected JButton refreshButton;
			protected JButton selectClusterConfiguration;
			protected JPanel secondPanel;
	
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
					Font normalFont = new Font("Ariel", Font.PLAIN, 11);
					Font titleFont = new Font("Ariel", Font.BOLD, 11);
					
					editorTabPane = new JTabbedPane();
					editBoxPanel = new JPanel();
					editorTabPane.addTab("Document", editBoxPanel);
					{
						MigLayout EBPLayout = new MigLayout(
								"fillx, wrap 3, ins 20 20 0 20",
								"[70]0[grow, fill]0[140]",
								"[70]0[70]10[fill]0[grow, fill]10[fill]0[]0[]");
	                    editBoxPanel.setLayout(EBPLayout);
	                    
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
	                	sentenceOptionsPanel.setBackground(new Color(252,242,206));
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
                    	translationOptionsPanel.setBackground(new Color(252,242,206));
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
                        documentOptionsPanel.setBackground(new Color(252,242,206));
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
					clusterScrollPane = new JScrollPane();
					editorTabPane.addTab("Clusters", clusterScrollPane);
					editorTabPane.setEnabledAt(1, false);
					{
						clusterScrollPane.setOpaque(true);
						//theScrollPane.setPreferredSize(new java.awt.Dimension(518, 369));
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

