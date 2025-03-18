package com.pykost.dao;

import com.pykost.entity.Author;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class AuthorDAOTest {
    @Container
    private final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("testAuthor")
                    .withUsername("user")
                    .withPassword("pass");

    private AuthorDAO authorDAO;

    @BeforeEach
    void setUp() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresContainer.getJdbcUrl());
        config.setUsername(postgresContainer.getUsername());
        config.setPassword(postgresContainer.getPassword());
        DataSource dataSource = new HikariDataSource(config);

        authorDAO = new AuthorDAO(dataSource);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE author (id BIGSERIAL PRIMARY KEY, name VARCHAR(200) NOT NULL)");
            statement.execute("create table Book (id BIGSERIAL PRIMARY KEY, name VARCHAR(200) NOT NULL, " +
                              "description VARCHAR(200), author_id BIGINT NOT NULL," +
                              "FOREIGN KEY (author_id) references Author (id))");

            statement.execute("INSERT INTO author (name) VALUES ('Author1')");
            statement.execute("INSERT INTO author (name) VALUES ('Author2')");

            statement.execute("INSERT INTO book (name, description, author_id) " +
                              "VALUES ('Book1', 'description1', 1)");
        }
    }

    @Test
    void delete() {
        boolean expected = authorDAO.delete(2L);
        assertThat(expected).isTrue();

        List<Author> listExpected = authorDAO.getAllEntity();
        assertThat(listExpected)
                .hasSize(1)
                .contains(new Author(1L, "Author1"));
    }

    @Test
    void save() {
        Author author = new Author();
        author.setName("Author3");
        Author expected = authorDAO.save(author);

        assertThat(expected.getId()).isEqualTo(3L);
        assertThat(expected.getName()).isEqualTo(author.getName());

        List<Author> listActual = authorDAO.getAllEntity();
        assertThat(listActual).hasSize(3);
    }

    @Test
    void update() {
        Author author = new Author(1L, "Author111");
        authorDAO.update(author);

        Optional<Author> author1 = authorDAO.findById(1L);

        assertThat(author1).isPresent();
        assertThat(author1.get().getId()).isEqualTo(1L);
        assertThat(author1.get().getName()).isEqualTo("Author111");
    }

    @Test
    void findById() {
        Optional<Author> author = authorDAO.findById(1L);

        assertThat(author).isPresent();
        assertThat(author.get().getId()).isEqualTo(1L);
        assertThat(author.get().getName()).isEqualTo("Author1");
    }

    @Test
    void getAllEntity() {
        List<Author> actual = List.of(new Author(1L, "Author1"),
                new Author(2L, "Author2"));
        List<Author> expected = authorDAO.getAllEntity();
        assertThat(expected)
                .hasSize(2)
                .containsAll(actual);
    }
}