/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Administrator
 */
public class SceneJpaController implements SceneDAO {

    public SceneJpaController() {
        emf = Persistence.createEntityManagerFactory("xyqdbPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Scene scene) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(scene);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findScene(scene.getId()) != null) {
                throw new PreexistingEntityException("Scene " + scene + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Scene scene) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            scene = em.merge(scene);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = scene.getId();
                if (findScene(id) == null) {
                    throw new NonexistentEntityException("The scene with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Scene scene;
            try {
                scene = em.getReference(Scene.class, id);
                scene.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The scene with id " + id + " no longer exists.", enfe);
            }
            em.remove(scene);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Scene> findSceneEntities() {
        return findSceneEntities(true, -1, -1);
    }

    public List<Scene> findSceneEntities(int maxResults, int firstResult) {
        return findSceneEntities(false, maxResults, firstResult);
    }

    private List<Scene> findSceneEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Scene as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Scene findScene(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Scene.class, id);
        } finally {
            em.close();
        }
    }

    public int getSceneCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Scene as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
