package com.pykost.dao;

import com.pykost.entity.Book;
import com.pykost.exception.DAOException;
import com.pykost.util.ConnectionManager;

import java.io.Serial;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements DAO<Book, Long>, Serializable {
    @Serial
    private static final long serialVersionUID = 879342512444L;
    private static final BookDAO INSTANCE = new BookDAO();

    private BookDAO() {
    }

    public static BookDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        String deleteSql = "DELETE FROM book WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Book save(Book book) {
        String saveSql = "INSERT INTO book(name, description, author_id) VALUES (?,?,?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
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
        String updateSql = "UPDATE book SET name = ?, description = ?, author_id = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
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
        String findByIdSql = "SELECT * FROM book WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByIdSql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = mapResultSetToBook(resultSet);
                    return Optional.of(book);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.empty();
    }

    public List<Book> getAllBooks() {
        String findAllBooksByAuthorSql = "SELECT * FROM book";
        List<Book> books = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllBooksByAuthorSql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = mapResultSetToBook(resultSet);
                    books.add(book);
                }
                return books;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setName(resultSet.getString("name"));
        book.setDescription(resultSet.getString("description"));

        AuthorDAO authorDAO = AuthorDAO.getInstance();
        book.setAuthor(authorDAO.findById(resultSet.getLong("author_id")).orElse(null));

        return book;
    }

}
