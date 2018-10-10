package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.ricardo.genetic.darwin.utils.Cache;
import de.ricardo.genetic.darwin.utils.Renderer;

public class Fitness {

	public double calculateFitnessLinear(Individuum individuum, 
			BufferedImage currentDrawing,
			BufferedImage goal) {

		BufferedImage individuumRender = Renderer.renderIndividuumToCanvas(individuum, currentDrawing);
		double fitness = 0.0;

		try {
			//fitness = getLocalFitness(individuum, individuumRender,goal);
			fitness = getGlobalFitness(individuumRender,goal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fitness;
	}

	public double calculateFitnessLinearComparision(Individuum individuum, 
			BufferedImage currentDrawing,
			BufferedImage prevoriousDrawing,
			BufferedImage goal) {

		BufferedImage individuumRender = Renderer.renderIndividuumToCanvas(individuum, currentDrawing);
		double fitness = 0.0;

		try {
			//fitness = getLocalFitness(individuum, individuumRender,goal);
			fitness = getGlobalFitnessWithCompare(prevoriousDrawing, currentDrawing, goal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fitness;
	}

	public double getLocalFitness(Individuum individuum,
			BufferedImage currentDrawing,
			BufferedImage goal) {

		Rectangle rect = new Rectangle(Renderer.DNARangeToImageRange(
				individuum.getDNA().start,goal));
		rect.add(Renderer.DNARangeToImageRange(individuum.getDNA().end,goal));

		double identicalPixel = 0;

		for(int x = 0; x < goal.getWidth(); x++) {
			for(int y = 0; y < goal.getHeight(); y++) {
				if((x >= rect.x && x < rect.x + rect.width) &&
						(y >= rect.y && y < rect.y + rect.height)) {

					if((currentDrawing.getRGB(x, y) == goal.getRGB(x, y)) && currentDrawing.getRGB(x, y) != 0) {
						identicalPixel++;
					}
				}
			}
		}

		return identicalPixel;
	}
	/*
	public double getGlobalFitness(BufferedImage currentDrawing,
			BufferedImage goal) throws Exception
	{
		int identicalPixel = 0;

		if ((currentDrawing.getWidth() != goal.getWidth()) || currentDrawing.getHeight() != goal.getHeight()) {
			throw new Exception();
		}

		for(int x = 0; x < goal.getWidth(); x++) {
			for(int y = 0; y < goal.getHeight(); y++) {
				if((currentDrawing.getRGB(x, y) == goal.getRGB(x, y)) && currentDrawing.getRGB(x, y) != 0) {
					identicalPixel++;
				}
			}
		}

		return (identicalPixel / (double) (goal.getWidth() * goal.getHeight())) * 100.0;
	}*/

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

	public double getGlobalFitnessWithCompare(
			BufferedImage prevoriousDrawing,
			BufferedImage currentDrawing,
			BufferedImage goal) throws Exception
	{
		int difference = 0;

		if ((currentDrawing.getWidth() != goal.getWidth()) || currentDrawing.getHeight() != goal.getHeight()) {
			throw new Exception();
		}

		for(int x = 0; x < goal.getWidth(); x++) {
			for(int y = 0; y < goal.getHeight(); y++) {
				int rgbA = goal.getRGB(x, y); 
				int rgbB = currentDrawing.getRGB(x, y);
				int rgbC = prevoriousDrawing.getRGB(x, y);

				//Penalty, es müssen sich möglichst viele Dinge ändern
				if(rgbB == rgbC) {
					difference++;
					continue;
				}

				if(rgbB == Color.MAGENTA.getRGB()) {
					difference++;
					continue;
				}

				int redA = (rgbA >> 16) & 0xff; 
				int greenA = (rgbA >> 8) & 0xff; 
				int blueA = (rgbA) & 0xff; 
				int redB = (rgbB >> 16) & 0xff; 
				int greenB = (rgbB >> 8) & 0xff; 
				int blueB = (rgbB) & 0xff; 
				difference += Math.abs(redA - redB); 
				difference += Math.abs(greenA - greenB); 
				difference += Math.abs(blueA - blueB);
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

		return percentage;
	}

}
