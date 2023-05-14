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
	
	private int countOfInsertedElements;
	
	
	public ClassicBloomFilter(long expectedItems, double accepatableFalsePositivityRate) {
		
		final Tuple tuple  = this.init(expectedItems, accepatableFalsePositivityRate);
		
		filter = new BitSet((int)tuple.numberOfBitsRequiredForStorage());
		
		generatePseudoRandomNumbers((int)tuple.numberOfHashFunctions());
		
	}

	@Override
	public int size() {
		
		return countOfInsertedElements;
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
	public void insert(BloomElement<?> element) {
		
		for (Integer seed : pseudoRandomNumbers) {
			
			filter.set(Math.abs(MurmurHash3.hash32(element.hashCode(), seed)) % filter.size()); // generate multiple hash
			
		}
		
		countOfInsertedElements++;

	}
	
	private void generatePseudoRandomNumbers(int numberOfRandomNumbers) {
		
		pseudoRandomNumbers = new ArrayList<>(numberOfRandomNumbers);
		
		Random random = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < numberOfRandomNumbers; i++) {
			
			pseudoRandomNumbers.add(random.nextInt());
			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		BloomElement<Integer > element = new BloomElement<>(100);
		BloomElement<Integer > element1 = new BloomElement<>(101);
		BloomElement<Integer > element2 = new BloomElement<>(200);
		BloomElement<Integer > element3 = new BloomElement<>(300);
		BloomElement<Integer > element4 = new BloomElement<>(300);
		BloomElement<Integer > element5 = new BloomElement<>(1400);
		
		ClassicBloomFilter f = new ClassicBloomFilter(6, 0.1);
		
		System.out.println("**");
//		f.insert(element);
//		f.insert(element1);
		f.insert(element2);
		f.insert(element3);
		//f.insert(element4);
		//f.insert(element5);
		
		
		System.out.println(f.probablyHas(element3));
		
		System.out.println(f.probablyHas(element5));
		
		System.out.println(f.size());
		
		
	}

}
