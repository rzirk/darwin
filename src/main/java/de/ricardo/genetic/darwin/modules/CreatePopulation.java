package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import de.ricardo.genetic.darwin.utils.Cache;
import de.ricardo.genetic.darwin.utils.Renderer;

public class CreatePopulation {
	
Renderer renderer;
	
	public CreatePopulation() {
		this.renderer = new Renderer();
	}
	
	public List<Individuum> initializePopulation(int populationSize, BufferedImage goal) {
		List<Individuum> individuals = new ArrayList<Individuum>();

		for (int i = 0; i < populationSize; i++) {
			individuals.add(initializeIndividuum(goal));
		}

		return individuals;
	}

	public Individuum initializeIndividuum(BufferedImage goal) {

		//Wähle zufällig Punkte im Bild aus nach denen Initialisiert wird
		int randomXStart, randomYStart;
		int randomXEnd, randomYEnd;

		Random rand = new Random();
		randomXStart = rand.nextInt(goal.getWidth());
		randomYStart = rand.nextInt(goal.getHeight());
		randomXEnd = rand.nextInt(goal.getWidth());
		randomYEnd = rand.nextInt(goal.getHeight());

		Point startPoint = renderer.ImageRangeToDNARange(new Point(randomXStart, randomYStart),
				goal);
		Point endPoint = renderer.ImageRangeToDNARange(new Point(randomXEnd, randomYEnd),
				goal);

		return new Individuum(startPoint, endPoint);
	}
}
