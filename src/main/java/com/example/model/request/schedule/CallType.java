package com.example.model.request.schedule;

public enum CallType {
    GET {
        @Override
        public <T> T accept(CallTypeVisitor<T> visitor, HttpCallRequest request) {
            return visitor.getRequest(request);
        }
    },
    POST {
        @Override
        public <T> T accept(CallTypeVisitor<T> visitor, HttpCallRequest request) {
            return visitor.postRequest(request);
        }
    },
    PUT {
        @Override
        public <T> T accept(CallTypeVisitor<T> visitor, HttpCallRequest request) {
            return visitor.putRequest(request);
        }
    };

    public abstract <T> T accept(CallTypeVisitor<T> visitor, HttpCallRequest request);
}
