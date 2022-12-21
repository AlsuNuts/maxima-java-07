package org.example;

import junit.framework.TestCase;
import org.example.repository.Cat;
import org.example.repository.CatRepository;
import org.example.repository.SimpleCatRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;


public class CatRepositoryTest
{
    private CatRepository repository;
    @Before
    public void init(){
        repository = new SimpleCatRepository();
    }
    @Test
    public void shouldCRUDWorks (){
        Cat cat1 = new Cat(1L, "Мурзик", 12, true);
        Cat cat2 = new Cat(2L, "Пушок", 10, false);
        Cat cat3 = new Cat(3L, "Кусец", 7, true);
        Cat cat4 = new Cat(4L, "Барсик", 8, false);
        Cat cat5 = new Cat(5L, "Мурка", 3, true);

        repository.create(cat1);
        repository.create(cat2);
        repository.create(cat3);
        repository.create(cat4);
        repository.create(cat5);

        List<Cat> cats = repository.findAll();
        assertEquals(5, cats.size());

        Cat testCat = repository.read(3L);
        assertEquals("Кусец", testCat.getName());
        assertEquals(7, testCat.getWeight());
        assertTrue(testCat.isAngry());


        Cat newCat1 = new Cat(5L, "Мурка", 3, false);
        Cat newCat2 = new Cat( 2L, "Пушок", 9, true);

        repository.update(newCat1.getId(), newCat1);
        repository.update(newCat2.getId(), newCat2);

        testCat = repository.read(5L);
        assertEquals("Мурка", testCat.getName());
        assertEquals(3, testCat.getWeight());
        assertTrue(!testCat.isAngry());

        repository.delete(1L);
        testCat = repository.read(1L);
        assertNull(testCat);
        repository.delete(4L);

        cats = repository.findAll();
        assertEquals(3, cats.size());

    }

}