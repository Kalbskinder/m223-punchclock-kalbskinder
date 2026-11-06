package ch.zli.m223.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jakarta.enterprise.context.ApplicationScoped;

import ch.zli.m223.model.Entry;

@ApplicationScoped
public class TimeSummaryService {

    /**
     * Sums up the recorded time per day. An entry counts towards the day of its check-in.
     *
     * @return date (sorted ascending) mapped to the total duration in the format HH:mm:ss
     */
    public Map<LocalDate, String> calculateSummaryPerDay(List<Entry> entries) {
        Map<LocalDate, Duration> totals = new TreeMap<>();
        for (Entry entry : entries) {
            LocalDate day = entry.getCheckIn().toLocalDate();
            Duration duration = Duration.between(entry.getCheckIn(), entry.getCheckOut());
            totals.merge(day, duration, Duration::plus);
        }

        Map<LocalDate, String> result = new LinkedHashMap<>();
        totals.forEach((day, total) -> result.put(day, format(total)));
        return result;
    }

    private String format(Duration duration) {
        return String.format("%02d:%02d:%02d",
            duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }
}
