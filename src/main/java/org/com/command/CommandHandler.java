package org.com.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler extends BukkitCommand
{
    private final CustomCommand executor;
    private final CustomTabCompleter tabCompleter;
    private final boolean async;
    private final JavaPlugin plugin;
    private final Logger logger;

    public CommandHandler(String name, CustomCommand executor, boolean async, JavaPlugin plugin)
    {
        this(name, executor, async, plugin, null);
    }

    public CommandHandler(String name, CustomCommand executor, boolean async, JavaPlugin plugin, CustomTabCompleter tabCompleter)
    {
        super(name);
        this.executor = executor;
        this.async = async;
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.tabCompleter = tabCompleter;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args)
    {
        if (this.getPermission() != null && !sender.hasPermission(this.getPermission()))
        {
            sender.sendMessage("관리자 전용 명령어입니다!");
            return true;
        }

        if (async)
        {
            AsyncCommandExecutor.executeAsync(plugin, sender, executor, args);
        }
        else
        {
            try
            {
                executor.execute(sender, args);
            }
            catch (Exception e)
            {
                logger.log(Level.SEVERE, "명령어 실행 중 오류 발생: " + e.getMessage(), e);
                sender.sendMessage("오류 발생! 관리자에게 문의하세요.");
            }
        }

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args)
    {
        if (tabCompleter != null)
        {
            return tabCompleter.complete(sender, args);
        }
        return super.tabComplete(sender, alias, args);
    }
}
