package DateWeekdayIterations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class DateWeekdayIterations {

	public DateWeekdayIterations() {

	}
	
	private static List<String> getDates(String startdate, String enddate) {
		List<String> years = new ArrayList<>();
		LocalDate startDate;
		try {
			startDate = LocalDate.parse(startdate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		}
		catch(DateTimeParseException ex) {
			System.out.println("Invalid startdate");
			return Collections.emptyList();
		}
		LocalDate endDate;
		if(enddate.isEmpty()) {
			endDate = LocalDate.now();
		}
		else {
			try {
			endDate = LocalDate.parse(enddate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			}
			catch(DateTimeParseException ex) {
				System.out.println("Invalid enddate");
				return Collections.emptyList();
			}
		}
		
		if(startDate.isAfter(endDate)) {
			LocalDate tmp = startDate;
			startDate = endDate;
			endDate = tmp;
			System.out.println("Startdate after enddate, inverted");
		}
		
		System.out.println("Startdate was a " + startDate.getDayOfWeek().getDisplayName(TextStyle.FULL, 
                Locale.ENGLISH)  + "\n");

		for(int year=startDate.getYear()+1; year<=endDate.getYear(); year++) {
			LocalDate tmp = LocalDate.of(year, startDate.getMonth(), startDate.getDayOfMonth());
			if(startDate.getDayOfWeek().equals(tmp.getDayOfWeek())) {
				years.add(String.valueOf(year));
			}	
		}
		return years;
	}
	
	private static void print(List<String> years) {
		for(String year : years) {
			System.out.println(year);
		}
	}
	
	public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Enter a startdate (dd.MM.yyyy):");
			String startDate = scanner.nextLine();
			System.out.println("Enter an enddate (dd.MM.yyyy):");
			String endDate = scanner.nextLine();
			print(getDates(startDate, endDate));
		}
	}

}
