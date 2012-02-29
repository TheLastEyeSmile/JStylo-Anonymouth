package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.GunningFogIndexEventDriver;

/**
 * Calculator for GUNNING_FOG_READABILITY_INDEX present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_GUNNING_FOG_READABILITY_INDEX extends Computer {

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		GunningFogIndexEventDriver gfied = new GunningFogIndexEventDriver();
		presentValue = gfied.getValue(doc);
		
	}

}
