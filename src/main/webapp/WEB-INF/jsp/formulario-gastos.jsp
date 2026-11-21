<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%-- A variável 'acao' já é fornecida pelo GastosPaginaController. --%>
<c:choose>
    <c:when test="${fn:contains(acao, '/editar/')}">
        <c:set var="metodoRest" value="PUT" />
        <c:set var="idGasto" value="${fn:substringAfter(acao, '/editar/')}" />
        <c:set var="urlApi" value="${ctx}/gastos/${idGasto}" />
        <c:set var="sucesso" value="editado" />
        <c:set var="textoBotao" value="Salvar alterações" />
    </c:when>
    <c:otherwise>
        <c:set var="metodoRest" value="POST" />
        <c:set var="urlApi" value="${ctx}/safra/${safraId}/gastos" />
        <c:set var="sucesso" value="cadastrado" />
        <c:set var="textoBotao" value="Cadastrar gasto" />
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${titulo}" /> - SafraControl</title>
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
                <h1><c:out value="${titulo}" /></h1>
                <p class="subtitle">Safra ID: <c:out value="${safraId}" /></p>
            </div>
            <a href="${ctx}/painel/safras/${safraId}/gastos"
               class="btn btn-outline">Voltar</a>
        </header>

        <section class="form-panel">
            <h2>Dados do gasto</h2>
            <p class="form-intro">Preencha as informações da despesa.</p>

            <c:forEach var="erro" items="${erros}">
                <div class="form-error"><c:out value="${erro.defaultMessage}" /></div>
            </c:forEach>
            <div id="erroGastoApi" class="form-error" role="alert" hidden></div>

            <form id="formGastoApi"
                  data-metodo="${metodoRest}"
                  data-api-url="${urlApi}"
                  data-sucesso-url="${ctx}/painel/safras/${safraId}/gastos?${sucesso}=1">

                <div class="form-grid">
                    <div class="field">
                        <label for="tipoGasto">Tipo de gasto</label>
                        <input type="text" id="tipoGasto" name="tipoGasto"
                               value="${fn:escapeXml(gastoDto.tipoGasto)}"
                               placeholder="Ex.: Diesel" required>
                    </div>

                    <div class="field">
                        <label for="dataGasto">Data do gasto</label>
                        <input type="date" id="dataGasto" name="dataGasto"
                               value="${gastoDto.dataGasto}" required>
                    </div>

                    <div class="field field-full">
                        <label for="descricao">Descrição</label>
                        <textarea id="descricao" name="descricao" rows="4"
                                  placeholder="Descreva o gasto realizado"
                                  required><c:out value="${gastoDto.descricao}" /></textarea>
                    </div>

                    <div class="field field-full">
                        <label for="valor">Valor do gasto (R$)</label>
                        <input type="number" id="valor" name="valor"
                               value="${gastoDto.valor}"
                               placeholder="Ex.: 450.00"
                               min="0.01" step="0.01" required>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <c:out value="${textoBotao}" />
                    </button>
                    <a href="${ctx}/painel/safras/${safraId}/gastos"
                       class="form-cancel">Cancelar</a>
                </div>
            </form>
        </section>
    </main>
</div>
<script src="${ctx}/formulario-gastos.js?v=2"></script>
</body>
</html>
