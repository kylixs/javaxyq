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
public class MedicineItemJpaController implements MedicineItemDAO {

    public MedicineItemJpaController() {
        emf = Persistence.createEntityManagerFactory("xyqdbPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MedicineItem medicineItem) throws PreexistingEntityException, MedicineItemException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(medicineItem);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedicineItem(medicineItem.getId()) != null) {
                throw new PreexistingEntityException("MedicineItem " + medicineItem + " already exists.", ex);
            }
            throw new MedicineItemException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MedicineItem medicineItem) throws NonexistentEntityException, MedicineItemException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            medicineItem = em.merge(medicineItem);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = medicineItem.getId();
                if (findMedicineItem(id) == null) {
                    throw new NonexistentEntityException("The medicineItem with id " + id + " no longer exists.");
                }
            }
            throw new MedicineItemException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException,MedicineItemException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MedicineItem medicineItem;
            try {
                medicineItem = em.getReference(MedicineItem.class, id);
                medicineItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicineItem with id " + id + " no longer exists.", enfe);
            }
            em.remove(medicineItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MedicineItem> findMedicineItemEntities() {
        return findMedicineItemEntities(true, -1, -1);
    }

    public List<MedicineItem> findMedicineItemEntities(int maxResults, int firstResult) {
        return findMedicineItemEntities(false, maxResults, firstResult);
    }

    private List<MedicineItem> findMedicineItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from MedicineItem as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MedicineItem findMedicineItem(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MedicineItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicineItemCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from MedicineItem as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    //************* custom *********************//
    public MedicineItem findMedicineItemByName(String name) {
    	EntityManager em = getEntityManager();
    	try {
    		Query query = em.createNamedQuery("MedicineItem.findByName");
    		query.setParameter("name", name);
    		return (MedicineItem) query.getSingleResult();
    	} finally {
    		em.close();
    	}
    }
    
    public List<MedicineItem> findMedicineItemsByType(int type) {
    	EntityManager em = getEntityManager();
    	try {
    		Query query = em.createNamedQuery("MedicineItem.findByType");
    		query.setParameter("type", Integer.toHexString(type));
    		return query.getResultList();
    	} finally {
    		em.close();
    	}
    }    

}
