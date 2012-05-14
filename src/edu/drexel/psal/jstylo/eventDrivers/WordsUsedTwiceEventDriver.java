package edu.drexel.psal.jstylo.eventDrivers;

public class WordsUsedTwiceEventDriver extends WordUsageCounterEventDriver {

	public String displayName() {
		return "Dis Legomena - Words used twice";
	}
	
	public WordsUsedTwiceEventDriver() {
		this.N = 2;
	}
}
