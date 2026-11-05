package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.dto.EntryDto;
import ch.zli.m223.model.Entry;
import ch.zli.m223.repository.EntryRepository;

@ApplicationScoped
public class EntryService {
    @Inject
    private EntryRepository entryRepository;

    @Transactional
    public EntryDto createEntry(EntryDto dto) {
        Entry entry = dto.toEntity();
        entryRepository.persist(entry);
        return entry.toDto();
    }

    @Transactional
    public void deleteEntry(Long id) {
        if (!entryRepository.deleteById(id)) {
            throw new NotFoundException("Entry with id " + id + " not found.");
        }
    }

    @Transactional
    public EntryDto updateEntry(Long id, EntryDto dto) {
        Entry existing = entryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Entry with id " + id + " not found."));
        existing.setCheckIn(dto.getCheckIn());
        existing.setCheckOut(dto.getCheckOut());
        return existing.toDto();
    }

    public List<EntryDto> findAll() {
        return entryRepository.listAll().stream()
                .map(Entry::toDto)
                .toList();
    }
}
