package com.pykost.dao;

import com.pykost.entity.AuthorEntity;
import com.pykost.entity.BookEntity;
import com.pykost.exception.DAOException;
import com.pykost.util.HikariCPDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAO implements BaseDAO<AuthorEntity, Long> {
    private final DataSource dataSource;

    public AuthorDAO() {
        this.dataSource = HikariCPDataSource.getDataSource();
    }

    public AuthorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean delete(Long id) {
        String deleteSql = "DELETE FROM author WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        String saveSql = "INSERT INTO author(name) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, authorEntity.getName());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    authorEntity.setId(generatedKeys.getLong("id"));
                }
                return authorEntity;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(AuthorEntity authorEntity) {
        String updateSql = "UPDATE author SET name = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, authorEntity.getName());
            preparedStatement.setLong(2, authorEntity.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<AuthorEntity> findById(Long id) {
        String findByAuthorIdSql = "SELECT * FROM author WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByAuthorIdSql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    AuthorEntity authorEntity = mapResultSetToAuthor(resultSet);
                    authorEntity.setBooks(findBooksByAuthorId(authorEntity.getId()));
                    return Optional.of(authorEntity);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<AuthorEntity> findAll() {
        String getAllAuthorSql = "SELECT * FROM author";
        List<AuthorEntity> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllAuthorSql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AuthorEntity authorEntity = mapResultSetToAuthor(resultSet);
                    list.add(authorEntity);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private List<BookEntity> findBooksByAuthorId(Long authorId) {
        String findByBookIdSql = "SELECT * FROM book WHERE author_id = ?";
        List<BookEntity> books = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
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

    private AuthorEntity mapResultSetToAuthor(ResultSet resultSet) throws SQLException {
        AuthorEntity newAuthorEntity = new AuthorEntity();
        newAuthorEntity.setId(resultSet.getLong("id"));
        newAuthorEntity.setName(resultSet.getString("name"));
        return newAuthorEntity;
    }

    private BookEntity mapResultSetToBook(ResultSet resultSet) throws SQLException {
        BookEntity book = new BookEntity();
        book.setId(resultSet.getLong("id"));
        book.setName(resultSet.getString("name"));
        book.setDescription(resultSet.getString("description"));

        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(resultSet.getLong("author_id"));
        book.setAuthor(authorEntity);

        return book;
    }

}
