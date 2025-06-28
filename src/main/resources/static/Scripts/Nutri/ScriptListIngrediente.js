document.addEventListener("DOMContentLoaded", function () {
  // Pesquisa
  const btnPesquisar = document.getElementById("btnPesquisar");
  const campoPesquisa = document.getElementById("campoPesquisa");
  const valorPesquisa = document.getElementById("valorPesquisa");
  const viewType = document.getElementById("viewType")?.value || "meus"; // 'meus' ou 'taco'

  if (btnPesquisar && campoPesquisa && valorPesquisa) {
    btnPesquisar.addEventListener("click", function () {
      const campo = campoPesquisa.value;
      const valor = valorPesquisa.value;

      if (!campo || !valor) {
        alert("Por favor, selecione um campo e insira um valor para pesquisa.");
        return;
      }

      // validação numérica
      const numericFields = ["PTN", "CHO", "LIP", "sodio", "gorduras"];
      if (numericFields.includes(campo) && isNaN(valor)) {
        alert("Por favor, insira um valor numérico válido.");
        return;
      }

      let url;

      if (viewType === "taco") {
        // URLs para pesquisa na tabela TACO (usuário4)
        switch (campo) {
          case "nome":
            url = `/nutricionista/ingredientes/usuario4/por-nome?nome=${encodeURIComponent(
              valor
            )}`;
            break;
          case "PTN":
            url = `/nutricionista/ingredientes/usuario4/por-ptn?ptn=${valor}`;
            break;
          case "CHO":
            url = `/nutricionista/ingredientes/usuario4/por-cho?cho=${valor}`;
            break;
          case "LIP":
            url = `/nutricionista/ingredientes/usuario4/por-lip?lip=${valor}`;
            break;
          case "sodio":
            url = `/nutricionista/ingredientes/usuario4/por-sodio?sodio=${valor}`;
            break;
          case "gorduras":
            url = `/nutricionista/ingredientes/usuario4/por-gordura-saturada?gorduraSaturada=${valor}`;
            break;
          default:
            alert("Campo de pesquisa inválido.");
            return;
        }
      } else {
        // URLs para pesquisa nos ingredientes próprios
        switch (campo) {
          case "nome":
            url = `/nutricionista/ingredientes/por-nome?nome=${encodeURIComponent(
              valor
            )}`;
            break;
          case "PTN":
            url = `/nutricionista/ingredientes/por-ptn?ptn=${valor}`;
            break;
          case "CHO":
            url = `/nutricionista/ingredientes/por-cho?cho=${valor}`;
            break;
          case "LIP":
            url = `/nutricionista/ingredientes/por-lip?lip=${valor}`;
            break;
          case "sodio":
            url = `/nutricionista/ingredientes/por-sodio?sodio=${valor}`;
            break;
          case "gorduras":
            url = `/nutricionista/ingredientes/por-gordura-saturada?gorduraSaturada=${valor}`;
            break;
          default:
            alert("Campo de pesquisa inválido.");
            return;
        }
      }

      window.location.href = url;
    });
  }

  // Toggle ATIVA ↔ INATIVA para ingredientes
  const toggleStatusBtn2 = document.getElementById("toggleStatusBtn2");
  if (toggleStatusBtn2) {
    toggleStatusBtn2.addEventListener("click", function () {
      const currentStatus = this.getAttribute("data-current-status");
      const newStatus = currentStatus === "ATIVA" ? "INATIVA" : "ATIVA";

      // Atualiza texto e atributo
      const span = this.querySelector("span");
      span.textContent = newStatus === "ATIVA" ? "Desarquivadas" : "Arquivadas";
      this.setAttribute("data-current-status", newStatus);

      // Redireciona para o filtro de status, mantendo a view
      const viewType = document.getElementById("viewType")?.value || "meus";
      window.location.href = `/nutricionista/ingredientes/por-status?status=${newStatus}&view=${viewType}`;
    });
  }
});
