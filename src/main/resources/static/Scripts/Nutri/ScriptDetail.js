document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const fichasLink = document.querySelector('a[href="/nutricionista/fichas"]');
    const navItems = document.querySelectorAll('nav a'); // Definindo navItems aqui

    // Verifica se está em /fichas ou /fichas/qualquer-coisa
    if (currentPath === '/nutricionista/fichas' || currentPath.startsWith('/nutricionista/fichas/')) {
        if (fichasLink) {
            fichasLink.classList.add('active');
        }
    }

    // Verifica outras rotas exatas
    navItems.forEach(item => {
        if (item.getAttribute('href') === currentPath) {
            item.classList.add('active');
        }
    });

    const menuToggle = document.getElementById('menu-toggle');
    const menuClose = document.getElementById('menu-close');
    const mobileMenu = document.getElementById('mobile-menu');

    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
        });
    }

    if (menuClose && mobileMenu) {
        menuClose.addEventListener('click', () => {
            mobileMenu.classList.add('hidden');
        });
    }
});