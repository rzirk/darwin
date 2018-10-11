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

			// get the Widget, if the Callable was able to create it
			try {
				Populations.addAll(completedFuture.get());
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				continue;
			}
		}
		//Alle Individuuen wurden initialisiert

		for(int i = 0; i < iterations; i++) {

			remainingFutures = 0;

			for (int j = 0; j < numberOfCPUCores; j++) {
				remainingFutures++;
				int range = j*(populationSize/numberOfCPUCores);
				compService.submit(new GenerationStepperCalculateFitness(current, goal, Populations.subList(range,range+populationSize/numberOfCPUCores)));
			}
			
			List<Individuum> populations2 = new ArrayList<Individuum>();

			while (remainingFutures > 0) {
				// block until a callable completes
				completedFuture = compService.take();
				remainingFutures--;

				// get the Widget, if the Callable was able to create it
				try {
					populations2.addAll(completedFuture.get());
				} catch (ExecutionException e) {
					Throwable cause = e.getCause();
					continue;
				}
			}

			Selection selection = new Selection();
			populations2 = selection.sortPopulationByFitness(populations2);

			if (i <= iterations-1) {

				//Reproduction
				Reproduction reproduction = new Reproduction();
				populations2 = reproduction.createNextGeneration(
						selection.selectFromPopulationBestOnes(populations2, numberOfBestSamples*2),
						numberOfChildren);

				//Mutation
				Mutation mutation = new Mutation();
				populations2 = mutation.mutatePopulation(populations2, mutationChance);

			} else {
				Populations = populations2;
			}
		}
		
		return Populations;
	}

}
