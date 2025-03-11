package com.pykost;

import com.pykost.dao.AuthorDAO;
import com.pykost.dao.BookDAO;
import com.pykost.entity.Author;
import com.pykost.entity.Book;
import com.pykost.util.ConnectionManager;

import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {
//        try (Connection connection = ConnectionManager.get()) {
//
//        } finally {
//            ConnectionManager.closePool();
//        }

//        AuthorDAO authorDAO = AuthorDAO.getInstance();
//        Author author = new Author();
//        author.setName("Александр Пушкин");
//        authorDAO.save(author);
//
//
//        BookDAO bookDAO = BookDAO.getInstance();
//        Book book = new Book();
//        book.setName("Капитанская дочка");
//        book.setDescription("исторический роман, в котором на фоне событий восстания Емельяна Пугачёва ");
//        book.setAuthor(author);
//        bookDAO.save(book);

        BookDAO bookDAO = BookDAO.getInstance();
        List<Book> byId = bookDAO.findByAuthor(3L);
        System.out.println(byId);

//        BookDAO bookDAO = BookDAO.getInstance();
//        Optional<Book> byId = bookDAO.findById(2L);
//        System.out.println(byId);


    }
}
