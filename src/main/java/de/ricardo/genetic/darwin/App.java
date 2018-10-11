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
import de.ricardo.genetic.darwin.utils.Cache;
import de.ricardo.genetic.darwin.utils.Renderer;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		int size_population = 20;
		int best_sample = 2;
		int number_of_child = size_population / best_sample;
		int number_of_generation = 5;
		int chance_of_mutation = 20;
		int numberOfRetries = 5;

		BufferedImage goal = null;
		try {
			goal = ImageIO.read(new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\largeGoal.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    

		Cache.getInstance().setGoalImage(goal);

		Fitness fitness = new Fitness();

		BufferedImage canvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 
		Graphics2D    graphics = canvas.createGraphics();

		//===============
		long sum_r = 0, sum_g = 0, sum_b = 0;
				
		for(int x = 0; x < goal.getWidth(); x++) {
			for(int y = 0; y < goal.getHeight(); y++) {
				
				int RGBColor = goal.getRGB(x, y);
				Color color = new Color(RGBColor);

				sum_r += color.getRed();
				sum_g += color.getBlue();
				sum_b += color.getGreen();			
			}
		}
		
		int size = goal.getWidth()*goal.getHeight();
		

			 Color color = new Color((int) (sum_r / size),
					(int) (sum_g / size),
					(int) (sum_b / size));	
		
		//=====================
		
		graphics.setColor(color);
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

				BufferedImage localCanvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 
				localCanvas.setData(canvas.getRaster());

				double fitnessValues[] = new double[population.size()];
				fitnessValues = fitness.calculateFitnessLinear(population, localCanvas, goal);
				for(int j=0; j<population.size(); j++) {
					population.get(j).fitness = fitnessValues[j];
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
