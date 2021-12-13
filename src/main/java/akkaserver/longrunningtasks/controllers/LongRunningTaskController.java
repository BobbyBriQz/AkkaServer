package akkaserver.longrunningtasks.controllers;

import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.japi.function.Function;
import akkaserver.longrunningtasks.models.LongRunningTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class LongRunningTaskController {

    public static int globalTransactionCount = 1;
    private static final Logger logger = LoggerFactory.getLogger(LongRunningTaskController.class);


    public static Function<HttpRequest, CompletionStage<HttpResponse>> doTask = httpRequest -> {
        logger.info("request got to: "+ httpRequest.getUri().getPathString());

        CompletableFuture<HttpResponse> response = new CompletableFuture<>();

        int requestNo = globalTransactionCount++;
        logger.info("Started task " + requestNo);

        //Simulating long running task
        BigInteger bigInteger = BigInteger.probablePrime(4200, new Random()).nextProbablePrime();
        LongRunningTask output =  new LongRunningTask(bigInteger);
        String json = new ObjectMapper().writeValueAsString(output);
        response.complete(HttpResponse.create().withStatus(200).withEntity(ContentTypes.APPLICATION_JSON, json));
        logger.info("Finished task "+ requestNo+" \n" + json);

        return response;
    };

}
