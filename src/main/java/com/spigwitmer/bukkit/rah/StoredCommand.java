package com.spigwitmer.bukkit.rah;

import java.util.UUID;

public class StoredCommand {
    private String username;
    private String command;
    private UUID worldUID;

    public StoredCommand(String cmd, String uname, UUID worlduid) {
        this.command = cmd;
        this.username = uname;
        this.worldUID = worlduid;
    }

    public String getUsername() { return username; }
    public String getCommand() { return command; }
    public UUID getWorldUID() { return worldUID; }

    public void setUsername(String uname) { this.username = uname; }
    public void setCommand(String cmd) { this.command = cmd; }
    public void setWorldUID(UUID uid) { this.worldUID = uid; }

    public String toString() {
        return "("+worldUID+") "+(username == null ? "# " : username+"$ ") + command;
    }
}
