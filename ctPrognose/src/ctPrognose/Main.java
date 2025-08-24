package ctPrognose;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Main {
    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
    
	private static final LocalDateTime CT_START = LocalDateTime.of(2009, Month.JANUARY, 22, 5, 36);
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("One argument");
			return;
		}
		int cntPostings = 1;
		try {
			cntPostings = Integer.parseInt(args[0]);
	        //System.out.println("Postings since start: " + cntPostings);
			if(cntPostings < 1) {
				System.out.println("Only positive numbers allowed");
				return;
			}
		}
		catch (Exception ex) {
			System.out.println("No valid argument or unknown error");
			return;
		}
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
        long secondsSinceStart = ChronoUnit.SECONDS.between(CT_START, now);
        //System.out.println("Days since start: " + secondsSinceStart);
        long prediction = secondsSinceStart / cntPostings * 1000 - secondsSinceStart;
        System.out.println("Predicted date: " + now.plusSeconds(prediction).format(FORMATTER));
	}
}
