package dao;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import entity.Acteur;
import entity.Film;

public class GestionActeurJPA implements IGestionActeur {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("punit1");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void addActeur(Acteur a) {
         EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(a);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<Acteur> getAllActeurs() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Acteur> query = em.createQuery(
                "SELECT DISTINCT a FROM Acteur a LEFT JOIN FETCH a.films ORDER BY a.nom, a.prenom",
                Acteur.class);
            // ------------------------
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }

     @Override
    public List<Acteur> getActeursByMc(String mc) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Acteur> query = em.createQuery(
                "SELECT DISTINCT a FROM Acteur a LEFT JOIN FETCH a.films WHERE lower(a.nom) LIKE lower(:x) OR lower(a.prenom) LIKE lower(:x) ORDER BY a.nom, a.prenom",
                 Acteur.class);
             // ------------------------
            query.setParameter("x", "%" + mc + "%");
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

     @Override
     public Acteur getActeur(int id) {
         EntityManager em = getEntityManager();
         try {
              TypedQuery<Acteur> query = em.createQuery(
                  "SELECT a FROM Acteur a LEFT JOIN FETCH a.films WHERE a.id = :id",
                  Acteur.class);
              query.setParameter("id", id);
              try {
                  return query.getSingleResult();
              } catch (javax.persistence.NoResultException e) {
                  return null;
              }
         } finally {
             if (em != null) em.close();
         }
     }

    @Override
    public void deleteActeur(int id) {
       EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Acteur acteur = em.find(Acteur.class, id); 
            if (acteur != null) {
                 if (!acteur.getFilms().isEmpty()) {
                     TypedQuery<Film> filmQuery = em.createQuery(
                        "SELECT f FROM Film f JOIN f.acteurs a WHERE a.id = :acteurId", Film.class);
                    filmQuery.setParameter("acteurId", id);
                    List<Film> filmsLies = filmQuery.getResultList();
                    for(Film film : filmsLies) {
                         film.getActeurs().remove(acteur); 
                         em.merge(film); 
                    }
                 }
                em.remove(acteur); 
            }
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
             throw new RuntimeException("Impossible de supprimer l'acteur. Erreur: " + e.getMessage(), e);
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public void updateActeur(Acteur a) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.merge(a); 
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<Acteur> getActeursByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Acteur> query = em.createQuery(
                "SELECT DISTINCT a FROM Acteur a LEFT JOIN FETCH a.films WHERE a.id IN :ids", Acteur.class);
            query.setParameter("ids", ids);
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }
}