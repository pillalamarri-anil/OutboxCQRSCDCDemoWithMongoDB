package dev.anil.demooutboxcqrscdcwithmongodb.Services;

import dev.anil.demooutboxcqrscdcwithmongodb.Models.DomainEvent;
import dev.anil.demooutboxcqrscdcwithmongodb.Models.OrderRead;
import dev.anil.demooutboxcqrscdcwithmongodb.Models.OutboxMessage;
import dev.anil.demooutboxcqrscdcwithmongodb.Repositories.DomainEventRepo;
import dev.anil.demooutboxcqrscdcwithmongodb.Repositories.OutboxMessageRepo;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
public class OrderCommandService {

    private final DomainEventRepo eventRepo;
    private final OutboxMessageRepo outboxRepo;
    private final MongoTemplate mongoTemplate;

    public OrderCommandService(DomainEventRepo eventRepo,
                               OutboxMessageRepo outboxRepo,
                               MongoTemplate mongoTemplate) {
        this.eventRepo = eventRepo;
        this.outboxRepo = outboxRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional  // MongoTransactionManager ensures both saves are atomic
    public void handleCreateOrder(String orderId, Map<String,Object> payload) {
        DomainEvent<OrderRead> event = new DomainEvent();
        event.setAggregateId(orderId);
        event.setType("OrderCreated");
        event.setOccuredAt(Instant.now());
        event.setPayLoad(new OrderRead());

        eventRepo.save(event);

        OutboxMessage<OrderRead> outbox = new OutboxMessage();
        outbox.setTopic("orders-events");
        outbox.setAggregateId(orderId);
        outbox.setEventType("OrderCreated");
        outbox.setPayload(event.getPayLoad());
        outbox.setCreatedAt(Instant.now());

        outboxRepo.save(outbox);
        // both saved in same mongo transaction
    }
}
