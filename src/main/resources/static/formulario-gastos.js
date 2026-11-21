document.addEventListener("DOMContentLoaded", function () {
    const formulario = document.getElementById("formGastoApi");
    if (!formulario) return;

    const avisoErro = document.getElementById("erroGastoApi");
    const botao = formulario.querySelector('button[type="submit"]');
    const textoBotao = botao.textContent;

    formulario.addEventListener("submit", async function (evento) {
        evento.preventDefault();
        avisoErro.hidden = true;

        const campos = formulario.elements;
        const dados = {
            tipoGasto: campos["tipoGasto"].value.trim(),
            descricao: campos["descricao"].value.trim(),
            valor: Number(campos["valor"].value.replace(",", ".")),
            dataGasto: campos["dataGasto"].value
        };

        if (!dados.tipoGasto || !dados.descricao ||
            !Number.isFinite(dados.valor) || dados.valor <= 0 ||
            !/^\d{4}-\d{2}-\d{2}$/.test(dados.dataGasto)) {
            avisoErro.textContent = "Preencha todos os campos com valores válidos.";
            avisoErro.hidden = false;
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
            avisoErro.textContent = erro.message;
            avisoErro.hidden = false;
        } finally {
            botao.disabled = false;
            botao.textContent = textoBotao;
        }
    });
});
