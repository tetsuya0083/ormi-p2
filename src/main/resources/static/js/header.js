document.addEventListener('DOMContentLoaded', () => {
    const profileBtn = document.getElementById('profile-btn');
    const dropdown = document.getElementById('profile-dropdown');

    const toggleDropdown = (event) => {
        event.stopPropagation();
        dropdown.classList.toggle('show');
    };

    const closeDropdown = () => {
        dropdown.classList.remove('show');
    };

    profileBtn.addEventListener('click', toggleDropdown);
    document.addEventListener('click', closeDropdown);
});

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
});
