document.addEventListener("DOMContentLoaded", function () {
    const corpoTabela = document.getElementById("corpoGastosApi");
    if (!corpoTabela) return;

    const tabela = document.getElementById("tabelaGastosApi");
    const avisoVazio = document.getElementById("semGastosApi");
    const avisoErro = document.getElementById("erroGastosApi");
    const totalGastos = document.getElementById("totalGastosApi");
    const apiUrl = corpoTabela.dataset.apiUrl;
    const apiGastos = corpoTabela.dataset.apiGastos;
    const urlVoltar = corpoTabela.dataset.urlVoltar;
    const moeda = new Intl.NumberFormat("pt-BR", {
        style: "currency", currency: "BRL"
    });

    function adicionarCelula(linha, conteudo, classe = "") {
        const celula = document.createElement("td");
        celula.textContent = conteudo;
        if (classe) celula.className = classe;
        linha.appendChild(celula);
        return celula;
    }

    function dataBrasileira(data) {
        if (!data) return "-";
        const partes = String(data).split("-");
        if (partes.length !== 3) return String(data);
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }

    async function excluirGasto(id, botao) {
        if (!confirm("Deseja realmente excluir este gasto?")) return;

        botao.disabled = true;
        botao.textContent = "Excluindo...";

        try {
            const resposta = await fetch(
                apiGastos + "/" + encodeURIComponent(id),
                { method: "DELETE" }
            );

            if (!resposta.ok) {
                throw new Error("Erro ao excluir gasto (HTTP " + resposta.status + ").");
            }

            window.location.href = urlVoltar + "?excluido=1";
        } catch (erro) {
            alert(erro.message);
            botao.disabled = false;
            botao.textContent = "Excluir";
        }
    }

    function adicionarGasto(gasto) {
        const linha = document.createElement("tr");
        const id = encodeURIComponent(gasto.id);

        adicionarCelula(linha, "#" + gasto.id, "id-cell");
        adicionarCelula(linha, gasto.tipoGasto || "-", "culture-name");
        adicionarCelula(linha, gasto.descricao || "-");
        adicionarCelula(linha, moeda.format(Number(gasto.valor) || 0));
        adicionarCelula(linha, dataBrasileira(gasto.dataGasto));

        const celulaAcoes = document.createElement("td");
        const acoes = document.createElement("div");
        acoes.className = "action-group";

        const editar = document.createElement("a");
        editar.className = "action-link";
        editar.textContent = "Editar";
        editar.href = urlVoltar + "/editar/" + id;
        acoes.appendChild(editar);

        const excluir = document.createElement("button");
        excluir.type = "button";
        excluir.className = "action-delete";
        excluir.textContent = "Excluir";
        excluir.addEventListener("click", function () {
            excluirGasto(gasto.id, excluir);
        });
        acoes.appendChild(excluir);

        celulaAcoes.appendChild(acoes);
        linha.appendChild(celulaAcoes);
        corpoTabela.appendChild(linha);
    }

    async function carregarGastos() {
        avisoErro.hidden = true;
        avisoVazio.hidden = true;

        try {
            const resposta = await fetch(apiUrl, {
                method: "GET",
                headers: { "Accept": "application/json" }
            });

            if (!resposta.ok) {
                throw new Error("Erro HTTP " + resposta.status);
            }

            const gastos = await resposta.json();
            if (!Array.isArray(gastos)) {
                throw new Error("A API não retornou uma lista de gastos.");
            }

            corpoTabela.replaceChildren();
            const total = gastos.reduce(function (soma, gasto) {
                return soma + (Number(gasto.valor) || 0);
            }, 0);
            totalGastos.textContent = moeda.format(total);

            if (gastos.length === 0) {
                tabela.hidden = true;
                avisoVazio.hidden = false;
            } else {
                gastos.forEach(adicionarGasto);
                tabela.hidden = false;
            }
        } catch (erro) {
            console.error("Erro ao consultar gastos:", erro);
            corpoTabela.replaceChildren();
            totalGastos.textContent = "—";
            tabela.hidden = true;
            avisoErro.hidden = false;
        }
    }

    carregarGastos();
});
