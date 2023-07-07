package net.hectus.hectusblockbattles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Convert Components to Strings
 */
public class Compring {
    public static String from(Component component) {
        return PlainTextComponentSerializer.plainText().serializeOr(component, "Couldn't convert Component to String!");
    }

    public static @NotNull String from(@NotNull TextComponent textComponent) {
        return textComponent.content();
    }
}
