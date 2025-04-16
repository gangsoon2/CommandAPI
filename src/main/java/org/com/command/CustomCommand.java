package org.com.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

@FunctionalInterface
public interface CustomCommand extends CommandExecutor
{
    void execute(CommandSender sender, String[] args) throws Exception;

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args)
    {
        try
        {
            execute(sender, args);
        }
        catch (Exception e)
        {
            Logger logger = Logger.getLogger("Minecraft");
            logger.log(Level.SEVERE, "명령어 실행 중 오류 발생: " + e.getMessage(), e);

            sender.sendMessage("오류 발생! 관리자에게 문의하세요.");
        }
        return true;
    }
}
