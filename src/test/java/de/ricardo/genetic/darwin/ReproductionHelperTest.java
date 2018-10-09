package de.ricardo.genetic.darwin;

import java.awt.Color;
import java.awt.Point;

import de.ricardo.genetic.darwin.utils.ReproductionHelper;
import junit.framework.TestCase;

public class ReproductionHelperTest extends TestCase {

	ReproductionHelper helper = new ReproductionHelper();

	public void testMixPoints() {
		Point point1 = new Point(10,5);
		Point point2 = new Point(10,5);

		assertEquals(point1.getX(),helper.mixPoints(point1, point2).getX());
		assertEquals(point1.getY(),helper.mixPoints(point1, point2).getY());
	}

	public void testMixInteger() {

		assertEquals(20,helper.mixInteger(20,20));
		boolean changedSign = false;

		for (int i = 0; i < 100; i++) {
			if(helper.mixInteger(423424, 23422) < 0) {
				changedSign = true;
			}
		}

		assertEquals(false, changedSign);
	}

	public void testMixColors() {
		Color color1 = new Color(50, 50, 50);
		Color color2 = new Color(50, 50, 50);

		assertEquals(color1.getRed(), helper.mixColors(color1, color2).getRed());
		assertEquals(color1.getGreen(), helper.mixColors(color1, color2).getGreen());
		assertEquals(color1.getBlue(), helper.mixColors(color1, color2).getBlue());
	}

}
