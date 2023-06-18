package com.bloomfamily.bloomfilter.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.MurmurHash3;

import com.bloomfamily.bloomfilter.interfaces.BloomFilter;
import com.bloomfamily.bloomfilter.obj.BloomElement;
import com.bloomfamily.bloomfilter.obj.Tuple;

public class ClassicBloomFilter implements BloomFilter {
	
	private BitSet filter = null;
	
	private List<Integer> pseudoRandomNumbers;
	
	private int filterSize;
	
	
	public ClassicBloomFilter(long expectedItems, double accepatableFalsePositivityRate) {
		
		final Tuple tuple  = this.initialize(expectedItems, accepatableFalsePositivityRate);
		
		filter = new BitSet((int)tuple.numberOfBitsRequiredForStorage());
		
		pseudoRandomNumbers = BloomFilterUtils.generatePseudoRandomNumbers((int)tuple.numberOfHashFunctions());
		
	}

	@Override
	public int size() {
		
		return filterSize;
	}

	@Override
	public boolean probablyHas(BloomElement<?> element) {
		
		for (Integer seed : pseudoRandomNumbers) {
			
			if (!filter.get(Math.abs(MurmurHash3.hash32(element.hashCode(), seed)) % filter.size())) {
				
				return false;
			}
			
		}
		
		return true;
	}

	@Override
	public boolean insert(BloomElement<?> element) {
		
		for (Integer seed : pseudoRandomNumbers) {
			
			filter.set(Math.abs(MurmurHash3.hash32(element.hashCode(), seed)) % filter.size()); // generate multiple hash
			
		}
		
		filterSize++;
		
		return true;

	}
	
}
