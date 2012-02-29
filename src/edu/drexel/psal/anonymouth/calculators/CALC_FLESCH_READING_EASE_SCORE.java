package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.FleschReadingEaseScoreEventDriver;

/**
 * Calculator for FLESCH_READING_EASE_SCORE present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_FLESCH_READING_EASE_SCORE extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		FleschReadingEaseScoreEventDriver couldYouHavePickedALongerName_QuestionMark = new FleschReadingEaseScoreEventDriver();
		presentValue = couldYouHavePickedALongerName_QuestionMark.getValue(doc);
		
	}
	
	

	
	
}
