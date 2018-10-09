package de.ricardo.genetic.darwin;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.ricardo.genetic.darwin.modules.Fitness;
import de.ricardo.genetic.darwin.modules.Individuum;
import de.ricardo.genetic.darwin.utils.Renderer;
import junit.framework.TestCase;

public class RendererTest extends TestCase {
	
	public void testRanging() {
		
		BufferedImage canvas = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
		
		Point startingPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point endPoint = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		Point middlePoint = new Point(0, 0);
		
		Point imageRangeMax = Renderer.DNARangeToImageRange(startingPoint, canvas);
		Point imageRangeMin = Renderer.DNARangeToImageRange(endPoint, canvas);
		Point imageRangeMiddle = Renderer.DNARangeToImageRange(middlePoint, canvas);

		
		assertEquals(new Point(400,400), imageRangeMax);
		assertEquals(new Point(200,200), imageRangeMiddle);
		assertEquals(new Point(0,0), imageRangeMin);
		
		Point resultMax = Renderer.ImageRangeToDNARange(imageRangeMax, canvas);
		Point resultMin = Renderer.ImageRangeToDNARange(imageRangeMin, canvas);
		Point resultMiddle = Renderer.ImageRangeToDNARange(imageRangeMiddle, canvas);

		//assertEquals(startingPoint, resultMax);
		//assertEquals(endPoint, resultMin);
		assertEquals(middlePoint, resultMiddle);

	}

	public void testRenderIndividuumToCanvas() {
		//Beginne oben links, und gehe bis zur Mitte unten
		Point startPoint = new Point(Integer.MIN_VALUE, Integer.MAX_VALUE);
		Point endPoint = new Point(0, Integer.MIN_VALUE);
		Color color = Color.red;

		BufferedImage canvas = new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR);
		Individuum individuum = new Individuum(startPoint, endPoint, color);
		 canvas = Renderer.renderIndividuumToCanvas(individuum, canvas);
		 
		 Fitness fitness = new Fitness();
		 try {
			ImageIO.write(canvas, "jpg", new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\test.jpg"));
			
		    BufferedImage goal = ImageIO.read(new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\testGoalRedSquare.jpg"));
			assertEquals(100, fitness.getGlobalFitness(canvas, goal));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}