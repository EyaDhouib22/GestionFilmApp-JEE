package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream; 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.IGestionCategorie;
import dao.IGestionFilm;
import dao.IGestionActeur;
import dao.GestionCategorieJPA;
import dao.GestionFilmJPA;
import dao.GestionActeurJPA;
import entity.Categorie;
import entity.Film;
import entity.Acteur;

@WebServlet(urlPatterns = {
    "/", "/accueil", "/search", "/delete", "/edit", "/film", "/addFilm",
    "/listeCat", "/categories", "/saveCategorie", "/editCategorie","/deleteCategorie", "/searchCategorie","/addCat",
    "/listeActeurs", "/searchActeurs", "/addActeurForm", "/saveActeur", "/editActeur", "/deleteActeur"
})
public class Controlleur extends HttpServlet {
    private static final long serialVersionUID = 1L;

    IGestionFilm gestionFilm;
    IGestionCategorie gestionCategorie;
    IGestionActeur gestionActeur;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            gestionFilm = new GestionFilmJPA();
            gestionCategorie = new GestionCategorieJPA();
            gestionActeur = new GestionActeurJPA();
        } catch (Exception e) {
            System.err.println("Erreur fatale lors de l'initialisation des DAO JPA !");
            e.printStackTrace();
            throw new ServletException("Erreur d'initialisation JPA", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");

        try {
            switch (path) {
                 case "/":
                 case "/accueil":
                     loadAccueil(request, response);
                     break;
                 case "/search":
                     String mcFilm = request.getParameter("mc");
                     request.setAttribute("Films", gestionFilm.getFilmsByMc(mcFilm == null ? "" : mcFilm));
                     request.setAttribute("mc", mcFilm);
                     request.getRequestDispatcher("accueil.jsp").forward(request, response);
                     break;
                 case "/edit": 
                     handleEditFilm(request, response);
                     break;
                 case "/delete": 
                     handleDeleteFilm(request, response);
                     break;
                 case "/addFilm": 
                     request.setAttribute("film", new Film());
                     loadCommonFilmFormData(request); // Charger catégories/acteurs pour les selects
                     request.getRequestDispatcher("film.jsp").forward(request, response);
                     break;
                 case "/film": 
                     request.setAttribute("film", new Film());
                     loadCommonFilmFormData(request);
                     request.getRequestDispatcher("film.jsp").forward(request, response);
                     break;

                 case "/listeCat":
                     loadListeCategories(request, response, null);
                     break;
                 case "/categories":
                      handleEditCategorieForm(request, response); 
                     break;
                 case "/deleteCategorie":
                      handleDeleteCategorie(request, response);
                     break;
                 case "/addCat": 
                     request.setAttribute("categorie", new Categorie());
                     request.getRequestDispatcher("categories.jsp").forward(request, response);
                     break;
                 case "/searchCategorie":
                     handleSearchCategorie(request, response);
                     break;
                 case "/editCategorie": 
                     handleEditCategorieForm(request, response);
                     break;

                 case "/listeActeurs":
                     loadListeActeurs(request, response, null);
                     break;
                  case "/searchActeurs":
                      handleSearchActeurs(request, response);
                      break;
                  case "/addActeurForm": 
                      request.setAttribute("acteur", new Acteur());
                      loadCommonActorFormData(request); 
                      request.getRequestDispatcher("acteurForm.jsp").forward(request, response);
                      break;
                  case "/editActeur": 
                      handleEditActeur(request, response);
                      break;
                 case "/deleteActeur":
                     handleDeleteActeur(request, response);
                     break;
                default:
                     System.out.println("Chemin non géré (GET) : " + path + " -> Redirection vers accueil");
                     response.sendRedirect("accueil");
                    break;
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur de format de nombre (GET) pour path " + path + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format d'identifiant invalide.");
        } catch (ServletException | IOException e) { 
             System.err.println("Erreur Servlet/IO (GET) pour path " + path + ": " + e.getMessage());
             e.printStackTrace();
             throw e; 
        } catch (Exception e) {
            System.err.println("Erreur inattendue (GET) pour path " + path);
            e.printStackTrace();
             request.setAttribute("errorMessage", "Une erreur serveur est survenue: " + e.getMessage());
             try {
                request.getRequestDispatcher("/WEB-INF/erreur.jsp").forward(request, response);
             } catch (Exception ex) {
                System.err.println("Impossible de forward vers la page d'erreur.");
                throw new ServletException("Erreur serveur", e); 
             }
        }
    }


    // -----DoGET------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");

        try {
            switch(path) {
                case "/film": 
                    handleSaveOrUpdateFilm(request, response);
                    break;
                case "/saveCategorie":
                     handleSaveOrUpdateCategorie(request, response);
                    break;
                case "/saveActeur":
                     handleSaveOrUpdateActeur(request, response);
                     
                    break;
                default:
                     System.out.println("Chemin non géré (POST) : " + path + " -> Redirection vers accueil");
                    response.sendRedirect("accueil");
                    break;
            }
        } catch (NumberFormatException e) {
             System.err.println("Erreur de format de nombre (POST) pour path " + path + ": " + e.getMessage());
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format de données invalide soumis.");
        } catch (ServletException | IOException e) {
             System.err.println("Erreur Servlet/IO (POST) pour path " + path + ": " + e.getMessage());
             e.printStackTrace();
             throw e; 
        } catch (Exception e) {
            System.err.println("Erreur inattendue (POST) pour path " + path);
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la sauvegarde : " + e.getMessage());
             if ("/saveActeur".equals(path)) {
                 loadCommonActorFormData(request);
                 Acteur acteurWithError = new Acteur();
                 acteurWithError.setId(Integer.parseInt(request.getParameter("id") != null && !request.getParameter("id").equals("0") ? request.getParameter("id") : "0"));
                 acteurWithError.setNom(request.getParameter("nom"));
                 acteurWithError.setPrenom(request.getParameter("prenom"));
                 request.setAttribute("acteur", acteurWithError);
                 request.getRequestDispatcher("acteurForm.jsp").forward(request, response);
             } else if ("/film".equals(path)) {
                  loadCommonFilmFormData(request);
                  request.getRequestDispatcher("film.jsp").forward(request, response);
             } else if ("/saveCategorie".equals(path)) {
                  request.getRequestDispatcher("categories.jsp").forward(request, response);
             }
              else {
                  response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur serveur lors du traitement.");
             }
        }
    }


    private void loadAccueil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         List<Film> listeFilms = gestionFilm.getAllFilms(); 
         request.setAttribute("Films", listeFilms);
         request.getRequestDispatcher("accueil.jsp").forward(request, response);
    }

    private void loadCommonFilmFormData(HttpServletRequest request) {
         request.setAttribute("allCategories", gestionCategorie.getAllCategories());
         request.setAttribute("allActeurs", gestionActeur.getAllActeurs());
    }

    private void loadCommonActorFormData(HttpServletRequest request) {
        try {
            List<Film> allFilms = gestionFilm.getAllFilms();
            request.setAttribute("allFilms", allFilms);
        } catch (Exception e) {
             System.err.println("Erreur lors du chargement des films pour le formulaire acteur: " + e.getMessage());
             request.setAttribute("formError", "Impossible de charger la liste des films.");
             request.setAttribute("allFilms", new ArrayList<Film>()); 
        }
    }

    private void loadListeCategories(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
         if (errorMessage != null) request.setAttribute("errorMessage", errorMessage);
         request.setAttribute("categories", gestionCategorie.getAllCategories());
         request.getRequestDispatcher("listeCat.jsp").forward(request, response);
     }

    private void loadListeActeurs(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
          if (errorMessage != null) request.setAttribute("errorMessage", errorMessage);
          request.setAttribute("acteurs", gestionActeur.getAllActeurs());
          request.getRequestDispatcher("listeActeurs.jsp").forward(request, response);
     }


    private void handleEditFilm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         int idEditFilm = Integer.parseInt(request.getParameter("id")); 
         Film filmToEdit = gestionFilm.getFilm(idEditFilm);
         if (filmToEdit == null) {
             response.sendRedirect("accueil?error=FilmNotFound&id=" + idEditFilm);
             return;
         }
         request.setAttribute("film", filmToEdit);
         loadCommonFilmFormData(request); 
         request.getRequestDispatcher("film.jsp").forward(request, response);
    }

     private void handleEditCategorieForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idCatEditParam = request.getParameter("id");
        Categorie categorieToEdit = null;
        if (idCatEditParam != null && !idCatEditParam.isEmpty() && !idCatEditParam.equals("0")) {
             int idCatEdit = Integer.parseInt(idCatEditParam); 
             categorieToEdit = gestionCategorie.getCategorie(idCatEdit);
             if (categorieToEdit == null) {
                 response.sendRedirect("listeCat?error=CatNotFound&id=" + idCatEdit);
                 return;
             }
        } else {
             categorieToEdit = new Categorie();
        }
         request.setAttribute("categorie", categorieToEdit); 
         request.getRequestDispatcher("categories.jsp").forward(request, response);
    }

    private void handleEditActeur(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idEditActeur = Integer.parseInt(request.getParameter("id")); 
        Acteur acteurToEdit = gestionActeur.getActeur(idEditActeur); 
         if (acteurToEdit == null) {
            response.sendRedirect("listeActeurs?error=ActeurNotFound&id=" + idEditActeur);
            return;
        }
        request.setAttribute("acteur", acteurToEdit);
        loadCommonActorFormData(request); 
        request.getRequestDispatcher("acteurForm.jsp").forward(request, response);
    }

    private void handleSearchCategorie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mcCategorie = request.getParameter("mc");
        List<Categorie> categoriesFound = gestionCategorie.getCategoriesByMc(mcCategorie == null ? "" : mcCategorie);
        request.setAttribute("categories", categoriesFound);
        request.setAttribute("mc", mcCategorie);
        request.getRequestDispatcher("listeCat.jsp").forward(request, response);
    }

     private void handleSearchActeurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          String mcActeur = request.getParameter("mc");
          request.setAttribute("acteurs", gestionActeur.getActeursByMc(mcActeur == null ? "" : mcActeur));
          request.setAttribute("mc", mcActeur);
          request.getRequestDispatcher("listeActeurs.jsp").forward(request, response);
      }



    private void handleSaveOrUpdateFilm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idParam = request.getParameter("id");
        String titre = request.getParameter("titre");
        String realisateur = request.getParameter("realisateur");
        int anneeSortie = 0;
        int duree = 0;

        // Validation des entrées
        List<String> errors = new ArrayList<>();
        if (titre == null || titre.trim().isEmpty()) errors.add("Le titre est requis.");
        try {
             anneeSortie = Integer.parseInt(request.getParameter("anneeSortie"));
             if (anneeSortie < 1888) errors.add("Année de sortie invalide."); 
        } catch (NumberFormatException e) { errors.add("Format de l'année de sortie invalide."); }
        try {
             duree = Integer.parseInt(request.getParameter("duree"));
             if (duree <= 0) errors.add("La durée doit être positive.");
        } catch (NumberFormatException e) { errors.add("Format de la durée invalide."); }

        // --- Gestion Catégories ---
        String[] categorieIdsStr = request.getParameterValues("categorieIds");
        List<Integer> categorieIds = new ArrayList<>();
        if (categorieIdsStr != null) {
            Stream.of(categorieIdsStr).forEach(idStr -> {
                try { categorieIds.add(Integer.parseInt(idStr)); } catch (NumberFormatException e) { System.err.println("ID catégorie invalide ignoré: " + idStr); }
            });
        }
        Set<Categorie> selectedCategorieSet = new HashSet<>(gestionCategorie.getCategoriesByIds(categorieIds));

        // --- Gestion Acteurs ---
        String[] acteurIdsStr = request.getParameterValues("acteurIds");
        List<Integer> acteurIds = new ArrayList<>();
         if (acteurIdsStr != null) {
             Stream.of(acteurIdsStr).forEach(idStr -> {
                 try { acteurIds.add(Integer.parseInt(idStr)); } catch (NumberFormatException e) { System.err.println("ID acteur invalide ignoré: " + idStr); }
             });
        }
        Set<Acteur> selectedActeurSet = new HashSet<>(gestionActeur.getActeursByIds(acteurIds));

         if (!errors.isEmpty()) {
             request.setAttribute("errorMessage", String.join("<br>", errors));
             loadCommonFilmFormData(request);
             Film filmWithError = new Film(titre, realisateur, anneeSortie, duree);
             filmWithError.setId(Integer.parseInt(idParam != null && !idParam.equals("0") ? idParam : "0"));
             filmWithError.setCategories(selectedCategorieSet); 
             filmWithError.setActeurs(selectedActeurSet);       
             request.setAttribute("film", filmWithError);
             request.getRequestDispatcher("film.jsp").forward(request, response);
             return;
         }


        Film film;
        boolean isUpdate = idParam != null && !idParam.trim().isEmpty() && !idParam.equals("0");

        if (isUpdate) {
            int filmId = Integer.parseInt(idParam); 
            film = gestionFilm.getFilm(filmId); 
            if (film == null) {
                 response.sendRedirect("accueil?error=FilmNotFound&id=" + filmId);
                 return;
            }
        } else {
            film = new Film();
        }
        film.setTitre(titre.trim());
        film.setRealisateur(realisateur != null ? realisateur.trim() : null);
        film.setAnneeSortie(anneeSortie);
        film.setDuree(duree);
        film.setCategories(selectedCategorieSet); 
        film.setActeurs(selectedActeurSet);       

        try {
            if (isUpdate) {
                gestionFilm.updateFilm(film);
            } else {
                gestionFilm.addFilm(film);
            }
            response.sendRedirect("accueil"); 
        } catch (Exception e) {
             System.err.println("Erreur lors de la sauvegarde/MAJ du film: " + e.getMessage());
             e.printStackTrace();
             request.setAttribute("errorMessage", "Erreur technique lors de la sauvegarde du film: " + e.getMessage());
             loadCommonFilmFormData(request);
             request.setAttribute("film", film); 
             request.getRequestDispatcher("film.jsp").forward(request, response);
        }
    }

    private void handleSaveOrUpdateCategorie(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idCatParam = request.getParameter("id");
        String nomCategorie = request.getParameter("nom");
        Categorie categorie;
        boolean isUpdate = idCatParam != null && !idCatParam.trim().isEmpty() && !idCatParam.equals("0");

        if (nomCategorie == null || nomCategorie.trim().isEmpty()) {
             request.setAttribute("errorMessage", "Le nom de la catégorie est requis.");
             Categorie catWithError = new Categorie();
              catWithError.setId(Integer.parseInt(idCatParam != null && !idCatParam.equals("0") ? idCatParam : "0"));
             request.setAttribute("categorie", catWithError);
             request.getRequestDispatcher("categories.jsp").forward(request, response);
             return;
         }


        if (isUpdate) {
            int id = Integer.parseInt(idCatParam);
            categorie = gestionCategorie.getCategorie(id);
              if (categorie == null) {
                 response.sendRedirect("listeCat?error=CatNotFound&id=" + id);
                 return;
             }
            categorie.setNom(nomCategorie.trim());
            gestionCategorie.updateCategorie(categorie); 
        } else {
            categorie = new Categorie(nomCategorie.trim());
            gestionCategorie.addCategorie(categorie); 
        }
        response.sendRedirect("listeCat");
    }


    private void handleSaveOrUpdateActeur(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idActeurParam = request.getParameter("id");
        String nomActeur = request.getParameter("nom");
        String prenomActeur = request.getParameter("prenom");

        String[] filmIdsStr = request.getParameterValues("filmIds"); 
        List<Integer> filmIds = new ArrayList<>();
        if (filmIdsStr != null) {
            for (String idStr : filmIdsStr) {
                try {
                    filmIds.add(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                    System.err.println("ID de film invalide soumis et ignoré : " + idStr);
                }
            }
        }
        // ----------------------------------------------------

        Acteur acteur;
        boolean isUpdate = idActeurParam != null && !idActeurParam.trim().isEmpty() && !idActeurParam.equals("0");

        List<String> errors = new ArrayList<>();
        if (nomActeur == null || nomActeur.trim().isEmpty()) errors.add("Le nom est requis.");
        if (prenomActeur == null || prenomActeur.trim().isEmpty()) errors.add("Le prénom est requis.");

        if (!errors.isEmpty()) {
            request.setAttribute("errorMessage", String.join("<br>", errors));
            loadCommonActorFormData(request); 
            Acteur acteurWithError = new Acteur(nomActeur, prenomActeur);
            acteurWithError.setId(Integer.parseInt(idActeurParam != null && !idActeurParam.equals("0") ? idActeurParam : "0"));
            request.setAttribute("acteur", acteurWithError);
            request.getRequestDispatcher("acteurForm.jsp").forward(request, response);
            return;
        }

        try {
            List<Film> selectedFilmList = gestionFilm.getFilmsByIds(filmIds);
            Set<Film> selectedFilmSet = new HashSet<>(selectedFilmList);

            if (isUpdate) {
                int id = Integer.parseInt(idActeurParam);
                acteur = gestionActeur.getActeur(id);
                if (acteur == null) {
                    response.sendRedirect("listeActeurs?error=ActeurNotFound&id=" + id);
                    return;
                }
            } else {
                acteur = new Acteur();
            }
            acteur.setNom(nomActeur.trim());
            acteur.setPrenom(prenomActeur.trim());
            acteur.setFilms(selectedFilmSet);
            if (isUpdate) {
                gestionActeur.updateActeur(acteur);
            } else {
                gestionActeur.addActeur(acteur);
            }
            response.sendRedirect("listeActeurs"); 

        } catch (NumberFormatException e) {
             System.err.println("Erreur NFE inattendue dans saveActeur: " + e.getMessage());
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID d'acteur invalide.");
        } catch (Exception e) {
             System.err.println("Erreur lors de la sauvegarde de l'acteur : " + e.getMessage());
             e.printStackTrace();
             request.setAttribute("errorMessage", "Erreur technique lors de la sauvegarde : " + e.getMessage());
             loadCommonActorFormData(request); 
             Acteur acteurWithError = new Acteur(nomActeur, prenomActeur);
              acteurWithError.setId(Integer.parseInt(idActeurParam != null && !idActeurParam.equals("0") ? idActeurParam : "0"));
             request.setAttribute("acteur", acteurWithError);
             request.getRequestDispatcher("acteurForm.jsp").forward(request, response);
        }
    }


    private void handleDeleteFilm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
             int idDeleteFilm = Integer.parseInt(request.getParameter("id"));
             gestionFilm.deleteFilm(idDeleteFilm);
             response.sendRedirect("accueil"); 
        } catch (NumberFormatException e) {
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de film invalide pour suppression.");
        } catch (Exception e) {
             System.err.println("Erreur lors de la suppression du film: " + e.getMessage());
             response.sendRedirect("accueil?error=DeleteFilmFailed&message=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void handleDeleteCategorie(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessage = null;
          try {
               int idCatDelete = Integer.parseInt(request.getParameter("id"));
               gestionCategorie.deleteCategorie(idCatDelete);
          } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de catégorie invalide pour suppression.");
                return; 
          } catch (RuntimeException e) { 
               System.err.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
               errorMessage = "Impossible de supprimer la catégorie. Vérifiez si elle est utilisée par des films.";
          }

          if (errorMessage != null) {
                response.sendRedirect("listeCat?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
          } else {
               response.sendRedirect("listeCat?success=delete"); 
          }
    }

    private void handleDeleteActeur(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessage = null;
           try {
                int idDeleteActeur = Integer.parseInt(request.getParameter("id"));
                gestionActeur.deleteActeur(idDeleteActeur); // Le DAO gère la rupture des liens avec les films
           } catch (NumberFormatException e) {
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID d'acteur invalide pour suppression.");
                 return;
           } catch (RuntimeException e) {
                System.err.println("Erreur lors de la suppression de l'acteur: " + e.getMessage());
                errorMessage = "Impossible de supprimer l'acteur: " + e.getMessage();
                 e.printStackTrace(); 
           }

           if (errorMessage != null) {
                 response.sendRedirect("listeActeurs?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
           } else {
                response.sendRedirect("listeActeurs?success=delete");
           }
    }

}