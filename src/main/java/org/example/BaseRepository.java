package org.example;
/*
Описать базовый интерфейс BaseRepository для реализации паттерна DAO
с CRUD операциями и выборкой всех объектов (для любого класса) findAll()
 */

//import java.util.List;
//import java.util.Optional;

public interface BaseRepository <T>{
    //я пока еще пытаюсь понять, нужно это или я усложняю, поэтому просто закомментирую следующие строчки
    //Optional<T> get(int id);
    //List<T> getAll();


    public T create(T item);
    public void read(T item);
    public void update(T item);
    public void delete(int id);
    public void findAll();
}
