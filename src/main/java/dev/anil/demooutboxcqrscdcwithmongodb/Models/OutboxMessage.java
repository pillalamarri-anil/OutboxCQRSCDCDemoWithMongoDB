package dev.anil.demooutboxcqrscdcwithmongodb.Models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.time.Instant;


@Getter
@Setter

@org.springframework.data.mongodb.core.mapping.Document(collection = "outbox")
public class OutboxMessage<T> {
    @Id
    private String id;
    private String topic;         // Kafka topic
    private String aggregateId;
    private String eventType;
    private T payload;
    private Instant createdAt;
    private boolean processed = false;  // mark after publish
    private Instant processedAt;

    // constructors/getters/setters
}

