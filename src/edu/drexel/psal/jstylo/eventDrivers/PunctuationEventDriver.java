package edu.drexel.psal.jstylo.eventDrivers;

import com.jgaap.generics.*;

public class PunctuationEventDriver extends EventDriver {

	/* ======
	 * fields
	 * ======
	 */
	
	private static String punct = "?!,.'`\":;";
	
	/* ==================
	 * overriding methods
	 * ==================
	 */
	
	public String displayName() {
		return "Punctuation";
	}

	public String tooltipText() {
		return "The frequencies of punctuation: ? ! , . ' ` \" : ;";
	}

	public boolean showInGUI() {
		return false;
	}
	
	@Override
	public EventSet createEventSet(Document doc) {
		EventSet es = new EventSet(doc.getAuthor());
		char[] cd = doc.getProcessedText();

		for (int i = 0; i < cd.length; i++)
			if (punct.contains("" + cd[i]))
				es.addEvent(new Event(cd[i]));
		
		return es;
	}
}
