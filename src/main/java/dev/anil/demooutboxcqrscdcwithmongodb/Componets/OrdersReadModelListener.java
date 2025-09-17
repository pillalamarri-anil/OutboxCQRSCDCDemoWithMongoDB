package dev.anil.demooutboxcqrscdcwithmongodb.Componets;

import dev.anil.demooutboxcqrscdcwithmongodb.Models.OrderRead;
import dev.anil.demooutboxcqrscdcwithmongodb.Repositories.OrderReadRepo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "orders-events", groupId = "orders-read")
public class OrdersReadModelListener {

    @Autowired
    private OrderReadRepo orderReadRepo;

    @KafkaHandler
    public void handle(String message) {
        Document document = Document.parse(message);
        String orderId = document.getString("orderId");

        OrderRead orderRead = orderReadRepo.findById(orderId).orElse(new OrderRead());
        orderRead.setId(orderId);
        orderRead.setStatus("CREATED");
        orderReadRepo.save(orderRead);
    }
}
