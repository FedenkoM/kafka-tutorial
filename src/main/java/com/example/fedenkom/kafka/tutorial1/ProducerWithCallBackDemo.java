package com.example.fedenkom.kafka.tutorial1;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallBackDemo {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProducerWithCallBackDemo.class);

        String bootstrapServers = "localhost:9092";
        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        // create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        // create producer record
        // send data asynchronous
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>("first_topic", "hello kafka: " + i);
            producer.send(producerRecord, (recordMetadata, e) -> {
                // execute every time record will be send or exception occurred
                if (e == null) {
                    // record was successful
                    logger.info(
                        "Receive new metadata.\n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offsets: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    logger.error("Error while producing data: " + e);
                }
            });
        }
        // flush data
        producer.flush();
        producer.close();

    }

}
