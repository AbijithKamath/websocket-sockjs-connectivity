package com.example.sockjsWebSocketConnectivity;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;




public class MainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.mountSubRouter("/dd/", eventBusHandler());




   // router.route().handler(eventBusHandler()).failureHandler(context -> logger.error("HTTP Server error: ", context.failure()));
    vertx.createHttpServer()
      .requestHandler(router)
      .exceptionHandler(throwable -> logger.error("HTTP Server error: ", throwable))
      .listen(8080, httpServerAsyncResult -> {
        if (httpServerAsyncResult.succeeded()) {
          startPromise.complete();
          logger.info("HTTP server started on port 8080");

        } else {
          startPromise.fail(httpServerAsyncResult.cause());
        }
      });
  }

  private Router eventBusHandler() {
    SockJSBridgeOptions options = new SockJSBridgeOptions()
      .addOutboundPermitted(new PermittedOptions().setAddressRegex("FromServer"))
      .addOutboundPermitted(new PermittedOptions().setAddressRegex("FromServerToClient2"))
      .addInboundPermitted(new PermittedOptions().setAddressRegex("FromClient"));

    EventHandler handler = new EventHandler(vertx.eventBus());
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

    return  sockJSHandler.bridge(options, handler);
  }


}
