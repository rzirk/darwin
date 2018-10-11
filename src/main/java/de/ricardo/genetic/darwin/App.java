package de.ricardo.genetic.darwin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
		int size_population = 120;
		int best_sample = 12;
		int lucky_few = 10;
		int number_of_child = size_population / best_sample;
		int number_of_generation = 10;
		int chance_of_mutation = 20;

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

		graphics.setColor(Color.MAGENTA);
		graphics.fillRect ( 0, 0, canvas.getWidth(), canvas.getHeight() );
		double alikness = 0.0;
		BufferedImage prevCanvas = null;

		CreatePopulation createPopulation = new CreatePopulation();
		//List<Individuum> population = createPopulation.initializePopulation(size_population, goal);

		int k = 0;
		double delta = 1.0;
		double prevoriousFitness = 0.0;
		Renderer renderer = new Renderer();
		GenerationStepper generationStepper = new GenerationStepper();

		while (alikness < 95.0) {//Komplettes Bild
			k++;
			List<Individuum> population = new ArrayList<Individuum>();
			try {
				population = generationStepper.startNextGeneration(
						canvas, goal, size_population, best_sample, chance_of_mutation, number_of_generation);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			//Render best solution
			prevCanvas = canvas;
			canvas = renderer.renderIndividuumToCanvas(population.get(population.size()-1), canvas);
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
