package com.bloomfamily.bloomfilter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.MurmurHash3;

import com.bloomfamily.bloomfilter.interfaces.BloomFilter;
import com.bloomfamily.bloomfilter.obj.BloomElement;
import com.bloomfamily.bloomfilter.obj.Tuple;

public class CountingBloomFilter implements BloomFilter {
	
	
	private byte [] filter = null;
	
	private List<Integer> pseudoRandomNumbers;
	
	private int filterSize;
	
	public CountingBloomFilter(long expectedItems, double accepatableFalsePositivityRate) {
		
		final Tuple tuple  = this.initialize(expectedItems, accepatableFalsePositivityRate);
		
		filter = new byte[(int)tuple.numberOfBitsRequiredForStorage()];
		
		pseudoRandomNumbers = BloomFilterUtils.generatePseudoRandomNumbers((int)tuple.numberOfHashFunctions());
		
	}

	@Override
	public int size() {

		return filterSize;
	}

	@Override
	public boolean probablyHas(BloomElement<?> element) {
		
		for (Integer seed : pseudoRandomNumbers) {
			
			if (filter[Math.abs(MurmurHash3.hash32(element.hashCode(), seed)) % filter.length] == 0) {
				
				return false;
			}
			
		}
		
		return true;
	}

	@Override
	public boolean insert(BloomElement<?> element) {
		

		for (Integer seed : pseudoRandomNumbers) {
			
			final int hashedIndex = (Math.abs(MurmurHash3.hash32(element.hashCode(), seed)) % filter.length);
			
			if (filter[hashedIndex] < 127) {
			
				filter[hashedIndex] ++; // add 1 to counter byte array
				
			} else {
				
				return false; //Unable to add element, as slot is full
			}
		}
		
		filterSize++;
		return true;
	}
	
	public boolean delete(BloomElement<?> element) {
		
		List<Integer> indexes2Delete = new ArrayList<>();
		
		for (Integer seed : pseudoRandomNumbers) {
			
			final int hashedIndex = (Math.abs(MurmurHash3.hash32(element.hashCode(), seed)) % filter.length);
			
			if (filter[hashedIndex] > 0) { // element exist
				
				indexes2Delete.add(hashedIndex);
				
			} else {
				
				return false; // element does not exist
			}
			
		}
		
		for (Integer hashedIndex : indexes2Delete) {
			
			filter[hashedIndex]--;
		}
		
		filterSize--;
		return true; // deletion successful
		
	}
	
}
