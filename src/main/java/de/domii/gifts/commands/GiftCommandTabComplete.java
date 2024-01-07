package de.domii.gifts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class GiftCommandTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("create");
            options.add("delete");
            options.add("edit");
            options.add("wand");

            return StringUtil.copyPartialMatches(args[0],options, new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
