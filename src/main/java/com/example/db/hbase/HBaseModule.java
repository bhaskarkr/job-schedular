package com.example.db.hbase;

import com.example.db.hbase.impl.ClientRepositoryImpl;
import com.google.inject.AbstractModule;

public class HBaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClientRepository.class).to(ClientRepositoryImpl.class);
    }
}
