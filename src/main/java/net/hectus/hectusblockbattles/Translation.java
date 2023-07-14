package net.hectus.hectusblockbattles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Translation {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static JsonNode en, de, es, fr, nl, no, pl;

    public static void init() throws IOException {
        en = MAPPER.readTree(new File(HBB.dataFolder, "en.json"));
        de = MAPPER.readTree(new File(HBB.dataFolder, "de.json"));
        es = MAPPER.readTree(new File(HBB.dataFolder, "es.json"));
        fr = MAPPER.readTree(new File(HBB.dataFolder, "fr.json"));
        nl = MAPPER.readTree(new File(HBB.dataFolder, "nl.json"));
        no = MAPPER.readTree(new File(HBB.dataFolder, "no.json"));
        pl = MAPPER.readTree(new File(HBB.dataFolder, "pl.json"));
    }

    public static String get(String key, Locale locale) {
        if (en.get(key) == null) throw new IllegalArgumentException("Translation-Key not found!");

        return switch (locale.getCountry().toLowerCase()) {
            case "de" -> de.get(key).asText();
            case "es" -> es.get(key).asText();
            case "fr" -> fr.get(key).asText();
            case "nl" -> nl.get(key).asText();
            case "no", "nn" -> no.get(key).asText();
            case "pl" -> pl.get(key).asText();
            default -> en.get(key).asText();
        };
    }

    public static String get(String key, Locale locale, Object... args) {
        return String.format(get(key, locale), args);
    }
}
