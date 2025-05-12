
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