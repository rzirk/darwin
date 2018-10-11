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

			double fitness = 0;
			float deltaRed = 0;
			float deltaGreen = 0;
			float deltaBlue = 0;
			float pixelFitness = 0;

			for(int y = 0; y < goal.getHeight(); y++) {

				pixels = inRaster.getPixels( 0, y, goal.getWidth(), 1, pixels );
				pixel_compare = inRasterComp.getPixels( 0, y, currentDrawing.getWidth(), 1, pixel_compare );

				for(int x = 0; x < goal.getWidth(); x++) {
					int m = x*3;

					deltaRed = pixels[m+0] - pixel_compare[m]; //Rot 
					deltaGreen = pixels[m+1] - pixel_compare[m+1];//Grün 
					deltaBlue = pixels[m+2] - pixel_compare[m+1];//Blau

					pixelFitness = deltaRed * deltaRed +
							deltaGreen * deltaGreen +
							deltaBlue * deltaBlue;

					//add the pixel fitness to the total fitness ( lower is better )
					fitness += Math.sqrt(pixelFitness);
				}


			}

			results[i] = fitness;

		}

		return results;
	}

	public double[] call() throws Exception {

		return getGlobalFitness(currentDrawings, goal);
	}
}
