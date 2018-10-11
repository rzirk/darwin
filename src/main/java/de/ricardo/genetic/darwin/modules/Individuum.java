package de.ricardo.genetic.darwin.modules;

import java.awt.Color;
import java.awt.Point;

public class Individuum implements Comparable<Individuum>{

	/*public class BrushDNA {
		public Point start;
		public Point end;
		public int brushSize;
		public Color brushColor;
	}*/

	public class CubeDNA { 
		public Point start; //Die Punkte müssen auf die Bildgröße gemappt werden
		public Point end;
		
		public boolean compare(CubeDNA dna1, CubeDNA dna2) {
			boolean result = true;
			
			if(!dna1.start.equals(dna2.start)) {
				result = false;
			}
			
			if(!dna1.end.equals(dna2.end)) {
				result = false;
			}
			
			return result;
		}
	}
	
	private CubeDNA DNA;
	public double fitness;

	public CubeDNA getDNA() {
		return DNA;
	}

	public void setDNA(CubeDNA dNA) {
		DNA = dNA;
	}
	
	public Individuum(Point startPoint, Point endPoint) {
		
		DNA = new CubeDNA();
		
		DNA.start = startPoint;
		DNA.end = endPoint;
		
		fitness = -1.0;
	}

    public int compareTo(Individuum o){
		
		if (this.fitness > o.fitness) {
			return -1;
		}
		if (this.fitness == o.fitness) {
			return 0;
		}
		if (this.fitness < o.fitness) {
			return 1;
		}
		
		return 0;
	}
}
