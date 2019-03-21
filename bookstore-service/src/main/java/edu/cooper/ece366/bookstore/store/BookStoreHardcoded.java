package edu.cooper.ece366.bookstore.store;

import edu.cooper.ece366.bookstore.model.Book;
import edu.cooper.ece366.bookstore.model.BookBuilder;

public class BookStoreHardcoded implements BookStore {

  public BookStoreHardcoded() {
  }

  @Override
  public Book getBook(final String id) {
    return new BookBuilder().id(1).title("a book").author("an author").build();
  }
}
