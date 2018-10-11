package de.ricardo.genetic.darwin.modules;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GenerationStepper {
	
	private ExecutorCompletionService executorCompletionService;
	int numberOfCPUCores = 4;
	
	public GenerationStepper() {
		this.executorCompletionService =  new ExecutorCompletionService(
				Executors.newFixedThreadPool(numberOfCPUCores));
	}

	public List<Individuum> startNextGeneration(BufferedImage current, BufferedImage goal,
			int populationSize, int numberOfBestSamples, int mutationChance, int iterations) throws InterruptedException {

		int numberOfChildren = populationSize / numberOfBestSamples;

		List<Individuum> Populations = new ArrayList<Individuum>();

		// service that wraps a thread pool with n threads
		CompletionService<List<Individuum>> compService = executorCompletionService;

		// how many futures there are to check
		int remainingFutures = 0;

		for (int i = 0; i < numberOfCPUCores; i++) {
			remainingFutures++;
			compService.submit(new GenerationStepperInitialize(current, goal, populationSize/numberOfCPUCores));
		}

		Future<List<Individuum>> completedFuture;

		while (remainingFutures > 0) {
			// block until a callable completes
			completedFuture = compService.take();
			remainingFutures--;

			try {
				Populations.addAll(completedFuture.get());
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				continue;
			}
		}
		//Alle Individuuen wurden initialisiert
		
		Selection selection = new Selection();
		Reproduction reproduction = new Reproduction();
		Mutation mutation = new Mutation();
		List<Individuum> populations2 = new ArrayList<Individuum>();


		for(int i = 0; i < iterations; i++) {

			populations2.clear();
			remainingFutures = 0;

			for (int j = 0; j < numberOfCPUCores; j++) {
				remainingFutures++;
				int range = j*(populationSize/numberOfCPUCores);
				compService.submit(new GenerationStepperCalculateFitness(current, goal, Populations.subList(range,range+populationSize/numberOfCPUCores)));
			}
			

			while (remainingFutures > 0) {
				// block until a callable completes
				completedFuture = compService.take();
				remainingFutures--;

				try {
					populations2.addAll(completedFuture.get());
				} catch (ExecutionException e) {
					Throwable cause = e.getCause();
					continue;
				}
			}

			populations2 = selection.sortPopulationByFitness(populations2);

			if (i < iterations-1) {
				
				//Reproduction
				populations2 = reproduction.createNextGeneration(
						selection.selectFromPopulationBestOnes(populations2, numberOfBestSamples*2),
						numberOfChildren);

				//Mutation
				populations2 = mutation.mutatePopulation(populations2, mutationChance);
			}
		}
		
		return populations2;
	}

}
