/**
 * 
 */
package de.ricardo.genetic.darwin;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import de.ricardo.genetic.darwin.modules.Fitness;
import junit.framework.TestCase;

/**
 * @author ricardo
 *
 */
public class FitnessTest extends TestCase {

	/**
	 * Test method for {@link de.ricardo.genetic.darwin.modules.Fitness#getGlobalFitness(java.awt.image.BufferedImage, java.awt.image.BufferedImage)}.
	 */
	public void testGetGlobalFitness() {
        try {
       BufferedImage current = ImageIO.read(new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\current.jpg"));
       BufferedImage goal = ImageIO.read(new File("C:\\Users\\ricardo\\Desktop\\genetischeAlgorithmen\\workspace\\darwin\\goal.jpg"));
       
       Fitness fitness = new Fitness();
		double percentage = fitness.getGlobalFitness(current, goal);
		
		assertEquals(50.0, percentage);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
