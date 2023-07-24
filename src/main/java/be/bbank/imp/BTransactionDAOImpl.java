package be.bbank.imp;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import be.bbank.dao.BTransactionDao;
import be.bbank.models.BTransaction;


public class BTransactionDAOImpl implements BTransactionDao {

    private final EntityManager em = Persistence.createEntityManagerFactory("bbank").createEntityManager();


    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void create(BTransaction entity) {
        em.getTransaction().begin();

        em.persist(entity);

        em.getTransaction().commit();
    }

    @Override
    public List<BTransaction> getAll() {
        TypedQuery<BTransaction> query = em.createQuery("select u from BTransaction u", BTransaction.class);
        List<BTransaction> liste = query.getResultList();
        em.clear();
        return liste;
    }

    @Override
    public Optional<BTransaction> getOne(Long id) {
        BTransaction btransaction = em.find(BTransaction.class, id);

        return Optional.ofNullable(btransaction);
    }

    @Override
    public void update(BTransaction entity) {
        em.getTransaction().begin();

        em.merge(entity);

        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        em.getTransaction().begin();

        Optional<BTransaction> btransaction = getOne(id);

        if (btransaction.isPresent()) {
            em.remove(btransaction.get());
            System.out.println("Suppression de : " + btransaction.get());
        }
        em.getTransaction().commit();
    }
}
