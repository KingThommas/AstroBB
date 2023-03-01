package net.hectus.hectusblockbattles;

import org.bukkit.entity.Player;

public class MatchVariables {
    private Player[] players = {,};
    private boolean turn = false;
    private int blocksPlaced = 0;
    private boolean turnJustStarted = true; //true = turn just started and waiting for the player to place something, false = the player is building a structure that needs more than one block like walls or warps

    public Player getPlayerFromTurn() {
        return players[turn?1:0];
    }

    public void setPlayers(Player p1, Player p2) {
        this.players = new Player[]{p1, p2};
    }

    public void nextPlayer() {
        turn = !turn;
        blocksPlaced++;
    }

    public boolean didTurnJustStarted() {
        return turnJustStarted;
    }

    public void setTurnJustStarted(boolean b) {
        turnJustStarted = b;
    }
}