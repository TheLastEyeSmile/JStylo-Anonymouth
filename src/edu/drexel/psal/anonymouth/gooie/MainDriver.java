package edu.drexel.psal.anonymouth.gooie;

import java.awt.Point;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.drexel.psal.jstylo.generics.*;

public class MainDriver 
{
	
	
	/**
	 * Initialize all main listeners.
	 */
	protected static void initListeners(final GUIMain main) 
	{
		// feature set buttons
		// ===================
		
		// feature set combo box
		main.prepAdvButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				PreProcessSettingsFrame settingsPane = new PreProcessSettingsFrame(main);
			}
		});
	}
}