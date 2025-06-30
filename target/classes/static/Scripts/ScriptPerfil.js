// Script para preview de imagem
function previewImagem(input) {
  if (input.files && input.files[0]) {
    const reader = new FileReader();

    reader.onload = function (e) {
      // Atualiza a visualização da imagem
      const imgElement = input.closest("div").querySelector("img");
      imgElement.src = e.target.result;

      // Adiciona uma classe para indicar que a imagem foi alterada
      imgElement.classList.add("ring-2", "ring-green-500");
    };

    reader.readAsDataURL(input.files[0]);
  }
}

// Aguardar o DOM estar completamente carregado
document.addEventListener("DOMContentLoaded", function () {
  // Configurar edição de username
  const usernameView = document.getElementById("username-view");
  const usernameEdit = document.getElementById("username-edit");
  const usernameEditBtn = document.getElementById("username-edit-btn");
  const usernameSaveBtn = document.getElementById("username-save");

  // Configurar edição de email
  const emailView = document.getElementById("email-view");
  const emailEdit = document.getElementById("email-edit");
  const emailEditBtn = document.getElementById("email-edit-btn");
  const emailSaveBtn = document.getElementById("email-save");

  // Configurar edição de senha
  const senhaView = document.getElementById("senha-view");
  const senhaEditFields = document.getElementById("senha-edit-fields");
  const senhaEditBtn = document.getElementById("senha-edit-btn");
  const senhaSaveBtn = document.getElementById("senha-save");
  const senhaCancelBtn = document.getElementById("senha-cancel");
  const novaSenhaInput = document.getElementById("novaSenhaInput");
  const confirmarNovaSenhaInput = document.getElementById(
    "confirmarNovaSenhaInput"
  );
  const senhaAtualInput = document.getElementById("senhaAtualInput");

  // Função para entrar no modo de edição
  function enterEditMode(viewElement, editElement, saveBtn) {
    editElement.value = viewElement.textContent;
    viewElement.classList.add("hidden");
    editElement.classList.remove("hidden");
    saveBtn.classList.remove("hidden");
    editElement.focus();
  }

  // Função para sair do modo de edição
  function exitEditMode(viewElement, editElement, saveBtn) {
    viewElement.textContent = editElement.value;
    editElement.classList.add("hidden");
    viewElement.classList.remove("hidden");
    saveBtn.classList.add("hidden");
  }

  // Função para entrar no modo de edição de senha
  function enterSenhaEditMode() {
    senhaEditFields.classList.remove("hidden");
    senhaSaveBtn.classList.remove("hidden");
    senhaCancelBtn.classList.remove("hidden");
    novaSenhaInput.focus();
  }

  // Função para sair do modo de edição de senha
  function exitSenhaEditMode() {
    // Validar senhas
    const novaSenha = novaSenhaInput.value;
    const confirmarNovaSenha = confirmarNovaSenhaInput.value;
    const senhaAtual = senhaAtualInput.value;

    if (novaSenha !== confirmarNovaSenha) {
      alert("As novas senhas não coincidem!");
      return;
    }

    if (!senhaAtual) {
      alert("Por favor, informe sua senha atual!");
      return;
    }

    if (!novaSenha) {
      alert("Por favor, informe a nova senha!");
      return;
    }

    // Limpar campos
    novaSenhaInput.value = "";
    confirmarNovaSenhaInput.value = "";
    senhaAtualInput.value = "";

    // Ocultar campos de edição
    senhaEditFields.classList.add("hidden");
    senhaSaveBtn.classList.add("hidden");
    senhaCancelBtn.classList.add("hidden");

    // Atualizar campos hidden do formulário
    document.getElementById("novaSenha").value = novaSenha;
    document.getElementById("confirmarNovaSenha").value = confirmarNovaSenha;
    document.getElementById("senhaAtual").value = senhaAtual;
  }

  // Função para cancelar edição de senha
  function cancelSenhaEditMode() {
    // Limpar campos
    novaSenhaInput.value = "";
    confirmarNovaSenhaInput.value = "";
    senhaAtualInput.value = "";

    // Ocultar campos de edição
    senhaEditFields.classList.add("hidden");
    senhaSaveBtn.classList.add("hidden");
    senhaCancelBtn.classList.add("hidden");
  }

  // Event listeners para username
  if (usernameEditBtn) {
    usernameEditBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      enterEditMode(usernameView, usernameEdit, usernameSaveBtn);
    });
  }

  if (usernameSaveBtn) {
    usernameSaveBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      exitEditMode(usernameView, usernameEdit, usernameSaveBtn);
    });
  }

  if (usernameEdit) {
    usernameEdit.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        e.stopPropagation();
        exitEditMode(usernameView, usernameEdit, usernameSaveBtn);
      } else if (e.key === "Escape") {
        e.preventDefault();
        e.stopPropagation();
        usernameEdit.value = usernameView.textContent;
        exitEditMode(usernameView, usernameEdit, usernameSaveBtn);
      }
    });
  }

  // Event listeners para email
  if (emailEditBtn) {
    emailEditBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      enterEditMode(emailView, emailEdit, emailSaveBtn);
    });
  }

  if (emailSaveBtn) {
    emailSaveBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      exitEditMode(emailView, emailEdit, emailSaveBtn);
    });
  }

  if (emailEdit) {
    emailEdit.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        e.stopPropagation();
        exitEditMode(emailView, emailEdit, emailSaveBtn);
      } else if (e.key === "Escape") {
        e.preventDefault();
        e.stopPropagation();
        emailEdit.value = emailView.textContent;
        exitEditMode(emailView, emailEdit, emailSaveBtn);
      }
    });
  }

  // Event listeners para senha
  if (senhaEditBtn) {
    senhaEditBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      enterSenhaEditMode();
    });
  }

  if (senhaSaveBtn) {
    senhaSaveBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      exitSenhaEditMode();
    });
  }

  if (senhaCancelBtn) {
    senhaCancelBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      e.stopImmediatePropagation();
      cancelSenhaEditMode();
    });
  }

  // Event listeners para campos de senha
  if (novaSenhaInput) {
    novaSenhaInput.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        e.stopPropagation();
        exitSenhaEditMode();
      } else if (e.key === "Escape") {
        e.preventDefault();
        e.stopPropagation();
        cancelSenhaEditMode();
      }
    });
  }

  if (confirmarNovaSenhaInput) {
    confirmarNovaSenhaInput.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        e.stopPropagation();
        exitSenhaEditMode();
      }
    });
  }

  if (senhaAtualInput) {
    senhaAtualInput.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        e.stopPropagation();
        exitSenhaEditMode();
      }
    });
  }
});

// Script do dropdown do usuário
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("userBtn");
  const dropdown = document.getElementById("userDropdown");

  btn.addEventListener("click", (e) => {
    e.stopPropagation();
    dropdown.classList.toggle("hidden");
  });

  document.addEventListener("click", (e) => {
    // Não fechar o dropdown se o clique for em botões de edição
    if (
      e.target.closest("#username-edit-btn") ||
      e.target.closest("#username-save") ||
      e.target.closest("#email-edit-btn") ||
      e.target.closest("#email-save") ||
      e.target.closest("#username-edit") ||
      e.target.closest("#email-edit") ||
      e.target.closest("#senha-edit-btn") ||
      e.target.closest("#senha-save") ||
      e.target.closest("#senha-cancel") ||
      e.target.closest("#senha-edit-fields") ||
      e.target.closest("#novaSenhaInput") ||
      e.target.closest("#confirmarNovaSenhaInput") ||
      e.target.closest("#senhaAtualInput")
    ) {
      return;
    }
    dropdown.classList.add("hidden");
  });
});
