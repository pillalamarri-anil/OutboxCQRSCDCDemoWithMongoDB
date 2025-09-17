package dev.anil.demooutboxcqrscdcwithmongodb.Repositories;

import dev.anil.demooutboxcqrscdcwithmongodb.Models.DomainEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainEventRepo extends MongoRepository<DomainEvent, String> {
}
