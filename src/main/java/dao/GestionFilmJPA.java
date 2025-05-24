package dao;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import entity.Film;

public class GestionFilmJPA implements IGestionFilm {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("punit1");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void addFilm(Film f) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(f);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<Film> getAllFilms() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Film> query = em.createQuery("SELECT DISTINCT f FROM Film f LEFT JOIN FETCH f.categories LEFT JOIN FETCH f.acteurs ORDER BY f.titre", Film.class);
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<Film> getFilmsByMc(String mc) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Film> query = em.createQuery(
                "SELECT DISTINCT f FROM Film f LEFT JOIN FETCH f.categories LEFT JOIN FETCH f.acteurs WHERE lower(f.titre) LIKE lower(:x) ORDER BY f.titre",
                 Film.class);
            query.setParameter("x", "%" + (mc == null ? "" : mc) + "%");
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public Film getFilm(int id) {
        EntityManager em = getEntityManager();
        try {
             TypedQuery<Film> query = em.createQuery(
                 "SELECT f FROM Film f LEFT JOIN FETCH f.categories LEFT JOIN FETCH f.acteurs WHERE f.id = :id", Film.class);
             query.setParameter("id", id);
             List<Film> results = query.getResultList();
             return results.isEmpty() ? null : results.get(0);
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public void deleteFilm(int id) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Film film = em.find(Film.class, id);
            if (film != null) {
                em.remove(film);
            }
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
             throw new RuntimeException("Erreur lors de la suppression du film", e);
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public void updateFilm(Film f) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.merge(f); 
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du film", e);
        } finally {
            if (em != null) em.close();
        }
    }
    
    @Override
    public List<Film> getFilmsByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Film> query = em.createQuery("SELECT f FROM Film f WHERE f.id IN :ids", Film.class);
            query.setParameter("ids", ids);
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }
}