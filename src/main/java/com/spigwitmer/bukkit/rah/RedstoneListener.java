
package com.spigwitmer.bukkit.rah;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.logging.*;
import java.util.UUID;
import java.util.List;

/**
 * @author dongs
 */
public class RedstoneListener extends BlockListener {
    private final RedstoneActivationHookPlugin plugin;
    private final Logger logger = Logger.getLogger("Minecraft");

    public RedstoneListener(final RedstoneActivationHookPlugin p) {
        this.plugin = p;
    }

    @Override
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        int dCurrentChange = event.getOldCurrent() - event.getNewCurrent();
        Block b = event.getBlock();

        if ( b.getType() == Material.REDSTONE_WIRE ) {
            String locationInfo = plugin.getLocationKey(b);
            StoredCommand scmd = plugin.getAction(locationInfo);
            if (scmd != null && dCurrentChange < 0) {
                Server srv = Bukkit.getServer();
                List<Player> players = srv.matchPlayer(scmd.getUsername());
                if (players.size() == 1) {
                    // only run command if exact match
                    players.get(0).performCommand(scmd.getCommand());
                }
            }
        }
    }
}
