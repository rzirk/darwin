package de.ricardo.genetic.darwin.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import de.ricardo.genetic.darwin.modules.Individuum;

public class Renderer {
	
	public Point DNARangeToImageRange(Point DNA, BufferedImage Canvas) {
		
		int offsetX = Canvas.getWidth()/2;
		int offsetY = Canvas.getHeight()/2;
		
		double newX = DNA.x * (offsetX / (double) Integer.MAX_VALUE);
		double newY = DNA.y * (offsetY / (double) Integer.MAX_VALUE);

		int ergX = (int) newX + offsetX;
		int ergY = (int) newY + offsetY;
		
		Point output = new Point(ergX,ergY);
		
		return output;
	}
	
	public Point ImageRangeToDNARange(Point Image, BufferedImage Canvas) {
		
		int offsetX = Canvas.getWidth()/2;
		int offsetY = Canvas.getHeight()/2;
		
		int newX = Image.x - offsetX;
		int newY = Image.y - offsetY;
		
		int ergX = newX * (Integer.MAX_VALUE / offsetX);
		int ergY = newY * (Integer.MAX_VALUE /offsetY);

		Point output = new Point(ergX,ergY);
		
		return output;
	}
	
	public BufferedImage renderIndividuumToCanvas(Individuum individuum, BufferedImage prevorirousDrawing) {
		BufferedImage newImage = new BufferedImage(prevorirousDrawing.getWidth(), prevorirousDrawing.getHeight(), prevorirousDrawing.getType());
		newImage.setData(prevorirousDrawing.getData());
		
		//Farbwert bestimmen
		//Nutze den gemittelten Farbwert aus dem Bildbereich
		Rectangle rect = new Rectangle(DNARangeToImageRange(
				individuum.getDNA().start,prevorirousDrawing));
		rect.add(DNARangeToImageRange(individuum.getDNA().end,newImage));
		
		long sum_r = 0, sum_g = 0, sum_b = 0;
		
		int test[] = new  int[rect.width*rect.height];
		Cache.getInstance().goal.getRGB(rect.x, rect.y, rect.width, rect.height, test, 0, 1);
		
		for(int x = rect.x; x < rect.x + rect.width; x++) {
			for(int y = rect.y; y < rect.y + rect.height; y++) {
				
				int RGBColor = test[(y-rect.y) + (x-rect.x)];
				Color color = new Color(RGBColor);

				sum_r += color.getRed();
				sum_g += color.getBlue();
				sum_b += color.getGreen();			
			}
		}
		
		Color color = null;
		if(rect.width == 0 || rect.height == 0) { //TODO Fix corrupted rectangles
			color = Color.WHITE;
		} else {
			 color = new Color((int) (sum_r / (rect.width * rect.height)),
					(int) (sum_g / (rect.width * rect.height)),
					(int) (sum_b / (rect.width * rect.height)));	
		}
		//=====================
		
		Graphics2D g2d = newImage.createGraphics();
		g2d.setColor(color);

		g2d.drawRect(rect.x,rect.y,rect.width,rect.height);
		g2d.fillRect(rect.x,rect.y,rect.width,rect.height);		
		g2d.drawImage(newImage, null, 0, 0);
		
		return newImage;
	};
}
