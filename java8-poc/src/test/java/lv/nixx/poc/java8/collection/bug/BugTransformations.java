package lv.nixx.poc.java8.collection.bug;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDate.of;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

public class BugTransformations {

	@Test
	public void createCalendar() throws ParseException {

		final Collection<Bug> bugs = Arrays.asList(new Bug(10, of(2017, 9, 5), of(2017, 9, 6), "1"),
				new Bug(20, of(2017, 9, 6), null, "1"), new Bug(30, of(2017, 9, 8), of(2017, 9, 8), "1"),
				new Bug(40, of(2017, 10, 1), of(2017, 10, 2), "1"));

		final LocalDate startDate = of(2017, 9, 1);
		final LocalDate endDate = of(2017, 9, 15);

		final Predicate<Bug> dateFilter = new Predicate<Bug>() {
			@Override
			public boolean test(Bug p) {
				LocalDate closeDate = p.getCloseDate();
				return p.getOpenDate().compareTo(startDate) >= 0 && closeDate == null
						|| closeDate.compareTo(endDate) <= 0;
			}
		};

		final Function<Bug, Stream<BugEntry>> mapper = new Function<Bug, Stream<BugEntry>>() {
			@Override
			public Stream<BugEntry> apply(Bug t) {
				final LocalDate openDate = t.getOpenDate();
				final LocalDate closeDate = t.getCloseDate();
				long numOfDaysBetween = ChronoUnit.DAYS.between(openDate, closeDate == null ? endDate : closeDate);
				return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween + 1)
						.mapToObj(i -> new BugEntry(t.getId(), t.getSeverity(), openDate.plusDays(i)));
			}
		};

		final List<CalendarEntry> calendarList = Stream
				.concat(IntStream.iterate(0, i -> i + 1).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
						.mapToObj(i -> new BugEntry(null, null, startDate.plusDays(i))),
						bugs.stream().filter(dateFilter).flatMap(mapper))
				.collect(Collectors.groupingBy(BugEntry::getDate,
						Collectors.reducing(ZERO, b -> b.getId() == null ? ZERO : ONE, BigDecimal::add)))
				.entrySet().stream().map(t -> new CalendarEntry(t.getKey(), t.getValue().intValue()))
				.sorted((b0, b1) -> b0.getDate().compareTo(b1.getDate())).collect(Collectors.toList());

		calendarList.forEach(System.out::println);

	}

	class BugEntry {
		LocalDate date;
		Integer id;
		String severity;

		BugEntry(Integer id, String severity, LocalDate date) {
			this.id = id;
			this.severity = severity;
			this.date = date;
		}

		LocalDate getDate() {
			return date;
		}

		Integer getId() {
			return id;
		}

		String getSeverity() {
			return severity;
		}

	}

	class CalendarEntry {
		LocalDate date;
		Integer bugCount;

		public CalendarEntry(LocalDate date, Integer bugCount) {
			this.date = date;
			this.bugCount = bugCount;
		}

		LocalDate getDate() {
			return date;
		}

		Integer getBugCount() {
			return bugCount;
		}

		@Override
		public String toString() {
			return "CalendarEntry [date=" + date + ", bugCount=" + bugCount + "]";
		}
		
	}

	public class Bug {
		private int id;
		private LocalDate openDate;
		private LocalDate closeDate;
		private String severity;

		public Bug(int id, LocalDate openDate, LocalDate closeDate, String severity) {
			this.id = id;
			this.openDate = openDate;
			this.closeDate = closeDate;
			this.severity = severity;
		}

		int getId() {
			return id;
		}

		LocalDate getOpenDate() {
			return openDate;
		}

		LocalDate getCloseDate() {
			return closeDate;
		}

		String getSeverity() {
			return severity;
		}
	}

}
