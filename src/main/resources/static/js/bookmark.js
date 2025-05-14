// static/js/bookmark.js
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".bookmark-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const postId = btn.dataset.postId;
            try {
                const resp = await fetch(`/api/bookmarks/${postId}`, {
                    method: 'DELETE'
                });
                if (!resp.ok) throw new Error(resp.statusText);
                btn.closest(".card").remove();
            } catch (e) {
                console.error("북마크 해제 실패:", e);
            }
        });
    });
});
