package org.example;

import org.example.model.Cat;
import org.example.repository.CatRepository;
import org.example.repository.SimpleCatRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CatRepositoryTest {
    private CatRepository repo;

    @Before
    public void init() {
        repo = new SimpleCatRepository("cats");
    }

    @Test
    public void shouldCRUDWorks() {
        Cat cat = new Cat(1L, "Мурзик", 10, true);
        repo.create(cat);

        cat = new Cat(2L, "Рамзес", 2, false);
        repo.create(cat);

        cat = new Cat(3L, "Барсик", 5, true);
        repo.create(cat);

        cat = new Cat(4L, "Мурка", 4, false);
        repo.create(cat);

        cat = new Cat(5L, "Карл III", 7, true);
        repo.create(cat);

        List<Cat> cats = repo.findAll();
        assertEquals(5, cats.size());

        Cat testCat = repo.read(3L);
        assertEquals("Барсик", testCat.getName());
        assertEquals(5, testCat.getWeight());
        assertTrue(testCat.isAngry());

        Cat newCat = new Cat(5L, "Карл III", 7, false);
        repo.update(newCat.getId(), newCat);

        newCat = new Cat(2L, "Рамзес", 3, true);
        repo.update(newCat.getId(), newCat);

        testCat = repo.read(5L);
        assertEquals("Карл III", testCat.getName());
        assertEquals(7, testCat.getWeight());
        assertFalse(testCat.isAngry());

        repo.delete(1L);
        testCat = repo.read(1L);
        assertNull(testCat);
        repo.delete(4L);

        cats = repo.findAll();
        assertEquals(3, cats.size());

        repo.closeDBConnection();
    }
}
