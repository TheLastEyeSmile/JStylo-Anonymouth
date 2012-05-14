package edu.drexel.psal.jstylo.eventCullers;

import java.util.*;

import com.jgaap.generics.*;

/**
 * Removes all events that appear as already-appeared.
 * 
 * @author Ariel Stolerman
 */
public class IgnoreAppearedEventCuller extends FrequencyEventsExtended {
	
	protected static Set<String> ignoreSet = new HashSet<String>();
	
	@Override
	public List<EventSet> cull(List<EventSet> eventSets) {
		
		// remove irrelevant events
		Event e;
		String s;
		for (EventSet es: eventSets) {
			for (int i=es.size()-1; i >= 0; i--) {
				e = es.eventAt(i);
				s = getEventContent(e.toString());
				if (ignoreSet.contains(s))
					es.removeEvent(e);
				else
					ignoreSet.add(s);
			}
		}

		return eventSets;
	}
	
	public String getEventContent(String eventString) {
		return eventString.replaceFirst("\\S+\\{", "").replaceAll("\\}$", "");
	}
	
	@Override
	public String displayName() {
		return "Ignore previously appeared events";
	}

	@Override
	public String tooltipText() {
		return displayName();
	}

	@Override
	public boolean showInGUI() {
		return false;
	}

	/*
	// main for testing
	public static void main(String[] args) throws Exception {
		EventDriver ed = new NaiveWordEventDriver();
		Document doc = new Document("./corpora/drexel_1/a/a_01.txt","a","a_01.txt");
		doc.load();
		doc.addCanonicizer(new UnifyCase());
		doc.processCanonicizers();
		EventSet es = ed.createEventSet(doc);
		List<EventSet> l = new ArrayList<EventSet>(1);
		l.add(es);
		EventCuller c = new MinAppearancesEventCuller();
		c.setParameter("N", 22);
		l = c.cull(l);
		es = l.get(0);
		if (es.size() > 0)
			System.out.println(es);
		else
			System.out.println("no events!");
	}
	*/
}
