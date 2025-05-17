package creeper_knc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static creeper_knc.FakeModBlocker.hexSupport;

public class ModBlockerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fakemodblocker.admin")) {
            sender.sendMessage(colorize(getMsg("command.no-permission")));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            FakeModBlocker.getInstance().reloadConfig();
            FakeModBlocker.getInstance().loadMessages();
            sender.sendMessage(colorize(getMsg("command.reload-success")));
            return true;
        }

        sender.sendMessage(colorize(getMsg("command.usage")));
        return true;
    }

    private String getMsg(String path) {
        return FakeModBlocker.getInstance().getMessages().getString(path, "&cMissing message: " + path);
    }

    private String colorize(String message) {
        if (hexSupport) {
            return applyHexColorCodes(message.replace("&", "§"));
        } else {
            return message.replace("&", "§");
        }
    }

    private String applyHexColorCodes(String msg) {
        Pattern pattern = Pattern.compile("&#([a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(msg);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append("§").append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
