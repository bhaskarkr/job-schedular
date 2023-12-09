package com.example.db.hbase.impl;

import com.example.db.hbase.ClientRepository;
import com.example.db.hbase.HBaseClient;
import com.example.db.model.StoredClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientRepositoryImpl implements ClientRepository {
    private static final String CLIENT_TABLE_NAME = "clients";
    private static final byte[] CLIENT_META_DATA_CF = "meta".getBytes();
    private static final byte[] CLIENT_META_DATA = "data".getBytes();
    private final Table clientTable;
    private final ObjectMapper objectMapper;

    @Inject
    public ClientRepositoryImpl(HBaseClient hBaseClient,
                                ObjectMapper objectMapper) {
        this.clientTable = hBaseClient.getTable(CLIENT_TABLE_NAME);
        this.objectMapper = objectMapper;
    }

    @Override
    public List<StoredClient> getAll() {
        Scan scan = new Scan().addColumn(CLIENT_META_DATA_CF, CLIENT_META_DATA);
        try {
            ResultScanner resultScanner = clientTable.getScanner(scan);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(resultScanner.iterator(), Spliterator.ORDERED), false)
                    .map(result -> {
                        try {
                            return objectMapper.readValue(result.getValue(CLIENT_META_DATA_CF, CLIENT_META_DATA), StoredClient.class);
                        } catch (IOException e) {
                            throw new InternalServerErrorException();
                        }
                    })
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StoredClient get(String clientName) {
        Get get = new Get(clientName.getBytes());
        try {
            Result result = clientTable.get(get);
            if(result.isEmpty()) {
                throw new BadRequestException();
            }
            return objectMapper.readValue(result.getValue(CLIENT_META_DATA_CF, CLIENT_META_DATA), StoredClient.class);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(StoredClient storedClient) {
        try {
            Put put = new Put(storedClient.getClientId().getBytes());
            put.addColumn(CLIENT_META_DATA_CF, CLIENT_META_DATA, objectMapper.writeValueAsBytes(storedClient));
            clientTable.put(put);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
