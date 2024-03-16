package me.krob.model.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ClientRequest {
    private final String userId, username;
}
