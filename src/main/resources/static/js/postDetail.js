// 댓글 이미지 클릭 시 모달에 이미지 띄우기
document.querySelectorAll('.comment-img').forEach(img => {
    img.addEventListener('click', () => {
        const modal = document.getElementById('imageModal');
        const modalImage = document.getElementById('modalImage');
        modal.style.display = "block";
        modalImage.src = img.src;
    });
});

// 모달 닫기
function closeModal() {
    const modal = document.getElementById('imageModal');
    modal.style.display = "none";
}
// 좋아요 클릭 이벤트 처리
document.addEventListener('DOMContentLoaded', function() {
    const likeButton = document.querySelector('#likeButton');
    likeButton.addEventListener('click', handleClick);
});

function handleClick(event) {
    // 클릭된 버튼의 ID로 게시글 ID 추출
    const postId = event.target.getAttribute('data-id');
    const isLiked = event.target.getAttribute('data-likestatus') === 'true';

    // AJAX 요청을 보내서 좋아요 상태를 서버에 반영
    $.ajax({
        url: '/posts/' + postId + '/like',
        method: 'POST',
        data: {
            liked: !isLiked  // 현재 상태와 반대값을 전송
        },
        success: function(response) {
            event.target.setAttribute('data-likestatus', response.isLiked ? 'true' : 'false');
            event.target.setAttribute('data-likescount', response.likesCount);
            event.target.textContent = response.isLiked
                ? '좋아요 취소 (' + response.likesCount + ')'
                : '좋아요 (' + response.likesCount + ')';
            event.target.className = response.isLiked ? 'liked-button' : 'unliked-button';
        },
        error: function() {
            alert("좋아요 처리에 실패했습니다.");
        }
    });
}

// 게시글 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        const isConfirmed = confirm('정말 삭제하시겠습니까?');
        if (!isConfirmed) return;

        let id = document.getElementById('post-id').value;
        fetch(`/api/posts/${id}`, {
            method: 'DELETE'
        }).then(() => {
            alert('삭제가 완료되었습니다');
            location.replace('/posts');
        });
    });
}


// 수정 버튼 클릭 시 수정 요청 보내기
const modifyButton = document.querySelector('.btn-outline-primary.btn-sm');  // 수정 버튼을 선택합니다.
if (modifyButton) {
    modifyButton.addEventListener('click', function() {
        const postId = document.querySelector('#postId').value;  // 게시글 ID (페이지에 hidden으로 넣어둔 경우)
        const title = document.querySelector('#title').value;  // 제목 필드
        const content = document.querySelector('#content').value;  // 내용 필드
        const category = document.querySelector('#category').value;  // 카테고리 선택 필드 (추가한 경우)

        // 수정 요청을 AJAX로 보내기
        $.ajax({
            url: '/posts/' + postId + '/edit',  // 수정할 게시글의 URL
            method: 'PUT',
            data: JSON.stringify({
                title: title,
                content: content,
                category: category
            }),
            contentType: 'application/json',
            success: function(response) {
                alert('수정이 완료되었습니다');
                location.replace(`/posts/${postId}`);  // 수정 후 해당 게시글 페이지로 이동
            },
            error: function() {
                alert('수정에 실패했습니다.');
            }
        });
    });
}