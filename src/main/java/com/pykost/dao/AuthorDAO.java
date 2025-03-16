package com.pykost.dao;

import com.pykost.entity.Author;
import com.pykost.entity.Book;
import com.pykost.exception.DAOException;
import com.pykost.util.ConnectionManager;

import java.io.Serial;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAO implements DAO<Author, Long>, Serializable {
    @Serial
    private static final long serialVersionUID = 576484572456L;
    private static final AuthorDAO INSTANCE = new AuthorDAO();

    private AuthorDAO() {
    }

    public static AuthorDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        String deleteSql = "DELETE FROM author WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Author save(Author author) {
        String saveSql = "INSERT INTO author(name) VALUES (?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getLong("id"));
                }
                return author;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Author author) {
        String updateSql = "UPDATE author SET name = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        String findByAuthorIdSql = "SELECT * FROM author WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByAuthorIdSql)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    Author author = mapResultSetToAuthor(resultSet);
                    author.setBooks(findBooksByAuthorId(author.getId()));
                    return Optional.of(author);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.empty();
    }

    public List<Author> getAllAuthors() {
        String getAllAuthorSql = "SELECT * FROM author";
        List<Author> list = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllAuthorSql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Author author = mapResultSetToAuthor(resultSet);
                    list.add(author);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private List<Book> findBooksByAuthorId(Long authorId) {
        String findByBookIdSql = "SELECT * FROM book WHERE author_id = ?";
        List<Book> books = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByBookIdSql)) {

            preparedStatement.setLong(1, authorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapResultSetToBook(resultSet));
                }
                return books;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Author mapResultSetToAuthor(ResultSet resultSet) throws SQLException {
        Author newAuthor = new Author();
        newAuthor.setId(resultSet.getLong("id"));
        newAuthor.setName(resultSet.getString("name"));
        return newAuthor;
    }

    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setName(resultSet.getString("name"));
        book.setDescription(resultSet.getString("description"));

        Author author = new Author();
        author.setId(resultSet.getLong("author_id"));
        book.setAuthor(author);

        return book;
    }

}
