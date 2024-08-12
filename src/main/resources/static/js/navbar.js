document.addEventListener('DOMContentLoaded', function() {
    const navLinks = document.querySelectorAll('.nav-link');
    const currentPath = window.location.pathname;

    navLinks.forEach(link => {
        console.log(link.getAttribute('data-path'));
        console.log(currentPath);
        if (link.getAttribute('data-path') === currentPath) {
            console.log("tes");
            link.classList.add('active-link');
        }
    });
});