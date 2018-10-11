package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import de.ricardo.genetic.darwin.utils.Cache;
import de.ricardo.genetic.darwin.utils.Renderer;
import de.ricardo.genetic.darwin.utils.ReproductionHelper;

public class Reproduction {
	
	private Renderer renderer;
	
	public Reproduction() {
		this.renderer = new Renderer();
	}

	public Individuum createChild(Individuum parent1, Individuum parent2) {
		Individuum child = null;

		Point childrenStartingPoint = ReproductionHelper.mixPoints(parent1.getDNA().start,
				parent2.getDNA().start);
		Point childrenEndPoint = ReproductionHelper.mixPoints(parent1.getDNA().end,
				parent2.getDNA().end);
		
		child = new Individuum(childrenStartingPoint, childrenEndPoint);
		
		return child;
	}
	
	public List<Individuum> createNextGeneration(List<Individuum> shuffledBreeders, int numberOfChildren) {
		List<Individuum> nextGeneration = new ArrayList<Individuum>();
		
		for (int i = 0; i < shuffledBreeders.size()/2; i++) {
			for(int j = 0; j < numberOfChildren; j++) {
				nextGeneration.add(createChild(shuffledBreeders.get(i), shuffledBreeders.get(i+1)));
			}
		}
		return nextGeneration;
	}
	
}
