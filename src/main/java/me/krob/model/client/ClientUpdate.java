package me.krob.model.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class ClientUpdate {
    private final int weight;
    private final int calories;
}
