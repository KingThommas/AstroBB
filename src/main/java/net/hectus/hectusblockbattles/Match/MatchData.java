package net.hectus.hectusblockbattles.Match;

import net.hectus.hectusblockbattles.SpecialAbilities.GlassWalls;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;
import net.hectus.hectusblockbattles.Warps.Warp;
import net.hectus.hectusblockbattles.Warps.Warps;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MatchData {

    public MatchData() {
        this.players = new Player[2];
        this.warps = new Warps();
        this.PW = new PumpkinWall();
        this.GW = new GlassWalls();
    }

    public MatchData(Player p1, Player p2) {
        this.players = new Player[] {p1, p2};
        this.warps = new Warps();
        this.PW = new PumpkinWall();
        this.GW = new GlassWalls();
    }

    public Warps warps;
    public PumpkinWall PW;
    public GlassWalls GW;
    public Material lastBlock;
    public double gameScore = 0;
    public Player[] players;
    public boolean turn = false;
    public double[] luckBoosts = {1, 1};
    public double[] blockBoosts = {-1, -1};
    public boolean turnJustStarted = true; //true = turn just started and waiting for the player to place something, false = the player is building a structure that needs more than one block like walls or warps
    public Warp currentWarp = Warp.DEFAULT;
    public boolean night = false;
}
