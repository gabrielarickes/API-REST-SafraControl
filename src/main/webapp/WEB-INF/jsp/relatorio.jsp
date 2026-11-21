
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:choose>
    <c:when test="${maisLucrativa}">
        <c:set var="urlApi"
               value="${ctx}/safra/mais_lucrativa" />
    </c:when>

    <c:otherwise>
        <c:set var="urlApi"
               value="${ctx}/safra/${safraId}/relatorio" />
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Relatório - SafraControl</title>

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
               class="menu-item">
                Cadastrar safra
            </a>

            <a href="${ctx}/painel/mais-lucrativa"
               class="menu-item active">
                Safra mais lucrativa
            </a>

        </nav>

        <div class="sidebar-footer">
            <p>SafraControl</p>
            <small>Controle e organização das safras</small>
        </div>

    </aside>

    <!-- CONTEÚDO PRINCIPAL -->
    <main class="main-content"
          id="paginaRelatorio"
          data-api-url="${urlApi}"
          data-contexto="${ctx}">

        <header class="page-header">

            <div>
                <p class="eyebrow">
                    RELATÓRIOS FINANCEIROS
                </p>

                <c:choose>
                    <c:when test="${maisLucrativa}">
                        <h1>Safra mais lucrativa</h1>

                        <p class="subtitle">
                            Safra com maior lucro líquido.
                        </p>
                    </c:when>

                    <c:otherwise>
                        <h1>Relatório financeiro</h1>

                        <p class="subtitle">
                            Resultado financeiro da safra.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>

            <a href="${ctx}/painel"
               class="btn btn-outline">
                Voltar ao painel
            </a>

        </header>

        <!-- CARREGAMENTO E ERROS -->

        <div id="carregandoRelatorio" class="alert">
            Carregando relatório...
        </div>

        <div id="erroRelatorio"
             class="form-error"
             role="alert"
             hidden>
        </div>

        <!-- INFORMAÇÕES RECEBIDAS DA API -->

        <div id="conteudoRelatorio" hidden>

            <section class="content-card report-info">

                <div class="section-header">

                    <div>
                        <h2 id="tituloSafra">-</h2>

                        <p>
                            Informações da produção agrícola
                        </p>
                    </div>

                    <span class="year-badge"
                          id="idSafra">
                        -
                    </span>

                </div>

                <div class="table-wrapper">

                    <table class="data-table">

                        <tbody>

                            <tr>
                                <td><strong>Ano</strong></td>
                                <td id="anoSafra">-</td>
                            </tr>

                            <tr>
                                <td>
                                    <strong>Tipo de cultura</strong>
                                </td>
                                <td id="culturaSafra">-</td>
                            </tr>

                            <tr>
                                <td>
                                    <strong>Quantidade produzida</strong>
                                </td>
                                <td id="quantidadeSafra">-</td>
                            </tr>

                            <tr>
                                <td>
                                    <strong>Unidade de medida</strong>
                                </td>
                                <td id="unidadeSafra">-</td>
                            </tr>

                            <tr>
                                <td>
                                    <strong>Valor por unidade</strong>
                                </td>
                                <td id="valorUnidadeSafra">-</td>
                            </tr>

                        </tbody>

                    </table>

                </div>

            </section>

            <!-- INDICADORES FINANCEIROS -->

            <div class="report-section-title">
                <h2>Resumo financeiro</h2>

                <p>
                    Receita, despesas e resultado da safra.
                </p>
            </div>

            <section class="report-stats">

                <div class="stat-card">

                    <p class="stat-label">
                        Receita bruta
                    </p>

                    <h2 class="stat-value"
                        id="receitaRelatorio">
                        -
                    </h2>

                    <p class="stat-description">
                        Quantidade × valor de venda
                    </p>

                </div>

                <div class="stat-card">

                    <p class="stat-label">
                        Total de gastos
                    </p>

                    <h2 class="stat-value"
                        id="gastosRelatorio">
                        -
                    </h2>

                    <p class="stat-description">
                        Despesas cadastradas
                    </p>

                </div>

                <div class="stat-card">

                    <p class="stat-label">
                        Lucro líquido
                    </p>

                    <h2 class="stat-value text-green"
                        id="lucroRelatorio">
                        -
                    </h2>

                    <p class="stat-description">
                        Receita bruta menos gastos
                    </p>

                </div>

            </section>

            <!-- AÇÕES -->

            <section class="report-actions">

                <a id="linkGastosRelatorio"
                   href="#"
                   class="btn btn-primary">
                    Ver gastos da safra
                </a>

                <c:if test="${not maisLucrativa}">
                    <a href="${ctx}/painel/mais-lucrativa"
                       class="btn btn-outline">
                        Ver safra mais lucrativa
                    </a>
                </c:if>

                <a href="${ctx}/painel"
                   class="btn btn-outline">
                    Voltar
                </a>

            </section>

            <p class="dashboard-note">
                Dados obtidos da API REST do SafraControl.
            </p>

        </div>

    </main>

</div>

<script src="${ctx}/relatorio-api.js?v=1"></script>

</body>
</html>
