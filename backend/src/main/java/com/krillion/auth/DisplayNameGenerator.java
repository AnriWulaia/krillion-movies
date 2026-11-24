package com.krillion.auth;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Creates public names like "BraveOtter42", so emails never appear on leaderboards. */
@Component
public class DisplayNameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "Brave", "Clever", "Swift", "Quiet", "Lucky", "Mighty",
            "Sunny", "Wild", "Cosmic", "Happy", "Sneaky", "Bold");

    private static final List<String> ANIMALS = List.of(
            "Otter", "Falcon", "Panda", "Fox", "Wolf", "Tiger",
            "Koala", "Raven", "Lynx", "Badger", "Heron", "Owl");

    public String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String animal = ANIMALS.get(random.nextInt(ANIMALS.size()));
        return adjective + animal + (10 + random.nextInt(90));
    }
}
