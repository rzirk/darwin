package de.ricardo.genetic.darwin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import com.arturmkrtchyan.sizeof4j.SizeOf;

import de.ricardo.genetic.darwin.modules.*;
import de.ricardo.genetic.darwin.utils.Renderer;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		int size_population = 100;
		int best_sample = 10;
		int lucky_few = 10;
		int number_of_child = size_population / best_sample;
		int number_of_generation = 10;
		int chance_of_mutation = 20;
		int numberOfRetries = 5;

		BufferedImage goal = null;
		try {
			goal = ImageIO.read(new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\largeGoal.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    

		Fitness fitness = new Fitness();

		BufferedImage canvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 
		Graphics2D    graphics = canvas.createGraphics();

		graphics.setColor(Color.MAGENTA);
		graphics.fillRect ( 0, 0, canvas.getWidth(), canvas.getHeight() );
		double alikness = 0.0;
		BufferedImage prevCanvas = null;

		CreatePopulation createPopulation = new CreatePopulation();
		//List<Individuum> population = createPopulation.initializePopulation(size_population, goal);

		int k = 0;
		double delta = 1.0;
		double prevoriousFitness = 0.0;

		while (alikness < 95.0) {//Komplettes Bild
			k++;
			List<Individuum> population = createPopulation.initializePopulation(size_population, goal);

			int retryCounter = 0;
			for(int i = 0; i < number_of_generation; i++) {

				if (retryCounter == numberOfRetries) {
					break;
				}

				//CalculateFitness
				for (int j = 0; j < population.size(); j++) {

					BufferedImage localCanvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 
					localCanvas.setData(canvas.getRaster());

					//if(i == 0) {
					population.get(j).fitness = fitness.calculateFitnessLinear(population.get(j),
							localCanvas, goal); 
					/*}
					else {
						population.get(j).fitness = fitness.calculateFitnessLinearComparision(population.get(j),
								localCanvas, prevCanvas, goal);
					}*/
				}
				//SortByFitness [Selection] (High index is better)
				population = Selection.sortPopulationByFitness(population);

				System.out.println("Highest fitness gen. "+(i+1)+": "+population.get(population.size()-1).fitness);

				delta = population.get(population.size()-1).fitness - prevoriousFitness;
				if(delta == 0.0) {
					retryCounter++;
				} else {
					retryCounter = 0;
				}

				prevoriousFitness = population.get(population.size()-1).fitness;
				//Reproduction
				population = Reproduction.createNextGeneration(
						Selection.selectFromPopulationBestOnes(population, best_sample*2),
						number_of_child);
				//Mutation
				population = Mutation.mutatePopulation(population, chance_of_mutation);
			}
			//Render best solution
			prevCanvas = canvas;
			canvas = Renderer.renderIndividuumToCanvas(population.get(population.size()-1), canvas);
			String test = "C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\test\\Testrender\\outputGen"+k+".jpg";
			try {
				ImageIO.write(canvas, "jpg", new File(test));
				alikness = fitness.getGlobalFitness(canvas, goal);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}