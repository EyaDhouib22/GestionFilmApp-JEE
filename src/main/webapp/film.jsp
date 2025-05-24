<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %> <%-- Assuming you have a navbar.jsp --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%-- Dynamic title based on whether film.id exists and is non-zero --%>
    <title>${empty film or film.id == 0 ? 'Ajouter un Film' : 'Modifier le Film'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <style>select[multiple] {min-height: 150px; }</style>
</head>
<body>
<div class="container mt-4">
    <div class="card">
        <div class="card-header">
             <h2>${empty film or film.id == 0 ? 'Ajouter un nouveau Film' : 'Modifier le Film'}</h2>
        </div>
        <div class="card-body">
            <form method="post" action="film">

                <input type="hidden" name="id" value="${not empty film ? film.id : 0}">

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="titre" class="form-label">Titre</label>
                        <input type="text" class="form-control" id="titre" name="titre" value="<c:out value="${film.titre}"/>" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="realisateur" class="form-label">Réalisateur</label>
                        <input type="text" class="form-control" id="realisateur" name="realisateur" value="<c:out value="${film.realisateur}"/>" required>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="anneeSortie" class="form-label">Année de Sortie</label>
                        <input type="number" class="form-control" id="anneeSortie" name="anneeSortie" value="${film.anneeSortie}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="duree" class="form-label">Durée (minutes)</label>
                        <input type="number" class="form-control" id="duree" name="duree" value="${film.duree}" required min="1">
                    </div>
                </div>

                <div class="row">
                     <%-- Categories Selection --%>
                    <div class="col-md-6 mb-3">
                        <label for="categorieIds" class="form-label">Catégories</label>
                        <select name="categorieIds" id="categorieIds" class="form-select" multiple required>
                            <%-- Iterate through all available categories provided by the controller --%>
                            <c:forEach items="${allCategories}" var="cat">
                                <c:set var="isSelected" value="false"/>
                                <c:if test="${not empty film.categories}">
                                    <c:forEach items="${film.categories}" var="filmCat">
                                        <c:if test="${filmCat.id == cat.id}">
                                            <c:set var="isSelected" value="true"/>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                                <option value="${cat.id}" ${isSelected ? 'selected' : ''}>
                                    <c:out value="${cat.nom}"/>
                                </option>
                            </c:forEach>
                        </select>
                        <div class="form-text">Sélectionner plusieurs catégories.</div>
                    </div>

                     <%-- Actors Selection --%>
                    <div class="col-md-6 mb-3">
                        <label for="acteurIds" class="form-label">Acteurs</label>
                        <select name="acteurIds" id="acteurIds" class="form-select" multiple required>
                            <c:forEach items="${allActeurs}" var="act">
                                <c:set var="isSelectedActor" value="false"/>
                                <c:if test="${not empty film.acteurs}"> 
                                    <c:forEach items="${film.acteurs}" var="filmAct">
                                        <c:if test="${filmAct.id == act.id}">
                                            <c:set var="isSelectedActor" value="true"/>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                                <option value="${act.id}" ${isSelectedActor ? 'selected' : ''}>
                                   <c:out value="${act.prenom} ${act.nom}"/>
                                </option>
                            </c:forEach>
                        </select>
                         <div class="form-text">Sélectionner plusieurs acteurs.</div>
                    </div>
                </div>

                 <div class="mt-3">
                     <button type="submit" class="btn btn-primary">
                         <i class="fas fa-save"></i> ${empty film or film.id == 0 ? 'Enregistrer' : 'Mettre à jour'}
                     </button>
                     <a href="accueil" class="btn btn-secondary">
                         <i class="fas fa-times"></i> Annuler
                     </a>
                 </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>