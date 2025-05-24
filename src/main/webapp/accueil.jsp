<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %> <%-- Assuming you have a navbar.jsp --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accueil - Liste des Films</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" /> <%-- Updated Font Awesome version --%>
    <style>.category-list span, .actor-list span {display: inline-block; margin-right: 5px;background-color: #e9ecef;padding: 2px 5px;border-radius: 4px;font-size: 0.9em; margin-bottom: 3px;}.action-buttons a {margin-right: 5px; }</style>
</head>
<body>
<div class="container mt-4">
    <h2>Liste des Films</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Erreur:</strong> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
     <c:if test="${not empty param.error}"> <%-- Check for error parameter from redirects --%>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Erreur:</strong> ${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
     </c:if>
    <c:if test="${not empty param.success}"> <%-- Check for success parameter from redirects --%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
             Opération réussie ! (${param.success})
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
     </c:if>


    <div class="d-flex justify-content-between align-items-center mb-3">
        <a href="film" class="btn btn-success">
            <i class="fas fa-plus"></i> Ajouter un Film
        </a>
        <form action="search" method="get" class="d-flex">
             <input type="search" name="mc" class="form-control me-2" placeholder="Rechercher par titre..." value="${mc}">
             <button class="btn btn-outline-primary" type="submit"><i class="fas fa-search"></i></button>
         </form>
    </div>

    <table class="table table-striped table-bordered table-hover">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Titre</th>
                <th>Réalisateur</th>
                <th>Année</th>
                <th>Durée</th>
                <th>Catégories</th>
                <th>Acteurs</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${Films}" var="f">
                <tr>
                    <td>${f.id}</td>
                    <td><c:out value="${f.titre}"/></td>
                    <td><c:out value="${f.realisateur}"/></td>
                    <td>${f.anneeSortie}</td>
                    <td>${f.duree} min</td>
                    <td class="category-list">
                        <%-- Display categories associated with the film --%>
                        <c:if test="${empty f.categories}">
                            <span class="text-muted">Aucune</span>
                        </c:if>
                        <c:forEach items="${f.categories}" var="cat">
                            <span><c:out value="${cat.nom}"/></span>
                        </c:forEach>
                    </td>
                    <td class="actor-list">
                        <%-- Display actors associated with the film --%>
                        <c:if test="${empty f.acteurs}">
                             <span class="text-muted">Aucun</span>
                        </c:if>
                        <c:forEach items="${f.acteurs}" var="act">
                            <span><c:out value="${act.prenom} ${act.nom}"/></span>
                        </c:forEach>
                    </td>
                    <td class="action-buttons">
                        <a href="edit?id=${f.id}" class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i>
                        </a>
                        <a href="delete?id=${f.id}" class="btn btn-danger btn-sm" title="Supprimer"
                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer le film \'${f.titre}\' ?');">
                            <i class="fas fa-trash"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty Films}">
                <tr>
                    <td colspan="8" class="text-center fst-italic">Aucun film trouvé.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>