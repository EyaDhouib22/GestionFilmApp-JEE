package dao;

import java.util.Collections; 
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import entity.Categorie;

public class GestionCategorieJPA implements IGestionCategorie {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("punit1");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void addCategorie(Categorie c) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(c);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<Categorie> getAllCategories() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Categorie> query = em.createQuery("SELECT c FROM Categorie c ORDER BY c.nom", Categorie.class);
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public Categorie getCategorie(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categorie.class, id);
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Categorie> getCategoriesByMc(String mc) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Categorie> query = em.createQuery(
                "SELECT c FROM Categorie c WHERE lower(c.nom) LIKE lower(:x) ORDER BY c.nom",
                 Categorie.class 
            );
            query.setParameter("x", "%" + (mc == null ? "" : mc) + "%");
            return query.getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void deleteCategorie(int id) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Categorie categorie = em.find(Categorie.class, id);
            if (categorie != null) {
                
                em.remove(categorie);
            }
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
            throw new RuntimeException("Impossible de supprimer la catégorie, elle est peut-être encore liée à des films.", e);
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public void updateCategorie(Categorie c) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.merge(c);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<Categorie> getCategoriesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Categorie> query = em.createQuery("SELECT c FROM Categorie c WHERE c.id IN :ids", Categorie.class);
            query.setParameter("ids", ids);
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }
}