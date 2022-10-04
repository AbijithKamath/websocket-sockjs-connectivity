package com.example.sockjsWebSocketConnectivity;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public class EventHandler implements Handler<BridgeEvent> {
  private final EventBus eventBus;
  public EventHandler(EventBus eventBus){
    this.eventBus = eventBus;
  }
  private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
  @Override
  public void handle(BridgeEvent bridgeEvent) {

    if (bridgeEvent.type() == BridgeEventType.SOCKET_CREATED)
      logger.info("A socket was created");

    if (bridgeEvent.type() == BridgeEventType.SEND){
        eventBus.<String>consumer("FromClient", message -> {
        logger.info("Message is :" + message.body());
          eventBus.publish("FromServerToClient2", message.body());


      });



    }


    bridgeEvent.complete(true);
  }
}
