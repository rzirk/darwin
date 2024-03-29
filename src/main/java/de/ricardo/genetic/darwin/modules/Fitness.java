package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.ricardo.genetic.darwin.utils.Renderer;

public class Fitness {
	
	Renderer renderer;
	
	public Fitness() {
		this.renderer = new Renderer();
	}

	public double calculateFitnessLinear(Individuum individuum, 
			BufferedImage currentDrawing,
			BufferedImage goal) {

		BufferedImage individuumRender = renderer.renderIndividuumToCanvas(individuum, currentDrawing);
		double fitness = 0.0;

		try {
			fitness = getGlobalFitness(individuumRender,goal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fitness;
	}

	public double getGlobalFitness(BufferedImage currentDrawing,
			BufferedImage goal) throws Exception
	{
		int difference = 0;

		if ((currentDrawing.getWidth() != goal.getWidth()) || currentDrawing.getHeight() != goal.getHeight()) {
			throw new Exception();
		}

		final WritableRaster inRaster     = goal.getRaster();
		final WritableRaster inRasterComp = currentDrawing.getRaster();

		int[] pixels = new int[3*goal.getWidth()];
		int[] pixel_compare = new int[3*currentDrawing.getWidth()];

		for(int y = 0; y < goal.getHeight(); y++) {

			pixels = inRaster.getPixels( 0, y, goal.getWidth(), 1, pixels );
			pixel_compare = inRasterComp.getPixels( 0, y, currentDrawing.getWidth(), 1, pixel_compare );

			for(int x = 0; x < goal.getWidth(); x++) {
				int m = x*3; 	;

				difference += Math.abs(pixels[m+0] - pixel_compare[m]); //Rot 
				difference += Math.abs(pixels[m+1] - pixel_compare[m+1]);//Grün 
				difference += Math.abs(pixels[m+2] - pixel_compare[m+1]);//Blau
			}
		}

		// Total number of red pixels = width * height 
		// Total number of blue pixels = width * height 
		// Total number of green pixels = width * height 
		// So total number of pixels = width * height * 3 
		double total_pixels = goal.getWidth() * goal.getHeight() * 3; 

		// Normalizing the value of different pixels 
		// for accuracy(average pixels per color 
		// component) 
		double avg_different_pixels = difference / 
				total_pixels; 

		// There are 255 values of pixels in total 
		double percentage = (avg_different_pixels / 
				255) * 100; 

		return  100 - percentage;
	}
	
	public static double[] concatAll(double[] first, double[]... rest) {
		  int totalLength = first.length;
		  for (double[] array : rest) {
		    totalLength += array.length;
		  }
		  double[] result = Arrays.copyOf(first, totalLength);
		  int offset = first.length;
		  for (double[] array : rest) {
		    System.arraycopy(array, 0, result, offset, array.length);
		    offset += array.length;
		  }
		  return result;
		}
}
