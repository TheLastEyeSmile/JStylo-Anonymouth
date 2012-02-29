package edu.drexel.psal.anonymouth.gooie;

import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.ProblemSet;
import edu.drexel.psal.jstylo.generics.WekaInstancesBuilder;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

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
			protected JScrollPane editBox;
			protected JTextPane editorBox;
			protected JScrollPane resultsTablePane;
			protected JTable resultsTable;
			protected JLabel classificationLabel;
			protected JLabel resultsTableLabel;
			private boolean tabMade = false;
			protected int resultsMaxIndex;
			protected String chosenAuthor;
	
			private String oldEditorBoxDoc = " ";
			private JScrollPane sentencePane;
			private JTextPane sentenceEditPane;
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
				GroupLayout editBoxPanelLayout = new GroupLayout((JComponent)editBoxPanel);
				editBoxPanel.setLayout(editBoxPanelLayout);
				editBoxPanel.setPreferredSize(new java.awt.Dimension(785, 563));
				{
					editBox = new JScrollPane();
					{
						editorBox = new JTextPane();
						editBox.setViewportView(editorBox);
						if(oldEditorBoxDoc.equals(" ")){
							editorBox.setText("Your document will be here.");
							editorBox.setEditable(false);
							editorBox.setPreferredSize(new java.awt.Dimension(758, 388));
						}
						else 
							editorBox.setText(oldEditorBoxDoc);
					}
				}
				{
					resultsTablePane = new JScrollPane();
					{
						TableModel resultsTableModel;
						if(oldResultsTableModel == null){
							resultsTableModel = 
									new DefaultTableModel(
											new String[][] { { "" }, { "" } },
											new String[] { "", "" });
						}
						else
							resultsTableModel =oldResultsTableModel;
						resultsTable = new JTable();
						resultsTablePane.setViewportView(resultsTable);
						resultsTable.setModel(resultsTableModel);
						if(tcr != null)
							resultsTable.setDefaultRenderer(Object.class, tcr);
					}
				}
				{
					classificationLabel = new JLabel();
					String chosenOne = EditorTabDriver.chosenAuthor;
					if(chosenOne.equals(ProblemSet.getDummyAuthor()))
						classificationLabel.setText("Unfortunately, your document seems to have been written by: "+EditorTabDriver.chosenAuthor);
					else if (chosenOne.equals("n/a"))
						classificationLabel.setText("Please process your document in order to recieve a classification result.");
					else
						classificationLabel.setText("Your document appears as if '"+EditorTabDriver.chosenAuthor+"' wrote it!");
				}
				{
					resultsTableLabel = new JLabel();
					if(oldResultsTableModel == null)
						resultsTableLabel.setText("Results of this Document's Classification (% probability of authorship per author)");
					else
						resultsTableLabel.setText("Results of **Last** Document's Classification (% probability of authorship per author)");
				}
			editBoxPanelLayout.setHorizontalGroup(editBoxPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(editBoxPanelLayout.createParallelGroup()
					    .addComponent(editBox, GroupLayout.Alignment.LEADING, 0, 761, Short.MAX_VALUE)
					    .addComponent(resultsTablePane, GroupLayout.Alignment.LEADING, 0, 761, Short.MAX_VALUE)
					    .addComponent(getSentencePane(), GroupLayout.Alignment.LEADING, 0, 761, Short.MAX_VALUE)
					    .addGroup(editBoxPanelLayout.createSequentialGroup()
					        .addGap(92)
					        .addGroup(editBoxPanelLayout.createParallelGroup()
					            .addGroup(editBoxPanelLayout.createSequentialGroup()
					                .addComponent(resultsTableLabel, GroupLayout.PREFERRED_SIZE, 628, GroupLayout.PREFERRED_SIZE)
					                .addGap(0, 0, Short.MAX_VALUE))
					            .addGroup(GroupLayout.Alignment.LEADING, editBoxPanelLayout.createSequentialGroup()
					                .addGap(35)
					                .addComponent(classificationLabel, 0, 570, Short.MAX_VALUE)
					                .addGap(23)))
					        .addGap(41)))
					.addContainerGap());
			editBoxPanelLayout.setVerticalGroup(editBoxPanelLayout.createSequentialGroup()
					.addContainerGap(33, 33)
					.addComponent(getSentencePane(), GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(editBox, GroupLayout.PREFERRED_SIZE, 379, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(resultsTableLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(resultsTablePane, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(classificationLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE));
				}
				tabMade = true;
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
					sentenceEditPane.setEnabled(false);
					sentenceEditPane.setVisible(false);
				}
				return sentenceEditPane;
			}

}

