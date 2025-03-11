package com.pykost.dao;

import com.pykost.entity.Author;
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

    private static final String FIND_ALL_BY_AUTHOR_SQL = """
            SELECT book.id,
                   book.name,
                   book.description,
                   book.author_id,
                   a.id ai,
                   a.name an
            FROM book
                    JOIN author a on a.id = book.author_id
            WHERE book.author_id = ?
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT book.id,
                   book.name,
                   book.description,
                   book.author_id,
                   a.id ai,
                   a.name an
            FROM book
                    JOIN author a on a.id = book.author_id
            WHERE book.id = ?;
            """;

    private BookDAO() {
    }

    public static BookDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL);
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Book save(Book book) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setLong(3, book.getAuthor().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong("id"));
            }
            return book;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Book book) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
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
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Book book = null;
            if (resultSet.next()) {
                book = buildBook(resultSet);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public List<Book> findByAuthor(Long id) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_AUTHOR_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        Author author = new Author(
                resultSet.getLong("ai"),
                resultSet.getString("an")
        );
        return new Book(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                author
        );
    }


}
