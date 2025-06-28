document.addEventListener("DOMContentLoaded", function () {
  const currentPath = window.location.pathname;
  const navItems = document.querySelectorAll("nav a");

  navItems.forEach((item) => {
    if (item.getAttribute("href") === currentPath) {
      item.classList.add("active");
    }
  });

  const checkbox = document.getElementById("aguaCheckbox");
  const inputsDiv = document.getElementById("aguaInputs");

  // Verificar se os elementos existem antes de adicionar event listeners
  if (checkbox && inputsDiv) {
    checkbox.addEventListener("change", () => {
      if (checkbox.checked) {
        inputsDiv.classList.remove("hidden");
      } else {
        inputsDiv.classList.add("hidden");
      }
    });
  }

  const menuToggle = document.getElementById("menu-toggle");
  const menuClose = document.getElementById("menu-close");
  const mobileMenu = document.getElementById("mobile-menu");

  if (menuToggle && mobileMenu) {
    menuToggle.addEventListener("click", () => {
      mobileMenu.classList.toggle("hidden");
    });
  }

  if (menuClose && mobileMenu) {
    menuClose.addEventListener("click", () => {
      mobileMenu.classList.add("hidden");
    });
  }

  navItems.forEach((item) => {
    if (item.getAttribute("href") === currentPath) {
      item.classList.add("active");
    }
  });
});

// Função para verificar se o usuário está logado
function verificarLogin() {
  // Fazer requisição AJAX para verificar status de autenticação
  fetch("/auth/status")
    .then((response) => response.json())
    .then((data) => {
      if (data.autenticado) {
        // Usuário está logado
        const currentPath = window.location.pathname;
        if (currentPath === "/" || currentPath === "/login") {
          // Redirecionar baseado no cargo
          let redirectUrl = "/admin"; // padrão
          if (data.cargo === "NUTRICIONISTA") {
            redirectUrl = "/nutricionista";
          } else if (data.cargo === "PRODUCAO") {
            redirectUrl = "/producao";
          }
          window.location.href = redirectUrl;
        }
        return true;
      } else {
        // Usuário não está logado
        return false;
      }
    })
    .catch((error) => {
      console.error("Erro ao verificar status de autenticação:", error);
      return false;
    });
}

// Função para fazer logout
function fazerLogout() {
  // Limpar localStorage
  localStorage.removeItem("rememberMe");

  // Fazer logout via Spring Security
  window.location.href = "/logout";
}

// Função para verificar se o usuário está na página de login
function verificarPaginaLogin() {
  const currentPath = window.location.pathname;
  if (currentPath === "/" || currentPath === "/login") {
    // Se estiver na página de login, verificar se já está logado
    verificarLogin();
  }
}

// Executar verificação quando a página carregar
document.addEventListener("DOMContentLoaded", function () {
  verificarPaginaLogin();
});
