package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Tag;
import com.estsoft.ormi_p2.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getOrCreateTag(String tagName) {

        Optional<Tag> existingTag = tagRepository.findByTag(tagName);

        return existingTag.orElseGet(() -> {
            Tag newTag = new Tag(tagName);
            return tagRepository.save(newTag);
        });
    }
}
