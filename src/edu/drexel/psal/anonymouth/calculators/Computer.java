package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

/**
 * The superclass for all 'CALC_*'subclasses that perform the live calculations for the continuously updating present value of a feature 
 * @author Andrew W.E. McDonald
 *
 */
abstract public class Computer {
	
	protected double presentValue;
	protected String genericName;
	private static Document theDocument;
	protected boolean isAvailable = false;
	protected String stringInBraces;
	
	
	/**
	 * Intializer for all subclasses of Computer 
	 * @param presentValue
	 * @param genericName
	 */
	public void initialize(double presentValue, String genericName, String stringInBraces){
		this.presentValue = presentValue;
		this.genericName = genericName;
		this.stringInBraces = stringInBraces;
	}
	
	/**
	 * The abstract method that must be overridden on a per=feature basis in order to set a new present value;
	 */
	abstract  protected void compute() throws EventGenerationException;
	
	/**
	 * run method to compute 
	 */
	public void run(){
		try {
			compute();
		} catch (EventGenerationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the document as a String
	 */
	protected Document getDocument(){
		return theDocument;
	}
	
	/**
	 * returns the rounded present value of the feature - to be used after calculation
	 * @return
	 */
	public double getPresentValue(){
		return Math.floor(presentValue*10000+.5)/10000;
	}
	

	/**
	 * returns the unrounded present value of the feature 
	 * @return
	 */
	 public double getUnroundedPresentValue(){
		 return presentValue;
	 }
	
	/**
	 * allows universally setting the document string
	 * @param theDocument
	 */
	public static void setTheDocument(Document theDocument){
		Computer.theDocument = theDocument;
	}
	
	/**
	 * returns true if the 'compute()' method has marked it as true, false if not.
	 * @return
	 * 	true if the compute() method has been implemented/marked isAvailable as true
	 */
	public boolean isAvailable(){
		return isAvailable;
	}
 
	
}
