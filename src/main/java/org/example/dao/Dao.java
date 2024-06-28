package org.example.dao;

import org.example.entity.Workspace;

import java.util.List;
import java.util.Optional;

public interface Dao<ID, Entity> {
    List<Entity> findAll();

    Optional<Entity> findById(ID id);

    boolean deleteById(ID id);

    Entity save(Entity entity);

    boolean update(Entity entity);


}
