document.addEventListener('DOMContentLoaded', function() {
    // Pesquisa
    const btnPesquisar = document.getElementById('btnPesquisar');
    const campoPesquisa = document.getElementById('campoPesquisa');
    const valorPesquisa = document.getElementById('valorPesquisa');
    if (btnPesquisar && campoPesquisa && valorPesquisa) {
        btnPesquisar.addEventListener('click', function() {
            const campo = campoPesquisa.value;
            const valor = valorPesquisa.value;

            if (!campo || !valor) {
                alert('Por favor, selecione um campo e insira um valor para pesquisa.');
                return;
            }

            let url;
            switch (campo) {
                case 'custoPerCapita':
                    url = `/nutricionista/fichas/custoPerCapita?custoPerCapita=${valor}`;
                    break;
                case 'custoTotal':
                    url = `/nutricionista/fichas/custoTotal?custoTotal=${valor}`;
                    break;
                case 'por-nome':
                    url = `/nutricionista/fichas/por-nome?nome=${encodeURIComponent(valor)}`;
                    break;
                case 'por-categoria':
                    url = `/nutricionista/fichas/por-categoria?categoria=${encodeURIComponent(valor)}`;
                    break;
                case 'por-rendimento':
                    url = `/nutricionista/fichas/por-rendimento?rendimento=${valor}`;
                    break;
                case 'por-numero':
                    url = `/nutricionista/fichas/por-numero?numero=${valor}`;
                    break;
                case 'vtc':
                    url = `/nutricionista/fichas/por-vtc?vtc=${valor}`;
                    break;
                case 'gramasPTN':
                    url = `/nutricionista/fichas/por-gramas-ptn?gramasPTN=${valor}`;
                    break;
                case 'gramasCHO':
                    url = `/nutricionista/fichas/por-gramas-cho?gramasCHO=${valor}`;
                    break;
                case 'gramasLIP':
                    url = `/nutricionista/fichas/por-gramas-lip?gramasLIP=${valor}`;
                    break;
                case 'gramasSodio':
                    url = `/nutricionista/fichas/por-gramas-sodio?gramasSodio=${valor}`;
                    break;
                case 'gramasSaturada':
                    url = `/nutricionista/fichas/por-gramas-saturada?gramasSaturada=${valor}`;
                    break;
                case 'status':
                    url = `/nutricionista/fichas/por-status?status=${valor}`;
                    break;
                default:
                    alert('Campo de pesquisa inválido.');
                    return;
            }

            window.location.href = url;
        });
    }

    // Toggle ATIVA ↔ INATIVA
    const toggleStatusBtn = document.getElementById('toggleStatusBtn');
    if (toggleStatusBtn) {
        toggleStatusBtn.addEventListener('click', function() {
            const currentStatus = this.getAttribute('data-current-status');
            const newStatus = currentStatus === 'ATIVA' ? 'INATIVA' : 'ATIVA';

            // Atualiza texto e atributo
            const span = this.querySelector('span');
            span.textContent = newStatus === 'ATIVA' ? 'Desarquivadas' : 'Arquivadas';
            this.setAttribute('data-current-status', newStatus);

            window.location.href = `/nutricionista/fichas/por-status?status=${newStatus}`;
        });
    }

    // Toggle COMPLETA ↔ INCOMPLETA
    const toggleCriacaoBtn = document.getElementById('toggleStatusCriacaoBtn');
    if (toggleCriacaoBtn) {
        toggleCriacaoBtn.addEventListener('click', function() {
            const current = this.getAttribute('data-current-status-criacao');
            const next = current === 'COMPLETA' ? 'INCOMPLETA' : 'COMPLETA';
            window.location.href = `/nutricionista/fichas/por-statusCriacao?statusCriacao=${next}`;
        });
    }
});
