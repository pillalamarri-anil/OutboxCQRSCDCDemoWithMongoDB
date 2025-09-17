package dev.anil.demooutboxcqrscdcwithmongodb.Models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter

@Document(collection = "orders_read")
public class OrderRead {
    @Id
    private String id;
    private String status;
    private int quantity;
    private String product;
    // ... other fields for fast querying
}
