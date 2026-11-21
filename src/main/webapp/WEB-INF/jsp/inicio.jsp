
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Dashboard - SafraControl</title>

    <link rel="stylesheet" href="${ctx}/style.css?v=10">
</head>

<body>

<div class="app-layout">

    <!-- MENU LATERAL -->
    <aside class="sidebar">

        <div class="logo">

            <div class="logo-icon">
                SC
            </div>

            <div>
                <h2>SafraControl</h2>
                <span>Gestão agrícola</span>
            </div>

        </div>

        <nav class="menu">

            <p class="menu-label">
                MENU PRINCIPAL
            </p>

            <a href="${ctx}/painel#indicadores"
               class="menu-item active">
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
               class="menu-item">
                Safra mais lucrativa
            </a>

        </nav>

        <div class="sidebar-footer">

            <p>SafraControl</p>

            <small>
                Controle e organização das safras
            </small>

        </div>

    </aside>

    <!-- CONTEÚDO PRINCIPAL -->
    <main class="main-content">

        <!-- CABEÇALHO -->
        <header class="page-header">

            <div>

                <p class="eyebrow">
                    PAINEL DE CONTROLE
                </p>

                <h1>Visão geral</h1>

                <p class="subtitle">
                    Acompanhe suas safras,
                    despesas e resultados financeiros.
                </p>

            </div>

            <a href="${ctx}/painel/novo"
               class="btn btn-primary">
                + Nova safra
            </a>

        </header>

        <!-- MENSAGENS DE SUCESSO -->

        <c:if test="${param.cadastrada == '1'}">

            <div class="alert alert-success">
                Safra cadastrada com sucesso!
            </div>

        </c:if>

        <c:if test="${param.editada == '1'}">

            <div class="alert alert-success">
                Safra atualizada com sucesso!
            </div>

        </c:if>

        <c:if test="${param.excluida == '1'}">

            <div class="alert alert-success">
                Safra excluída com sucesso!
            </div>

        </c:if>

        <!-- INDICADORES FINANCEIROS -->

        <section id="indicadores" class="stats-grid">

            <!-- TOTAL DE SAFRAS -->
            <div class="stat-card">

                <div class="stat-icon icon-green">
                    SC
                </div>

                <p class="stat-label">
                    Total de safras
                </p>

                <h2 class="stat-value"
                    id="indicadorSafras"
                    data-api-url="${ctx}/safra">

                    Carregando...

                </h2>

                <p class="stat-description">
                    Safras registradas no sistema
                </p>

            </div>

            <!-- RECEITA BRUTA -->
            <div class="stat-card">

                <div class="stat-icon icon-blue">
                    R$
                </div>

                <p class="stat-label">
                    Receita bruta
                </p>

                <h2 class="stat-value"
                    id="indicadorReceita">

                    Carregando...

                </h2>

                <p class="stat-description">
                    Receita total das safras cadastradas
                </p>

            </div>

            <!-- TOTAL DE GASTOS -->
            <div class="stat-card">

                <div class="stat-icon icon-orange">
                    R$
                </div>

                <p class="stat-label">
                    Total de gastos
                </p>

                <h2 class="stat-value"
                    id="indicadorGastos">

                    Carregando...

                </h2>

                <p class="stat-description">
                    Soma das despesas registradas
                </p>

            </div>

            <!-- LUCRO LÍQUIDO -->
            <div class="stat-card">

                <div class="stat-icon icon-green">
                    R$
                </div>

                <p class="stat-label">
                    Lucro líquido
                </p>

                <h2 class="stat-value text-green"
                    id="indicadorLucro">

                    Carregando...

                </h2>

                <p class="stat-description">
                    Receita bruta menos gastos
                </p>

            </div>

        </section>

        <!-- LISTAGEM DE SAFRAS -->

        <section id="safras" class="content-card">

            <!-- TÍTULO DA TABELA -->
            <div class="section-header">

                <div>

                    <h2>Safras cadastradas</h2>

                    <p>
                        Consulte e gerencie os registros
                        da sua produção agrícola.
                    </p>

                </div>

                <a href="${ctx}/painel/novo"
                   class="btn btn-primary">
                    + Cadastrar safra
                </a>

            </div>

            <!-- FILTROS -->

            <form action="${ctx}/painel#safras"
                  method="get"
                  class="filters-form">

                <!-- FILTRO POR ANO -->
                <div class="filter-field">

                    <label for="ano">
                        Ano
                    </label>

                    <input
                        type="number"
                        id="ano"
                        name="ano"
                        min="1"
                        placeholder="Ex.: 2026">

                </div>

                <!-- FILTRO POR CULTURA -->
                <div class="filter-field">

                    <label for="cultura">
                        Tipo de cultura
                    </label>

                    <input
                        type="text"
                        id="cultura"
                        name="cultura"
                        placeholder="Ex.: Soja">

                </div>

                <!-- BOTÕES -->
                <div class="filter-buttons">

                    <button type="submit"
                            class="btn btn-primary">
                        Filtrar
                    </button>

                    <a href="${ctx}/painel#safras"
                       class="btn btn-outline">
                        Limpar
                    </a>

                </div>

            </form>

            <!-- TABELA PREENCHIDA PELA API REST -->

            <div class="table-wrapper"
                 id="tabelaSafrasApi">

                <table class="data-table">

                    <thead>

                        <tr>
                            <th>ID</th>
                            <th>Cultura</th>
                            <th>Ano</th>
                            <th>Produção</th>
                            <th>Valor por unidade</th>
                            <th>Ações</th>
                        </tr>

                    </thead>

                    <tbody
                        id="corpoSafrasApi"
                        data-api-url="${ctx}/safra"
                        data-contexto="${ctx}">

                        <tr>
                            <td colspan="6">
                                Carregando safras...
                            </td>
                        </tr>

                    </tbody>

                </table>

            </div>

            <!-- NENHUMA SAFRA ENCONTRADA -->

            <div id="semSafrasApi"
                 class="empty-state"
                 hidden>

                <h3>
                    Nenhuma safra encontrada
                </h3>

                <p id="mensagemSemSafrasApi">
                    Nenhuma safra cadastrada.
                </p>

                <a href="${ctx}/painel#safras"
                   class="btn btn-outline">
                    Limpar filtros
                </a>

                <a href="${ctx}/painel/novo"
                   class="btn btn-primary">
                    Cadastrar safra
                </a>

            </div>

            <!-- ERRO AO CONSULTAR A API -->

            <div id="erroSafrasApi"
                 class="form-error"
                 role="alert"
                 hidden>

                Não foi possível carregar as safras.
                Verifique a conexão com a API REST.

            </div>

        </section>

        <p class="dashboard-note">
            Os indicadores financeiros são calculados
            a partir dos relatórios das safras cadastradas.
        </p>

    </main>

</div>

<!-- JAVASCRIPT DA LISTAGEM REST -->
<script src="${ctx}/safras-api.js?v=2"></script>

<!-- JAVASCRIPT DOS INDICADORES REST -->
<script src="${ctx}/indicadores-api.js?v=2"></script>

</body>
</html>
