package com.distribuida.servicios;

import com.distribuida.db.Book;
import com.distribuida.dtos.BookDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookRepositoryImpl implements BookRepository {


    @PersistenceContext(unitName = "books")
    private EntityManager entityManager;


    @Override
    public List<Book> findAll() {
        return entityManager.createNamedQuery("Book.findAll", Book.class).getResultList();
    }

    @Override
    public Book findById(Integer id) {
        return entityManager.find(Book.class, Integer.valueOf(id));
    }

    @Override
    public void insert(Book book) {
        entityManager.persist(book);
    }

    @Override
    public void update(Book book) {

    }

    @Override
    public void delete(Integer id) {

    }
}
