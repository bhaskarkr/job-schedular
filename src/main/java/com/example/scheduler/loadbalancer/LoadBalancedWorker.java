package com.example.scheduler.loadbalancer;

public interface LoadBalancedWorker {
    void activate();
    void deactivate();
}
