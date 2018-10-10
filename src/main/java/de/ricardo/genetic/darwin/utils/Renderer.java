package de.ricardo.genetic.darwin.utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import de.ricardo.genetic.darwin.modules.Individuum;

public class Renderer {
	
	public static Point DNARangeToImageRange(Point DNA, BufferedImage Canvas) {
		
		int offsetX = Canvas.getWidth()/2;
		int offsetY = Canvas.getHeight()/2;
		
		double newX = DNA.x * (offsetX / (double) Integer.MAX_VALUE);
		double newY = DNA.y * (offsetY / (double) Integer.MAX_VALUE);

		int ergX = (int) newX + offsetX;
		int ergY = (int) newY + offsetY;
		
		Point output = new Point(ergX,ergY);
		
		return output;
	}
	
	public static Point ImageRangeToDNARange(Point Image, BufferedImage Canvas) {
		
		int offsetX = Canvas.getWidth()/2;
		int offsetY = Canvas.getHeight()/2;
		
		int newX = Image.x - offsetX;
		int newY = Image.y - offsetY;
		
		int ergX = newX * (Integer.MAX_VALUE / offsetX);
		int ergY = newY * (Integer.MAX_VALUE /offsetY);

		Point output = new Point(ergX,ergY);
		
		return output;
	}
	
	public static BufferedImage renderIndividuumToCanvas(Individuum individuum, BufferedImage prevorirousDrawing) {
		BufferedImage newImage = new BufferedImage(prevorirousDrawing.getWidth(), prevorirousDrawing.getHeight(), prevorirousDrawing.getType());
		newImage.setData(prevorirousDrawing.getData());
		
		Graphics2D g2d = newImage.createGraphics();
		g2d.setColor(individuum.getDNA().Color);
		
		Rectangle rect = new Rectangle(DNARangeToImageRange(
				individuum.getDNA().start,prevorirousDrawing));
		rect.add(DNARangeToImageRange(individuum.getDNA().end,newImage));

		g2d.drawRect(rect.x,rect.y,rect.width,rect.height);
		g2d.fillRect(rect.x,rect.y,rect.width,rect.height);		
		g2d.drawImage(newImage, null, 0, 0);
		
		return newImage;
	};
}
