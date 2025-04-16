package org.com.command;

import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface CustomTabCompleter
{
    List<String> complete(CommandSender sender, String[] args);
}
