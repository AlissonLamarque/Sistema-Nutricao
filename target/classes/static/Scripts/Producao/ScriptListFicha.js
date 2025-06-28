document.addEventListener("DOMContentLoaded", function () {
  const btnPesquisar = document.getElementById("btnPesquisar");
  const campoPesquisa = document.getElementById("campoPesquisa");
  const valorPesquisa = document.getElementById("valorPesquisa");
  const btnPesquisaEspecifica = document.getElementById(
    "btnPesquisaEspecifica"
  );
  const btnPesquisaTag = document.getElementById("btnPesquisaTag");
  const tipoPesquisa = document.getElementById("tipoPesquisa");

  function atualizarBotoesPesquisa(tipo) {
    if (!btnPesquisaEspecifica || !btnPesquisaTag) return;
    if (tipo === "especifico") {
      btnPesquisaEspecifica.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-green-200 border-green-700 text-green-900 shadow transition-all duration-200 focus:outline-none";
      btnPesquisaTag.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-white border-gray-300 text-gray-400 transition-all duration-200 focus:outline-none";
      valorPesquisa.placeholder = "Valor do campo";
    } else {
      btnPesquisaEspecifica.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-white border-gray-300 text-gray-400 transition-all duration-200 focus:outline-none";
      btnPesquisaTag.className =
        "group relative flex items-center justify-center w-10 h-10 rounded-full border-2 bg-green-200 border-green-700 text-green-900 shadow transition-all duration-200 focus:outline-none";
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
      url = `/producao/fichas/por-tag?campo=${encodeURIComponent(
        campo
      )}&tag=${encodeURIComponent(valor)}`;
    } else {
      // URLs existentes para pesquisa específica
      switch (campo) {
        case "custoPerCapita":
          url = `/producao/fichas/custoPerCapita?custoPerCapita=${valor}`;
          break;
        case "custoTotal":
          url = `/producao/fichas/custoTotal?custoTotal=${valor}`;
          break;
        case "por-nome":
          url = `/producao/fichas/por-nome?nome=${encodeURIComponent(valor)}`;
          break;
        case "por-categoria":
          url = `/producao/fichas/por-categoria?categoria=${encodeURIComponent(
            valor
          )}`;
          break;
        case "por-rendimento":
          url = `/producao/fichas/por-rendimento?rendimento=${valor}`;
          break;
        case "por-numero":
          url = `/producao/fichas/por-numero?numero=${valor}`;
          break;
        case "vtc":
          url = `/producao/fichas/por-vtc?vtc=${valor}`;
          break;
        case "gramasPTN":
          url = `/producao/fichas/por-gramas-ptn?gramasPTN=${valor}`;
          break;
        case "gramasCHO":
          url = `/producao/fichas/por-gramas-cho?gramasCHO=${valor}`;
          break;
        case "gramasLIP":
          url = `/producao/fichas/por-gramas-lip?gramasLIP=${valor}`;
          break;
        case "gramasSodio":
          url = `/producao/fichas/por-gramas-sodio?gramasSodio=${valor}`;
          break;
        case "gramasSaturada":
          url = `/producao/fichas/por-gramas-saturada?gramasSaturada=${valor}`;
          break;
        case "status":
          url = `/producao/fichas/por-status?status=${valor}`;
          break;
        default:
          alert("Campo de pesquisa inválido.");
          return;
      }
    }

    window.location.href = url;
  });
});
