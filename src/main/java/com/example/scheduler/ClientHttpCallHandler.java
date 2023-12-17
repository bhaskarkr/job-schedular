package com.example.scheduler;

import com.example.model.request.schedule.CallTypeVisitor;
import com.example.model.request.schedule.HttpCallRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.ClientErrorException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class ClientHttpCallHandler {
    private final OkHttpClient httpClient;

    @Inject
    public ClientHttpCallHandler() {
        // TODO : move to config
        int maxIdleConnections = 5; // Maximum number of idle connections to keep in the pool
        long keepAliveDuration = 5; // Idle connection keep-alive time in minutes

        this.httpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES))
                .connectTimeout(10, TimeUnit.SECONDS) // Set connection timeout to 10 seconds
                .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout to 30 seconds
                .build();

    }

    public void executeCall(HttpCallRequest httpCallRequest) {
        try {
            Request request = httpCallRequest.getCallType().accept(new CallTypeVisitor<Request>() {
                @Override
                public Request getRequest(HttpCallRequest httpCallRequest) {
                    return new Request.Builder()
                            .url(httpCallRequest.getUrl())
                            .headers(Headers.of(httpCallRequest.getHeaders()))
                            .get()
                            .build();
                }

                @Override
                public Request postRequest(HttpCallRequest httpCallRequest) {
                    return new Request.Builder()
                            .url(httpCallRequest.getUrl())
                            .headers(Headers.of(httpCallRequest.getHeaders()))
                            .post(RequestBody.create(Objects.toString(httpCallRequest.getPayload(), ""), MediaType.parse("application/json")))
                            .build();
                }

                @Override
                public Request putRequest(HttpCallRequest httpCallRequest) {
                    return new Request.Builder()
                            .url(httpCallRequest.getUrl())
                            .headers(Headers.of(httpCallRequest.getHeaders()))
                            .put(RequestBody.create(Objects.toString(httpCallRequest.getPayload(), ""), MediaType.parse("application/json")))
                            .build();
                }
            }, httpCallRequest);
            Response response = httpClient.newCall(request).execute();
            if(response.isSuccessful() && response.body() != null) {
                log.info("{}", Arrays.toString(response.body().bytes()));

            }
            else
                log.info("Api call failed with status : {}", response.code());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
