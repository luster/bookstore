package edu.cooper.ece366.bookstore.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.JsonSerializerMiddlewares;
import com.spotify.apollo.route.Middleware;
import com.spotify.apollo.route.Middlewares;
import com.spotify.apollo.route.Route;
import edu.cooper.ece366.bookstore.model.Book;
import edu.cooper.ece366.bookstore.model.BookBuilder;
import edu.cooper.ece366.bookstore.store.BookStore;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import okio.ByteString;

public class BookHandlers {

  // todo: clean this up after db is wired up
  private static final Book book =
      new BookBuilder().id(1).title("a book").author("an author").build();

  private final ObjectMapper objectMapper;
  private final BookStore bookStore;

  public BookHandlers(final ObjectMapper objectMapper, final BookStore bookStore) {
    this.objectMapper = objectMapper;
    this.bookStore = bookStore;
  }

  public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
    return Stream.of(
        Route.sync("GET", "/books", this::getBooks).withMiddleware(jsonMiddleware()),
        Route.sync("GET", "/book/<id>", this::getBook).withMiddleware(jsonMiddleware())
    );
  }

  @VisibleForTesting
  Book getBook(final RequestContext requestContext) {
    return bookStore.getBook(requestContext.pathArgs().get("id"));
  }

  @VisibleForTesting
  List<Book> getBooks(final RequestContext requestContext) {
    return Collections.singletonList(book);
  }

  private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
    return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
        .and(Middlewares::httpPayloadSemantics)
        .and(responseAsyncHandler -> requestContext ->
            responseAsyncHandler.invoke(requestContext)
                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
  }
}
