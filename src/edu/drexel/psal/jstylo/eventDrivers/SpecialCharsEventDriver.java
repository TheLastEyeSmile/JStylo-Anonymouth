package edu.drexel.psal.jstylo.eventDrivers;

import com.jgaap.generics.*;

public class SpecialCharsEventDriver extends EventDriver {

	/* ======
	 * fields
	 * ======
	 */
	
	private static String specialChars = "~@#$%^&*-_=+><[]{}/\\|";
	
	/* ==================
	 * overriding methods
	 * ==================
	 */
	
	public String displayName() {
		return "Special Characters";
	}

	public String tooltipText() {
		return "The frequencies of special characters: " +
				"~, @, #, $, %, ^, &, *, -, _, =, +, >, <, [, ], {, }, /, \\, |";
	}

	public boolean showInGUI() {
		return false;
	}
	
	@Override
	public EventSet createEventSet(Document doc) {
		EventSet es = new EventSet(doc.getAuthor());
		char[] cd = doc.getProcessedText();

		for (int i = 0; i < cd.length; i++)
			if (specialChars.contains("" + cd[i]))
				es.addEvent(new Event(cd[i]));
		
		return es;
	}
}
