package edu.cooper.ece366.bookstore.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import edu.cooper.ece366.bookstore.model.Book;
import edu.cooper.ece366.bookstore.model.BookBuilder;
import edu.cooper.ece366.bookstore.store.BookStore;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookHandlersTest {

  @Mock ObjectMapper objectMapper;
  @Mock BookStore bookStore;

  @Mock RequestContext requestContext;

  private BookHandlers testClass;

  @Before
  public void setup() {
    testClass = new BookHandlers(objectMapper, bookStore);
  }

  @Test
  public void getBook() {
    // setup vars
    Book expected = new BookBuilder().id(1).title("a book").author("an author").build();

    // mock dependencies and inputs
    when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("id", "1"));
    when(bookStore.getBook("1")).thenReturn(expected);

    // call test class
    Book actual = testClass.getBook(requestContext);

    // assert and verify
    assertEquals(expected, actual);

    verifyZeroInteractions(objectMapper);
  }
}
