package de.ricardo.genetic.darwin.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Cache{

	public BufferedImage goal;
	public int RGBValues[][];
	private static Cache single_instance = null; 

	public Cache() {
	}

	public void setGoalImage(BufferedImage goal) {
		this.goal = goal;
		RGBValues = new int[goal.getWidth()][goal.getHeight()];

		for(int x = 0; x < goal.getWidth(); x++) {
			for(int y = 0; y < goal.getHeight(); y++) {
				RGBValues[x][y] = goal.getRGB(x, y);
			}
		}
	}

	// static method to create instance of Singleton class 
	public static Cache getInstance() 
	{ 
		if (single_instance == null) 
			single_instance = new Cache(); 

		return single_instance; 
	} 
}
