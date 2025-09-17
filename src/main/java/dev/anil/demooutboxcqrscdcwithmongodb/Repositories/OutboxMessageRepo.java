package dev.anil.demooutboxcqrscdcwithmongodb.Repositories;

import dev.anil.demooutboxcqrscdcwithmongodb.Models.OutboxMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxMessageRepo extends MongoRepository<OutboxMessage, String> {
}
