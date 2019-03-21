package edu.cooper.ece366.bookstore.store;

import com.typesafe.config.Config;
import edu.cooper.ece366.bookstore.model.Book;
import edu.cooper.ece366.bookstore.model.BookBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookStoreJdbc implements BookStore {

  private static final String GET_BOOK_STATEMENT =
      "select id, title, author from books where id = ?";

  private final Config config;

  public BookStoreJdbc(final Config config) {
    this.config = config;
  }

  @Override
  public Book getBook(final String id) {
    Connection connection;
    try {
      connection =
          DriverManager.getConnection(
              config.getString("mysql.jdbc"),
              config.getString("mysql.user"),
              config.getString("mysql.password"));

      PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_STATEMENT);
      preparedStatement.setInt(1, Integer.parseInt(id));

      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.first()) {
        return new BookBuilder()
            .id(resultSet.getInt("id"))
            .title(resultSet.getString("title"))
            .author(resultSet.getString("author"))
            .build();
      } else {
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("error fetching book", e);
    }
  }
}
