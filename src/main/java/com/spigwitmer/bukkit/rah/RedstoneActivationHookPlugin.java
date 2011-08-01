
package com.spigwitmer.bukkit.rah;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.block.Block;
import java.util.logging.*;

import java.util.HashMap;

/**
 *
 * @author FILTHDICK
 */
public class RedstoneActivationHookPlugin extends JavaPlugin {
    private HashMap<String,StoredCommand> redstoneActions;
    private final Logger logger = Logger.getLogger("Minecraft");

    public enum HandlerAction {
        ACTION_GET, ACTION_ADD, ACTION_REMOVE
    }

    public HashMap<String,StoredCommand> getCurrentActions() {
        return redstoneActions;
    }

    public String getLocationKey(Block b) {
        return String.format("%s-%d-%d-%d", b.getWorld().getUID(), b.getX(), b.getY(), b.getZ());
    }

    public StoredCommand getAction(String location) {
        try {
            return redstoneActionHandler(HandlerAction.ACTION_GET, location, null);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not perform action: "+e.getMessage());
            return null;
        }
    }

    public StoredCommand addAction(String location, StoredCommand command) {
        try {
            return redstoneActionHandler(HandlerAction.ACTION_ADD, location, command);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not perform action: "+e.getMessage());
            return null;
        }
    }

    public StoredCommand removeAction(String location) {
        try {
            return redstoneActionHandler(HandlerAction.ACTION_REMOVE, location, null);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not perform action: "+e.getMessage());
            return null;
        }
    }

    private synchronized StoredCommand redstoneActionHandler(HandlerAction action, String location, StoredCommand command) throws Exception {
        StoredCommand ret;
        switch (action) {
            case ACTION_GET:
                ret = redstoneActions.get(location);
                break;
            case ACTION_ADD:
                ret = redstoneActions.put(location,command);
                break;
            case ACTION_REMOVE:
                ret = redstoneActions.remove(location);
                break;

            default:
                ret = null;
                break;
        }
        return ret;
    }

    public void onDisable() {
        redstoneActions.clear();
    }

    public void onEnable() {
        redstoneActions = new HashMap<String,StoredCommand>();

        // TODO: Place any custom enable code here including the registration of any events
        RedstoneListener redstoneListener = new RedstoneListener(this);

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, redstoneListener, Priority.Normal, this);

        // Register our commands
        getCommand("redstonehook").setExecutor(new RedstoneHookCommand(this));

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
}
