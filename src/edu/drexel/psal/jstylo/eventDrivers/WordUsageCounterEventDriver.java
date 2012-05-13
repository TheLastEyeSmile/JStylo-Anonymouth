package edu.drexel.psal.jstylo.eventDrivers;

import java.util.*;

import com.jgaap.eventDrivers.NaiveWordEventDriver;
import com.jgaap.generics.Document;
import com.jgaap.generics.Event;
import com.jgaap.generics.EventGenerationException;
import com.jgaap.generics.EventSet;

public class WordUsageCounterEventDriver extends SingleNumericEventDriver {
	
	protected int N = 1;
	
	/* ==================
	 * overriding methods
	 * ==================
	 */
	
	public String displayName() {
		return "Hapax Legomena - Words used once";
	}

	public String tooltipText() {
		return displayName();
	}

	public boolean showInGUI() {
		return false;
	}
	
	@Override
	public double getValue(Document doc) throws EventGenerationException {
		EventSet words = new NaiveWordEventDriver().createEventSet(doc);
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		
		String word;
		for (Event wordEvent: words) {
			word = wordEvent.getEvent();
			if (wordCount.containsKey(word))
				wordCount.put(word, wordCount.get(word) + 1);
			else
				wordCount.put(word, 1);
		}
		
		int count = 0;
		for (String key: wordCount.keySet())
			if (wordCount.get(key) == N)
				count++;
		
		return count;
	}
}
