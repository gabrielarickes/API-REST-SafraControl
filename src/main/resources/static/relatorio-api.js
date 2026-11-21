
document.addEventListener("DOMContentLoaded", async function () {

    const pagina = document.getElementById("paginaRelatorio");

    if (!pagina) {
        return;
    }

    const urlApi = pagina.dataset.apiUrl;
    const ctx = pagina.dataset.contexto;

    const carregando = document.getElementById(
        "carregandoRelatorio"
    );

    const mensagemErro = document.getElementById(
        "erroRelatorio"
    );

    const conteudo = document.getElementById(
        "conteudoRelatorio"
    );

    const moeda = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    });

    const numero = new Intl.NumberFormat("pt-BR");

    function preencher(id, valor) {
        document.getElementById(id).textContent = valor;
    }

    try {

        // GET do relatório na API REST
        const resposta = await fetch(urlApi, {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        });

        if (!resposta.ok) {

            if (resposta.status === 404) {
                throw new Error(
                    "Não foi possível encontrar o relatório da safra."
                );
            }

            throw new Error(
                "Erro ao consultar o relatório: " +
                resposta.status
            );
        }

        const relatorio = await resposta.json();

        if (!relatorio || relatorio.safraId == null) {
            throw new Error(
                "A API não retornou um relatório válido."
            );
        }

        // Informações da safra
        preencher(
            "tituloSafra",
            relatorio.tipoCultura + " - " + relatorio.ano
        );

        preencher("idSafra", "Safra #" + relatorio.safraId);
        preencher("anoSafra", relatorio.ano);
        preencher("culturaSafra", relatorio.tipoCultura);

        preencher(
            "quantidadeSafra",
            numero.format(Number(relatorio.quantidade))
        );

        preencher("unidadeSafra", relatorio.unidadeMedida);

        preencher(
            "valorUnidadeSafra",
            moeda.format(Number(relatorio.valorVendaUnidade))
        );

        // Indicadores financeiros
        preencher(
            "receitaRelatorio",
            moeda.format(Number(relatorio.receitaBruta))
        );

        preencher(
            "gastosRelatorio",
            moeda.format(Number(relatorio.totalGastos))
        );

        const lucro = Number(relatorio.lucroLiquido);

        preencher(
            "lucroRelatorio",
            moeda.format(lucro)
        );

        const campoLucro = document.getElementById(
            "lucroRelatorio"
        );

        campoLucro.classList.toggle("text-red", lucro < 0);
        campoLucro.classList.toggle("text-green", lucro >= 0);

        // Link para os gastos da safra
        const linkGastos = document.getElementById(
            "linkGastosRelatorio"
        );

        linkGastos.href =
            ctx + "/painel/safras/" +
            encodeURIComponent(relatorio.safraId) +
            "/gastos";

        // Mostra o relatório
        carregando.hidden = true;
        conteudo.hidden = false;

    } catch (erro) {

        console.error("Erro ao carregar relatório:", erro);

        carregando.hidden = true;

        mensagemErro.textContent = erro.message;
        mensagemErro.hidden = false;

    }

});
