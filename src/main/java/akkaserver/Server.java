package akkaserver;

import java.util.concurrent.CompletionStage;

import akka.actor.typed.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;
import akkaserver.commons.behaviour.ServerBehavior;
import akkaserver.longrunningtasks.controllers.LongRunningTaskController;
import akkaserver.transactions.controllers.TransactionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static akka.http.javadsl.server.Directives.*;

public class Server {

    public static ActorSystem<ServerBehavior.Command> actorSystem = ActorSystem.create(ServerBehavior.create(), "server");

    Logger logger = LoggerFactory.getLogger(Server.class);
    /**
     * GUIDE TO REPLICATE (Too lazy to make a README lol)
     *
     * Run server
     *
     *
     * Test with Jmeter
     * Installation guide for JMETER-
     * >> brew install jmeter
     * >> open /opt/homebrew/Cellar/jmeter/5.4.1/bin/jmeter
     *
     * Path to test: localhost:8090/api/v1/8090
     *
     * Behaviour: First 10 requests are accepted simultaneously/concurrently while others are queued
     * Then, the queued requests are treated individually as others finish.
     * Consequently, some request may timeout
     *
     * Please consult logs on Run tab
     * **/

    public static void main(String[] args) {
        int port = 8090;
        new Server().run(port);
    }

    private Route createRoutes(){
        return pathPrefix("api", ()->
                pathPrefix("v1", ()->
                    concat(
                        path("transaction", () ->
                                concat(
                                        get(()-> handle(TransactionController.getAllTransactions)),
                                        post(()-> handle(TransactionController.addTransaction))
                                )
                        ),

                        path("longRunningTask", () ->
                                concat(
                                        get(()-> handle(LongRunningTaskController.doTask))
                                )
                        )
                    )
                )
        );
    }

    private void run(int port){
        CompletionStage<ServerBinding> serverBinding =
                Http.get(actorSystem).newServerAt("localhost", port).bind(createRoutes());

        serverBinding.whenComplete(
                (server, throwable) -> {
                    if(throwable != null){
                        logger.error("Server did not start up");
                        return;
                    }
                    logger.info("Server is up at "+ server.localAddress());
                }
        );
    }
}
