document.addEventListener('DOMContentLoaded', function() {
    const navLinks = document.querySelectorAll('.nav-link');
    console.log(navLinks)
    const currentPath = window.location.pathname;

    navLinks.forEach(link => {
        if (link.getAttribute('data-path') === currentPath) {
            link.classList.add('active-link');
        }
    });
});