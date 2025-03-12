package com.pykost.dao;

import com.pykost.entity.Book;
import com.pykost.exception.DAOException;
import com.pykost.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements DAO<Book, Long> {
    private static final BookDAO INSTANCE = new BookDAO();
    private static final String DELETE_SQL = """
            DELETE FROM book
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO book(name, description, author_id)
            VALUES (?,?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE book
            SET name = ?,
            description = ?,
            author_id = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_BOOKS_BY_AUTHOR_SQL = """
            SELECT id,
                   name,
                   description,
                   author_id
            FROM book
            WHERE author_id = ?
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id,
                   name,
                   description,
                   author_id
                   FROM book
            WHERE id = ?;
            """;

    private BookDAO() {
    }

    public static BookDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Book save(Book book) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setLong(3, book.getAuthor().getId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong("id"));
                }
                return book;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Book book) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setLong(3, book.getAuthor().getId());
            preparedStatement.setLong(4, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Book book = null;
                if (resultSet.next()) {
                    book = new Book();
                    book.setId(resultSet.getLong("id"));
                    book.setName(resultSet.getString("name"));
                    book.setDescription(resultSet.getString("description"));
                }
                return Optional.ofNullable(book);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public List<Book> findAllBooksByAuthor(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BOOKS_BY_AUTHOR_SQL)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Book> books = new ArrayList<>();
                Book book;
                while (resultSet.next()) {
                    book = new Book();
                    book.setId(resultSet.getLong("id"));
                    book.setName(resultSet.getString("name"));
                    book.setDescription(resultSet.getString("description"));
                    books.add(book);
                }
                return books;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

}
