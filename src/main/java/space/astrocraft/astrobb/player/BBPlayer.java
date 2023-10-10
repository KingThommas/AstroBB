package space.astrocraft.astrobb.player;

import net.hectus.color.McColor;
import net.hectus.util.Randomizer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.astrocraft.astrobb.InGameShop;
import space.astrocraft.astrobb.Trace;
import space.astrocraft.astrobb.Translation;
import space.astrocraft.astrobb.match.GameFlow;
import space.astrocraft.astrobb.match.Match;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class BBPlayer {
    public final Player player;
    private int luck = 10;
    private int dieCounter = -3;
    private int burningCounter = -3;
    private int jailCounter = -3;
    private int revives = 0;
    private boolean defended, attacked, doubleCounterAttack, canAlwaysMove;
    private boolean movement = true;
    private Match.PlayerState state;

    @Contract(pure = true)
    public BBPlayer(@NotNull Player player) {
        this.player = player;
    }

    public Locale locale() {
        return player.locale();
    }

    public int luck() {
        System.out.println(player.getName() + ".luck() - return: " + luck + " by: " + Trace.last());
        return luck;
    }
    public void addLuck(int luck) {
        System.out.println(player.getName() + ".addLuck(" + "luck = " + luck + ") by: " + Trace.last());
        this.luck += luck;
    }
    public void removeLuck(int luck) {
        System.out.println(player.getName() + ".removeLuck(" + "luck = " + luck + ") by: " + Trace.last());
        this.luck -= luck;
    }

    @Contract(pure = true)
    public static int chance(double chance, int luck) {
        System.out.println("BBPlayer.chance(" + "chance = " + chance + ", luck = " + luck + ") by: " + Trace.last());
        return (int) (chance - (1 - chance) * ((double) luck / 100));
    }

    public DyeColor randomSheepColor() {
        int pink = chance(1, luck * 2);
        int blue = chance(10, (int) (luck * 1.5));
        int brown = chance(15, luck);
        int black = chance(15, luck);
        int gray = chance(15, luck);
        int light_gray = chance(15, luck);
        int white = 30;

        int num = new Random().nextInt(pink+blue+brown+black+gray+light_gray+white+1);

        DyeColor out;
        if (num < pink) out = DyeColor.PINK; else num += pink;
        if (num < blue) out = DyeColor.BLUE; else num += blue;
        if (num < brown) out = DyeColor.BROWN; else num += brown;
        if (num < black) out = DyeColor.BLACK; else num += black;
        if (num < gray) out = DyeColor.GRAY; else num += gray;
        if (num < light_gray) out = DyeColor.LIGHT_GRAY;
        else out = DyeColor.WHITE;

        System.out.println(player.getName() + ".randomSheepColor() - return: " + out + " by: " + Trace.last());
        return out;
    }

    public boolean isDefended() {
        System.out.println(player.getName() + ".isDefended() - return: " + defended + " by: " + Trace.last());
        return defended;
    }
    public void setDefended(boolean defended) {
        System.out.println(player.getName() + ".setDefended(" + "defended = " + defended + ") by: " + Trace.last());
        this.defended = defended;
    }

    public boolean hasToDoubleCounterAttack() {
        System.out.println(player.getName() + ".hasToDoubleCounterAttack() - return: " + doubleCounterAttack + " by: " + Trace.last());
        return doubleCounterAttack;
    }
    public void setDoubleCounterAttack(boolean doubleCounterAttack) {
        System.out.println(player.getName() + ".setDoubleCounterAttack(" + "doubleCounterAttack = " + doubleCounterAttack + ") by: " + Trace.last());
        this.doubleCounterAttack = doubleCounterAttack;
    }

    public boolean cantMove() {
        System.out.println(player.getName() + ".cantMove() - return: " + !movement + " by: " + Trace.last());
        return !movement;
    }
    public void setMovement(boolean movement) {
        System.out.println(player.getName() + ".setMovement(" + "movement = " + movement + ") by: " + Trace.last());
        this.movement = movement;
    }

    public Match.PlayerState getState() {
        System.out.println(player.getName() + ".getState() - return: " + state + " by: " + Trace.last());
        return state;
    }
    public void setState(Match.PlayerState state) {
        System.out.println(player.getName() + ".setState(" + "state = " + state + ") by: " + Trace.last());
        this.state = state;
    }

    public boolean isAttacked() {
        System.out.println(player.getName() + ".isAttacked() - return: " + attacked + " by: " + Trace.last());
        return attacked;
    }
    public void setAttacked(boolean attacked) {
        System.out.println(player.getName() + ".setAttacked(" + "attacked = " + attacked + ") by: " + Trace.last());
        this.attacked = attacked;
    }

    public void startDieCounter(int turns) {
        System.out.println(player.getName() + ".startDieCounter(" + "turns = " + turns + ") by: " + Trace.last());
        dieCounter = turns;
    }
    public void startBurnCounter(int turns) {
        System.out.println(player.getName() + ".startBurnCounter(" + "turns = " + turns + ") by: " + Trace.last());
        burningCounter = turns;
    }
    public void startJailCounter(int turns) {
        System.out.println(player.getName() + ".startJailCounter(" + "turns = " + turns + ") by: " + Trace.last());
        jailCounter = turns;
    }
    public boolean isJailed() {
        System.out.println(player.getName() + ".isJailed() - return: " + (jailCounter >= 0) + " by: " + Trace.last());
        return jailCounter >= 0;
    }

    public void makeAlwaysMove() {
        System.out.println(player.getName() + ".makeAlwaysMove()");
        canAlwaysMove = true;
    }
    public boolean canAlwaysMove() {
        System.out.println(player.getName() + ".canAlwaysMove() - return: " + canAlwaysMove);
        return canAlwaysMove;
    }

    public void count() {
        System.out.println(player.getName() + ".count() by: " + Trace.last());

        dieCounter--;
        if (dieCounter == 0) GameFlow.lose(this, GameFlow.LoseReason.DEATH_COUNTER);
        if (burningCounter == 0) GameFlow.lose(this, GameFlow.LoseReason.BURNING);
    }

    public void swapHotbars() {
        System.out.println(player.getName() + ".swapHotbars() by: " + Trace.last());

        PlayerInventory inv = player.getInventory();
        int i = 0;
        while (i < 9) {
            ItemStack item1 = inv.getItem(i);
            inv.setItem(i, inv.getItem(i + 27));
            inv.setItem(i + 27, item1);
            i++;
        }
    }

    public void giveRandomItem(){
        System.out.println(player.getName() + ".giveRandomItem() by: " + Trace.last());

        ArrayList<InGameShop.ShopItem> items = new ArrayList<>(InGameShop.SHOP_ITEMS_NEUTRAL);
        items.addAll(InGameShop.SHOP_ITEMS_HOT);
        items.addAll(InGameShop.SHOP_ITEMS_COLD);
        items.addAll(InGameShop.SHOP_ITEMS_WATER);
        items.addAll(InGameShop.SHOP_ITEMS_NATURE);
        items.addAll(InGameShop.SHOP_ITEMS_REDSTONE);
        items.addAll(InGameShop.SHOP_ITEMS_DREAM);
        items.addAll(InGameShop.SHOP_ITEMS_GARDEN);
        items.addAll(InGameShop.SHOP_ITEMS_WARPS);
        ItemStack item = (ItemStack) Randomizer.fromCollection(items);
        player.getInventory().addItem(item);
    }

    public boolean hasRevive() {
        System.out.println(player.getName() + ".hasRevive() - return: " + revives + " by: " + Trace.last());
        return revives >= 1;
    }

    public void useRevive(){
        System.out.println(player.getName() + ".useRevive() by: " + Trace.last());

        revives -= 1;
        sendMessage(McColor.LIME + Translation.get("player.revive", player.locale()));
        Match.getOpposite(this).sendMessage(McColor.RED + Translation.get("player.revive.opponent", Match.getOpposite(this).locale(), player.getName()));
    }

    public void addRevive(){
        revives += 1;
    }

    //==========================================//
    // The next code is basically just from the //
    // Bukkit Player but so it's easier to use. //
    //==========================================//

    // Also, only add bukkit player stuff here, the rest belongs over this

    public void sendMessage(@NotNull String msg) {
        player.sendMessage(Component.text(msg));
    }

    public void sendActionBar(@NotNull String msg) {
        player.sendActionBar(Component.text(msg));
    }

    public void showTitle(@NotNull String ttl, @NotNull String sttl, @Nullable Title.Times tms) {
        if (tms == null) player.showTitle(Title.title(Component.text(ttl), Component.text(sttl)));
        else player.showTitle(Title.title(Component.text(ttl), Component.text(sttl), tms));
    }

    public void clearTitle() {
        player.clearTitle();
    }

    public void playSound(@NotNull Sound snd) {
        player.playSound(snd);
    }

    public void playSound(@NotNull Sound sound, double x, double y, double z) {
        player.playSound(sound, x, y, z);
    }

    public void addPotionEffect(PotionEffectType type, int dur, int amp) {
        player.addPotionEffect(new PotionEffect(type, dur, amp));
    }
}
