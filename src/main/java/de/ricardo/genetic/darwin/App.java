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
		int size_population = 100;
		int best_sample = 10;
		int number_of_generation = 10;
		int chance_of_mutation = 5;

		BufferedImage goal = null;
		try {
			goal = ImageIO.read(new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\largeGoal.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    

		Cache.getInstance().setGoalImage(goal);

		BufferedImage canvas = new BufferedImage(goal.getWidth(), goal.getHeight(), goal.getType()); 
		Graphics2D    graphics = canvas.createGraphics();

		//===============
		long sum_r = 0, sum_g = 0, sum_b = 0;
				
		for(int x = 0; x < goal.getWidth(); x++) {
			for(int y = 0; y < goal.getHeight(); y++) {
				
				int RGBColor = goal.getRGB(x, y);
				Color color = new Color(RGBColor);

				sum_r += color.getRed();
				sum_g += color.getGreen();
				sum_b += color.getBlue();			
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

		Renderer renderer = new Renderer();
		GenerationStepper generationStepper = new GenerationStepper();

		int generationCounter = 0;
		while (alikness < 90.0) {//Komplettes Bild
			generationCounter++;
			List<Individuum> population = new ArrayList<Individuum>();
			try {
				population = generationStepper.startNextGeneration(
						canvas, goal, size_population, best_sample, chance_of_mutation, number_of_generation);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("Iteration: "+generationCounter);
			System.out.println("Best Fitness: "+population.get(population.size()-1).fitness);
			System.out.println("Worst Fitness: "+population.get(0).fitness);
			System.out.println();

			//Render best solution
			canvas = renderer.renderIndividuumToCanvas(population.get(population.size()-1), canvas);
			alikness = population.get(population.size()-1).fitness;

			String test = "C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\test\\Testrender\\outputGen"+generationCounter+".jpg";
			try {
				ImageIO.write(canvas, "jpg", new File(test));

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
