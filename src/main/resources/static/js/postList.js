document.addEventListener("DOMContentLoaded", function () {
    const bookmarkButtons = document.querySelectorAll(".bookmark-btn");

    // 북마크 버튼 클릭 시 이미지 변경
    bookmarkButtons.forEach(button => {
        button.addEventListener("click", function () {
            const img = this.querySelector("img");
            const isBookmarked = img.getAttribute("src").includes("on");

            const newImgSrc = isBookmarked
                ? "/img/card_bookmark_off.png"
                : "/img/card_bookmark_on.png";

            img.setAttribute("src", newImgSrc);
        });
    });

    // 정렬 옵션 변경 시 URL 변경
    $('#sortSelect').on('change', function() {
        const sortValue = $(this).val();
        const currentPage = new URLSearchParams(window.location.search).get('page') || 1;

        // 카테고리 값을 포함하지 않고 URL 업데이트
        const url = `/posts?sort=${sortValue}&page=${currentPage}`;
        window.location.href = url;
    });

    // 페이징 처리
    $('.pagination .page-link').on('click', function(e) {
        e.preventDefault();
        const page = $(this).attr('href').split('=')[1];
        const sortValue = $('#sortSelect').val();

        // 카테고리 값을 포함하지 않고 URL 업데이트
        const url = `/posts?sort=${sortValue}&page=${page}`;
        window.location.href = url;
    });
});


// 폼 제출 전에 입력 검증
document.getElementById('searchForm').addEventListener('submit', function(event) {
    var searchInput = document.getElementById('searchInput').value;
    if (!searchInput.trim()) {
        event.preventDefault(); // 폼 제출 방지
        alert('검색어를 입력해주세요.');
    }
});