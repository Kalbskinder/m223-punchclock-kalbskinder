package ch.zli.m223.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import ch.zli.m223.model.Category;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.CategoryRepository;
import ch.zli.m223.repository.EntryRepository;
import ch.zli.m223.repository.TagRepository;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExampleDataService {
    @Inject CategoryRepository categoryRepository;
    @Inject TagRepository tagRepository;
    @Inject EntryRepository entryRepository;

    @ConfigProperty(name = "punchclock.example-data.enabled",
        defaultValue = "false")
    boolean enabled;

    void start(@Observes StartupEvent event) {
        if (enabled) {
            createExampleData();
        }
    }

    @Transactional
    public void createExampleData() {
        if (categoryRepository.count() > 0
            || tagRepository.count() > 0
            || entryRepository.count() > 0
        ) {
            return; // example data already exists
        }

        // Categories ("Support" stays unused on purpose)
        Category projekt = category("Projektarbeit");
        Category schule = category("Schule");
        Category admin = category("Administration");
        category("Support");

        // Tags ("Remote" stays unused on purpose)
        Tag backend = tag("Backend");
        Tag frontend = tag("Frontend");
        Tag meeting = tag("Meeting");
        Tag doku = tag("Dokumentation");
        Tag bugfix = tag("Bugfix");
        tag("Remote");

        // Entries for one work week (Monday 2. to Friday 6. November 2026)
        entry(2, "08:00", "12:00", projekt, Set.of(backend));
        entry(2, "13:00", "17:00", projekt, Set.of(backend, bugfix));
        entry(3, "08:15", "11:45", schule, Set.of(doku));
        entry(3, "13:00", "14:00", admin, Set.of(meeting));
        entry(4, "08:00", "12:00", projekt, Set.of(frontend, backend));
        entry(4, "13:30", "16:30", projekt, Set.of(frontend, meeting, doku));
        entry(5, "09:00", "12:00", schule, Set.of());   // category without tags
        entry(6, "08:00", "10:00", null, Set.of(bugfix)); // tags without category
        entry(6, "10:15", "12:00", null, Set.of());      // neither category nor tags

        Log.infof("Example data created: %d categories, %d tags, %d entries",
            categoryRepository.count(), tagRepository.count(), entryRepository.count());
    }

    private Category category(String title) {
        Category category = new Category();
        category.setTitle(title);
        categoryRepository.persist(category);
        return category;
    }

    private Tag tag(String title) {
        Tag tag = new Tag();
        tag.setTitle(title);
        tagRepository.persist(tag);
        return tag;
    }

    private void entry(int day, String from, String to, Category category, Set<Tag> tags) {
        LocalDate date = LocalDate.of(2026, 11, day);
        Entry entry = new Entry();
        entry.setCheckIn(LocalDateTime.of(date, LocalTime.parse(from)));
        entry.setCheckOut(LocalDateTime.of(date, LocalTime.parse(to)));
        entry.setCategory(category);
        entry.setTags(new HashSet<>(tags));
        entryRepository.persist(entry);
    }
}
