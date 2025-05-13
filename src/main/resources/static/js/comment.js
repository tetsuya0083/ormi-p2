document.getElementById("create-comment-btn").addEventListener("click", function () {
    const postId = document.getElementById("post-id").value;
    const commentContent = document.getElementById("comment").value;

    if (!commentContent.trim()) {
        alert("댓글 내용을 입력하세요.");
        return;
    }

    fetch(`/api/posts/${postId}/comments`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            postId: postId,
            content: commentContent,
        })
    })
        .then(response => {
            if (!response.ok) throw new Error("댓글 등록 실패");
            return response.json();
        })
        .then(comment => {
            // 1. 댓글 폼 초기화
            document.getElementById("comment").value = "";

            // 2. 새 댓글을 HTML로 만들어서 추가
            const commentHtml = `
            <div class="border-top pt-3">
                <div class="d-flex align-items-center mb-2">
                    <img src="${comment.profileImage || '/images/default-profile.jpg'}" class="profile-img mr-2" alt="프로필">
                    <strong class="mr-2">${comment.nickname}</strong>
                    <span class="mr-2 text-muted small">Lv.${comment.userLevel}</span>
                    <span class="text-muted small">${comment.createdAtFormatted}</span>
                </div>
                <div>${comment.content}</div>
            </div>
        `;
            document.getElementById("comments-container").insertAdjacentHTML("afterbegin", commentHtml);
        })
        .catch(err => {
            console.error(err);
            alert("댓글 등록 중 오류가 발생했습니다.");
        });
});



function getCookie(key) {
    const cookies = document.cookie.split(';');
    for (let item of cookies) {
        const [cookieKey, value] = item.trim().split('=');
        if (cookieKey === key) return value;
    }
    return null;
}

function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),  // JWT 토큰
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();  // 성공하면 성공 콜백 실행
        }

        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {  // 토큰 만료 처리
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ refreshToken: refresh_token }),
            })
                .then(res => res.ok ? res.json() : Promise.reject())
                .then(result => {
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);  // 토큰 갱신 후 재시도
                })
                .catch(fail);
        } else {
            fail();  // 실패 처리
        }
    }).catch(fail);
}
