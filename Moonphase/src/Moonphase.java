import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.swing.JFrame;

public class Moonphase extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final DecimalFormat FORMATER = new DecimalFormat("0.00");
	final static LocalDateTime KNOWN_FULLMOON = LocalDateTime.of(2023, Month.SEPTEMBER, 29, 11, 57, 36);
    final static double SYNODIC_PERIOD = 29.530589 * 86400;
    private double phase = 0.0;
    private int r = 500;
    
	  public Moonphase() {
		    setTitle("Drawing the Moonphase");
		    setSize(r+100, r+100);
		    setVisible(true);
		    setDefaultCloseOperation(EXIT_ON_CLOSE);
		    phase = getPhase();
	        System.out.println(getText());
		    //phase = 1.0;
	  }

    public static void main(String[] args) {
    	new Moonphase();
    }
    
    private double getPhase() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
        long between = ChronoUnit.SECONDS.between(KNOWN_FULLMOON, now);
        //System.out.println("Seconds between dates: " + between);
        //System.out.println("Phase: " + phase);
        BigDecimal bigDecimal = new BigDecimal(between / SYNODIC_PERIOD);
        int intValue = bigDecimal.intValue();
        BigDecimal decimalValue = bigDecimal.subtract(new BigDecimal(intValue)).round(new MathContext(2));
        return decimalValue.doubleValue() == 0.00 ? 1.00 : decimalValue.doubleValue();
    }
    
    private double getSemiMinor() {
    	 return Math.cos(phase*2*Math.PI);
    }
    
    private double getPhaseFactor() {
    	return Math.abs(getSemiMinor());
    }
    
    private String getText() {
        String text = "";
        if (phase < 0.25) {
            text = "Abnehmender Mond (drittes Viertel)";
        }
        else if (phase == 0.25) {
            text = "Abnehmender Halbmond (letztes Viertel)";
        }
        else if (0.25 < phase && phase < 0.50) {
            text = "Abnehmende Sichel";
        }
        else if (phase == 0.50) {
            text = "Neumond";
        }
        else if (0.50 < phase && phase < 0.75) {
            text = "Zunehmende Sichel";
        }
        else if (phase == 0.75) {
            text = "Zunehmender Halbmond (erstes Viertel)";
        }
        else if (0.75 < phase && phase < 1.00) {
            text = "Zunehmender Mond (zweites Viertel)";
        }
        else if (phase == 1.00) {
            text = "Vollmond";
        }
        text += ", Beleuchtung " + FORMATER.format(getEnlightedPercentage()) + "%";
        return text;
    }
    
    private double getEnlightedPercentage() {
    	double semiMinor = getSemiMinor();
        double area = Math.PI * 1 * semiMinor / 2; // Fläche beleuchtete / unbeleuchtete Ellipse
        //System.out.println("Fläche Halbellipse: " + area);
        area = Math.PI / 2 + area; // Gesamte beleuchtete Mondfläche 
        //System.out.println("Beleuchtete Mondfläche: " + area);
        return area / Math.PI * 100; // Prozent vom Vollkreis
    }
    
    private void paintMoonCircle(Graphics2D g2d, Color color) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(color);
        Arc2D.Double drawable = new Arc2D.Double((this.getWidth()-r)/2,
                		(this.getHeight()-r)/2,
                		r,     //  Breite des Platzierungsrechtecks
                		r,     //  Höhe des Platzierungsrechtecks
                        0.0,        //  Startwinkel
                        360.0,        //  Drehwinkel des Bogens
                        Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
    }
            
    private void paintNewMoon(Graphics2D g2d) {
    	paintMoonCircle(g2d, Color.BLUE);
    }
    
    private void paintFullMoon(Graphics2D g2d) {
    	paintMoonCircle(g2d, Color.WHITE);
    }
    
    private void paintHalfMoon(Graphics2D g2d, double rotationAngle) {
        
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.WHITE);
        Arc2D.Double drawable = new Arc2D.Double((this.getWidth()-r)/2,
                		(this.getHeight()-r)/2,
                		r,     //  Breite des Platzierungsrechtecks
                		r,     //  Höhe des Platzierungsrechtecks
                        90.0,        //  Startwinkel
                        rotationAngle,        //  Drehwinkel des Bogens
                        Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
    }
    
    private void paintWaningMoonThirdQuarter(Graphics2D g2d) {
        
    	double phaseFactor = getPhaseFactor();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.WHITE);
        Arc2D.Double drawable = new Arc2D.Double((this.getWidth()-r)/2,
                		(this.getHeight()-r)/2,
                		r,     //  Breite des Platzierungsrechtecks
                		r,     //  Höhe des Platzierungsrechtecks
                        90.0,        //  Startwinkel
                        180.0,        //  Drehwinkel des Bogens
                        Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
        g2d.setColor(Color.WHITE);
        drawable = new Arc2D.Double((this.getWidth()-r*phaseFactor)/2,
        		(this.getHeight()-r)/2,
        		r*phaseFactor,     //  Breite des Platzierungsrechtecks
        		r,     //  Höhe des Platzierungsrechtecks
                90.0,        //  Startwinkel
                -180.0,        //  Drehwinkel des Bogens
                Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
    }
    
    private void paintWaningMoonLastQuarter(Graphics2D g2d) {
        
    	double phaseFactor = getPhaseFactor();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.WHITE);
        Arc2D.Double drawable = new Arc2D.Double((this.getWidth()-r)/2,
                		(this.getHeight()-r)/2,
                		r,     //  Breite des Platzierungsrechtecks
                		r,     //  Höhe des Platzierungsrechtecks
                        90.0,        //  Startwinkel
                        180.0,        //  Drehwinkel des Bogens
                        Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
        g2d.setColor(Color.BLUE);
        drawable = new Arc2D.Double((this.getWidth()-r*phaseFactor)/2,
        		(this.getHeight()-r)/2,
        		r*phaseFactor,     //  Breite des Platzierungsrechtecks
        		r,     //  Höhe des Platzierungsrechtecks
                90.0,        //  Startwinkel
                180.0,        //  Drehwinkel des Bogens
                Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
    }

    private void paintWaxingMoonFirstQuarter(Graphics2D g2d) {
        
    	double phaseFactor = getPhaseFactor();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.WHITE);
        Arc2D.Double drawable = new Arc2D.Double((this.getWidth()-r)/2,
                		(this.getHeight()-r)/2,
                		r,     //  Breite des Platzierungsrechtecks
                		r,     //  Höhe des Platzierungsrechtecks
                        90.0,        //  Startwinkel
                        -180.0,        //  Drehwinkel des Bogens
                        Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
        g2d.setColor(Color.BLUE);
        drawable = new Arc2D.Double((this.getWidth()-r*phaseFactor)/2,
        		(this.getHeight()-r)/2,
        		r*phaseFactor,     //  Breite des Platzierungsrechtecks
        		r,     //  Höhe des Platzierungsrechtecks
                90.0,        //  Startwinkel
                -180.0,        //  Drehwinkel des Bogens
                Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
    }
        
    private void paintWaxingMoonSecondQuarter(Graphics2D g2d) {
        
    	double phaseFactor = getPhaseFactor();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.WHITE);
        Arc2D.Double drawable = new Arc2D.Double((this.getWidth()-r)/2,
                		(this.getHeight()-r)/2,
                		r,     //  Breite des Platzierungsrechtecks
                		r,     //  Höhe des Platzierungsrechtecks
                        90.0,        //  Startwinkel
                        -180.0,        //  Drehwinkel des Bogens
                        Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
        g2d.setColor(Color.WHITE);
        drawable = new Arc2D.Double((this.getWidth()-r*phaseFactor)/2,
        		(this.getHeight()-r)/2,
        		r*phaseFactor,     //  Breite des Platzierungsrechtecks
        		r,     //  Höhe des Platzierungsrechtecks
                90.0,        //  Startwinkel
                180.0,        //  Drehwinkel des Bogens
                Arc2D.OPEN);
        g2d.fill(drawable);
        g2d.draw(drawable);
    }
        
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints settings = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(settings);
        if (phase < 0.25) {
            paintWaningMoonThirdQuarter(g2d);
        }
        else if (phase == 0.25) {
        	paintHalfMoon(g2d, 180.0);
        }
        else if (0.25 < phase && phase < 0.50) {
        	paintWaningMoonLastQuarter(g2d);
        }
        else if (phase == 0.50) {
    		paintNewMoon(g2d);
    	}
        else if (0.50 < phase && phase < 0.75) {
        	paintWaxingMoonFirstQuarter(g2d);
        }
        else if (phase == 0.75) {
        	paintHalfMoon(g2d, -180.0);
    	}
        else if (0.75 < phase && phase < 1.00) {
        	paintWaxingMoonSecondQuarter(g2d);
        }
        else if(phase == 1.00) {
    		paintFullMoon(g2d);
    	}

        g.setFont(new Font("Sans", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        g.drawString(getText(), 27, r+80);
        g.dispose();


    }
}
