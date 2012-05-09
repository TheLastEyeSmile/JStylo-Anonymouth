package edu.drexel.psal.jstylo.eventDrivers;

import java.io.*;
import java.util.*;

import com.jgaap.eventDrivers.NaiveWordEventDriver;
import com.jgaap.generics.*;

public class WordLengthEventDriver extends EventDriver {
	
	/* ==================
	 * overriding methods
	 * ==================
	 */
	
	public String displayName() {
		return "Word Lengths";
	}

	public String tooltipText() {
		return "The frequencies of all distinct word lengths in the document.";
	}

	public boolean showInGUI() {
		return false;
	}
	
	@Override
	public EventSet createEventSet(Document doc) {
		EventSet es = new EventSet(doc.getAuthor());
		EventSet words = new NaiveWordEventDriver().createEventSet(doc);
		for (Event e: words) {
			es.addEvent(new Event("" + e.getEvent().length()));
		}
		
		return es;
	}
}
