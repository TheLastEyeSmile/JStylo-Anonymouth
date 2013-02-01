package edu.drexel.psal.anonymouth.gooie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

import com.jgaap.generics.Document;

import weka.classifiers.*;

import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;

public class PreProcessSettingsFrame extends JFrame
{
	protected JSplitPane splitPane;
	protected JTree tree;
	protected JScrollPane treeScrollPane;
	protected JScrollPane mainScrollPane;
	protected JScrollPane bottomScrollPane;
	protected GUIMain main;
	
	protected JPanel treePanel;
	protected JPanel mainPanel;
	protected JPanel bottomPanel;
	
	public PreProcessSettingsFrame(GUIMain main)
	{
		super("Pre-Process Settings");
		init(main);
	}
	
	private void init(GUIMain main)
	{
		this.main = main;
		this.setVisible(true);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(new Dimension((int)(screensize.getWidth()*.8), (int)(screensize.getHeight()*.8)));
		
		//treeScrollPane.setMinimumSize(minimumSize);
		//mainScrollPane.setMinimumSize(minimumSize);
		treePanel = new JPanel();
		treePanel.setPreferredSize(new Dimension(200,500));
		treePanel.setBackground(Color.WHITE);
		treeScrollPane = new JScrollPane();
		treeScrollPane.setPreferredSize(new Dimension(200,500));
		treeScrollPane.setViewportView(treePanel);
		
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(500,500));
		mainScrollPane = new JScrollPane();
		mainScrollPane.setPreferredSize(new Dimension(500,500));
		mainScrollPane.setViewportView(mainPanel);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeScrollPane, mainScrollPane);
		splitPane.setPreferredSize(new Dimension(700,500));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);
		
		this.add(splitPane, BorderLayout.NORTH);
		
		bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(700,100));
		bottomScrollPane = new JScrollPane();
		bottomScrollPane.setPreferredSize(new Dimension(700,100));
		bottomScrollPane.setViewportView(bottomPanel);
		this.add(bottomScrollPane, BorderLayout.SOUTH);
		
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Pre-Process");
		createNodes(top);
		tree = new JTree(top);
		treeScrollPane = new JScrollPane(tree);
		
		this.pack();
		this.setLocationRelativeTo(null); // makes it form in the center of the screen
	}
	
	private void createNodes(DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode section = null;
	    DefaultMutableTreeNode subSection = null;
	    
	    section = new DefaultMutableTreeNode("Documents");
	    top.add(section);
	    
	    section = new DefaultMutableTreeNode("Features");
	    top.add(section);
	    
	    section = new DefaultMutableTreeNode("Classifiers");
	    top.add(section);
	}
	
	public void closeWindow() 
	{
		//main.setEnabled(true);
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
}