document.addEventListener("DOMContentLoaded", function () {
  let ingredienteCounter = 0;
  const ingredientesAdicionados = [];

  initCalculoFC();
  initAutoResizeTextareas();
  initStatusCriacao();
  initCalculoPerCapita();
  initPesoPorcao();
  initAguaCheckbox();
  calcularPerfilNutricional();
  initIngredienteAutocomplete();
  initCalculoCustoUsado();
  initCalculoFCC();

  // Função específica para edição - remoção de ingredientes existentes
  window.removerIngrediente = function (element) {
    const row = element.closest("tr");
    if (row) {
      row.remove();
      atualizarCustoTotal();
      calcularPerfilNutricional();

      // Dispara evento para recalcular FCC
      document.dispatchEvent(new Event("ingredienteRemovido"));
    }
  };

  // Função para adicionar novos ingredientes na edição
  window.adicionarIngrediente = function () {
    const select = document.getElementById("ingredienteSelect");
    const ingredienteId = select.value;
    const selectedOption = select.options[select.selectedIndex];
    if (!ingredienteId) return;

    const medidaCaseira = document.getElementById("medidaCaseira").value;
    const pb = parseFloat(document.getElementById("pb").value) || 0;
    const pl = parseFloat(document.getElementById("pl").value) || 0;
    const custoKg = parseFloat(document.getElementById("custoKg").value) || 0;

    if (!medidaCaseira || !pb || !custoKg) {
      alert("Preencha todos os campos do ingrediente!");
      return;
    }

    const fcFormatado = document.querySelector(".fc-display").textContent;
    const fcValue = document.querySelector(".fc-value").value || 0;
    const custoUsadoText = document.getElementById("custoUsado").value;
    const custoUsadoVal = document.querySelector(
      '[name="custoUsadoValue"]'
    ).value;

    // Encontra o próximo índice disponível
    const rows = document.querySelectorAll(".ingrediente-row");
    const idx = rows.length;

    const row = document.createElement("tr");
    row.className = "border bg-gray-50 ingrediente-row";

    row.innerHTML = `
            <td class="px-4 py-2 border border-black text-center">${
              selectedOption.text
            }</td>
            <td class="px-4 py-2 border border-black text-center">${medidaCaseira}</td>
            <td class="px-4 py-2 border border-black text-center">
              <div class="flex items-center justify-center">
                ${pb}<span class="ml-1 text-sm">g</span>
              </div>
              <input type="hidden" name="ingredientes[${idx}].pb" value="${pb}"/>
            </td>
            <td class="px-4 py-2 border border-black text-center">
              <div class="flex items-center justify-center">
                ${pl}<span class="ml-1 text-sm">g</span>
              </div>
              <input type="hidden" name="ingredientes[${idx}].pl" value="${pl}"/>
            </td>
            <td class="px-2 py-2 border border-black text-center">
              ${fcFormatado}
              <input type="hidden" name="ingredientes[${idx}].fc" value="${fcValue}"/>
            </td>
            <td class="px-4 py-2 border border-black text-center">
              <div class="flex items-center justify-center">
                <span class="mr-1 text-sm">R$</span>${custoKg.toFixed(2)}
              </div>
              <input type="hidden" name="ingredientes[${idx}].custoKg" value="${custoKg.toFixed(
      2
    )}"/>
            </td>
            <td class="px-4 py-2 border border-black text-center">
              <div class="flex items-center justify-center">
                <span class="mr-1 text-sm">R$</span>${custoUsadoText}
              </div>
              <input type="hidden" name="ingredientes[${idx}].custoUsado" value="${custoUsadoVal}"/>
            </td>
            <td class="px-4 py-2 border border-black text-center">
              <button type="button" onclick="removerIngrediente(this)"
                      class="w-8 h-8 rounded-full border border-black bg-red-900 hover:bg-red-950 text-white flex items-center justify-center mx-auto">
                <i class="fa-solid fa-minus text-2xl"></i>
              </button>
            </td>
            <input type="hidden" name="ingredientes[${idx}].ingredienteId" value="${ingredienteId}">
            <input type="hidden" name="ingredientes[${idx}].medidaCaseira" value="${medidaCaseira}">
            <input type="hidden" class="ingrediente-ptn" value="${
              selectedOption.dataset.ptn || 0
            }">
            <input type="hidden" class="ingrediente-cho" value="${
              selectedOption.dataset.cho || 0
            }">
            <input type="hidden" class="ingrediente-lip" value="${
              selectedOption.dataset.lip || 0
            }">
            <input type="hidden" class="ingrediente-sodio" value="${
              selectedOption.dataset.sodio || 0
            }">
            <input type="hidden" class="ingrediente-saturada" value="${
              selectedOption.dataset.saturada || 0
            }">
        `;

    document.getElementById("ingredientesAdicionados").appendChild(row);
    select.value = "";
    document.getElementById("medidaCaseira").value = "";
    document.getElementById("pb").value = "";
    document.getElementById("pl").value = "";
    document.getElementById("custoKg").value = "";
    document.getElementById("custoUsado").value = "";

    select.focus();
    calcularPerfilNutricional();
    atualizarCustoTotal();
    atualizarCustoUsadoTodosIngredientes();

    // Dispara evento para recalcular FCC
    document.dispatchEvent(new Event("ingredienteAdicionado"));
  };

  function atualizarCustoTotal() {
    let total = 0;
    document.querySelectorAll('input[name$=".custoUsado"]').forEach((input) => {
      total += parseFloat(input.value) || 0;
    });
    document.getElementById("custoTotal").value = total.toFixed(2);
  }

  function initIngredienteAutocomplete() {
    const searchInput = document.getElementById("ingredienteSearch");
    const suggestionsContainer = document.getElementById(
      "ingredienteSuggestions"
    );
    const select = document.getElementById("ingredienteSelect");

    const ingredientesDisponiveis = Array.from(select.options)
      .filter((opt) => opt.value)
      .map((opt) => ({
        id: opt.value,
        nome: opt.text.trim().toLowerCase(),
        text: opt.text.trim(),
        ptn: opt.dataset.ptn,
        cho: opt.dataset.cho,
        lip: opt.dataset.lip,
        sodio: opt.dataset.sodio,
        saturada: opt.dataset.saturada,
      }));

    function renderSuggestions(query) {
      if (!query) {
        return suggestionsContainer.classList.add("hidden");
      }
      const q = query.trim().toLowerCase();
      const filtered = ingredientesDisponiveis.filter((ing) =>
        ing.nome.includes(q)
      );
      if (filtered.length === 0) {
        suggestionsContainer.innerHTML = `<div class="px-4 py-2 text-gray-500">Nenhum ingrediente encontrado</div>`;
      } else {
        suggestionsContainer.innerHTML = filtered
          .map(
            (ing) => `
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
                     data-id="${ing.id}"
                     data-nome="${ing.nome}">
                  ${ing.text}
                </div>
            `
          )
          .join("");
      }
      suggestionsContainer.classList.remove("hidden");
    }

    searchInput.addEventListener("input", (e) => {
      const query = e.target.value.trim().toLowerCase();
      renderSuggestions(query);

      const ingredienteExato = ingredientesDisponiveis.find(
        (ing) => ing.nome === query
      );

      if (ingredienteExato) {
        select.value = ingredienteExato.id;
      } else {
        select.value = "";
      }
    });

    suggestionsContainer.addEventListener("click", (e) => {
      const item = e.target.closest("[data-id]");
      if (!item) return;
      select.value = item.dataset.id;
      searchInput.value = item.textContent.trim();
      suggestionsContainer.classList.add("hidden");
      document.getElementById("medidaCaseira").focus();
    });

    document.addEventListener("click", (e) => {
      if (
        !searchInput.contains(e.target) &&
        !suggestionsContainer.contains(e.target)
      ) {
        suggestionsContainer.classList.add("hidden");
      }
    });

    select.classList.add("hidden");
  }

  function calcularPerfilNutricional() {
    const tabelaNutricional = document.getElementById(
      "nutricionalIngredientes"
    );
    tabelaNutricional.innerHTML = "";

    let totais = {
      gramasPTN: 0,
      gramasCHO: 0,
      gramasLIP: 0,
      gramasSodio: 0,
      gramasSaturada: 0,
      kcalPTN: 0,
      kcalCHO: 0,
      kcalLIP: 0,
    };

    document.querySelectorAll(".ingrediente-row").forEach((row) => {
      // Verifica se o ingrediente está completo (campo de peso preenchido)
      const plInput = row.querySelector('[name$=".pl"]');
      if (!plInput || !plInput.value || parseFloat(plInput.value) <= 0) {
        // Linha incompleta: mantém visível mas não calcula
        return;
      }

      const nutriData = calcularNutrientesPorIngrediente(row);
      adicionarLinhaNutricional(tabelaNutricional, nutriData);

      Object.keys(totais).forEach((key) => {
        totais[key] += nutriData[key] || 0;
      });
    });

    const totalVTC = totais.kcalPTN + totais.kcalCHO + totais.kcalLIP;
    const porcentagens = calcularPorcentagensNutrientes(
      totalVTC,
      totais.kcalPTN,
      totais.kcalCHO,
      totais.kcalLIP
    );

    adicionarLinhasTotais(tabelaNutricional, totais, porcentagens);
    atualizarCamposNutricionais(totalVTC, totais, porcentagens);
  }

  function calcularNutrientesPorIngrediente(row) {
    const pl = parseFloat(row.querySelector('[name$=".pl"]').value) || 0;

    const fatores = ["ptn", "cho", "lip", "sodio", "saturada"].map(
      (nutriente) => {
        const el = row.querySelector(`.ingrediente-${nutriente}`);
        const value = el && el.value ? parseFloat(el.value) : 0;
        return { nutriente, value };
      }
    );

    return {
      nome: row.querySelector("td:first-child").textContent,
      pl,
      gramasPTN: (fatores.find((f) => f.nutriente === "ptn").value / 100) * pl,
      gramasCHO: (fatores.find((f) => f.nutriente === "cho").value / 100) * pl,
      gramasLIP: (fatores.find((f) => f.nutriente === "lip").value / 100) * pl,
      gramasSodio:
        (fatores.find((f) => f.nutriente === "sodio").value / 100) * pl,
      gramasSaturada:
        (fatores.find((f) => f.nutriente === "saturada").value / 100) * pl,
      kcalPTN:
        (fatores.find((f) => f.nutriente === "ptn").value / 100) * pl * 4,
      kcalCHO:
        (fatores.find((f) => f.nutriente === "cho").value / 100) * pl * 4,
      kcalLIP:
        (fatores.find((f) => f.nutriente === "lip").value / 100) * pl * 9,
    };
  }

  // Funções auxiliares de renderização
  function adicionarLinhaNutricional(tabela, data) {
    const row = document.createElement("tr");
    row.className = "border border-black";
    row.innerHTML = `
        <td class="px-2 py-2 border border-black">${data.nome}</td>
        <td class="px-2 py-2 border border-black">${data.pl.toFixed(2)}</td>
        <td class="px-2 py-2 border border-black">${data.gramasPTN.toFixed(
          2
        )}</td>
        <td class="px-2 py-2 border border-black">${data.gramasCHO.toFixed(
          2
        )}</td>
        <td class="px-2 py-2 border border-black">${data.gramasLIP.toFixed(
          2
        )}</td>
        <td class="px-2 py-2 border border-black">${data.gramasSodio.toFixed(
          2
        )}</td>
        <td class="px-2 py-2 border border-black">${data.gramasSaturada.toFixed(
          2
        )}</td>
    `;
    tabela.appendChild(row);
  }

  function calcularPorcentagensNutrientes(totalVTC, kcalPTN, kcalCHO, kcalLIP) {
    return {
      porcentPTN: totalVTC === 0 ? 0 : (kcalPTN / totalVTC) * 100,
      porcentCHO: totalVTC === 0 ? 0 : (kcalCHO / totalVTC) * 100,
      porcentLIP: totalVTC === 0 ? 0 : (kcalLIP / totalVTC) * 100,
    };
  }

  function adicionarLinhasTotais(tabela, totais, porcentagens) {
    adicionarLinhaTotal(
      tabela,
      "Gramas",
      totais.gramasPTN,
      totais.gramasCHO,
      totais.gramasLIP,
      totais.gramasSodio,
      totais.gramasSaturada
    );

    adicionarLinhaTotal(
      tabela,
      "Kcal",
      totais.kcalPTN,
      totais.kcalCHO,
      totais.kcalLIP
    );

    adicionarLinhaTotal(
      tabela,
      "%",
      porcentagens.porcentPTN,
      porcentagens.porcentCHO,
      porcentagens.porcentLIP
    );
  }

  function adicionarLinhaTotal(
    tabela,
    tipo,
    ptn,
    cho,
    lip,
    sodio = null,
    saturada = null
  ) {
    const row = document.createElement("tr");
    row.className = "border border-black font-bold bg-gray-100";

    const cols =
      sodio !== null
        ? `<td>${sodio.toFixed(2)}</td><td>${saturada.toFixed(2)}</td>`
        : `<td colspan="2"></td>`;

    row.innerHTML = `
        <td class="px-2 py-2 border border-black">${tipo}</td>
        <td class="px-2 py-2 border border-black"></td>
        <td class="px-2 py-2 border border-black">${ptn.toFixed(2)}</td>
        <td class="px-2 py-2 border border-black">${cho.toFixed(2)}</td>
        <td class="px-2 py-2 border border-black">${lip.toFixed(2)}</td>
        ${cols}
    `;
    tabela.appendChild(row);
  }

  function atualizarCamposNutricionais(totalVTC, totais, porcentagens) {
    const vtcDisplay = document.getElementById("vtc-display");
    if (vtcDisplay) vtcDisplay.textContent = totalVTC.toFixed(2);

    const campos = {
      vtc: totalVTC,
      kcalPtn: totais.kcalPTN,
      kcalCho: totais.kcalCHO,
      kcalLip: totais.kcalLIP,
      gramasPtn: totais.gramasPTN,
      gramasCho: totais.gramasCHO,
      gramasLip: totais.gramasLIP,
      gramasSodio: totais.gramasSodio,
      gramasSaturada: totais.gramasSaturada,
      porcentPtn: porcentagens.porcentPTN,
      porcentCho: porcentagens.porcentCHO,
      porcentLip: porcentagens.porcentLIP,
    };

    Object.entries(campos).forEach(([id, valor]) => {
      const campo = document.getElementById(id);
      if (campo) campo.value = parseFloat(valor).toFixed(2);
    });
  }

  function initCalculoFC() {
    const pbInput = document.getElementById("pb");
    const plInput = document.getElementById("pl");
    const fcDisplayElem = document.querySelector(".fc-display");
    const fcValueInput = document.querySelector(".fc-value");

    function atualizarFC() {
      const pb = parseFloat(pbInput.value) || 0;
      const pl = parseFloat(plInput.value) || 0;
      const fc = pl === 0 ? 0 : pb / pl;
      if (fc === 0) {
        fcDisplayElem.textContent = "0.00";
        fcDisplayElem.classList.add("text-gray-400");
      } else {
        fcDisplayElem.textContent = fc.toFixed(2);
        fcDisplayElem.classList.remove("text-gray-400");
      }
    }

    pbInput.addEventListener("input", atualizarFC);
    plInput.addEventListener("input", atualizarFC);
    atualizarFC();
  }

  function initCalculoCustoUsado() {
    const pbInput = document.getElementById("pb");
    const custoKgInput = document.getElementById("custoKg");
    const custoUsadoDisplay = document.getElementById("custoUsado"); // seu input readonly
    const custoUsadoValue = document.createElement("input"); // hidden temporário

    // pra submissão, vamos anexar esse hidden no form
    custoUsadoValue.type = "hidden";
    custoUsadoValue.name = "custoUsadoValue";
    document.getElementById("form-ficha").appendChild(custoUsadoValue);

    function atualizarCustoUsado() {
      const pb = parseFloat(pbInput.value) || 0;
      const custoKg = parseFloat(custoKgInput.value) || 0;
      const usado = (custoKg * pb) / 1000;
      if (usado === 0) {
        custoUsadoDisplay.value = "";
        custoUsadoDisplay.classList.add("text-gray-400");
      } else {
        custoUsadoDisplay.value = usado.toFixed(2);
        custoUsadoDisplay.classList.remove("text-gray-400");
      }
      custoUsadoValue.value = usado.toFixed(2);
    }

    pbInput.addEventListener("input", atualizarCustoUsado);
    custoKgInput.addEventListener("input", atualizarCustoUsado);
    atualizarCustoUsado();
  }

  function initAutoResizeTextareas() {
    function setupTextarea(id) {
      const textarea = document.getElementById(id);
      if (textarea) {
        textarea.addEventListener("input", function () {
          this.style.height = "auto";
          this.style.height = this.scrollHeight + "px";
        });
      }
    }

    setupTextarea("autoExpandTextarea");
    setupTextarea("autoExpandTextarea2");
  }

  function initStatusCriacao() {
    const checkbox = document.getElementById("statusCheckbox");
    const statusField = document.getElementById("statusCriacaoField");

    function updateStatusCriacao() {
      statusField.value = checkbox.checked ? "COMPLETA" : "INCOMPLETA";
    }

    if (checkbox) {
      checkbox.addEventListener("change", updateStatusCriacao);
      updateStatusCriacao();
    }
  }

  function initCalculoPerCapita() {
    const custoTotalInput = document.getElementById("custoTotal");
    const numeroPorcoesInput = document.getElementById("numeroPorcoes");
    const perCapitaInput = document.getElementById("custoPerCapita");

    function atualizarPerCapita() {
      const total = parseFloat(custoTotalInput.value) || 0;
      const porcoes = parseFloat(numeroPorcoesInput.value) || 1;
      const resultado = porcoes > 0 ? total / porcoes : 0;

      if (resultado > 0) {
        perCapitaInput.value = resultado.toFixed(2);
      }
    }

    custoTotalInput.addEventListener("input", atualizarPerCapita);
    numeroPorcoesInput.addEventListener("input", atualizarPerCapita);
    atualizarPerCapita();
  }

  function initPesoPorcao() {
    const numEl = document.getElementById("numeroPorcoes");
    const rendEl = document.getElementById("rendimento");
    const pesoPorcaoEl = document.getElementById("pesoPorcao");

    function updateNumeroPorcoes() {
      const pesoPorcao = parseFloat(pesoPorcaoEl.value) || 0;
      const rendimento = parseFloat(rendEl.value) || 0;

      if (pesoPorcao > 0 && rendimento > 0) {
        // Calcula quantas porções podem ser feitas com o rendimento e peso informado
        // Sempre arredonda para menos (Math.floor)
        const numeroPorcoes = Math.floor(rendimento / pesoPorcao);
        numEl.value = numeroPorcoes > 0 ? numeroPorcoes : "";
      } else {
        numEl.value = "";
      }

      // Atualiza o custo per capita quando o número de porções mudar
      const custoTotalInput = document.getElementById("custoTotal");
      const perCapitaInput = document.getElementById("custoPerCapita");
      if (custoTotalInput && perCapitaInput) {
        const total = parseFloat(custoTotalInput.value) || 0;
        const porcoes = parseFloat(numEl.value) || 1;
        const resultado = porcoes > 0 ? total / porcoes : 0;
        if (resultado > 0) {
          perCapitaInput.value = resultado.toFixed(2);
        }
      }
    }

    function updatePesoPorcao() {
      const n = parseFloat(numEl.value) || 0;
      const r = parseFloat(rendEl.value) || 0;
      pesoPorcaoEl.value = n > 0 && r > 0 ? (r / n).toFixed(2) : "";
    }

    // Quando o peso da porção ou rendimento mudar, recalcula o número de porções
    pesoPorcaoEl.addEventListener("input", updateNumeroPorcoes);
    rendEl.addEventListener("input", updateNumeroPorcoes);

    // Inicializa os valores
    updateNumeroPorcoes();
  }

  function initAguaCheckbox() {
    const checkbox = document.getElementById("aguaCheckbox");
    const aguaInputs = document.getElementById("aguaInputs");

    if (checkbox && aguaInputs) {
      checkbox.addEventListener("change", function () {
        aguaInputs.classList.toggle("hidden", !this.checked);
      });
    }
  }

  function initCalculoFCC() {
    const rendimentoInput = document.getElementById("rendimento");
    const qntdAguaInput = document.querySelector('input[name*="qntdAgua"]');
    const porcentAguaInput = document.querySelector(
      'input[name*="porcentAgua"]'
    );
    const fccInput = document.querySelector('input[name*="fcc"]');

    // Limpa o campo FCC no início
    if (fccInput) {
      fccInput.value = "";
    }

    function calcularFCC() {
      // Soma todos os PLs dos ingredientes
      let somaPLs = 0;
      document.querySelectorAll('input[name$=".pl"]').forEach((input) => {
        somaPLs += parseFloat(input.value) || 0;
      });

      const rendimento = parseFloat(rendimentoInput.value) || 0;
      const qntdAgua = parseFloat(qntdAguaInput?.value) || 0;
      const porcentAgua = parseFloat(porcentAguaInput?.value) || 0;

      // Calcula a água que faz parte do prato final (não foi perdida na hidratação)
      const aguaNoPrato = (qntdAgua * porcentAgua) / 100;

      // Peso total dos ingredientes crus (PLs + água que faz parte do prato)
      const pesoCru = somaPLs + aguaNoPrato;

      // Calcula o FCC: Rendimento / Peso Cru
      let fcc = 0;
      if (pesoCru > 0) {
        fcc = rendimento / pesoCru;
      }

      // Atualiza o campo FCC
      if (fccInput) {
        if (pesoCru > 0 && rendimento > 0) {
          fccInput.value = fcc.toFixed(4);
        } else {
          fccInput.value = "";
        }
      }

      console.log(`FCC Calculado: ${fcc.toFixed(4)}`);
      console.log(`Soma PLs: ${somaPLs}g`);
      console.log(`Água no prato: ${aguaNoPrato}g`);
      console.log(`Peso cru total: ${pesoCru}g`);
      console.log(`Rendimento: ${rendimento}g`);
    }

    // Event listeners para recalcular FCC quando os valores mudarem
    rendimentoInput.addEventListener("input", calcularFCC);
    if (qntdAguaInput) qntdAguaInput.addEventListener("input", calcularFCC);
    if (porcentAguaInput)
      porcentAguaInput.addEventListener("input", calcularFCC);

    // Recalcula FCC quando ingredientes são adicionados/removidos
    document.addEventListener("ingredienteAdicionado", calcularFCC);
    document.addEventListener("ingredienteRemovido", calcularFCC);
    document.addEventListener("ingredienteModificado", calcularFCC);

    // Calcula FCC inicial
    calcularFCC();
  }

  function atualizarCustoUsadoTodosIngredientes() {
    document.querySelectorAll(".ingrediente-row").forEach(function (row) {
      const pbInput = row.querySelector('input[name$=".pb"]');
      const custoKgInput = row.querySelector('input[name$=".custoKg"]');
      const custoUsadoInput = row.querySelector(
        'input[name$=".custoUsado"], .custo-usado-edit'
      );
      if (pbInput && custoKgInput && custoUsadoInput) {
        const pb = parseFloat(pbInput.value) || 0;
        const custoKg = parseFloat(custoKgInput.value) || 0;
        const usado = (custoKg * pb) / 1000;
        custoUsadoInput.value = usado > 0 ? usado.toFixed(2) : "";
      }
    });
  }

  // Adiciona listeners para todos os PB e custoKg dos ingredientes existentes
  document.querySelectorAll(".ingrediente-row").forEach(function (row) {
    const pbInput = row.querySelector('input[name$=".pb"]');
    const custoKgInput = row.querySelector('input[name$=".custoKg"]');
    if (pbInput)
      pbInput.addEventListener("input", atualizarCustoUsadoTodosIngredientes);
    if (custoKgInput)
      custoKgInput.addEventListener(
        "input",
        atualizarCustoUsadoTodosIngredientes
      );
  });

  // Atualiza ao carregar a página
  atualizarCustoUsadoTodosIngredientes();

  document
    .getElementById("form-ficha")
    .addEventListener("submit", function (e) {
      calcularPerfilNutricional();
      return true;
    });

  document.querySelectorAll(".ingrediente-row").forEach(function (row) {
    const pbInput = row.querySelector('input[name$=".pb"]');
    const plInput = row.querySelector('input[name$=".pl"]');
    const fcInput = row.querySelector('input[name$=".fc"]');

    function atualizarFC() {
      const pb = parseFloat(pbInput.value) || 0;
      const pl = parseFloat(plInput.value) || 0;
      const fc = pl === 0 ? 0 : pb / pl;
      fcInput.value = fc.toFixed(2);

      // Recalcula FCC quando PL for modificado
      document.dispatchEvent(new Event("ingredienteModificado"));
    }

    if (pbInput && plInput && fcInput) {
      pbInput.addEventListener("input", atualizarFC);
      plInput.addEventListener("input", atualizarFC);
      atualizarFC();
    }
  });
});
