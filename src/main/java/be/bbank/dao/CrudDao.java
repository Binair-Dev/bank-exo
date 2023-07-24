package be.bbank.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

public interface CrudDao<TENTITY, TID> {

    EntityManager getEm();

    // Create
    void create(TENTITY entity);

    // Read
    List<TENTITY> getAll();

    Optional<TENTITY> getOne(TID id);

    // Update
    void update(TENTITY entity);

    // Delete
    void delete(TID id);
}
