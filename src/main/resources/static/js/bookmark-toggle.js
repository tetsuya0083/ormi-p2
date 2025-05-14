// // static/js/bookmark-toggle.js
// document.addEventListener("DOMContentLoaded", () => {
//     document.querySelectorAll(".bookmark-btn").forEach(btn => {
//         btn.addEventListener("click", async () => {
//             const postId = btn.dataset.postId;
//             const img    = btn.querySelector("img");
//             const isOn   = img.src.includes("card_bookmark_on.png");
//             try {
//                 const resp = await fetch(`/api/bookmarks/${postId}`, {
//                     method: isOn ? 'DELETE' : 'POST'
//                 });
//                 if (!resp.ok) throw new Error(resp.statusText);
//                 img.src = isOn
//                     ? "/img/card_bookmark_off.png"
//                     : "/img/card_bookmark_on.png";
//             } catch (e) {
//                 console.error("북마크 토글 실패:", e);
//             }
//         });
//     });
// });

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".bookmark-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const postId = btn.dataset.postId;
            const img = btn.querySelector("img");
            const isBookmarked = btn.classList.contains("bookmarked");

            try {
                const response = await fetch(`/api/bookmarks/${postId}`, {
                    method: isBookmarked ? 'DELETE' : 'POST',
                });

                if (!response.ok) throw new Error(response.statusText);

                if (isBookmarked) {
                    // 해제 상태로 변경
                    img.src = "/img/card_bookmark_off.png";
                    btn.classList.remove("bookmarked");
                } else {
                    // 북마크 상태로 변경
                    img.src = "/img/card_bookmark_on.png";
                    btn.classList.add("bookmarked");
                }

            } catch (error) {
                console.error("북마크 토글 실패:", error);
            }
        });
    });
});
