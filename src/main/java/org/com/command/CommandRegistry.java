package org.com.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandRegistry
{
    private final JavaPlugin plugin;
    private final Map<String, Command> registeredCommands = new ConcurrentHashMap<>();
    private final CommandMap commandMap;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger;

    public CommandRegistry(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.commandMap = Bukkit.getServer().getCommandMap();
        this.logger = plugin.getLogger();
    }

    public void registerCommand(String name, CustomCommand executor, boolean async, String permission)
    {
        lock.writeLock().lock();

        try {
            if (commandMap.getCommand(name) != null || registeredCommands.containsKey(name))
            {
                logger.warning("이미 등록된 명령어: " + name);
                return;
            }

            CommandHandler command = new CommandHandler(name, executor, async, plugin);

            if (permission != null && !permission.isEmpty())
            {
                command.setPermission(permission);
            }

            commandMap.register(plugin.getName(), command);
            registeredCommands.put(name, command);
            logger.info("동적 명령어 등록 완료: /" + name + " (권한: " + permission + ")");
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    public void registerCommand(String name, CustomCommand executor, boolean async, String permission, CustomTabCompleter tabCompleter)
    {
        lock.writeLock().lock();

        try
        {
            if (commandMap.getCommand(name) != null || registeredCommands.containsKey(name))
            {
                logger.warning("이미 등록된 명령어: " + name);
                return;
            }

            CommandHandler command = new CommandHandler(name, executor, async, plugin, tabCompleter);

            if (permission != null && !permission.isEmpty())
            {
                command.setPermission(permission);
            }

            commandMap.register(plugin.getName(), command);
            registeredCommands.put(name, command);
            logger.info("동적 명령어 등록 완료: /" + name + " (권한: " + permission + ")");
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    public void unregisterCommand(String name)
    {
        lock.writeLock().lock();

        try
        {
            Command command = registeredCommands.remove(name);

            if (command != null)
            {
                command.unregister(commandMap);
                logger.info("동적 명령어 제거 완료: /" + name);
            }
            else
            {
                logger.warning("제거할 명령어가 존재하지 않습니다: " + name);
            }
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "명령어 제거 중 오류 발생: " + e.getMessage(), e);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    public void reloadCommand(String name, CustomCommand executor, boolean async, String permission)
    {
        unregisterCommand(name);
        registerCommand(name, executor, async, permission);
    }

    public void listCommands(CommandSender sender)
    {
        lock.readLock().lock();

        try
        {
            sender.sendMessage("등록된 명령어 목록: " + String.join(", ", registeredCommands.keySet()));
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    public void clearCommands()
    {
        lock.writeLock().lock();

        try
        {
            for (String command : registeredCommands.keySet())
            {
                unregisterCommand(command);
            }
            registeredCommands.clear();
            logger.info("모든 동적 명령어가 해제되었습니다.");
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }
}
