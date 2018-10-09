package de.ricardo.genetic.darwin.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Cache{

	public BufferedImage goal;
	private static Cache single_instance = null; 

	public Cache() {
	}

	public void setGoalImage(BufferedImage goal) {
		this.goal = goal;
	}

	// static method to create instance of Singleton class 
	public static Cache getInstance() 
	{ 
		if (single_instance == null) 
			single_instance = new Cache(); 

		return single_instance; 
	} 
}
