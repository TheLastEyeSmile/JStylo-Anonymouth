package edu.drexel.psal.jstylo.eventDrivers;

import java.util.*;

import com.jgaap.eventDrivers.*;
import com.jgaap.generics.*;

public class SimpsonsDiversityIndexEventDriver extends SingleNumericEventDriver {

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
	public SimpsonsDiversityIndexEventDriver() {
		wordsED = new NaiveWordEventDriver();
	}
	
	/* ==================
	 * overriding methods
	 * ==================
	 */
	
	public String displayName() {
		return "Simpson's Diversity Index";
	}

	public String tooltipText() {
		return "For n = frequency of a word, N = total words, Simpson's diversity index is: " +
				"1 - (SUM_n (n(n-1)) / N(N-1))";
	}

	public boolean showInGUI() {
		return false;
	}

	@Override
	public double getValue(Document doc) throws EventGenerationException {
		EventSet words = wordsED.createEventSet(doc);
		double res = 0;
		
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		String word;
		// count word frequencies
		for (Event e: words) {
			word = e.getEvent();
			if (wordCount.containsKey(word))
				wordCount.put(word, wordCount.get(word) + 1);
			else
				wordCount.put(word, 1);
		}
		
		int N = words.size();
		double N_N_minus_1 = N * (N - 1);
		
		double sum = 0;
		int n;
		for (String key: wordCount.keySet()) {
			n = wordCount.get(key);
			sum += n * (n - 1) / N_N_minus_1;
		}
		res = 1 - sum;
		return res;
	}
}
