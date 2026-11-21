<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gastos - SafraControl</title>
    <link rel="stylesheet" href="${ctx}/style.css?v=12">
</head>
<body>
<div class="app-layout">
    <aside class="sidebar">
        <div class="logo">
            <div class="logo-icon">SC</div>
            <div><h2>SafraControl</h2><span>Gestão agrícola</span></div>
        </div>
        <nav class="menu">
            <p class="menu-label">MENU PRINCIPAL</p>
            <a href="${ctx}/painel#indicadores" class="menu-item">Visão geral</a>
            <a href="${ctx}/painel#safras" class="menu-item active">Safras</a>
            <a href="${ctx}/painel/novo" class="menu-item">Cadastrar safra</a>
            <a href="${ctx}/painel/mais-lucrativa" class="menu-item">Safra mais lucrativa</a>
        </nav>
        <div class="sidebar-footer">
            <p>SafraControl</p>
            <small>Controle e organização das safras</small>
        </div>
    </aside>

    <main class="main-content">
        <header class="page-header">
            <div>
                <p class="eyebrow">GERENCIAMENTO DE GASTOS</p>
                <h1>Gastos da safra</h1>
                <p class="subtitle">Safra ID: <c:out value="${safraId}" /></p>
            </div>
            <a href="${ctx}/painel#safras" class="btn btn-outline">Voltar para safras</a>
        </header>

        <c:if test="${param.cadastrado eq '1'}">
            <div class="alert alert-success">Gasto cadastrado com sucesso!</div>
        </c:if>
        <c:if test="${param.editado eq '1'}">
            <div class="alert alert-success">Gasto atualizado com sucesso!</div>
        </c:if>
        <c:if test="${param.excluido eq '1'}">
            <div class="alert alert-success">Gasto excluído com sucesso!</div>
        </c:if>

        <section class="gastos-resumo">
            <div class="stat-card">
                <p class="stat-label">Total de gastos desta safra</p>
                <h2 class="stat-value" id="totalGastosApi">Carregando...</h2>
                <p class="stat-description">Soma das despesas cadastradas</p>
            </div>
        </section>

        <section class="content-card">
            <div class="section-header">
                <div>
                    <h2>Despesas cadastradas</h2>
                    <p>Consulte e gerencie os gastos desta safra.</p>
                </div>
                <a href="${ctx}/painel/safras/${safraId}/gastos/novo"
                   class="btn btn-primary">+ Novo gasto</a>
            </div>

            <div class="table-wrapper" id="tabelaGastosApi">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tipo de gasto</th>
                            <th>Descrição</th>
                            <th>Valor</th>
                            <th>Data</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody id="corpoGastosApi"
                           data-api-url="${ctx}/safra/${safraId}/gastos"
                           data-api-gastos="${ctx}/gastos"
                           data-url-voltar="${ctx}/painel/safras/${safraId}/gastos">
                        <tr><td colspan="6">Carregando gastos...</td></tr>
                    </tbody>
                </table>
            </div>

            <div id="semGastosApi" class="empty-state" hidden>
                <h3>Nenhum gasto cadastrado</h3>
                <p>Esta safra ainda não possui despesas registradas.</p>
                <a href="${ctx}/painel/safras/${safraId}/gastos/novo"
                   class="btn btn-primary">Cadastrar gasto</a>
            </div>

            <div id="erroGastosApi" class="form-error" role="alert" hidden>
                Não foi possível consultar os gastos pela API REST.
            </div>
        </section>
    </main>
</div>
<script src="${ctx}/gastos-api.js?v=1"></script>
</body>
</html>
