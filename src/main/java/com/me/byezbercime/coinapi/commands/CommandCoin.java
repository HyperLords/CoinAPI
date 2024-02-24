package com.me.byezbercime.coinapi.commands;

import com.me.byezbercime.coinapi.CoinAPI;
import com.me.byezbercime.coinapi.coinsapi.Coin;
import com.me.byezbercime.coinapi.coinsapi.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandCoin implements CommandExecutor, TabCompleter {

    public CommandCoin(){
        JavaPlugin plugin=CoinAPI.getInstance();
        plugin.getCommand("coin").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player){
            Player player=(Player)sender;

            if (player.hasPermission("coinapi.player.command")){

                if (args.length==0){
                    Coin playerCoin= Coin.playerCoinHashMap.get(player.getUniqueId());
                    player.sendMessage(CoinAPI.getInstance().getColor("&eMy coin: "+playerCoin.getCoin()));
                return true;}
                else if (args[0].equalsIgnoreCase("send")){
                    OfflinePlayer targetOfflinePlayer= Bukkit.getOfflinePlayer(args[1]);
                    int point=Integer.parseInt(args[2]);

                    if (targetOfflinePlayer==null);
                    if (!targetOfflinePlayer.isOnline())return true;
                    if (targetOfflinePlayer.getName()==player.getName())return true;
                    Coin playerData= Coin.playerCoinHashMap.get(player.getUniqueId());
                    if (point==-1||point==0||point>playerData.getCoin())return true;

                    new CoinManager().sendPlayerCoin(player,targetOfflinePlayer.getPlayer(),point);
                    player.sendMessage(CoinAPI.getInstance().getColor("&aSuccess sending "+point+" coin to "+targetOfflinePlayer.getName()));
                    targetOfflinePlayer.getPlayer().sendMessage(CoinAPI.getInstance().getColor("&a"+player.getName()+" for you send coin "+point+""));
                return true;}
                else if (args[0].equalsIgnoreCase("look")){
                    OfflinePlayer targetOfflinePlayer= Bukkit.getOfflinePlayer(args[1]);

                    if (targetOfflinePlayer==null) return true;
                    if (targetOfflinePlayer.getName()==player.getName()){
                        Coin playerCoin= Coin.playerCoinHashMap.get(player.getUniqueId());
                        player.sendMessage(CoinAPI.getInstance().getColor("&eMy coin: "+playerCoin.getCoin()));
                    return true;}

                    player.sendMessage(new CoinManager().lookPlayerCoin(targetOfflinePlayer));
                return true;}

                if (player.hasPermission("coinapi.admin.command")){
                    if (args[0].equalsIgnoreCase("add")){
                        OfflinePlayer targetOfflinePlayer= Bukkit.getOfflinePlayer(args[1]);
                        int point=Integer.parseInt(args[2]);

                        if (targetOfflinePlayer==null) return true;
                        if (!targetOfflinePlayer.isOnline())return true;
                        Coin offlinePlayerCoin= Coin.playerCoinHashMap.get(targetOfflinePlayer.getUniqueId());
                        if (point==-1||point==0)return true;

                        new CoinManager().addCoin(targetOfflinePlayer.getPlayer(),point);
                        player.sendMessage(CoinAPI.getInstance().getColor("&aSuccess added "+point+" coin to "+targetOfflinePlayer.getName()));
                    return true;}
                    else if (args[0].equalsIgnoreCase("set")){
                        OfflinePlayer targetOfflinePlayer= Bukkit.getOfflinePlayer(args[1]);
                        int point=Integer.parseInt(args[2]);

                        if (targetOfflinePlayer==null||!targetOfflinePlayer.isOnline()) return true;
                        if (!targetOfflinePlayer.isOnline())return true;
                        Coin offlinePlayerCoin= Coin.playerCoinHashMap.get(targetOfflinePlayer.getUniqueId());
                        if (point==-1||point==0)return true;

                        new CoinManager().setCoin(targetOfflinePlayer.getPlayer(),point);
                        player.sendMessage(CoinAPI.getInstance().getColor("&aSuccess arranged "+point+" coin to "+targetOfflinePlayer.getName()));
                    return true;}
                    else if (args[0].equalsIgnoreCase("deleteaccount")){
                        OfflinePlayer targetOfflinePlayer= Bukkit.getOfflinePlayer(args[1]);
                    return true;}
                    else if (args[0].equalsIgnoreCase("remove")){
                        OfflinePlayer targetOfflinePlayer= Bukkit.getOfflinePlayer(args[1]);
                        int point=Integer.parseInt(args[2]);

                        if (targetOfflinePlayer==null) return true;
                        if (!targetOfflinePlayer.isOnline())return true;
                        Coin offlinePlayerCoin= Coin.playerCoinHashMap.get(targetOfflinePlayer.getUniqueId());
                        if (point==-1||point==0||point>offlinePlayerCoin.getCoin())return true;

                        new CoinManager().removeCoin(targetOfflinePlayer.getPlayer(),point);
                        player.sendMessage(CoinAPI.getInstance().getColor("&aSuccess removed "+point+" coin to "+targetOfflinePlayer.getName()));
                    return true;}
                return true;}

            return true;}
        return true;}
    return true;}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("coin")){
            if (args.length==4){
                for (int argumentValue=3;argumentValue<500;argumentValue++) {
                    if (args.length==argumentValue){
                        return new ArrayList<>();
                    }
                }
            }
            else if (args.length==3){
                if (args[0].equalsIgnoreCase("add")
                        ||args[0].equalsIgnoreCase("set")){
                    List<String> arguments= Arrays.asList("1");
                    List<String> tabs= new ArrayList<>();
                    for (String searchArgument : arguments) {
                        if (searchArgument.startsWith(args[2].toLowerCase())){
                            tabs.add(searchArgument);
                        }
                    }
                return tabs;
                }
                else if (args[0].equalsIgnoreCase("send")){
                    Player player=(Player)sender;
                    Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
                    return Arrays.asList(""+coin.getCoin());
                }
                else if (args[0].equalsIgnoreCase("remove")){
                    OfflinePlayer targetOfflinePlayer=Bukkit.getOfflinePlayer(args[1]);
                    Coin coin=Coin.playerCoinHashMap.get(targetOfflinePlayer.getUniqueId());
                    return Arrays.asList(""+coin.getCoin());
                }
                else if (args[0].equalsIgnoreCase("look")){
                    for (int argumentValue=3;argumentValue<500;argumentValue++) {
                        if (args.length==argumentValue){
                            return new ArrayList<>();
                        }
                    }
                }
            }
            else if (args.length==2){
                if (args[0].equalsIgnoreCase("add")
                        ||args[0].equalsIgnoreCase("set")
                        ||args[0].equalsIgnoreCase("remove")
                        ||args[0].equalsIgnoreCase("look")
                        ||args[0].equalsIgnoreCase("deleteaccount")
                        ||args[0].equalsIgnoreCase("send")){

                    List<String> arguments=new ArrayList<>();
                    List<String> tabs=new ArrayList<>();
                    String playerArgument=args[1];

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        arguments.add(players.getName());
                    }

                    for (String playersSearch : arguments) {
                        if (playersSearch.startsWith(playerArgument)){
                            tabs.add(playersSearch);
                        }
                    }
                return tabs;
                }
                else if (args[0].equalsIgnoreCase("look")){
                    List<String> arguments= Arrays.asList("1");
                    List<String> tabs= new ArrayList<>();
                    for (String searchArgument : arguments) {
                        if (searchArgument.startsWith(args[2].toLowerCase())){
                            tabs.add(searchArgument);
                        }
                    }
                    return tabs;
                }
                else {
                    for (int argumentValue=0;argumentValue<500;argumentValue++) {
                        if (args.length==argumentValue){
                            return new ArrayList<>();
                        }
                    }
                }
            }
            else if (args.length==1){
                List<String> arguments=new ArrayList<>();
                if (sender.hasPermission("coinapi.admin.command")){
                    arguments.add("add");
                    arguments.add("set");
                    arguments.add("deleteaccount");
                    arguments.add("remove");
                }
                arguments.add("send");
                arguments.add("look");
            return arguments;
            }
        }
        return null;
    }
}
