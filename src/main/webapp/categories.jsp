<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${empty categorie or categorie.id == 0 ? 'Ajouter une Catégorie' : 'Modifier la Catégorie'}</title> <%-- Dynamic Title --%>
<link rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
    integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
    crossorigin="anonymous" referrerpolicy="no-referrer" />
<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
    crossorigin="anonymous">
</head>
<body>
<div class="container mt-4"> 
    <div class="card"> 
        <div class="card-header">
             <h2>${empty categorie or categorie.id == 0 ? 'Ajouter une nouvelle Catégorie' : 'Modifier la Catégorie'}</h2>
        </div>
        <div class="card-body">
            <form method="post" action="saveCategorie">
                <input type="hidden" name="id" value="${not empty categorie ? categorie.id : 0}">

                <div class="mb-3">
                    <label for="nom" class="form-label">Nom de la catégorie</label>
                    <input type="text" class="form-control" id="nom" name="nom" value="${categorie.nom}" required>
                </div>

                 <button type="submit" class="btn btn-primary">
                     <i class="fas fa-save"></i> ${empty categorie or categorie.id == 0 ? 'Ajouter' : 'Modifier'}
                 </button>
                 <a href="listeCat" class="btn btn-secondary"> <%-- Link to the category list --%>
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