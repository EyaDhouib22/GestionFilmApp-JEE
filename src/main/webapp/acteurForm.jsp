<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%-- Ajout de la JSTL functions --%>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" <%-- Vérifiez version/intégrité --%>
	integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<title>${empty acteur or acteur.id == 0 ? 'Ajouter un Acteur' : 'Modifier l\'Acteur'}</title>
</head>
<body>
<div class="container mt-4">
    <div class="card">
        <div class="card-header">
             <h2>${empty acteur or acteur.id == 0 ? 'Ajouter un nouvel Acteur' : 'Modifier l\'Acteur'}</h2>
        </div>
        <div class="card-body">

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>
             <c:if test="${not empty formError}"> 
                <div class="alert alert-warning" role="alert">
                    <c:out value="${formError}"/>
                </div>
            </c:if>


            <%-- Le formulaire pointe vers /saveActeur --%>
            <form method="post" action="saveActeur">
                <input type="hidden" name="id" value="${not empty acteur ? acteur.id : 0}">

                <div class="form-group mb-3">
                    <label for="prenom" class="form-label">Prénom</label>
                    <input type="text" class="form-control" id="prenom" name="prenom" value="<c:out value='${acteur.prenom}'/>" required>
                </div>
                 <div class="form-group mb-3">
                    <label for="nom" class="form-label">Nom</label>
                    <input type="text" class="form-control" id="nom" name="nom" value="<c:out value='${acteur.nom}'/>" required>
                </div>

                 <%-- ****** Section pour la sélection des Films ****** --%>
                <div class="form-group mb-3">
                    <label for="filmIds" class="form-label">Films Joués</label>
                  
                    <select multiple class="form-select" id="filmIds" name="filmIds" size="10"> <%-- size="10" pour afficher 10 éléments --%>
                        <%-- Boucle sur tous les films disponibles passés par le contrôleur --%>
                        <c:forEach items="${allFilms}" var="filmOption">
                           
                            <c:set var="isSelected" value="${false}" />
                            <c:if test="${not empty acteur.films}"> 
                                <c:forEach items="${acteur.films}" var="acteurFilm">
                                    <c:if test="${acteurFilm.id == filmOption.id}">
                                        <c:set var="isSelected" value="${true}" />
                                    </c:if>
                                </c:forEach>
                            </c:if>

                            <option value="${filmOption.id}" ${isSelected ? 'selected' : ''}>
                                <c:out value="${filmOption.titre}"/> (${filmOption.anneeSortie})
                            </option>
                        </c:forEach>
                        <c:if test="${empty allFilms}">
                            <option disabled>Aucun film disponible à associer</option>
                        </c:if>
                    </select>
                     <div class="form-text">Maintenez Ctrl (ou Cmd sur Mac) pour sélectionner plusieurs films.</div>
                </div>

                 <button type="submit" class="btn btn-primary">
                     <i class="fas fa-save"></i> ${empty acteur or acteur.id == 0 ? 'Ajouter' : 'Modifier'}
                 </button>
                 <a href="listeActeurs" class="btn btn-secondary">
                     <i class="fas fa-times"></i> Annuler
                 </a>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
</body>
</html>