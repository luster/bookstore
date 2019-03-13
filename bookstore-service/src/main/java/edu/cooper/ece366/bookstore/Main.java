package edu.cooper.ece366.bookstore;

import com.spotify.apollo.Environment;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import com.spotify.apollo.route.SyncHandler;

public class Main {

  public static void main(String[] args) throws LoadingException {
    HttpService.boot(Main::init, "bookstore-service", args);
  }

  private static void init(final Environment environment) {
    SyncHandler<String> pingHandler = requestContext -> "pong";
    final SyncHandler<String> handler = (RequestContext requestContext) ->
        requestContext.pathArgs().get("name");
    environment
        .routingEngine()
        .registerAutoRoute(Route.sync("GET", "/ping", pingHandler))
        .registerAutoRoute(Route.sync("GET", "/name/<name>", handler));
  }

}
