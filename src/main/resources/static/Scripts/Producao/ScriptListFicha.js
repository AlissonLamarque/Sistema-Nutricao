document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('btnPesquisar').addEventListener('click', function() {
        const campo = document.getElementById('campoPesquisa').value;
        const valor = document.getElementById('valorPesquisa').value;

        if (!campo || !valor) {
            alert('Por favor, selecione um campo e insira um valor para pesquisa.');
            return;
        }

        let url;
        switch(campo) {
            case 'custoPerCapita':
                url = `/producao/fichas/custoPerCapita?custoPerCapita=${valor}`;
                break;
            case 'custoTotal':
                url = `/producao/fichas/custoTotal?custoTotal=${valor}`;
                break;
            case 'por-nome':
                url = `/producao/fichas/por-nome?nome=${encodeURIComponent(valor)}`;
                break;
            case 'por-categoria':
                url = `/producao/fichas/por-categoria?categoria=${encodeURIComponent(valor)}`;
                break;
            case 'por-rendimento':
                url = `/producao/fichas/por-rendimento?rendimento=${valor}`;
                break;
            case 'por-numero':
                url = `/producao/fichas/por-numero?numero=${valor}`;
                break;
            case 'vtc':
                url = `/producao/fichas/por-vtc?vtc=${valor}`;
                break;
            case 'gramasPTN':
                url = `/producao/fichas/por-gramas-ptn?gramasPTN=${valor}`;
                break;
            case 'gramasCHO':
                url = `/producao/fichas/por-gramas-cho?gramasCHO=${valor}`;
                break;
            case 'gramasLIP':
                url = `/producao/fichas/por-gramas-lip?gramasLIP=${valor}`;
                break;
            case 'gramasSodio':
                url = `/producao/fichas/por-gramas-sodio?gramasSodio=${valor}`;
                break;
            case 'gramasSaturada':
                url = `/producao/fichas/por-gramas-saturada?gramasSaturada=${valor}`;
                break;
            case 'status':
                url = `/producao/fichas/por-status?status=${valor}`;
                break;
            default:
                alert('Campo de pesquisa inválido.');
                return;
        }

        window.location.href = url;
    });
});