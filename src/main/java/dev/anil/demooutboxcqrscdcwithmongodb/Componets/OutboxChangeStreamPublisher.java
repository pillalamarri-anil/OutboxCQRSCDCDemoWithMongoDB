package dev.anil.demooutboxcqrscdcwithmongodb.Componets;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OutboxChangeStreamPublisher {

    private MongoTemplate mongoTemplate;
    private KafkaTemplate<String, String> kafkaTemplate;

    public OutboxChangeStreamPublisher(MongoTemplate mongoTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.kafkaTemplate = kafkaTemplate;
        watch();
    }

    private void watch() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("outbox");

        collection.watch().forEach(change -> {
            Document fullDocument = change.getFullDocument();
            if(fullDocument == null)
                return;

            String id = fullDocument.getObjectId("_id").toHexString();

            boolean isProcessed = fullDocument.getBoolean("processed");

            if(!isProcessed)
            {
                String topic = fullDocument.getString("topic");
                String payloadJson = ((Document)fullDocument.get("payload")).toJson();
                kafkaTemplate.send(topic, fullDocument.getString("aggregateId"), payloadJson)
                        .whenComplete((result, exception) -> {
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                            else
                                markProcessed(id);
                        });
            }
        });
    }

    private void markProcessed(String outboxId) {
        Query q = Query.query(Criteria.where("_id").is(new ObjectId(outboxId)));
        Update u = new Update().set("processed", true).set("processedAt", Instant.now());
        mongoTemplate.updateFirst(q, u, "outbox");

    }
}