document.addEventListener("DOMContentLoaded", function () {
    const formulario = document.getElementById("formGastoApi");
    if (!formulario) return;

    const avisoErro = document.getElementById("erroGastoApi");
    const botao = formulario.querySelector('button[type="submit"]');
    const textoBotao = botao.textContent.trim();
    const campos = formulario.elements;

    // Vazio no cadastro; preenchido na edição
    const gastoId = formulario.dataset.gastoId;
    const urlCarregar = formulario.dataset.carregarUrl;

    function mostrarErro(texto) {
        avisoErro.textContent = texto;
        avisoErro.hidden = false;
    }

    // Bloqueia ou libera todos os campos do formulário
    function bloquearFormulario(bloquear) {
        Array.from(campos).forEach(function (campo) {
            campo.disabled = bloquear;
        });
    }

    // GET /safra/{id}/gastos: confirma que a safra existe e,
    // na edição, traz o gasto para preencher o formulário
    async function carregarDados() {
        avisoErro.hidden = true;
        bloquearFormulario(true);
        botao.textContent = "Carregando...";

        try {
            const resposta = await fetch(urlCarregar, {
                method: "GET",
                headers: { "Accept": "application/json" }
            });

            if (resposta.status === 404) {
                mostrarErro("Safra não encontrada.");
                return;
            }

            if (!resposta.ok) {
                throw new Error("Erro HTTP " + resposta.status);
            }

            const gastos = await resposta.json();

            if (gastoId) {
                const gasto = gastos.find(function (item) {
                    return String(item.id) === String(gastoId);
                });

                if (!gasto) {
                    mostrarErro("Gasto não encontrado nesta safra.");
                    return;
                }

                campos["tipoGasto"].value = gasto.tipoGasto || "";
                campos["descricao"].value = gasto.descricao || "";
                campos["valor"].value = gasto.valor;
                campos["dataGasto"].value = gasto.dataGasto || "";
            }

            bloquearFormulario(false);
        } catch (erro) {
            console.error("Erro ao carregar dados do gasto:", erro);
            mostrarErro("Não foi possível carregar os dados pela API.");
        } finally {
            botao.textContent = textoBotao;
        }
    }

    formulario.addEventListener("submit", async function (evento) {
        evento.preventDefault();
        avisoErro.hidden = true;

        const dados = {
            tipoGasto: campos["tipoGasto"].value.trim(),
            descricao: campos["descricao"].value.trim(),
            valor: Number(campos["valor"].value.replace(",", ".")),
            dataGasto: campos["dataGasto"].value
        };

        if (!dados.tipoGasto || !dados.descricao ||
            !Number.isFinite(dados.valor) || dados.valor <= 0 ||
            !/^\d{4}-\d{2}-\d{2}$/.test(dados.dataGasto)) {
            mostrarErro("Preencha todos os campos com valores válidos.");
            return;
        }

        botao.disabled = true;
        botao.textContent = "Salvando...";

        try {
            const resposta = await fetch(formulario.dataset.apiUrl, {
                method: formulario.dataset.metodo,
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(dados)
            });

            if (!resposta.ok) {
                let mensagem = "Não foi possível salvar o gasto.";
                if (resposta.status === 400) {
                    mensagem = "Verifique os dados preenchidos.";
                } else if (resposta.status === 404) {
                    mensagem = "Safra ou gasto não encontrado.";
                }
                throw new Error(mensagem + " (HTTP " + resposta.status + ")");
            }

            window.location.href = formulario.dataset.sucessoUrl;
        } catch (erro) {
            console.error("Erro ao salvar gasto:", erro);
            mostrarErro(erro.message);
        } finally {
            botao.disabled = false;
            botao.textContent = textoBotao;
        }
    });

    carregarDados();
});
