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
	public List<Individuum> initializePopulation(int populationSize, BufferedImage goal) {
		List<Individuum> individuals = new ArrayList<Individuum>();
		//BufferedImage canvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 

		for (int i = 0; i < populationSize; i++) {
			individuals.add(initializeIndividuum(goal));
			//Debug
			/*canvas = Renderer.renderIndividuumToCanvas(individuals.get(i), canvas);
			String test = "C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\test\\firstPopulation\\test_"+i+".jpg";

			try {
				ImageIO.write(canvas, "jpg", new File(test));
			}catch(IOException exception) {
				exception.printStackTrace();
			}*/
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

		Point startPoint = Renderer.ImageRangeToDNARange(new Point(randomXStart, randomYStart),
				goal);
		Point endPoint = Renderer.ImageRangeToDNARange(new Point(randomXEnd, randomYEnd),
				goal);

		return new Individuum(startPoint, endPoint);
	}
}
