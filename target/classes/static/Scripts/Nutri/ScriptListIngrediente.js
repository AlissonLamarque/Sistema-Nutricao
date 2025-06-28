document.addEventListener("DOMContentLoaded", function () {
  // Pesquisa
  const btnPesquisar = document.getElementById("btnPesquisar");
  const campoPesquisa = document.getElementById("campoPesquisa");
  const valorPesquisa = document.getElementById("valorPesquisa");
  const viewType = document.getElementById("viewType")?.value || "meus"; // 'meus' ou 'taco'
  const btnPesquisaEspecifica = document.getElementById(
    "btnPesquisaEspecifica"
  );
  const btnPesquisaTag = document.getElementById("btnPesquisaTag");
  const tipoPesquisa = document.getElementById("tipoPesquisa");

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

        // Para pesquisa por tags, não permitir campo "nome"
        if (campo === "nome") {
          alert(
            "Pesquisa por tags não está disponível para o campo Nome. Use PTN, CHO, LIP, Sódio ou Gorduras Saturadas."
          );
          return;
        }
      } else {
        // validação numérica para pesquisa específica
        const numericFields = ["PTN", "CHO", "LIP", "sodio", "gorduras"];
        if (numericFields.includes(campo) && isNaN(valor)) {
          alert("Por favor, insira um valor numérico válido.");
          return;
        }
      }

      let url;

      if (viewType === "taco") {
        // URLs para pesquisa na tabela TACO (usuário4)
        if (tipo === "tags") {
          url = `/nutricionista/ingredientes/usuario4/por-tag?campo=${encodeURIComponent(
            campo
          )}&tag=${encodeURIComponent(valor)}`;
        } else {
          // URLs existentes para pesquisa específica
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
        }
      } else {
        // URLs para pesquisa nos ingredientes próprios
        if (tipo === "tags") {
          url = `/nutricionista/ingredientes/por-tag?campo=${encodeURIComponent(
            campo
          )}&tag=${encodeURIComponent(valor)}`;
        } else {
          // URLs existentes para pesquisa específica
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
