package me.krob.model.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refreshToken")
@Getter
@Setter
public class RefreshToken {
    @Id
    private String id;

    private String username;

    private String token;
    private Instant creation;
    private Instant expiry;
}
