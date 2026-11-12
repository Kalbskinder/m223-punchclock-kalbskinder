package ch.zli.m223.service;

import java.util.HashSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.dto.EntryDetailDto;
import ch.zli.m223.dto.EntryDto;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.EntryRepository;

@ApplicationScoped
public class EntryService {
    @Inject
    EntryRepository entryRepository;

    @Inject
    CategoryService categoryService;

    @Inject
    TagService tagService;

    @Inject
    TimeSummaryService timeSummaryService;

    public List<EntryDto> findAll() {
        return entryRepository.listAll().stream()
                .map(Entry::toDto)
                .toList();
    }

    public List<EntryDetailDto> findAllDetails() {
        return entryRepository.listAll().stream()
                .map(Entry::toDetailDto)
                .toList();
    }

    public EntryDetailDto findDetailById(Long id) {
        return getEntry(id).toDetailDto();
    }

    @Transactional
    public EntryDto createEntry(EntryDto dto) {
        Entry entry = dto.toEntity();
        applyRelations(entry, dto);
        entryRepository.persist(entry);
        return entry.toDto();
    }

    @Transactional
    public EntryDto updateEntry(Long id, EntryDto dto) {
        Entry entry = getEntry(id);
        entry.setCheckIn(dto.getCheckIn());
        entry.setCheckOut(dto.getCheckOut());
        entry.setDescription(dto.getDescription());
        applyRelations(entry, dto);
        return entry.toDto();
    }

    @Transactional
    public void deleteEntry(Long id) {
        if (!entryRepository.deleteById(id)) {
            throw new NotFoundException("Entry with id " + id + " not found.");
        }
    }

    public Map<LocalDate, String> getTimeSummaries() {
        List<Entry> entries = entryRepository.listAll();
        return timeSummaryService.calculateSummaryPerDay(entries);
    }

    /** Loads category and tags by their IDs; unknown IDs result in a 404. */
    private void applyRelations(Entry entry, EntryDto dto) {
        entry.setCategory(dto.getCategoryId() != null
                ? categoryService.getCategory(dto.getCategoryId())
                : null);

        Set<Tag> tags = new HashSet<>();
        for (Long tagId : dto.getTagIds()) {
            tags.add(tagService.getTag(tagId));
        }
        entry.setTags(tags);
    }

    private Entry getEntry(Long id) {
        return entryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Entry with id " + id + " not found."));
    }
}
