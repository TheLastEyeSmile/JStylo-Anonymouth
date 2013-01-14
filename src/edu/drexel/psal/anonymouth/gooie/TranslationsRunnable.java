package edu.drexel.psal.anonymouth.gooie;

import java.awt.Color;
import java.util.Date;
import com.memetix.mst.language.Language;
import java.util.ArrayList;
//import edu.drexel.psal.anonymouth.gooie.EditorTabDriver;

import edu.drexel.psal.anonymouth.gooie.Translation;
import edu.drexel.psal.anonymouth.utils.TaggedSentence;

public class TranslationsRunnable extends EditorTabDriver implements Runnable
{
	private TaggedSentence sentence;
	private GUIMain main;
	
	/**
	 * Class that handles the 2-way translation of a given sentence. It starts a new thread so the main thread
	 * doesn't freeze up. This allows it to update the user about the progress of the translations.
	 * @param sentence String that holds the sentence to be translated
	 * @param main GUIMain object needed to access fields of the GUI
	 */
	public TranslationsRunnable (TaggedSentence sentence, GUIMain main)
	{
		this.sentence = sentence;
		this.main = main;
	}
	
	/**
	 * Compact method that sets everything's enabled property while the thread is translating.
	 * The components listed are listed because they can possibly interrupt or overlap the translating.
	 * @param b boolean that controls the "enabled" value of the listed components
	 */
	public void setAllEnabled(boolean b)
	{
		eits.nextSentenceButton.setEnabled(b);
		eits.transButton.setEnabled(b);
		eits.prevSentenceButton.setEnabled(b);
		//main.translationsComboBox.setEnabled(b);
		main.processButton.setEnabled(b);
	}
	
	/**
	 * Handles the translations. Is called when TranslationRunnable.start() is called. Assumes that the translations
	 * Table has already been cleared (normally by the "next" button).
	 */
	public void run() 
	{
		setAllEnabled(false); // disable everything to start so there are no interruptions
		main.editorHelpTabPane.setSelectedIndex(2); // or whatever index "Translation" tab is at
		
		// set up the progress bar
		main.editorProgressBar.setMinimum(0);
		main.editorProgressBar.setMaximum(translator.getAllLangs().length);
		main.editorProgressBar.setValue(0);
		
		// finish set up for translation
		Date start = new Date();
		//main.translationsComboBox.addItem("ORIGINAL - " + sentence.getUntagged().trim());
		//main.translationsComboBox.setSelectedIndex(0);
		main.editingProgressBarLabel.setText("Translating Sentence... 0 of " + translator.getAllLangs().length + " languages.");
		
		// translate all languages and add them and their anonIndex to the ArrayLists
		for (int i = 0; i < translator.getAllLangs().length; i++)
		{
			Language lang = translator.getAllLangs()[i];
			
			String translation = translator.getTranslation(sentence.getUntagged().trim(), lang);
			TaggedSentence taggedTrans = new TaggedSentence(translation);
			sentence.getTranslations().add(taggedTrans);
			main.translationsTable.setValueAt(sentence.getTranslations().get(i).getUntagged(), i, 0);
			main.editorProgressBar.setValue(i+1);
			main.editingProgressBarLabel.setText("Translating Sentence... " + (i+1) + " of " + translator.getAllLangs().length + " languages.");
		}
		
		// sorts the translations by anonIndex and populates the translation drop down.
		//sentence.sortTranslations(); // sort by anonIndexs
		Date end = new Date();
		
		main.editingProgressBarLabel.setText("Translated in " + ((end.getTime()-start.getTime())/1000.0) + " seconds.");
		
		setAllEnabled(true);
    }
}
