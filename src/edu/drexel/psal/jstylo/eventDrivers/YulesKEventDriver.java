package edu.drexel.psal.jstylo.eventDrivers;

import java.util.*;

import com.jgaap.eventDrivers.*;
import com.jgaap.generics.*;

public class YulesKEventDriver extends SingleNumericEventDriver {

	/* ======
	 * fields
	 * ======
	 */
	
	/**
	 * Event drivers to be used.
	 */
	private NaiveWordEventDriver wordsED;
	
	
	/* ============
	 * constructors
	 * ============
	 */
	
	/**
	 * Default constructor.
	 */
	public YulesKEventDriver() {
		wordsED = new NaiveWordEventDriver();
	}
	
	/* ==================
	 * overriding methods
	 * ==================
	 */
	
	public String displayName() {
		return "Yule's Characteristic K";
	}

	public String tooltipText() {
		return "For x = number of times a words occurs, fx = number of words occurring x times, S1 = SUM(x fx), S2 = SUM(x^2 fx), " +
				"Yule's K is defined: 10^4 * (S2/S1^2 - 1/S1^2)";
	}

	public boolean showInGUI() {
		return false;
	}

	public double getValue(Document doc) throws EventGenerationException {
		EventSet words = wordsED.createEventSet(doc);
		double res = 0;
		
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		String word;
		// count word frequencies - x
		for (Event e: words) {
			word = e.getEvent();
			if (wordCount.containsKey(word))
				wordCount.put(word, wordCount.get(word) + 1);
			else
				wordCount.put(word, 1);
		}
		
		// count fx
		Map<Integer,Integer> freqCount = new HashMap<Integer,Integer>();
		int count;
		for (Integer value: wordCount.values()) {
			count = 0;
			for (String key: wordCount.keySet())
				if (wordCount.get(key).intValue() == value.intValue())
					count++;
			freqCount.put(value, count);
		}
		
		// calculate S1, S2
		double S1 = 0;
		double S2 = 0;
		int x, fx;
		for (String key: wordCount.keySet()) {
			x = wordCount.get(key);
			fx = freqCount.get(x);
			S1 += x * fx;
			S2 += x * x * fx;
		}
		
		// calculate Yule's K
		res = 10000 * (S2 - 1) / (S1 * S1);
		
		return res;
	}
}
