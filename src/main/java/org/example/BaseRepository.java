package org.example;
/*
Описать базовый интерфейс BaseRepository для реализации паттерна DAO
с CRUD операциями и выборкой всех объектов (для любого класса) findAll()
 */

public interface BaseRepository <T>{
    public T create(T item);
    public void read(T item);
    public void update(T item);
    public void delete(int id);
    public void findAll();
}
