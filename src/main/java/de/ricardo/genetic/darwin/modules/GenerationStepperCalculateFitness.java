package de.ricardo.genetic.darwin.modules;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Callable;

public class GenerationStepperCalculateFitness implements Callable<List<Individuum>>{

	List<Individuum> population;
	BufferedImage current;
	BufferedImage goal;

	public GenerationStepperCalculateFitness(BufferedImage current, BufferedImage goal,
			List<Individuum> population) {

		//Erstelle threadfähige Kopien
		this.current = new BufferedImage(current.getWidth(),current.getHeight() ,current.getType());
		this.current.setData(current.getData());

		this.goal = new BufferedImage(goal.getWidth(),goal.getHeight() ,goal.getType());
		this.goal.setData(goal.getData());

		this.population = population;
	}

	public List<Individuum> call() throws Exception {

		Fitness fitness = new Fitness();

		for(Individuum individuum : population) {
			//CalculateFitness
			BufferedImage localCanvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 
			localCanvas.setData(current.getRaster());

			individuum.fitness = fitness.calculateFitnessLinear(individuum, localCanvas, goal);
		}

		return population;
	}

}
