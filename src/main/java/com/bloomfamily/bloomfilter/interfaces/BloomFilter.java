package com.bloomfamily.bloomfilter.interfaces;

import com.bloomfamily.bloomfilter.obj.BloomElement;
import com.bloomfamily.bloomfilter.obj.Tuple;

public interface BloomFilter {
	
	int size();
	
	boolean probablyHas(BloomElement<?> element);
	
	void insert(BloomElement<?> element);
	
	default Tuple init(long expectedItems, double accepatableFalsePositivityRate) {
		
		double numberOfBitsRequiredForStorage = ((-1 * expectedItems) * Math.log(accepatableFalsePositivityRate)) / Math.pow(Math.log(2), 2);
		
		long numberOfHashFunctions = (long)Math.ceil((numberOfBitsRequiredForStorage/expectedItems) * Math.log(2));
		
		return new Tuple(numberOfBitsRequiredForStorage, numberOfHashFunctions);
		
		
	}

}
