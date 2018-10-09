package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import com.arturmkrtchyan.sizeof4j.SizeOf;

public class Mutation {

	public static Individuum mutateIndividuum(Individuum individuum) {
		Random rand = new Random();
		int genomMutation = rand.nextInt(3);

		int xOrY = rand.nextInt(2); //X is 0, Y is 1

		switch (genomMutation) {
		case 0: //Startpoint
			if (xOrY == 0) { //X
				int newPoint = individuum.getDNA().start.x;
				individuum.getDNA().start.x = mutateIntBitwise(newPoint);
			} else {
				int newPoint = individuum.getDNA().start.y;
				individuum.getDNA().start.y = mutateIntBitwise(newPoint);
			}
			break;
		case 1: //Endpoint
			if (xOrY == 0) { //X
				int newPoint = individuum.getDNA().end.x;
				individuum.getDNA().end.x = mutateIntBitwise(newPoint);
			} else {
				int newPoint = individuum.getDNA().end.y;
				individuum.getDNA().end.y = mutateIntBitwise(newPoint);
			}
			break;
		case 2: //Color
			int newColor = individuum.getDNA().Color.getRGB();
			newColor = mutateIntBitwise(newColor);

			int  red   = (newColor & 0x00ff0000) >> 16;
			int  green = (newColor & 0x0000ff00) >> 8;
			int  blue  =  newColor & 0x000000ff;

			individuum.getDNA().Color = new Color(red, green, blue);

			break;
		}

		return individuum;
	}

	public static List<Individuum> mutatePopulation(List<Individuum> population, int chanceOfMutation) {
		Random rand = new Random();
		
		for(int i = 0; i < population.size(); i++) {
			if (rand.nextInt(100) < chanceOfMutation) {
				Individuum mutator = population.get(i);
				population.set(i,mutateIndividuum(mutator));
			}
		}
		
		return population;
	}

	private static int mutateIntBitwise(int mutator) {

		int numberOfBits = SizeOf.intSize()*8;
		Random rand = new Random();

		mutator = mutator ^ (int)Math.pow(rand.nextInt(numberOfBits),2);

		return mutator;
	}

}
