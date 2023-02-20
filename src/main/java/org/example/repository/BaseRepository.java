package org.example.repository;

import java.util.List;

public interface BaseRepository<T, I> {
    // Insert (создание записей)
    boolean create(T element);
    // Select (чтение записей)
    T read(I id);
    // Update (редактирование записей)
    int update(I id, T element);
    // Delete (удаление записей)
    void delete(I id);
    List<T> findAll();
}
