package com.bloomfamily.bloomfilter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BloomFilterUtils {

	private static List<Integer> pseudoRandomNumbers = null;

	public static List<Integer> generatePseudoRandomNumbers(int numberOfRandomNumbers) {

		pseudoRandomNumbers = new ArrayList<>(numberOfRandomNumbers);

		Random random = new Random(System.currentTimeMillis());

		for (int i = 0; i < numberOfRandomNumbers; i++) {

			pseudoRandomNumbers.add(random.nextInt());

		}

		return pseudoRandomNumbers;

	}

}
