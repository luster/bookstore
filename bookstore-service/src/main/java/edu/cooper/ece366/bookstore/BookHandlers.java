package edu.cooper.ece366.bookstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.JsonSerializerMiddlewares;
import com.spotify.apollo.route.Middleware;
import com.spotify.apollo.route.Middlewares;
import com.spotify.apollo.route.Route;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import okio.ByteString;

public class BookHandlers {

  private final ObjectMapper objectMapper;

  public BookHandlers(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
    return Stream.of(
        Route.sync("GET", "/books", this::getBooks)
    ).map(r -> r.withMiddleware(jsonMiddleware()));
  }

  private List<Book> getBooks(final RequestContext requestContext) {
    Book book = new BookBuilder().title("a book").author("an author").build();
    return Collections.singletonList(book);
  }

  private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
    return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
        .and(Middlewares::httpPayloadSemantics);
  }
}
