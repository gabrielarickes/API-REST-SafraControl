
document.addEventListener("DOMContentLoaded", async function () {

    const campoSafras = document.getElementById("indicadorSafras");
    const campoReceita = document.getElementById("indicadorReceita");
    const campoGastos = document.getElementById("indicadorGastos");
    const campoLucro = document.getElementById("indicadorLucro");

    if (!campoSafras || !campoReceita ||
        !campoGastos || !campoLucro) {
        return;
    }

    const apiUrl = campoSafras.dataset.apiUrl;

    const moeda = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    });

    // Mantém os filtros preenchidos
    const parametros = new URLSearchParams(window.location.search);

    const campoAno = document.getElementById("ano");
    const campoCultura = document.getElementById("cultura");

    if (campoAno) {
        campoAno.value = parametros.get("ano") || "";
    }

    if (campoCultura) {
        campoCultura.value = parametros.get("cultura") || "";
    }

    async function consultarApi(url) {

        const resposta = await fetch(url, {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        });

        if (!resposta.ok) {
            throw new Error(
                "Erro HTTP " + resposta.status + " em " + url
            );
        }

        return await resposta.json();
    }

    try {

        // Consulta todas as safras
        const safras = await consultarApi(apiUrl);

        if (!Array.isArray(safras)) {
            throw new Error("Resposta de safras inválida.");
        }

        // O total de safras já pode ser exibido
        campoSafras.textContent = safras.length;

        // Consulta os relatórios individualmente
        const resultados = await Promise.allSettled(

            safras.map(function (safra) {

                return consultarApi(
                    apiUrl + "/" +
                    encodeURIComponent(safra.id) +
                    "/relatorio"
                );
            })
        );

        let receitaCentavos = 0;
        let gastosCentavos = 0;
        let ocorreuErro = false;

        resultados.forEach(function (resultado, indice) {

            if (resultado.status === "rejected") {

                ocorreuErro = true;

                console.error(
                    "Erro no relatório da safra ID " +
                    safras[indice].id,
                    resultado.reason
                );

                return;
            }

            const relatorio = resultado.value;

            const receita = Number(relatorio.receitaBruta);
            const gastos = Number(relatorio.totalGastos);

            if (!Number.isFinite(receita) ||
                !Number.isFinite(gastos)) {

                ocorreuErro = true;

                console.error(
                    "Valores inválidos no relatório da safra ID " +
                    safras[indice].id,
                    relatorio
                );

                return;
            }

            receitaCentavos += Math.round(receita * 100);
            gastosCentavos += Math.round(gastos * 100);

        });

        if (ocorreuErro) {

            campoReceita.textContent = "Indisponível";
            campoGastos.textContent = "Indisponível";
            campoLucro.textContent = "Indisponível";

            return;
        }

        // Calcula os totais
        const lucroCentavos =
            receitaCentavos - gastosCentavos;

        campoReceita.textContent =
            moeda.format(receitaCentavos / 100);

        campoGastos.textContent =
            moeda.format(gastosCentavos / 100);

        campoLucro.textContent =
            moeda.format(lucroCentavos / 100);

        campoLucro.classList.toggle(
            "text-red",
            lucroCentavos < 0
        );

        campoLucro.classList.toggle(
            "text-green",
            lucroCentavos >= 0
        );

    } catch (erro) {

        console.error(
            "Erro ao carregar indicadores:",
            erro
        );

        campoSafras.textContent = "Indisponível";
        campoReceita.textContent = "Indisponível";
        campoGastos.textContent = "Indisponível";
        campoLucro.textContent = "Indisponível";

    }

});
