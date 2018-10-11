package de.ricardo.genetic.darwin.modules;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Callable;

public class GenerationStepperInitialize implements Callable<List<Individuum>>{

	int populationSize;
	BufferedImage current;
	BufferedImage goal;

	public GenerationStepperInitialize(BufferedImage current, BufferedImage goal,
			int populationSize) {

		//Erstelle threadfähige Kopien
		this.current = new BufferedImage(current.getWidth(),current.getHeight() ,current.getType());
		this.current.setData(current.getData());

		this.goal = new BufferedImage(goal.getWidth(),goal.getHeight() ,goal.getType());
		this.goal.setData(goal.getData());

		this.populationSize = populationSize;
	}

	public List<Individuum> call() throws Exception {
		CreatePopulation createPopulation = new CreatePopulation();

		return createPopulation.initializePopulation(populationSize, goal);
	}

}
