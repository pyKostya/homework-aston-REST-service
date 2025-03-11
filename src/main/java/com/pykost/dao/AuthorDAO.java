package com.pykost.dao;

import com.pykost.entity.Author;
import com.pykost.entity.Book;
import com.pykost.exception.DAOException;
import com.pykost.util.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class AuthorDAO implements DAO<Author, Long> {
    private static final AuthorDAO INSTANCE = new AuthorDAO();

    private static final String DELETE_SQL = """
            DELETE FROM author
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO author(name)
            VALUES (?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE author
            SET name = ?
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id,
                   name
            FROM author
            WHERE id = ?;
            """;

    private AuthorDAO() {
    }

    public static AuthorDAO getInstance() {
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
    public Author save(Author author) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getName());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong("id"));
            }
            return author;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Author author) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, author.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = new Author(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                );
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

}
