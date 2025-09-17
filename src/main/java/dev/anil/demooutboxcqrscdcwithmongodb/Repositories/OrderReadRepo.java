package dev.anil.demooutboxcqrscdcwithmongodb.Repositories;

import dev.anil.demooutboxcqrscdcwithmongodb.Models.OrderRead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReadRepo extends MongoRepository<OrderRead, String> {
}
