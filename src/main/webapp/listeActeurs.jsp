<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Liste des Acteurs</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" /> <%-- Vérifiez la version si nécessaire --%>
<style>
    /* Optionnel: pour limiter la hauteur si la liste de films est longue */
    .film-list {
        max-height: 100px; /* Ajustez si besoin */
        overflow-y: auto;
        font-size: 0.9em; /* Rendre le texte un peu plus petit */
        padding-left: 10px;
    }
</style>
</head>
<body>
<div class="container mt-4">
    <h2>Liste des Acteurs</h2>

    <%-- Afficher message d'erreur si présent --%>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            ${errorMessage}
        </div>
    </c:if>
     <c:if test="${not empty requestScope.error}"> <%-- Pour les erreurs passées en paramètre d'URL --%>
        <div class="alert alert-danger" role="alert">
             <c:choose>
                 <c:when test="${requestScope.error == 'NomPrenomRequis'}">Nom et prénom sont requis.</c:when>
                 <c:when test="${requestScope.error == 'ActeurNotFound'}">Acteur non trouvé.</c:when>
                 <%-- Ajoutez d'autres cas d'erreur si nécessaire --%>
                 <c:otherwise>Erreur: ${requestScope.error}</c:otherwise>
             </c:choose>
        </div>
    </c:if>


    <div class="mb-3">
        <a href="addActeurForm" class="btn btn-success">
            <i class="fas fa-plus"></i> Ajouter un Acteur
        </a>
        <form action="searchActeurs" method="get" class="d-inline-block float-end">
             <div class="input-group">
                 <input type="text" name="mc" class="form-control" placeholder="Rechercher..." value="${mc}">
                 <button class="btn btn-outline-secondary" type="submit"><i class="fas fa-search"></i></button>
             </div>
         </form>
    </div>

    <table class="table table-striped table-bordered">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Prénom</th>
                <th>Nom</th>
                <th>Films Joués</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${acteurs}" var="acteur">
                <tr>
                    <td>${acteur.id}</td>
                    <td><c:out value="${acteur.prenom}"/></td> 
                    <td><c:out value="${acteur.nom}"/></td>
                    <td>
                        <%-- films d'acteur --%>
                        <c:choose>
                            <c:when test="${not empty acteur.films}">
                                <div class="film-list"> 
                                    <c:forEach items="${acteur.films}" var="film" varStatus="loop">
                                        <c:out value="${film.titre}"/> (${film.anneeSortie})<c:if test="${not loop.last}"><br/></c:if>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Aucun</span> 
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="editActeur?id=${acteur.id}" class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i>
                        </a>
                        <a href="deleteActeur?id=${acteur.id}" class="btn btn-danger btn-sm" title="Supprimer" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet acteur ?\nCela pourrait échouer s\'il est encore lié à des films.');">
                            <i class="fas fa-trash"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty acteurs}">
                <tr>
                    <td colspan="5" class="text-center">Aucun acteur trouvé.</td> 
                </tr>
            </c:if>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>