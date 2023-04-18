package net.hectus.hectusblockbattles.match;

import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Match {

    private final MatchData matchData;

    public Match() {
        this.matchData = new MatchData();
    }

    public Match(MatchData matchData) {
        this.matchData = matchData;
    }

    public Player getCurrentTurnPlayer() {
        return matchData.players[matchData.turn ? 1 : 0];
    }
    public Player getOppositeTurnPlayer() {
        return matchData.players[matchData.turn ? 0 : 1];
    }
    public Material getLastBlock() {
        return matchData.lastBlock;
    }

    public void changeTurn() {
        matchData.turn = !matchData.turn;
    }

    public void end(Player won, Player lost, Player causeSubject, String cause) {
        PlayerModeManager.initializePlayerMode(won);
        PlayerModeManager.initializePlayerMode(lost);
        if (causeSubject != null) {
            if (causeSubject == won) {
                cause = "you " + cause;
            } else {
                cause = "your opponent " + cause;
            }
        }
        won.sendMessage("You won the match because " + cause);
        lost.sendMessage("You lost the match because " + cause);
    }

    public boolean check(int x, int y, int z, Material material, Player player) { //VERY NOT FINISHED!!!

        changeTurn();
        if (material == Material.CARVED_PUMPKIN) {
            if (matchData.turnJustStarted && matchData.PW.didPlayerStartPlacingPumpkin(x, y, z, material, player)) {
                matchData.turnJustStarted = false;
                return true;
            } else if (!matchData.turnJustStarted) {
                int result = matchData.PW.didPlayerContinuePlacingPumpkin(x, y, z, material, true, player);
                if (result == 1) {
                    return true;
                } else if (result == 2) {
                    setLuckBoost(matchData.turn, getLuckBoost(matchData.turn) + (getLuckBoost(matchData.turn) * 0.2));
                    matchData.night = true;
                    return true;
                }
            }
        } else if (material == Material.GLASS || material.name().startsWith("STAINED_GLASS")) {
            if (matchData.turnJustStarted && matchData.GW.didPlayerStartPlacingGlass(x, y, z, material)) {
                return true;
            } else if (!matchData.turnJustStarted) {
                Material result = matchData.GW.didPlayerContinuePlacingGlass(x, y, z, material);
                if (result == Material.WHITE_CONCRETE) {
                    return true;
                } else if (result != Material.RED_CONCRETE) {
                    if(result == Material.GREEN_STAINED_GLASS) {
                        setLuckBoost(matchData.turn, getLuckBoost(matchData.turn) * 0.15);
                    } else if (result == Material.RED_STAINED_GLASS) {
                        setLuckBoost(!matchData.turn, getLuckBoost(!matchData.turn) - (getLuckBoost(!matchData.turn) * 0.1));
                    }
                    return true;
                }
            }
        }
        else if (material == Material.NETHERRACK || material == Material.SHROOMLIGHT) {
            if (matchData.turnJustStarted && matchData.warps.didPlayerStartPlacing(x, y, z, material, player)) {
                return true;
            } else if (!matchData.turnJustStarted) {
                Material[] result = matchData.warps.didPlayerContinuePlacing(x, y, z, material, player);
                if (Arrays.equals(result, new Material[]{Material.WHITE_CONCRETE})) {
                    return true;
                } else if (!Arrays.equals(result, new Material[]{Material.RED_CONCRETE})) {
                    if (Math.random() < 0.3 * getLuckBoost(matchData.turn)) {
                        setCurrentWarp(Warp.NETHER);
                        setBlockBoosts(0, 1.5);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Player getPlayer(boolean b) {
        return matchData.players[b ? 1 : 0];
    }

    public boolean getTurnFromPlayer(Player player) {
        return matchData.players[1] == player;
    }

    public boolean getTurn() {
        return matchData.turn;
    }

    public boolean getTurnJustStarted() {
        return matchData.turnJustStarted;
    }

    public void setLuckBoost(boolean player, double boost) {
        matchData.luckBoosts[player?1:0] = boost;
    }

    public double getLuckBoost(boolean player) {
        return matchData.luckBoosts[player?1:0];
    }

    public double getGameScore() {
        return matchData.gameScore;
    }

    public void setGameScore(double score) {
        matchData.gameScore = score;
    }

    public void setCurrentWarp(Warp warp) {
        matchData.currentWarp = warp;
        if (warp.isNight()) {
            matchData.night = false;
        }
    }

    public Warp getCurrentWarp() {
        return matchData.currentWarp;
    }

    public boolean getNight() {
        return matchData.night;
    }

    public void setBlockBoosts(double group, double boost) {
        matchData.blockBoosts[0] = group;
        matchData.blockBoosts[1] = boost;
    }

    public double[] getBlockBoosts() {
        return matchData.blockBoosts;
    }
}
