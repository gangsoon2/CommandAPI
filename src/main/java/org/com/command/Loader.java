package org.com.command;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader extends JavaPlugin
{
    private static Loader instance;
    private CommandRegistry commandRegistry;
    private static final ExecutorService asyncExecutor = Executors.newFixedThreadPool(10);

    @Override
    public void onEnable()
    {
        instance = this;
        commandRegistry = new CommandRegistry(this);
        getLogger().info("명령어API 플러그인 활성화");
    }

    @Override
    public void onDisable()
    {
        getLogger().info("명령어API 플러그인 비활성화");

        if (commandRegistry != null)
        {
            commandRegistry.clearCommands();
        }
        asyncExecutor.shutdown();
    }

    public static Loader getInstance()
    {
        return instance;
    }

    public CommandRegistry getCommandRegistry()
    {
        return commandRegistry;
    }
}
