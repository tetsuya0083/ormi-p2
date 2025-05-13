// static/js/bookmark-toggle.js
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".bookmark-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const postId = btn.dataset.postId;
            const img    = btn.querySelector("img");
            const isOn   = img.src.includes("card_bookmark_on.png");
            try {
                const resp = await fetch(`/api/bookmarks/${postId}`, {
                    method: isOn ? 'DELETE' : 'POST'
                });
                if (!resp.ok) throw new Error(resp.statusText);
                img.src = isOn
                    ? "/img/card_bookmark.png"
                    : "/img/card_bookmark_on.png";
            } catch (e) {
                console.error("북마크 토글 실패:", e);
            }
        });
    });
});
