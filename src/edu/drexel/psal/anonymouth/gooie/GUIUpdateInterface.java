package edu.drexel.psal.anonymouth.gooie;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import weka.classifiers.Classifier;

import edu.drexel.psal.jstylo.generics.*;
import edu.drexel.psal.jstylo.generics.FeatureDriver.ParamTag;

import com.jgaap.generics.*;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;

public class GUIUpdateInterface {
	
	
	

	// about dialog
	// ============
	
	protected static String version = "0.0.4";
/*
	protected static void showAbout(GUIMain main) {
		ImageIcon logo = new ImageIcon("./Anonymouth_LOGO.png", "Anonymouth Logo");
		String content =
				"<html><p>" +
				"<h3>Anonymouth</h3><br>" +
				"Version "+version+"<br>" +
				"Author: Andrew W.E. McDonald<br>" +
				"Privacy, Security and Automation Lab (PSAL)<br>" +
				"Drexel University<br>" +
				"<br>" +
				"Powered by: JSylo,Weka, JGAAP" +
				"</p></html>";
		
		JOptionPane.showMessageDialog(main, 
				content,
				"About JStylo",
				JOptionPane.INFORMATION_MESSAGE,
				logo);
	}
*/
	/* ========================
	 * documents tab operations
	 * ========================
	 */
	
	public static void updateDocPrepColor(GUIMain main)
	{
		Color ready = new Color(0,255,128);
		Color notReady = new Color(255,102,102);
		if (main.documentsAreReady())
		{
			main.prepDocLabel.setBackground(ready);
		}
		else
		{
			main.prepDocLabel.setBackground(notReady);
		}	
	}
	
	/**
	 * Updates the documents tab view with the current problem set.
	 */
	protected static void updateProblemSet(GUIMain main) {
		Logger.logln("GUI Update: update documents tab with current problem set started");
		
		// update test documents table
		updateTestDocTable(main);
		
		// update training corpus tree
		updateTrainDocTree(main);
		
		// update user sample documents table
		updateUserSampleDocTable(main);
		
		updateDocPrepColor(main);
	}
	
	/**
	 * Updates the test documents table with the current problem set. 
	 */
	protected static void updateTestDocTable(GUIMain main) {
		DefaultListModel dlm = (DefaultListModel)main.prepMainDocList.getModel();
		dlm.removeAllElements();
		List<Document> testDocs = main.ps.getTestDocs();
		for (int i=0; i<testDocs.size(); i++)
			dlm.addElement(testDocs.get(i).getTitle());
		
		updateDocPrepColor(main);
	}
	
	/**
	 * Updates the User Sample documents table with the current problem set. 
	 */
	protected static void updateUserSampleDocTable(GUIMain main) {
		DefaultListModel dlm = (DefaultListModel)main.prepSampleDocsList.getModel();
		dlm.removeAllElements();
		List<Document> userSampleDocs = main.ps.getTrainDocs(ProblemSet.getDummyAuthor());
		for (int i=0; i<userSampleDocs.size(); i++)
			dlm.addElement(userSampleDocs.get(i).getTitle());// todo this is where it fails (from the note in DocsTabDriver).. it fails with a "NullPointerException".... (when "create new problem set" is clicked when there isn't a problem set there. [ i.e. as soon as Anonymouth starts up]) 
		
		updateDocPrepColor(main);
	}

	/**
	 * Updates the training corpus tree with the current problem set. 
	 */
	protected static void updateTrainDocTree(GUIMain main) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(main.ps.getTrainCorpusName());
		Map<String,List<Document>> trainDocsMap = main.ps.getAuthorMap();
		DefaultMutableTreeNode authorNode, docNode;
		for (String author: trainDocsMap.keySet()) {
			if(author.equals(ProblemSet.getDummyAuthor()))
					continue;
			authorNode = new DefaultMutableTreeNode(author);
			root.add(authorNode);
			for (Document doc: trainDocsMap.get(author)){
				docNode = new DefaultMutableTreeNode(doc.getTitle());
				authorNode.add(docNode);
			}
		}
		DefaultTreeModel trainTreeModel = new DefaultTreeModel(root);
		main.trainCorpusJTree.setModel(trainTreeModel);
		
		updateDocPrepColor(main);
	}
	
	/**
	 * Clears the text from the document preview text box.
	 */
	protected static void clearDocPreview(GUIMain main) {
		main.docPreviewJTextPane.setText("");
		main.docPreviewNameJLabel.setText("");
	}
	
	
	/*
	 * =======================
	 * features tab operations
	 * =======================
	 */
	
	public static void updateFeatPrepColor(GUIMain main)
	{
		Color ready = new Color(0,255,128);
		Color notReady = new Color(255,102,102);
		if (main.featuresAreReady())
		{
			main.prepFeatLabel.setBackground(ready);
		}
		else
		{
			main.prepFeatLabel.setBackground(notReady);
		}	
	}
	
	/**
	 * Updates the feature set view when a new feature set is selected / created.
	 */
	protected static void updateFeatureSetView(GUIMain main) {
		CumulativeFeatureDriver cfd = main.cfd;
		
		// update name
		main.featuresSetNameJTextField.setText(cfd.getName() == null ? "" : cfd.getName());
		
		// update description
		main.featuresSetDescJTextPane.setText(cfd.getDescription() == null ? "" : cfd.getDescription());
		
		// update list of features
		clearFeatureView(main);
		main.featuresJListModel.removeAllElements();
		for (int i=0; i<cfd.numOfFeatureDrivers(); i++) 
			main.featuresJListModel.addElement(cfd.featureDriverAt(i).getName());
	}
	
	/**
	 * Updates the feature view when a feature is selected in the features tab.
	 */
	protected static void updateFeatureView(GUIMain main, int selected) {
		// clear all
		clearFeatureView(main);
		
		// unselected
		if (selected == -1)
			return;
		
		// selected
		FeatureDriver fd = main.cfd.featureDriverAt(selected);
		
		// name and description
		main.featuresFeatureNameJTextField.setText(fd.getName());
		main.featuresFeatureDescJTextPane.setText(fd.getDescription());
		
		// update feature extractor
		main.featuresFeatureExtractorContentJLabel.setText(fd.getUnderlyingEventDriver().displayName());
		main.featuresFeatureExtractorConfigJScrollPane.setViewportView(getParamPanel(fd.getUnderlyingEventDriver()));
		
		// update canonicizers
		List<Canonicizer> canons = fd.getCanonicizers();
		if (canons != null) {
			for (int i=0; i<canons.size(); i++)
				main.featuresCanonJListModel.addElement(canons.get(i).displayName());
		}
		
		// update cullers
		List<EventCuller> cullers = fd.getCullers();
		if (cullers != null) {
			for (int i=0; i<cullers.size(); i++)
				main.featuresCullJListModel.addElement(cullers.get(i).displayName());
		}

		// update normalization
		main.featuresNormContentJLabel.setText(fd.getNormBaseline().getTitle());
		main.featuresFactorContentJLabel.setText(fd.getNormFactor().toString());
	}
	
	/**
	 * Resets the feature view in the features tab.
	 */
	protected static void clearFeatureView(GUIMain main) {
		main.featuresFeatureNameJTextField.setText("");
		main.featuresFeatureDescJTextPane.setText("");
		main.featuresFeatureExtractorContentJLabel.setText("");
		main.featuresFeatureExtractorConfigJScrollPane.setViewportView(null);
		main.featuresCanonJListModel.removeAllElements();
		main.featuresCanonConfigJScrollPane.setViewportView(null);
		main.featuresCullJListModel.removeAllElements();
		main.featuresCullConfigJScrollPane.setViewportView(null);
		main.featuresNormContentJLabel.setText("");
		main.featuresFactorContentJLabel.setText("");
	}
	
	/**
	 * Creates a panel with parameters and their values for the given event driver / canonicizer / culler.
	 */
	protected static JPanel getParamPanel(Parameterizable p) {
		List<Pair<String,ParamTag>> params = FeatureDriver.getClassParams(p.getClass().getName());
		
		JPanel panel = new JPanel(new GridLayout(params.size(),2,5,5));
		for (Pair<String,ParamTag> param: params) {
			JLabel name = new JLabel(param.getFirst()+": ");
			name.setVerticalAlignment(JLabel.TOP);
			panel.add(name);
			JLabel value = new JLabel(p.getParameter(param.getFirst()));
			value.setVerticalAlignment(JLabel.TOP);
			panel.add(value);
		}
		
		return panel;
	}
	
	
	/* ===============
	 * Classifiers tab
	 * ===============
	 */
	
	public static void updateClassPrepColor(GUIMain main)
	{
		Color ready = new Color(0,255,128);
		Color notReady = new Color(255,102,102);
		if (main.classifiersAreReady())
		{
			main.prepClassLabel.setBackground(ready);
		}
		else
		{
			main.prepClassLabel.setBackground(notReady);
		}	
	}
	
	/**
	 * Updates the list of selected classifiers with respect to the list of classifiers.
	 */
	protected static void updateClassList(GUIMain main) {
		DefaultListModel model = (DefaultListModel)main.classJList.getModel();
		List<Classifier> classifiers = main.classifiers;
		
		model.removeAllElements();
		for (Classifier c: classifiers) {
			String className = c.getClass().getName();
			model.addElement(className.substring(className.lastIndexOf(".")+1));
		}
	}
}

















