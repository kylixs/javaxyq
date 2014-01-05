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
public class SceneTeleporterJpaController implements SceneTeleporterDAO {

    public SceneTeleporterJpaController() {
        emf = Persistence.createEntityManagerFactory("xyqdbPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SceneTeleporter sceneTeleporter) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sceneTeleporter);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSceneTeleporter(sceneTeleporter.getId()) != null) {
                throw new PreexistingEntityException("SceneTeleporter " + sceneTeleporter + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SceneTeleporter sceneTeleporter) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sceneTeleporter = em.merge(sceneTeleporter);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sceneTeleporter.getId();
                if (findSceneTeleporter(id) == null) {
                    throw new NonexistentEntityException("The sceneTeleporter with id " + id + " no longer exists.");
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
            SceneTeleporter sceneTeleporter;
            try {
                sceneTeleporter = em.getReference(SceneTeleporter.class, id);
                sceneTeleporter.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sceneTeleporter with id " + id + " no longer exists.", enfe);
            }
            em.remove(sceneTeleporter);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SceneTeleporter> findSceneTeleporterEntities() {
        return findSceneTeleporterEntities(true, -1, -1);
    }

    public List<SceneTeleporter> findSceneTeleporterEntities(int maxResults, int firstResult) {
        return findSceneTeleporterEntities(false, maxResults, firstResult);
    }

    private List<SceneTeleporter> findSceneTeleporterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from SceneTeleporter as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public SceneTeleporter findSceneTeleporter(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SceneTeleporter.class, id);
        } finally {
            em.close();
        }
    }

    public int getSceneTeleporterCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from SceneTeleporter as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    //************************* custom *******************************//
    public List<SceneTeleporter> findTeleportersBySceneId(int sceneId) {
        EntityManager em = getEntityManager();
        try {
        	Query q = em.createNamedQuery("SceneTeleporter.findByStartId");
        	q.setParameter("startId", sceneId);
        	return q.getResultList();
        } finally {
            em.close();
        }
    }

}
