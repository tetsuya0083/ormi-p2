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
        // 태그가 이미 존재하는지 확인
        Optional<Tag> existingTag = tagRepository.findByTag(tagName);

        // 존재하면 반환, 없으면 새로 생성하여 저장
        return existingTag.orElseGet(() -> {
            Tag newTag = new Tag(tagName);
            return tagRepository.save(newTag);
        });
    }
}
