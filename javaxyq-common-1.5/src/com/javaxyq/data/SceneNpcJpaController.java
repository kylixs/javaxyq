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
public class SceneNpcJpaController implements SceneNpcDAO {

    public SceneNpcJpaController() {
        emf = Persistence.createEntityManagerFactory("xyqdbPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SceneNpc sceneNpc) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sceneNpc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSceneNpc(sceneNpc.getId()) != null) {
                throw new PreexistingEntityException("SceneNpc " + sceneNpc + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SceneNpc sceneNpc) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sceneNpc = em.merge(sceneNpc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sceneNpc.getId();
                if (findSceneNpc(id) == null) {
                    throw new NonexistentEntityException("The sceneNpc with id " + id + " no longer exists.");
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
            SceneNpc sceneNpc;
            try {
                sceneNpc = em.getReference(SceneNpc.class, id);
                sceneNpc.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sceneNpc with id " + id + " no longer exists.", enfe);
            }
            em.remove(sceneNpc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SceneNpc> findSceneNpcEntities() {
        return findSceneNpcEntities(true, -1, -1);
    }

    public List<SceneNpc> findSceneNpcEntities(int maxResults, int firstResult) {
        return findSceneNpcEntities(false, maxResults, firstResult);
    }

    private List<SceneNpc> findSceneNpcEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from SceneNpc as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public SceneNpc findSceneNpc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SceneNpc.class, id);
        } finally {
            em.close();
        }
    }

    public int getSceneNpcCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from SceneNpc as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    //************************* custom *******************************//
    public List<SceneNpc> findNpcsBySceneId(int sceneId) {
        EntityManager em = getEntityManager();
        try {
        	Query q = em.createNamedQuery("SceneNpc.findBySceneId");
        	q.setParameter("sceneId", sceneId);
        	return q.getResultList();
        } finally {
            em.close();
        }
    }
    public int getNextSceneNpcId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select max(o.id) from SceneNpc as o");
            int maxId = ((Integer) q.getSingleResult()).intValue();
            return maxId +1;
        } finally {
            em.close();
        }
    }    
}
