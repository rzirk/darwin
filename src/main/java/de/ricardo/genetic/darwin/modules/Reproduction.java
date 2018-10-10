package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.print.CancelablePrintJob;

import de.ricardo.genetic.darwin.utils.Cache;
import de.ricardo.genetic.darwin.utils.Renderer;
import de.ricardo.genetic.darwin.utils.ReproductionHelper;

public class Reproduction {
	

	public static Individuum createChild(Individuum parent1, Individuum parent2) {
		Individuum child = null;

		Point childrenStartingPoint = ReproductionHelper.mixPoints(parent1.getDNA().start,
				parent2.getDNA().start);
		Point childrenEndPoint = ReproductionHelper.mixPoints(parent1.getDNA().end,
				parent2.getDNA().end);
		
		//Nutze den gemittelten Farbwert aus dem Bildbereich
		Rectangle rect = new Rectangle(Renderer.DNARangeToImageRange(childrenStartingPoint, Cache.getInstance().goal));
		rect.add(Renderer.DNARangeToImageRange(childrenEndPoint, Cache.getInstance().goal));

		long sum_r = 0, sum_g = 0, sum_b = 0;
		
		int test[] = new  int[rect.width*rect.height];
		Cache.getInstance().goal.getRGB(rect.x, rect.y, rect.width, rect.height, test, 0, 1);
		
		for(int x = rect.x; x < rect.x + rect.width; x++) {
			for(int y = rect.y; y < rect.y + rect.height; y++) {
				
				int RGBColor = test[(y-rect.y) + (x-rect.x)];
				Color color = new Color(RGBColor);

				sum_r += color.getRed();
				sum_g += color.getBlue();
				sum_b += color.getGreen();			
			}
		}
		
		Color color = null;
		if(rect.width == 0 || rect.height == 0) {
			color = new Color(Cache.getInstance().goal.getRGB(
					Renderer.DNARangeToImageRange(childrenStartingPoint,Cache.getInstance().goal).x,
					Renderer.DNARangeToImageRange(childrenStartingPoint,Cache.getInstance().goal).y));
		} else {
			 color = new Color((int) (sum_r / (rect.width * rect.height)),
					(int) (sum_g / (rect.width * rect.height)),
					(int) (sum_b / (rect.width * rect.height)));	
		}
		
		child = new Individuum(childrenStartingPoint, childrenEndPoint,
				color);
		
		return child;
	}
	
	public static List<Individuum> createNextGeneration(List<Individuum> shuffledBreeders, int numberOfChildren) {
		List<Individuum> nextGeneration = new ArrayList<Individuum>();
		
		for (int i = 0; i < shuffledBreeders.size()/2; i++) {
			for(int j = 0; j < numberOfChildren; j++) {
				nextGeneration.add(createChild(shuffledBreeders.get(i), shuffledBreeders.get(i+1)));
			}
		}
		return nextGeneration;
	}
	
}
