package com.pykost.dao;

import com.pykost.entity.AuthorEntity;
import com.pykost.entity.BookEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
class BookDAOTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("testBook")
                    .withUsername("user")
                    .withPassword("pass");

    private BookDAO bookDAO;
    private static DataSource dataSource;

    @BeforeAll
    static void beforeAll() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresContainer.getJdbcUrl());
        config.setUsername(postgresContainer.getUsername());
        config.setPassword(postgresContainer.getPassword());
        dataSource = new HikariDataSource(config);
    }

    @BeforeEach
    void setUp() throws SQLException {
        bookDAO = new BookDAO(dataSource);
        initializeDatabase(dataSource);
    }

    @AfterEach
    void afterEach() throws SQLException {
        clearDatabase(dataSource);
    }

    @Test
    void delete() {
        boolean expected = bookDAO.delete(2L);
        assertThat(expected).isTrue();

        List<BookEntity> listExpected = bookDAO.findAll();
        assertThat(listExpected).hasSize(2);
    }

    @Test
    void save() {
        BookEntity book = new BookEntity();
        book.setName("Book4");
        book.setDescription("description4");
        book.setAuthor(new AuthorEntity(2L, "Author2"));
        BookEntity expected = bookDAO.save(book);

        assertThat(expected.getId()).isEqualTo(4L);
        assertThat(expected.getName()).isEqualTo(book.getName());
        assertThat(expected.getDescription()).isEqualTo(book.getDescription());
        assertThat(expected.getAuthor().getId()).isEqualTo(book.getAuthor().getId());

        List<BookEntity> listActual = bookDAO.findAll();
        assertThat(listActual).hasSize(4);
    }

    @Test
    void update() {
        BookEntity book = new BookEntity();
        book.setId(3L);
        book.setName("Book333");
        book.setDescription("description333");
        book.setAuthor(new AuthorEntity(2L, "Author2"));

        boolean updateResult = bookDAO.update(book);
        assertThat(updateResult).isTrue();

        Optional<BookEntity> expected = bookDAO.findById(3L);

        assertThat(expected).isPresent();
        assertThat(expected.get().getId()).isEqualTo(3L);
        assertThat(expected.get().getName()).isEqualTo(book.getName());
        assertThat(expected.get().getDescription()).isEqualTo(book.getDescription());
        assertThat(expected.get().getAuthor().getId()).isEqualTo(book.getAuthor().getId());
    }

    @Test
    void findById() {
        Optional<BookEntity> expected = bookDAO.findById(2L);

        assertThat(expected).isPresent();
        assertThat(expected.get().getId()).isEqualTo(2L);
        assertThat(expected.get().getName()).isEqualTo("Book2");
    }

    @Test
    void getAllEntity() {
        BookEntity book = new BookEntity(3L, "Book3",
                "description3", new AuthorEntity(1L, "Author1"));
        List<BookEntity> daoAllEntity = bookDAO.findAll();

        assertThat(daoAllEntity)
                .hasSize(3)
                .contains(book);
    }

    private static void initializeDatabase(DataSource dataSource) throws SQLException {
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
            statement.execute("INSERT INTO book (name, description, author_id) " +
                              "VALUES ('Book2', 'description2', 2)");
            statement.execute("INSERT INTO book (name, description, author_id) " +
                              "VALUES ('Book3', 'description3', 1)");
        }
    }
    private static void clearDatabase(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE book");
            statement.execute("DROP TABLE author");
        }
    }
}