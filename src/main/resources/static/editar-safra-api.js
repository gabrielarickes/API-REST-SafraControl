
document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formEditarSafra");

    if (!formulario) {
        return;
    }

    const mensagemErro = document.getElementById("erroApi");

    const botaoSalvar = formulario.querySelector(
        'button[type="submit"]'
    );

    formulario.addEventListener("submit", async function (evento) {

        // Impede o envio tradicional do formulário
        evento.preventDefault();

        mensagemErro.hidden = true;

        // Recupera os dados preenchidos
        const dados = {

            ano: Number(
                formulario.elements["ano"].value
            ),

            tipoCultura: formulario.elements["tipoCultura"]
                .value.trim(),

            quantidade: Number(
                formulario.elements["quantidade"].value
            ),

            unidadeMedida: formulario.elements["unidadeMedida"]
                .value.trim(),

            valorVendaUnidade: Number(
                formulario.elements["valorVendaUnidade"]
                    .value.replace(",", ".")
            )
        };

        // Validação dos dados
        if (
            !Number.isInteger(dados.ano) ||
            dados.ano < 1 ||
            !dados.tipoCultura ||
            !Number.isInteger(dados.quantidade) ||
            dados.quantidade < 0 ||
            !dados.unidadeMedida ||
            !Number.isFinite(dados.valorVendaUnidade) ||
            dados.valorVendaUnidade < 0
        ) {

            mensagemErro.textContent =
                "Verifique os dados informados.";

            mensagemErro.hidden = false;

            return;
        }

        botaoSalvar.disabled = true;
        botaoSalvar.textContent = "Salvando...";

        try {

            // Requisição PUT para a API REST
            const resposta = await fetch(
                formulario.dataset.apiUrl,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },

                    body: JSON.stringify(dados)
                }
            );

            if (!resposta.ok) {

                let mensagem =
                    "Não foi possível atualizar a safra.";

                if (resposta.status === 400) {
                    mensagem =
                        "Verifique os dados preenchidos.";
                }

                if (resposta.status === 404) {
                    mensagem =
                        "A safra não foi encontrada.";
                }

                if (resposta.status === 409) {
                    mensagem =
                        "Já existe uma safra com essa cultura e esse ano.";
                }

                throw new Error(mensagem);
            }

            // Atualização realizada com sucesso
            window.location.href =
                formulario.dataset.sucessoUrl;

        } catch (erro) {

            console.error("Erro ao editar safra:", erro);

            mensagemErro.textContent = erro.message;
            mensagemErro.hidden = false;

        } finally {

            botaoSalvar.disabled = false;
            botaoSalvar.textContent = "Salvar alterações";

        }

    });

});
