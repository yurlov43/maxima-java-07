package org.example.repository;

import org.example.model.Cat;

public interface CatRepository extends BaseRepository<Cat, Long> {
    void closeDBConnection();
}
