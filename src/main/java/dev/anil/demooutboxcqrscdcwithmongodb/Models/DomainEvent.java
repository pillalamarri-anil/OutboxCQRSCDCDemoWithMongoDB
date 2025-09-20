package dev.anil.demooutboxcqrscdcwithmongodb.Models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Setter

@org.springframework.data.mongodb.core.mapping.Document(collection = "events")
public class DomainEvent<T> {

    @Id
    private String id;
    private String aggregateId;
    private String type;
    private Instant occuredAt;
    private T payLoad;
}
