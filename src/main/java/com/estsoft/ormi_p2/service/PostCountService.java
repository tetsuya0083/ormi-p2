package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.dto.CategoryPostCountDto;
import com.estsoft.ormi_p2.repository.PostCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCountService {
    private final PostCountRepository postRepository;

    public List<CategoryPostCountDto> getPostCountsByCategory(Long userId) {
// 실제로 작성된 카테고리 수만 조회
        List<CategoryPostCountDto> countsFromDb = postRepository.countPostsByCategory(userId);

        // DB 결과를 Map으로 변환
        Map<Category, Long> countMap = countsFromDb.stream()
                .collect(Collectors.toMap(CategoryPostCountDto::getCategory, CategoryPostCountDto::getPostCount));

        // 모든 ENUM을 돌면서 없는 항목은 0으로 채움
        List<CategoryPostCountDto> result = new ArrayList<>();
        for (Category category : Category.values()) {
            long count = countMap.getOrDefault(category, 0L);
            result.add(new CategoryPostCountDto(category, count));
        }

        return result;
    }
}
