package net.hectus.hectusblockbattles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;

/**
 * Convert Components to Strings
 */
public class Compring {
    @Contract("_ -> !null")
    public static String from(Component component) {
        return PlainTextComponentSerializer.plainText().serializeOr(component, "Couldn't convert Component to String!");
    }

    public static Component format(String format, Object... args) {
        return Component.text(String.format(format, args));
    }
}
