package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.SpecialAbilities.GlassWalls;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;
import net.hectus.hectusblockbattles.SpecialAbilities.Warps;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MatchVariables {
    private PumpkinWall PW = new PumpkinWall();
    private Warps warps = new Warps();
    private GlassWalls GW = new GlassWalls();
    private Material lastBlock;
    private double gameScore = 0;
    private Player[] players = {,};
    private boolean turn = false;
    private double[] luckBoosts = {1,1};

    private double[] blockBoosts = {-1,-1};
    private boolean turnJustStarted = true; //true = turn just started and waiting for the player to place something, false = the player is building a structure that needs more than one block like walls or warps
    private String currentWarp = "default";
    private boolean night = false;
    public Player getCurrentTurnPlayer() {
        return players[turn?1:0];
    }

    public boolean getCurrentTurnBoolean() {
        return turn;
    }

    public void setPlayers(Player p1, Player p2) {
        this.players = new Player[]{p1, p2};
    }

    public Player getPlayer(boolean b) {
        return players[b?1:0];
    }

    public boolean getTurnFromPlayer(Player player) {
        return players[1]==player;
    }

    public void setTurn(boolean b) {
        turn = b;
    }

    public boolean getTurn() {
        return turn;
    }

    public boolean getTurnJustStarted() {
        return turnJustStarted;
    }

    public void setTurnJustStarted(boolean b) {
        turnJustStarted = b;
    }

    public void setLuckBoost(boolean player, double boost) {
        luckBoosts[player?1:0] = boost;
    }

    public double getLuckBoost(boolean player) {
        return luckBoosts[player?1:0];
    }

    public double getGameScore() {
        return gameScore;
    }

    public void setGameScore(double d) {
        gameScore = d;
    }

    public void setLastBlock(Material material) {
        lastBlock = material;
    }

    public Material getLastBlock() {
        return lastBlock;
    }

    public PumpkinWall getPumpkinWall(){
        return PW;
    }

    public GlassWalls getGlassWalls() {
        return GW;
    }

    public Warps getWarps() {
        return warps;
    }

    public void setCurrentWarp(String s) {
        currentWarp = s;
        if (Objects.equals(s, "nether") || Objects.equals(s, "geode") || Objects.equals(s, "cave")) {
            night = false;
        }
    }

    public String getCurrentWarp() {
        return currentWarp;
    }

    public boolean getNight() {
        return night;
    }

    public void setNight(boolean b) {
        night = b;
    }

    public void setBlockBoosts(double group, double boost) {
        blockBoosts[0] = group;
        blockBoosts[1] = boost;
    }

    public double[] getBlockBoosts() {
        return blockBoosts;
    }
}