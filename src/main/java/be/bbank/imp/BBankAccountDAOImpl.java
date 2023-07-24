package be.bbank.imp;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import be.bbank.dao.BBankAccountDao;
import be.bbank.models.BBankAccount;


public class BBankAccountDAOImpl implements BBankAccountDao {

    private final EntityManager em = Persistence.createEntityManagerFactory("bbank").createEntityManager();


    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void create(BBankAccount entity) {
        em.getTransaction().begin();

        em.persist(entity);

        em.getTransaction().commit();
    }

    @Override
    public List<BBankAccount> getAll() {
        TypedQuery<BBankAccount> query = em.createQuery("select u from BBankAccount u", BBankAccount.class);
        List<BBankAccount> liste = query.getResultList();
        em.clear();
        return liste;
    }

    @Override
    public Optional<BBankAccount> getOne(Long id) {
        BBankAccount bBankAccount = em.find(BBankAccount.class, id);

        return Optional.ofNullable(bBankAccount);
    }

    @Override
    public void update(BBankAccount entity) {
        em.getTransaction().begin();

        em.merge(entity);

        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        em.getTransaction().begin();

        Optional<BBankAccount> bbankAccount = getOne(id);

        if (bbankAccount.isPresent()) {
            em.remove(bbankAccount.get());
            System.out.println("Suppression de : " + bbankAccount.get());
        }
        em.getTransaction().commit();
    }
}
