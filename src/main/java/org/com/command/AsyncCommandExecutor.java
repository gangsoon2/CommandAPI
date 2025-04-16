package org.com.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncCommandExecutor
{
    private static final ExecutorService asyncExecutor = Executors.newFixedThreadPool(10);
    private static final Logger logger = Logger.getLogger("Minecraft");

    public static void executeAsync(JavaPlugin plugin, CommandSender sender, CustomCommand executor, String[] args)
    {
        asyncExecutor.execute(() ->
        {
            try
            {
                executor.execute(sender, args);
            }
            catch (Exception e)
            {
                logger.log(Level.SEVERE, "비동기 명령어 실행 중 오류 발생: " + e.getMessage(), e);
                sender.sendMessage("오류 발생! 관리자에게 문의하세요.");
            }
        });
    }

    public static void shutdown()
    {
        asyncExecutor.shutdown();
    }
}
