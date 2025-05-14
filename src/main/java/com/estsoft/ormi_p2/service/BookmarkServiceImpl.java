package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Bookmark;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.repository.BookmarkRepository;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.repository.UserRepository;
import com.estsoft.ormi_p2.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;  // 추가

    @Autowired
    public BookmarkServiceImpl(
            BookmarkRepository bookmarkRepo,
            PostRepository postRepo,
            UserRepository userRepo   // 주입
    ) {
        this.bookmarkRepo = bookmarkRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Page<Post> getBookmarkedPosts(
            Long userId, Category category, String keyword, Pageable pageable
    ) {
        if (keyword != null && keyword.isBlank()) keyword = null;
        return bookmarkRepo.findBookmarkedPosts(userId, category, keyword, pageable);
    }

    @Override
    @Transactional
    public void addBookmark(Long userId, Long postId) {
        // 이미 북마크 되어 있는지 확인
        if (!bookmarkRepo.existsByUser_UserIdAndPost_PostId(userId, postId)) {
            // User 엔티티 조회
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            // Post 엔티티 조회
            Post post = postRepo.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
            // Bookmark 생성 및 저장
            Bookmark b = Bookmark.of(user, post);
            bookmarkRepo.save(b);
        }
    }

    @Override
    @Transactional
    public void removeBookmark(Long userId, Long postId) {
        bookmarkRepo.deleteByUser_UserIdAndPost_PostId(userId, postId);
    }

    @Override
    public List<Long> getBookmarkedPostIds(Long userId) {
        return bookmarkRepo.findByUser_UserId(userId)
                .stream()
                .map(b -> b.getPost().getPostId())
                .collect(Collectors.toList());
    }
}
