package be.bbank.imp;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import be.bbank.dao.BTitularDao;
import be.bbank.models.BTitular;


public class BTitularDAOImpl implements BTitularDao {

    private final EntityManager em = Persistence.createEntityManagerFactory("bbank").createEntityManager();


    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void create(BTitular entity) {
        em.getTransaction().begin();

        em.persist(entity);

        em.getTransaction().commit();
    }

    @Override
    public List<BTitular> getAll() {
        TypedQuery<BTitular> query = em.createQuery("select u from BTitular u", BTitular.class);
        List<BTitular> liste = query.getResultList();
        em.clear();
        return liste;
    }

    @Override
    public Optional<BTitular> getOne(Long id) {
        BTitular btitular = em.find(BTitular.class, id);

        return Optional.ofNullable(btitular);
    }

    @Override
    public void update(BTitular entity) {
        em.getTransaction().begin();

        em.merge(entity);

        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        em.getTransaction().begin();

        Optional<BTitular> btitular = getOne(id);

        if (btitular.isPresent()) {
            em.remove(btitular.get());
            System.out.println("Suppression de : " + btitular.get());
        }
        em.getTransaction().commit();
    }
}
