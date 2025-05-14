// 댓글 이미지 클릭 시 모달에 이미지 띄우기
document.querySelectorAll('.comment-img').forEach(img => {
    img.addEventListener('click', () => {
        const modal = document.getElementById('imageModal');
        const modalImage = document.getElementById('modalImage');
        if (modal && modalImage) {
            modal.style.display = "block";
            modalImage.src = img.src;
        }
    });
});

// 모달 닫기
function closeModal() {
    const modal = document.getElementById('imageModal');
    if (modal) modal.style.display = "none";
}

// 좋아요 클릭 이벤트 처리
document.addEventListener('DOMContentLoaded', function () {
    const likeButton = document.querySelector('#likeButton');
    if (likeButton) {
        likeButton.addEventListener('click', handleClick);
    }

    // 게시글 삭제 기능
    const deleteButton = document.getElementById('delete-btn');
    if (deleteButton) {
        deleteButton.addEventListener('click', handleDelete);
    }

    // 수정 버튼 클릭 시 수정 요청 보내기
    const modifyButton = document.querySelector('.btn-outline-primary.btn-sm');
    if (modifyButton) {
        modifyButton.addEventListener('click', handleModify);
    }
});

function handleClick(event) {
    const postId = event.target.getAttribute('data-id');
    const isLiked = event.target.getAttribute('data-likestatus') === 'true';

    if (!postId) return;

    $.ajax({
        url: `/posts/${postId}/like`,
        method: 'POST',
        data: {
            liked: !isLiked
        },
        success: function (response) {
            event.target.setAttribute('data-likestatus', response.isLiked ? 'true' : 'false');
            event.target.setAttribute('data-likescount', response.likesCount);
            event.target.textContent = response.isLiked
                ? `좋아요 취소 (${response.likesCount})`
                : `좋아요 (${response.likesCount})`;
            event.target.className = response.isLiked ? 'liked-button' : 'unliked-button';
        },
        error: function () {
            alert("좋아요 처리에 실패했습니다.");
        }
    });
}

function handleDelete() {
    const isConfirmed = confirm('정말 삭제하시겠습니까?');
    if (!isConfirmed) return;

    const postIdInput = document.getElementById('post-id');
    if (!postIdInput) return;

    const id = postIdInput.value;

    fetch(`/api/posts/${id}`, {
        method: 'DELETE'
    })
        .then(() => {
            alert('삭제가 완료되었습니다');
            location.replace('/posts');
        })
        .catch(() => {
            alert('삭제 요청 중 오류가 발생했습니다.');
        });
}

function handleModify() {
    const postId = document.querySelector('#postId')?.value;
    const title = document.querySelector('#title')?.value;
    const content = document.querySelector('#content')?.value;
    const category = document.querySelector('#category')?.value;

    if (!postId || !title || !content || !category) {
        alert('모든 필드를 입력해주세요.');
        return;
    }

    $.ajax({
        url: `/posts/${postId}/edit`,
        method: 'PUT',
        data: JSON.stringify({
            title: title,
            content: content,
            category: category
        }),
        contentType: 'application/json',
        success: function () {
            alert('수정이 완료되었습니다');
            location.replace(`/posts/${postId}`);
        },
        error: function () {
            alert('수정에 실패했습니다.');
        }
    });
}
