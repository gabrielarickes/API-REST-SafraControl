
document.addEventListener("DOMContentLoaded", function () {

    const corpoTabela = document.getElementById("corpoSafrasApi");
    const tabela = document.getElementById("tabelaSafrasApi");
    const avisoVazio = document.getElementById("semSafrasApi");
    const avisoErro = document.getElementById("erroSafrasApi");

    if (!corpoTabela) {
        return;
    }

    const apiUrl = corpoTabela.dataset.apiUrl;
    const ctx = corpoTabela.dataset.contexto;

    const parametros = new URLSearchParams(window.location.search);

    const ano = (parametros.get("ano") || "").trim();

    const cultura = (parametros.get("cultura") || "")
        .trim()
        .toLocaleLowerCase("pt-BR");

    const formatarMoeda = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    });

    // Cria uma célula da tabela
    function adicionarCelula(linha, texto, classe = "") {

        const celula = document.createElement("td");
        celula.textContent = texto;

        if (classe) {
            celula.className = classe;
        }

        linha.appendChild(celula);
        return celula;
    }

    // Cria os links de edição, gastos e relatório
    function criarLink(texto, endereco) {

        const link = document.createElement("a");

        link.textContent = texto;
        link.href = endereco;
        link.className = "action-link";

        return link;
    }

    // DELETE exclui uma safra pela API REST
    async function excluirSafra(id, botao) {

        const confirmou = confirm(
            "Deseja realmente excluir esta safra? " +
            "Os gastos associados também serão excluídos."
        );

        if (!confirmou) {
            return;
        }

        botao.disabled = true;
        botao.textContent = "Excluindo...";

        try {

            const resposta = await fetch(
                apiUrl + "/" + encodeURIComponent(id),
                {
                    method: "DELETE"
                }
            );

            if (!resposta.ok) {
                throw new Error(
                    "Não foi possível excluir a safra. " +
                    "Código HTTP: " + resposta.status
                );
            }

            // Retorna ao painel após a exclusão
            window.location.href =
                ctx + "/painel?excluida=1#safras";

        } catch (erro) {

            console.error("Erro ao excluir safra:", erro);

            alert(erro.message);

            botao.disabled = false;
            botao.textContent = "Excluir";
        }
    }

    // Cria as linhas da tabela
    function adicionarSafra(safra) {

        const linha = document.createElement("tr");

        const id = encodeURIComponent(safra.id);

        // ID
        adicionarCelula(
            linha,
            "#" + safra.id,
            "id-cell"
        );

        // Cultura
        adicionarCelula(
            linha,
            safra.tipoCultura,
            "culture-name"
        );

        // Ano
        const celulaAno = document.createElement("td");
        const badge = document.createElement("span");

        badge.className = "year-badge";
        badge.textContent = safra.ano;

        celulaAno.appendChild(badge);
        linha.appendChild(celulaAno);

        // Quantidade produzida
        const quantidade = Number(safra.quantidade)
            .toLocaleString("pt-BR");

        adicionarCelula(
            linha,
            quantidade + " " + safra.unidadeMedida
        );

        // Valor por unidade
        adicionarCelula(
            linha,
            formatarMoeda.format(safra.valorVendaUnidade)
        );

        // Ações
        const celulaAcoes = document.createElement("td");
        const grupoAcoes = document.createElement("div");

        grupoAcoes.className = "action-group";

        // Editar
        grupoAcoes.appendChild(
            criarLink(
                "Editar",
                ctx + "/painel/editar/" + id
            )
        );

        // Gastos
        grupoAcoes.appendChild(
            criarLink(
                "Gastos",
                ctx + "/painel/safras/" + id + "/gastos"
            )
        );

        // Relatório
        grupoAcoes.appendChild(
            criarLink(
                "Relatório",
                ctx + "/painel/safras/" + id + "/relatorio"
            )
        );

        // Botão DELETE
        const botaoExcluir = document.createElement("button");

        botaoExcluir.type = "button";
        botaoExcluir.className = "action-delete";
        botaoExcluir.textContent = "Excluir";

        botaoExcluir.addEventListener("click", function () {
            excluirSafra(safra.id, botaoExcluir);
        });

        grupoAcoes.appendChild(botaoExcluir);

        celulaAcoes.appendChild(grupoAcoes);
        linha.appendChild(celulaAcoes);

        corpoTabela.appendChild(linha);
    }

    // GET consulta as safras pela API REST
    async function carregarSafras() {

        try {

            const resposta = await fetch(apiUrl, {
                method: "GET",
                headers: {
                    "Accept": "application/json"
                }
            });

            if (!resposta.ok) {
                throw new Error(
                    "Erro ao consultar safras: " + resposta.status
                );
            }

            const safras = await resposta.json();

            if (!Array.isArray(safras)) {
                throw new Error("Resposta inesperada da API");
            }

            // Aplica os filtros
            const safrasFiltradas = safras.filter(function (safra) {

                const correspondeAno =
                    ano === "" || String(safra.ano) === ano;

                const nomeCultura = String(
                    safra.tipoCultura || ""
                ).toLocaleLowerCase("pt-BR");

                const correspondeCultura =
                    cultura === "" || nomeCultura.includes(cultura);

                return correspondeAno && correspondeCultura;
            });

            corpoTabela.replaceChildren();

            // Mostra os resultados
            if (safrasFiltradas.length === 0) {

                tabela.hidden = true;
                avisoVazio.hidden = false;

                const mensagem = document.getElementById(
                    "mensagemSemSafrasApi"
                );

                const temFiltro = ano !== "" || cultura !== "";

                mensagem.textContent = temFiltro
                    ? "Nenhuma safra corresponde aos filtros informados."
                    : "Ainda não existem safras cadastradas.";

            } else {

                tabela.hidden = false;
                avisoVazio.hidden = true;

                safrasFiltradas.forEach(adicionarSafra);
            }

        } catch (erro) {

            console.error("Erro ao carregar safras:", erro);

            tabela.hidden = true;
            avisoVazio.hidden = true;
            avisoErro.hidden = false;
        }
    }

    carregarSafras();

});
