package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.service.BookmarkService;
import com.estsoft.ormi_p2.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    public BookmarkApiController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    /** POST  /api/bookmarks/{postId}  → 북마크 추가 */
    @PostMapping("/{postId}")
    public ResponseEntity<Void> add(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        bookmarkService.addBookmark(user.getUserId(), postId);
        return ResponseEntity.ok().build();
    }

    /** DELETE /api/bookmarks/{postId} → 북마크 해제 */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        bookmarkService.removeBookmark(user.getUserId(), postId);
        return ResponseEntity.ok().build();
    }
}
