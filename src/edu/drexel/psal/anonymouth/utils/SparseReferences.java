package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Scanner;

import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;


/**
 * A way to keep track of indices and values in an array
 * @author Andrew W.E. McDonald
 *
 */
public class SparseReferences {

	protected ArrayList<Reference> references;
	
	/**
	 * constructor. 
	 * @param initialSizeOfReferenceArrayList
	 */
	public SparseReferences(int initialSizeOfReferenceArrayList){
		references = new ArrayList<Reference>(initialSizeOfReferenceArrayList);
	}
	
	
	/**
	 * inserts 'value' at 'index' provided that index is within the range [0,sizeOfArray). if not, returns false. 
	 * NOTE: 'put' does not preserve data at location 'index'. Any value in position 'index' will be overwritten with 'value'
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean addNewReference(int index,int value){
		if(index >= 0){
			Reference r = new Reference(index, value);
			if(references.contains(r)){
				Logger.logln("Cannot add duplicate references, addNewReference failed.",LogOut.STDERR);
				return false;
			}
			references.add(r);
			return true;
		}
		Logger.logln("Cannot add Reference with 'index' less than zero",LogOut.STDERR);
		return false;
	}
	
	/**
	 * Adds a Reference to the ArrayList of Reference objects, provided that the list does not already contain a Reference to the attribute that 
	 * is being referenced by the Reference reference, and that the input reference has an index (the attribute's index) greater than or equal to zero.
	 * @param reference
	 * @return
	 */
	public boolean addNewReference(Reference reference){
		if(reference.index >= 0){
			if(references.contains(reference)){
				Logger.logln("Cannot add duplicate references, addNewReference failed.",LogOut.STDERR);
				return false;
			}
			references.add(new Reference(reference.index,reference.value));
			return true;
		}
		Logger.logln("Cannot add Reference with 'index' less than zero",LogOut.STDERR);
		return false;
	}
	
	
	/**
	 * Merges the argument into this instance of SparseReferences
	 * @param notThis
	 * @return
	 */
	public void merge(SparseReferences notThis){
		for(Reference notThisEither:notThis.references){
			if(references.contains(notThisEither)){
				int thisIndex = references.indexOf(notThisEither);
				references.add(thisIndex,references.get(thisIndex).merge(notThisEither));
			}
			else{
				references.add(notThisEither);
			}
		}
	}
	
	
	
	
	
	/**
	 * Subtracts the "argument" (right) SparseReferences' references from the "calling" (left) SparseReferences' references, i.e.:
	 * theLeftOne.leftMinusRight(theRightOne)
	 * 
	 * the newest version should call the function, and the old version should be passed in => new (minus) old => postive number if a feature's values increased, 
	 * and negative if they decreased. the resulting output SparseReferences object is used to update the Attribute values corresponding to the index held in each Reference.
	 * @param sia
	 * @return
	 */
	public SparseReferences leftMinusRight(SparseReferences sia){
		//sia=references of old sentence
		double tempValue;
		int tempIndex;
		int indexOfRef;
		SparseReferences adjustmentReferences = new SparseReferences((sia.references.size()+this.references.size())); // absolute max size
		Reference newRef;
		ArrayList<Reference> cloneOfThis = (ArrayList<Reference>) this.references.clone();
		for(Reference r: sia.references){
			if(cloneOfThis.contains(r)){
				indexOfRef = cloneOfThis.indexOf(r);
				tempValue = cloneOfThis.get(indexOfRef).value - r.value;
				tempIndex = r.index;
				newRef = new Reference(tempIndex,tempValue);
				cloneOfThis.remove(indexOfRef);
				Logger.logln("");
			}
			else{// There are zero appearances of the feature in the new SparseReferences, so just multiply the number found in the old SparseReferences by -1 (all were removed)
				newRef = new Reference(r.index,(-r.value));
				Logger.logln("Reference not in both lists");
			}
			Logger.logln("Left Minus Right addNewRef");
			adjustmentReferences.addNewReference(newRef);
		}
		if(cloneOfThis.isEmpty() == false){ //there are still values in the clone which means new attributes / features were added. These all went from a count of zero to whatever their value is now. So, positive change
			for(Reference r:cloneOfThis){
				newRef = new Reference(r.index,r.value);
				Logger.logln("Left Minus Right addNewRef new features added");
				adjustmentReferences.addNewReference(newRef);
			}
		}
		return adjustmentReferences; 
	}
	
	
	
	
	/**
	 * returns string representation of contained index and integer array in the form of [ index => [ value0, value1, ..., valuen]]
	 */
	public String toString(){
		String whatIsInside = "[";
		int i=0;
		int numRefs = references.size();
		for(i=0;i<numRefs;i++){
			whatIsInside += references.get(i).toString();
			if(i<numRefs-1) whatIsInside+= ",";
		}	
		whatIsInside +="]";
		return whatIsInside;
	}
	
	
	/**
	 * returns the number of stored References 
	 * @return
	 */
	public int length(){
		return references.size();
	}
	
	
	/*
	public static void main(String[] args){
	
	
	}
	*/
	
}

