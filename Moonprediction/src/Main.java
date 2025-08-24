import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Main {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	private static final LocalDateTime KNOWN_FULLMOON = LocalDateTime.of(2023, Month.SEPTEMBER, 29, 11, 57, 36);
	private static final double SYNODIC_PERIOD = 29.530589 * 86400;
	
    private static double getPhase(LocalDateTime now) {
        long secondsDelta = ChronoUnit.SECONDS.between(KNOWN_FULLMOON, now);
        BigDecimal periods = new BigDecimal(secondsDelta / SYNODIC_PERIOD);
        int preDecimalPlace = periods.intValue();
        BigDecimal decimalPlace = periods.subtract(new BigDecimal(preDecimalPlace));
        return decimalPlace.doubleValue();
    }
	
    private static String getDate(LocalDateTime now, double phaseDiff) {
		return now.plusSeconds((long) (phaseDiff*SYNODIC_PERIOD)).format(FORMATTER);
    	
    }
    
	public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
        double phase = getPhase(now);
		System.out.println("Abnehmender Halbmond: " + getDate(now, 0.25 - phase));
		System.out.println("Neumond: " + getDate(now, 0.5 - phase));
		System.out.println("Zunehmender Halbmond: " + getDate(now, 0.75 - phase));
		System.out.println("Vollmond: " + getDate(now, 1.0 - phase));
    }
}
