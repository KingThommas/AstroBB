package net.hectus.hectusblockbattles.player;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.match.Match;
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
    public BBPlayer(Player player) {
        this.player = player;
    }

    public int luck() {
        return luck;
    }
    public void addLuck(int luck) {
        this.luck += luck;
    }
    public void removeLuck(int luck) {
        this.luck -= luck;
    }

    public static int chance(double chance, int luck) {
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

        if (num < pink) return DyeColor.PINK; else num += pink;
        if (num < blue) return DyeColor.BLUE; else num += blue;
        if (num < brown) return DyeColor.BROWN; else num += brown;
        if (num < black) return DyeColor.BLACK; else num += black;
        if (num < gray) return DyeColor.GRAY; else num += gray;
        if (num < light_gray) return DyeColor.LIGHT_GRAY;
        else return DyeColor.WHITE;
    }

    public boolean isDefended() {
        return defended;
    }
    public void setDefended(boolean defended) {
        this.defended = defended;
    }

    public boolean hasToDoubleCounterAttack() {
        return doubleCounterAttack;
    }
    public void setDoubleCounterAttack(boolean doubleCounterAttack) {
        this.doubleCounterAttack = doubleCounterAttack;
    }

    public boolean canMove() {
        return movement;
    }
    public void setMovement(boolean movement) {
        this.movement = movement;
    }

    public Match.PlayerState getState() {
        return state;
    }
    public void setState(Match.PlayerState state) {
        this.state = state;
    }

    public boolean isAttacked() {
        return attacked;
    }
    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    public void startDieCounter(int turns) {
        dieCounter = turns;
    }
    public void startBurnCounter(int turns) {
        burningCounter = turns;
    }
    public void startJailCounter(int turns) {
        jailCounter = turns;
    }
    public boolean isJailed() {
        return jailCounter >= 0;
    }

    public void makeAlwaysMove() {
        canAlwaysMove = true;
    }
    public boolean canAlwaysMove() {
        return canAlwaysMove;
    }

    public void count() {
        dieCounter--;
        if (dieCounter == 0 || burningCounter == 0) {
            Match.win(Match.getOpposite(this));
        }
    }

    public void swapHotbars() {
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
        ArrayList<InGameShop.ShopItem> items = new ArrayList<>();
        items.addAll(InGameShop.SHOP_ITEMS_1);
        items.addAll(InGameShop.SHOP_ITEMS_2);
        ItemStack item = (ItemStack) Randomizer.fromCollection(items);
        player.getInventory().addItem(item);
    }

    public boolean hasRevive() {
        return revives >= 1;
    }

    public void useRevive(){
        revives -= 1;
        sendMessage(McColor.LIME + Translation.get("player.revive", locale()));
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

    public Locale locale() {
        return player.locale();
    }

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
