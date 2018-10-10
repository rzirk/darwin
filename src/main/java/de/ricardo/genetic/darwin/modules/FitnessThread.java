package de.ricardo.genetic.darwin.modules;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

public class FitnessThread implements Callable<double[]> {

	private List<BufferedImage> currentDrawings;
	private BufferedImage goal;

	public FitnessThread(List<BufferedImage> currentDrawings,
			BufferedImage goal) {
		this.currentDrawings = currentDrawings;
		this.goal = goal;
	}
	
	public double[] getGlobalFitness(List<BufferedImage> currentDrawings,
			BufferedImage goal) throws Exception
	{
		double results[] = new double[currentDrawings.size()];
		
		for (int i = 0; i < currentDrawings.size(); i++) {
		
		BufferedImage currentDrawing = currentDrawings.get(i);
		
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
				int m = x*3;
				
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
		
		results[i] = 100 - percentage;
		
		}

		return results;
	}

	public double[] call() throws Exception {

		return getGlobalFitness(currentDrawings, goal);
	}
}
