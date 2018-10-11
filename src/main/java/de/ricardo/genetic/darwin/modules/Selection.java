package de.ricardo.genetic.darwin.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Selection {

	public List<Individuum> sortPopulationByFitness(List<Individuum> population) {
		
		Collections.sort(population);
		return population;
	}
	
	public List<Individuum> selectFromPopulation(List<Individuum> sortedPoulation, int numberOfBestSamples,
			int numberOfLuckeFew) {
		List<Individuum> breeders = new ArrayList<Individuum>();
		
		for (int i = 1; i <= numberOfBestSamples; i++) {
			breeders.add(sortedPoulation.get(sortedPoulation.size() - i));
		}
		
		Random rand = new Random();
		for (int i = 0; i < numberOfLuckeFew; i++) {
			breeders.add(sortedPoulation.get(
					rand.nextInt(sortedPoulation.size())));
		}
		
		Collections.shuffle(breeders);
		
		return breeders;
	}
	
	public List<Individuum> selectFromPopulationBestOnes(List<Individuum> sortedPoulation, int numberOfBestSamples) {
		List<Individuum> breeders = new ArrayList<Individuum>();
		
		for (int i = 1; i <= numberOfBestSamples; i++) {
			breeders.add(sortedPoulation.get(sortedPoulation.size() - i));
		}
		
		Collections.shuffle(breeders);
		
		return breeders;
	}
}
