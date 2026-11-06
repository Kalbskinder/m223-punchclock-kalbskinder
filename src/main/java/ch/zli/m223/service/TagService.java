package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.dto.TagDto;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.EntryRepository;
import ch.zli.m223.repository.TagRepository;

@ApplicationScoped
public class TagService {
    @Inject
    TagRepository tagRepository;

    @Inject
    EntryRepository entryRepository;

    public List<TagDto> findAll() {
        return tagRepository.listAll().stream()
                .map(Tag::toDto)
                .toList();
    }

    public TagDto findById(Long id) {
        return getTag(id).toDto();
    }

    @Transactional
    public TagDto createTag(TagDto dto) {
        Tag tag = dto.toEntity();
        tagRepository.persist(tag);
        return tag.toDto();
    }

    @Transactional
    public TagDto updateTag(Long id, TagDto dto) {
        Tag tag = getTag(id);
        tag.setTitle(dto.getTitle());
        return tag.toDto();
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = getTag(id);
        // Remove the tag from all entries first, so the join table has no dangling rows.
        List<Entry> entries = entryRepository.list("select e from Entry e join e.tags t where t = ?1", tag);
        entries.forEach(entry -> entry.getTags().remove(tag));
        tagRepository.delete(tag);
    }

    Tag getTag(Long id) {
        return tagRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Tag with id " + id + " not found."));
    }
}
