document.addEventListener("DOMContentLoaded", function () {
  // Pesquisa
  const btnPesquisar = document.getElementById("btnPesquisar");
  const campoPesquisa = document.getElementById("campoPesquisa");
  const valorPesquisa = document.getElementById("valorPesquisa");
  const togglePesquisaBtn = document.getElementById("togglePesquisaBtn");
  const togglePesquisaText = document.getElementById("togglePesquisaText");
  const tipoPesquisa = document.getElementById("tipoPesquisa");
  const btnPesquisaEspecifica = document.getElementById(
    "btnPesquisaEspecifica"
  );
  const btnPesquisaTag = document.getElementById("btnPesquisaTag");

  // Toggle entre tipos de pesquisa
  if (togglePesquisaBtn) {
    togglePesquisaBtn.addEventListener("click", function () {
      const currentType = tipoPesquisa.value;
      const newType = currentType === "especifico" ? "tags" : "especifico";

      tipoPesquisa.value = newType;
      togglePesquisaText.textContent =
        newType === "especifico" ? "Específico" : "Tags";

      // Atualizar placeholder do input
      const valorPesquisa = document.getElementById("valorPesquisa");
      if (valorPesquisa) {
        valorPesquisa.placeholder =
          newType === "especifico" ? "Valor do campo" : "Alta, Media ou Baixa";
      }
    });
  }

  function atualizarBotoesPesquisa(tipo) {
    if (!btnPesquisaEspecifica || !btnPesquisaTag) return;
    if (tipo === "especifico") {
      btnPesquisaEspecifica.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-[#599e4a] border-green-900 text-green-950 shadow transition-all duration-200 focus:outline-none";
      btnPesquisaTag.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-white border-gray-300 text-gray-400 transition-all duration-200 focus:outline-none";
      valorPesquisa.placeholder = "Valor do campo";
    } else {
      btnPesquisaEspecifica.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-white border-gray-300 text-gray-400 transition-all duration-200 focus:outline-none";
      btnPesquisaTag.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-[#599e4a] border-green-900 text-green-950 shadow transition-all duration-200 focus:outline-none";
      valorPesquisa.placeholder = "Alta, Media ou Baixa";
    }
    tipoPesquisa.value = tipo;
    valorPesquisa.value = "";
  }

  if (btnPesquisaEspecifica && btnPesquisaTag) {
    btnPesquisaEspecifica.addEventListener("click", function () {
      atualizarBotoesPesquisa("especifico");
    });
    btnPesquisaTag.addEventListener("click", function () {
      atualizarBotoesPesquisa("tags");
    });
    // Inicialização
    atualizarBotoesPesquisa(
      tipoPesquisa.value === "tags" ? "tags" : "especifico"
    );
  }

  if (btnPesquisar && campoPesquisa && valorPesquisa) {
    btnPesquisar.addEventListener("click", function () {
      const campo = campoPesquisa.value;
      const valor = valorPesquisa.value;
      const tipo = tipoPesquisa.value;

      if (!campo || !valor) {
        alert("Por favor, selecione um campo e insira um valor para pesquisa.");
        return;
      }

      // Validação específica para pesquisa por tags
      if (tipo === "tags") {
        const valoresValidos = ["alta", "media", "baixa"];
        if (!valoresValidos.includes(valor.toLowerCase())) {
          alert("Para pesquisa por tags, use apenas: Alta, Media ou Baixa");
          return;
        }

        // Para pesquisa por tags, não permitir campos de texto
        const camposTexto = ["por-nome", "por-categoria"];
        if (camposTexto.includes(campo)) {
          alert(
            "Pesquisa por tags não está disponível para campos de texto. Use campos numéricos."
          );
          return;
        }
      } else {
        // validação numérica para pesquisa específica
        const numericFields = [
          "custoPerCapita",
          "custoTotal",
          "por-rendimento",
          "por-numero",
          "vtc",
          "gramasPTN",
          "gramasCHO",
          "gramasLIP",
          "gramasSodio",
          "gramasSaturada",
        ];
        if (numericFields.includes(campo) && isNaN(valor)) {
          alert("Por favor, insira um valor numérico válido.");
          return;
        }
      }

      let url;
      if (tipo === "tags") {
        url = `/nutricionista/fichas/por-tag?campo=${encodeURIComponent(
          campo
        )}&tag=${encodeURIComponent(valor)}`;
      } else {
        // URLs existentes para pesquisa específica
        switch (campo) {
          case "custoPerCapita":
            url = `/nutricionista/fichas/custoPerCapita?custoPerCapita=${valor}`;
            break;
          case "custoTotal":
            url = `/nutricionista/fichas/custoTotal?custoTotal=${valor}`;
            break;
          case "por-nome":
            url = `/nutricionista/fichas/por-nome?nome=${encodeURIComponent(
              valor
            )}`;
            break;
          case "por-categoria":
            url = `/nutricionista/fichas/por-categoria?categoria=${encodeURIComponent(
              valor
            )}`;
            break;
          case "por-rendimento":
            url = `/nutricionista/fichas/por-rendimento?rendimento=${valor}`;
            break;
          case "por-numero":
            url = `/nutricionista/fichas/por-numero?numero=${valor}`;
            break;
          case "vtc":
            url = `/nutricionista/fichas/por-vtc?vtc=${valor}`;
            break;
          case "gramasPTN":
            url = `/nutricionista/fichas/por-gramas-ptn?gramasPTN=${valor}`;
            break;
          case "gramasCHO":
            url = `/nutricionista/fichas/por-gramas-cho?gramasCHO=${valor}`;
            break;
          case "gramasLIP":
            url = `/nutricionista/fichas/por-gramas-lip?gramasLIP=${valor}`;
            break;
          case "gramasSodio":
            url = `/nutricionista/fichas/por-gramas-sodio?gramasSodio=${valor}`;
            break;
          case "gramasSaturada":
            url = `/nutricionista/fichas/por-gramas-saturada?gramasSaturada=${valor}`;
            break;
          case "status":
            url = `/nutricionista/fichas/por-status?status=${valor}`;
            break;
          default:
            alert("Campo de pesquisa inválido.");
            return;
        }
      }

      window.location.href = url;
    });
  }

  // Toggle ATIVA ↔ INATIVA
  const toggleStatusBtn = document.getElementById("toggleStatusBtn");
  if (toggleStatusBtn) {
    toggleStatusBtn.addEventListener("click", function () {
      const currentStatus = this.getAttribute("data-current-status");
      const newStatus = currentStatus === "ATIVA" ? "INATIVA" : "ATIVA";

      // Atualiza texto e atributo
      const span = this.querySelector("span");
      span.textContent = newStatus === "ATIVA" ? "Desarquivadas" : "Arquivadas";
      this.setAttribute("data-current-status", newStatus);

      window.location.href = `/nutricionista/fichas/por-status?status=${newStatus}`;
    });
  }

  // Toggle COMPLETA ↔ INCOMPLETA
  const toggleCriacaoBtn = document.getElementById("toggleStatusCriacaoBtn");
  if (toggleCriacaoBtn) {
    toggleCriacaoBtn.addEventListener("click", function () {
      const current = this.getAttribute("data-current-status-criacao");
      const next = current === "COMPLETA" ? "INCOMPLETA" : "COMPLETA";
      window.location.href = `/nutricionista/fichas/por-statusCriacao?statusCriacao=${next}`;
    });
  }
});
