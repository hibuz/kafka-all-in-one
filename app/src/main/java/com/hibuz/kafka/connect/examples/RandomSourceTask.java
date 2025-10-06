package com.hibuz.kafka.connect.examples;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import com.hibuz.kafka.connect.examples.config.RandomConnectorConfig;

public class RandomSourceTask extends SourceTask {
    private CountDownLatch stopLatch = new CountDownLatch(1);
    private boolean shouldWait = false;
    private int sleepInSeconds;
    private String topic;

    @Override
    public String version() {
        return RandomConnectorConfig.CONNECTOR_VERSION;
    }

    @Override
    public void start(Map<String, String> props) {
        RandomConnectorConfig config = new RandomConnectorConfig(props);
        sleepInSeconds = config.getInt(RandomConnectorConfig.SLEEP_CONFIG);
        topic = config.getString(RandomConnectorConfig.TOPIC_CONFIG);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        boolean shouldStop = false;
        if (shouldWait) {
            shouldStop = stopLatch.await(sleepInSeconds, TimeUnit.SECONDS);
        }
        if (!shouldStop) {
            shouldWait = true;
            return getSourceRecords();
        } else {
            return null;
        }
    }

    private List<SourceRecord> getSourceRecords() {
        return Collections.singletonList(new SourceRecord(
                null,
                null,
                topic,
                RandomConnectorConfig.VALUE_SCHEMA,
                new Random().nextInt()));
    }

    @Override
    public void stop() {
        stopLatch.countDown();
    }

}
