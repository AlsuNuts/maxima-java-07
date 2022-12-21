package org.example.repository;

import java.util.List;

public interface BaseRepository<T, I> {
    boolean create(T element);      //save()
    T read(I id);                   //findById()
    int update(I id, T element);    //save()
    void delete(I id);              //remove()
    List<T> findAll();              //search(), get... select
}
