package edu.cooper.ece366.bookstore.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Book {
  Integer id();

  String title();

  String author();
}
