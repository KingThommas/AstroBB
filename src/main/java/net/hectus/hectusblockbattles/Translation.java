package net.hectus.hectusblockbattles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Translation {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static JsonNode en, de, es, fr, nl, no, pl, ru;

    public static void init() throws IOException {
        System.out.println("Translation.init() by: " + Trace.last());

        en = MAPPER.readTree(new File(HBB.dataFolder, "en.json"));
        de = MAPPER.readTree(new File(HBB.dataFolder, "de.json"));
        es = MAPPER.readTree(new File(HBB.dataFolder, "es.json"));
        fr = MAPPER.readTree(new File(HBB.dataFolder, "fr.json"));
        nl = MAPPER.readTree(new File(HBB.dataFolder, "nl.json"));
        no = MAPPER.readTree(new File(HBB.dataFolder, "no.json"));
        pl = MAPPER.readTree(new File(HBB.dataFolder, "pl.json"));
        ru = MAPPER.readTree(new File(HBB.dataFolder, "ru.json"));
    }

    public static @NotNull String get(String key, Locale locale) {
        if (en.get(key) == null) throw new IllegalArgumentException("Translation-Key not found!");

        String translation =  switch (locale.getLanguage().toLowerCase()) {
            case "de" -> de.get(key).asText();
            case "es" -> es.get(key).asText();
            case "fr" -> fr.get(key).asText();
            case "nl" -> nl.get(key).asText();
            case "no" -> no.get(key).asText();
            case "pl" -> pl.get(key).asText();
            case "ru" -> ru.get(key).asText();
            default -> en.get(key).asText();
        };
        if (translation == null || translation.isBlank() || translation.isEmpty()) return get(key, locale);
        return translation;
    }

    public static String get(String key, Locale locale, Object... args) {
        return String.format(get(key, locale), args);
    }
}
