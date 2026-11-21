
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Nova Safra - SafraControl</title>

    <link rel="stylesheet" href="${ctx}/style.css?v=10">
</head>

<body>

<div class="app-layout">

    <!-- MENU LATERAL -->
    <aside class="sidebar">

        <div class="logo">

            <div class="logo-icon">SC</div>

            <div>
                <h2>SafraControl</h2>
                <span>Gestão agrícola</span>
            </div>

        </div>

        <nav class="menu">

            <p class="menu-label">MENU PRINCIPAL</p>

            <a href="${ctx}/painel#indicadores"
               class="menu-item">
                Visão geral
            </a>

            <a href="${ctx}/painel#safras"
               class="menu-item">
                Safras
            </a>

            <a href="${ctx}/painel/novo"
               class="menu-item active">
                Cadastrar safra
            </a>

            <a href="${ctx}/painel/mais-lucrativa"
               class="menu-item">
                Safra mais lucrativa
            </a>

        </nav>

        <div class="sidebar-footer">
            <p>SafraControl</p>
            <small>Controle e organização das safras</small>
        </div>

    </aside>

    <!-- CONTEÚDO PRINCIPAL -->
    <main class="main-content">

        <header class="page-header">

            <div>
                <p class="eyebrow">
                    GERENCIAMENTO DE SAFRAS
                </p>

                <h1>Nova safra</h1>

                <p class="subtitle">
                    Cadastre uma nova produção agrícola.
                </p>
            </div>

            <a href="${ctx}/painel"
               class="btn btn-outline">
                Voltar
            </a>

        </header>

        <section class="form-panel">

            <h2>Dados da safra</h2>

            <p class="form-intro">
                Preencha as informações abaixo para
                cadastrar uma nova safra.
            </p>

            <!-- ERROS ENVIADOS PELO SPRING -->

            <c:forEach var="erro" items="${erros}">

                <div class="form-error">
                    <c:out value="${erro.defaultMessage}"/>
                </div>

            </c:forEach>

            <c:if test="${not empty erro}">

                <div class="form-error">
                    <c:out value="${erro}"/>
                </div>

            </c:if>

            <!-- ERROS DA REQUISIÇÃO REST -->

            <div id="erroApi"
                 class="form-error"
                 role="alert"
                 hidden>
            </div>

            <!-- FORMULÁRIO -->

            <form id="formNovaSafra"
                  data-api-url="${ctx}/safra"
                  data-sucesso-url="${ctx}/painel?cadastrada=1">

                <div class="form-grid">

                    <!-- ANO -->

                    <div class="field">

                        <label for="ano">
                            Ano da safra
                        </label>

                        <input type="number"
                               id="ano"
                               name="ano"
                               value="${safraDto.ano}"
                               placeholder="Ex.: 2026"
                               min="1"
                               required>

                    </div>

                    <!-- CULTURA -->

                    <div class="field">

                        <label for="tipoCultura">
                            Tipo de cultura
                        </label>

                        <input type="text"
                               id="tipoCultura"
                               name="tipoCultura"
                               value="${fn:escapeXml(safraDto.tipoCultura)}"
                               placeholder="Ex.: Soja"
                               required>

                    </div>

                    <!-- QUANTIDADE -->

                    <div class="field">

                        <label for="quantidade">
                            Quantidade produzida
                        </label>

                        <input type="number"
                               id="quantidade"
                               name="quantidade"
                               value="${safraDto.quantidade}"
                               placeholder="Ex.: 500"
                               min="0"
                               required>

                    </div>

                    <!-- UNIDADE DE MEDIDA -->

                    <div class="field">

                        <label for="unidadeMedida">
                            Unidade de medida
                        </label>

                        <input type="text"
                               id="unidadeMedida"
                               name="unidadeMedida"
                               list="unidades"
                               value="${fn:escapeXml(safraDto.unidadeMedida)}"
                               placeholder="Ex.: sacas"
                               required>

                        <datalist id="unidades">
                            <option value="kg">
                            <option value="sacas">
                            <option value="toneladas">
                            <option value="unidades">
                        </datalist>

                    </div>

                    <!-- VALOR DE VENDA -->

                    <div class="field field-full">

                        <label for="valorVendaUnidade">
                            Valor de venda por unidade (R$)
                        </label>

                        <input type="number"
                               id="valorVendaUnidade"
                               name="valorVendaUnidade"
                               value="${safraDto.valorVendaUnidade}"
                               placeholder="Ex.: 120.00"
                               step="0.01"
                               min="0"
                               required>

                    </div>

                </div>

                <!-- BOTÕES -->

                <div class="form-actions">

                    <button type="submit"
                            class="btn btn-primary">
                        Cadastrar safra
                    </button>

                    <a href="${ctx}/painel"
                       class="form-cancel">
                        Cancelar
                    </a>

                </div>

            </form>

        </section>

    </main>

</div>

<!-- JAVASCRIPT DA API REST -->
<script src="${ctx}/cadastrar-safra-api.js?v=1"></script>

</body>
</html>
