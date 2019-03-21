package edu.cooper.ece366.bookstore.store;

import edu.cooper.ece366.bookstore.model.Book;

public interface BookStore {

  Book getBook(String id);

}
