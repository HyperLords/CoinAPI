package com.me.byezbercime.coinapi;

import com.me.byezbercime.coinapi.coinsapi.Coin;
import com.me.byezbercime.coinapi.commands.CommandCoin;
import com.me.byezbercime.coinapi.database.MongoDBManager;
import com.me.byezbercime.coinapi.listeners.JoinEvent;
import com.me.byezbercime.coinapi.listeners.QuitEvent;
import com.me.byezbercime.coinapi.coinsapi.CoinManager;
import com.me.byezbercime.coinapi.database.MySQLManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoinAPI extends JavaPlugin {

    @Getter
    private static CoinAPI instance;
    @Override
    public void onEnable() {
        instance=this;

        new CoinManager().loadConfiguration();
        new CoinManager().loadPlayerCoinData();

        new MySQLManager().connect();
        new MongoDBManager().connect();

        getLogger().info("CoinAPI active");

        registerCommands();
        registerListeners();

    }

    @Override
    public void onDisable() {
        Coin.playerCoinHashMap.clear();
        new CoinManager().backupPlayerCoinData();
        new MySQLManager().disconnect();
        new MongoDBManager().disconnect();

        getLogger().info("Goodbye CoinAPI");
    }

    public String getColor(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    private void registerCommands(){
        getCommand("coin").setExecutor(new CommandCoin());
    }

    private void registerListeners(){
        Bukkit.getServer().getPluginManager().registerEvents(new QuitEvent(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(),this);
    }

}
