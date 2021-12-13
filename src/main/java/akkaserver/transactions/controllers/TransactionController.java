package akkaserver.transactions.controllers;


import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.japi.function.Function;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akkaserver.Server;
import akkaserver.commons.behaviour.ServerBehavior;
import akkaserver.transactions.model.Transaction;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public static Function<HttpRequest, CompletionStage<HttpResponse>> getAllTransactions = httpRequest -> {
        logger.info("request got to: "+ httpRequest.getUri().getPathString());

        CompletableFuture<HttpResponse> response = new CompletableFuture<>();

        CompletionStage<List<Transaction>> transactionsFuture = AskPattern.ask(Server.actorSystem,
                ServerBehavior.ReceiveListOfTransactionsCommand::new,
                Duration.ofSeconds(5),
                Server.actorSystem.scheduler()
        );



        transactionsFuture.whenComplete(
                ((transactions, throwable) -> {
                    if(throwable != null){
                        response.complete(HttpResponse.create().withStatus(500));
                        return;
                    }

                    try {
                        String json = new ObjectMapper().writeValueAsString(transactions);
                        response.complete(HttpResponse.create()
                                .withStatus(StatusCodes.OK)
                                .withEntity(ContentTypes.APPLICATION_JSON, json)
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        response.complete(HttpResponse.create().withStatus(500));
                    }
                })
        );

        return response;
    };

    public static Function<HttpRequest, CompletionStage<HttpResponse>> addTransaction = httpRequest -> {
        CompletableFuture<HttpResponse> response = new CompletableFuture<>();

        return response;
    };
}
