package de.ricardo.genetic.darwin.utils;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import com.arturmkrtchyan.sizeof4j.SizeOf;

public class ReproductionHelper {

	public static Point mixPoints(Point point1, Point point2) {
		Point childPoint = null;

		int childrenValueX = mixInteger(point1.x, point2.x);
		int childrenValueY = mixInteger(point1.y, point2.y);

		childPoint = new Point(childrenValueX, childrenValueY);

		return childPoint;
	}

	public static int mixInteger(int int1, int int2) {
		int childInt = 0;

		Random rand = new Random();
		for (int i = 0; i < SizeOf.intSize() * 8; i++) {
			if(rand.nextBoolean()) { //Parent 1
				if (i == 31) {
					childInt = childInt | (int1 & Integer.MIN_VALUE);
				} else {
					childInt = childInt | (int1 & (int) Math.pow(2,i));
				}
			} else { //Parent 2
				if (i == 31) {
					childInt = childInt | (int2 & Integer.MIN_VALUE);
				} else {
				childInt = childInt | (int2 & (int) Math.pow(2,i));
				}
			}
		}
		return childInt;
	}

	public static Color mixColors(Color color1, Color color2) {
		Color childColor = null;

		int childrenValueRed   = mixInteger(color1.getRed(), color2.getRed());
		int childrenValueGreen = mixInteger(color1.getGreen(), color2.getGreen());
		int childrenValueBlue  = mixInteger(color1.getBlue(), color2.getBlue());

		childColor = new Color(childrenValueRed, 
				childrenValueGreen, childrenValueBlue);

		return childColor;
	}

}
