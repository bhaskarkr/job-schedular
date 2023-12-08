package com.example.db.hbase;

import com.google.inject.Singleton;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.Objects;

/**
 *  Author : Bhaskar Kumar
 *  Date : 8 Dec 2023
 */
@Slf4j
@Singleton
public class HBaseClient implements Managed {
    private final Connection connection;

    public HBaseClient() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(configuration);
    }

    public Table getTable(final String tableName) {
        try(Table table = connection.getTable(TableName.valueOf(tableName))) {
            return table;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() throws Exception {
        log.info("HBase connection established");
    }

    @Override
    public void stop() throws Exception {
        if (Objects.nonNull(connection)) {
            connection.close();
        }
        log.info("HBase connection closed");
    }

}
