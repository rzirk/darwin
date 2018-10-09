package de.ricardo.genetic.darwin;

import java.awt.Color;
import java.awt.Point;

import de.ricardo.genetic.darwin.modules.Individuum;
import de.ricardo.genetic.darwin.modules.Individuum.CubeDNA;
import de.ricardo.genetic.darwin.modules.Reproduction;
import junit.framework.TestCase;

public class ReproductionTest extends TestCase {

	public void testCreateChild() {
		Individuum parent1 = new Individuum(new Point(10, 50), new Point(20, 90),
				new Color(32, 23, 100));
		Individuum parent2 = new Individuum(new Point(10, 50), new Point(20, 90),
				new Color(32, 23, 100));
		
		Reproduction reproduction = new Reproduction();
		
		Individuum child = reproduction.createChild(parent1, parent2);
		
		CubeDNA comp = parent1.getDNA();
		
		assertEquals(true,comp.compare(parent1.getDNA(), child.getDNA()));
	}

}
