package net.hectus.hectusblockbattles.player;

import net.hectus.util.Formatter;
import net.hectus.util.Randomizer;

public class Cancer {
    private int stage;
    private Position position;

    public Cancer() {
        stage = 0;
        position = (Position) Randomizer.fromArray(Position.values());
    }

    public boolean count() {
        stage++;
        return stage != 5;
    }

    public int stage() {
        return stage;
    }

    public String getPosition() {
        return Formatter.toPascalCase(position.toString()) + " Cancer";
    }

    enum Position {
        BLADDER, BREAST, COLORECTAL, KIDNEY, LUNG,
        LYMPHOMA, MELANOMA, PANCREATIC, PROSTATE, THYROID, UTERINE,
        ORAL, BALL
    }
}
