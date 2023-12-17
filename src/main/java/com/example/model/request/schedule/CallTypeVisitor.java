package com.example.model.request.schedule;

public interface CallTypeVisitor<T> {
    T getRequest(HttpCallRequest httpCallRequest);
    T postRequest(HttpCallRequest httpCallRequest);
    T putRequest(HttpCallRequest httpCallRequest);
}
