package com.example.db.hbase;

import com.example.db.hbase.impl.ClientRepositoryImpl;
import com.example.db.hbase.impl.JobRepositoryImpl;
import com.example.db.hbase.impl.TaskRepositoryImpl;
import com.google.inject.AbstractModule;

public class HBaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClientRepository.class).to(ClientRepositoryImpl.class);
        bind(TaskRepository.class).to(TaskRepositoryImpl.class);
        bind(JobRepository.class).to(JobRepositoryImpl.class);
    }
}
