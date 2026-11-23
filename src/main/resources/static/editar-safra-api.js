document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formEditarSafra");

    if (!formulario) {
        return;
    }

    const mensagemErro = document.getElementById("erroApi");

    const botaoSalvar = formulario.querySelector(
        'button[type="submit"]'
    );

    const textoBotao = botaoSalvar.textContent.trim();

    const campos = formulario.elements;

    function mostrarErro(texto) {
        mensagemErro.textContent = texto;
        mensagemErro.hidden = false;
    }

    // Bloqueia ou libera todos os campos do formulário
    function bloquearFormulario(bloquear) {
        Array.from(campos).forEach(function (campo) {
            campo.disabled = bloquear;
        });
    }

    // GET - Consulta a safra pela API REST e preenche o formulário
    async function carregarSafra() {

        mensagemErro.hidden = true;
        bloquearFormulario(true);
        botaoSalvar.textContent = "Carregando...";

        try {

            const resposta = await fetch(
                formulario.dataset.apiUrl,
                {
                    method: "GET",
                    headers: {
                        "Accept": "application/json"
                    }
                }
            );

            if (resposta.status === 404) {
                mostrarErro("A safra não foi encontrada.");
                return;
            }

            if (!resposta.ok) {
                throw new Error("Erro HTTP " + resposta.status);
            }

            const safra = await resposta.json();

            campos["ano"].value = safra.ano;
            campos["tipoCultura"].value = safra.tipoCultura || "";
            campos["quantidade"].value = safra.quantidade;
            campos["unidadeMedida"].value = safra.unidadeMedida || "";
            campos["valorVendaUnidade"].value = safra.valorVendaUnidade;

            bloquearFormulario(false);

        } catch (erro) {

            console.error("Erro ao carregar safra:", erro);

            mostrarErro(
                "Não foi possível carregar a safra pela API."
            );

        } finally {

            botaoSalvar.textContent = textoBotao;

        }
    }

    formulario.addEventListener("submit", async function (evento) {

        // Impede o envio tradicional do formulário
        evento.preventDefault();

        mensagemErro.hidden = true;

        // Recupera os dados preenchidos
        const dados = {

            ano: Number(
                campos["ano"].value
            ),

            tipoCultura: campos["tipoCultura"]
                .value.trim(),

            quantidade: Number(
                campos["quantidade"].value
            ),

            unidadeMedida: campos["unidadeMedida"]
                .value.trim(),

            valorVendaUnidade: Number(
                campos["valorVendaUnidade"]
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

            mostrarErro("Verifique os dados informados.");

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

            mostrarErro(erro.message);

        } finally {

            botaoSalvar.disabled = false;
            botaoSalvar.textContent = textoBotao;

        }

    });

    carregarSafra();

});
