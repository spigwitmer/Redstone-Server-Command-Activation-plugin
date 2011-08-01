package com.spigwitmer.bukkit.rah;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.logging.*;
import com.earth2me.essentials.TargetBlock;
import java.util.HashMap;
import java.util.UUID;

public class RedstoneHookCommand implements CommandExecutor {
    private final RedstoneActivationHookPlugin plugin;
    private final Logger logger = Logger.getLogger("Minecraft");

    public RedstoneHookCommand(RedstoneActivationHookPlugin p) {
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] split) {

        Player player = (Player)sender;

        Block tgtblk = new TargetBlock(player).getTargetBlock();
        boolean isTargetRedstone = tgtblk.getType() == Material.REDSTONE_WIRE;
        String location = plugin.getLocationKey(tgtblk);
        StoredCommand curCommand = plugin.getAction(location);

        logger.log(Level.INFO, String.format("split.length: %d", split.length));
        logger.log(Level.INFO, String.format("label: %s", label));

        switch(split.length) {
            case 0:
                if (!isTargetRedstone) {
                    sender.sendMessage("Target block is not redstone wire");
                } else {
                    sender.sendMessage( String.format("Current command: %s", (curCommand == null) ? "none" : curCommand.getCommand()) );
                }
                break;
            case 1:
                if ( split[0].compareToIgnoreCase("clear") == 0 ) {
                    if ( isTargetRedstone ) {
                        plugin.removeAction(location);
                        sender.sendMessage("Redstone action cleared");
                    } else {
                        sender.sendMessage("Target block is not redstone wire");
                    }
                } else if ( split[0].compareToIgnoreCase("getmaps") == 0 ) {
                    sender.sendMessage( String.format("current mappings: %s", plugin.getCurrentActions().toString()) );
                }
                break;
            default:
                if ( split[0].compareToIgnoreCase("set") == 0 && isTargetRedstone ) {
                    String storedcmd = "";
                    for(int i = 1; i < split.length; i++) {
                        storedcmd += split[i] + " ";
                    }
                    storedcmd = storedcmd.trim();
                    plugin.addAction(location, new StoredCommand(storedcmd,player.getName(),tgtblk.getWorld().getUID()));
                    sender.sendMessage("Command added to redstone wire: "+storedcmd);
                }
                break;
        }

        return true;
    }
}

