package Root;

import java.text.DecimalFormat;

public class Heron {
	public static void main(String[] args) {
		
		//getDistance();		
		double area = 10000;
		int rounds = 10;
		System.out.println("\nArea: " + area + "\tRoot: " + squareRoot(area, rounds) + " ");
	}
	
	private static double squareRoot(double area, int rounds) {
		double xn = area / 4;
		double yn;
		DecimalFormat df = new DecimalFormat("#.000000000000000");
				
		for(int n = 0; n < rounds; n++) {
			yn = area / xn;
			System.out.println("xn" + n + ": " + df.format(xn) + "\tyn" + n + ": " + df.format(yn));
			xn = (xn + yn)/2;
		}
		return xn;
	}
	
	private static void getDistance() {
		
		long lastHit = 0;
		for(int i=0; i< 1000; i++) {
			double root = Math.sqrt(i);
			if(root*root == i) {
				System.out.println("root: " + root + " i: " + i +" distance: " + (i - lastHit));
				lastHit = i;
			}
		}
	}
}
