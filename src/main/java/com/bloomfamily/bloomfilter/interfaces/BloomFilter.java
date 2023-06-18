package com.bloomfamily.bloomfilter.interfaces;

import com.bloomfamily.bloomfilter.obj.BloomElement;
import com.bloomfamily.bloomfilter.obj.Tuple;

public interface BloomFilter {
	
	int size();
	
	boolean probablyHas(BloomElement<?> element);
	
	boolean insert(BloomElement<?> element);
	
	default Tuple initialize(long expectedItems, double accepatableFalsePositivityRate) {
		
		/*
		 * n: number of items expected to be stored in filter 
		 * p: acceptable false positive rate {between 0..1} (e.g. 0.01 → 1%) 
		 * m: number of bits needed in the bloom filter
		 * k: the number of hash functions to be applied
		 *  
		 * The formula:
		 * 
		 * m = -n * ln(p) / (ln(2)^2) 
		 * k = m/n * ln(2) 
		 * 
		 */
		
		double numberOfBitsRequiredForStorage = ((-1 * expectedItems) * Math.log(accepatableFalsePositivityRate)) / Math.pow(Math.log(2), 2);
		
		long numberOfHashFunctions = (long)Math.ceil((numberOfBitsRequiredForStorage/expectedItems) * Math.log(2));
		
		return new Tuple(numberOfBitsRequiredForStorage, numberOfHashFunctions);
		
		
	}

}
