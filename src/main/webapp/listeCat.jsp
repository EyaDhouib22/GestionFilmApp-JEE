<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Liste des Catégories</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
</head>
<body>
<div class="container mt-4">
    <h2>Liste des Catégories</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            ${errorMessage}
        </div>
    </c:if>
     <c:if test="${not empty successMessage}">
        <div class="alert alert-success" role="alert">
            ${successMessage}
        </div>
     </c:if>


    <div class="mb-3">
        <a href="addCat" class="btn btn-success"> 
            <i class="fas fa-plus"></i> Ajouter une Catégorie
        </a>
        <form action="searchCategories" method="get" class="d-inline-block float-end">
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
                <th>Nom</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${categories}" var="c"> 
                <tr>
                    <td>${c.id}</td>
                    <td>${c.nom}</td>
                    <td>
                        <a href="editCategorie?id=${c.id}" class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i>
                        </a>
                        <a href="deleteCategorie?id=${c.id}" class="btn btn-danger btn-sm" title="Supprimer"
                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette catégorie ?');">
                            <i class="fas fa-trash"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty categories}">
                <tr>
                    <td colspan="3" class="text-center">Aucune catégorie trouvée.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>