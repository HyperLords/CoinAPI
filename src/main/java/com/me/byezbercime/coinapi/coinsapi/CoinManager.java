package com.me.byezbercime.coinapi.coinsapi;

import com.me.byezbercime.coinapi.CoinAPI;
import com.me.byezbercime.coinapi.database.MongoDBManager;
import com.me.byezbercime.coinapi.database.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CoinManager{

    private File dataFile=new File("plugins/CoinAPI/data.yml");
    private FileConfiguration dataConfig= YamlConfiguration.loadConfiguration(dataFile);
    private File configFile=new File("plugins/CoinAPI/config.yml");
    private FileConfiguration configuration= YamlConfiguration.loadConfiguration(configFile);

    public void loadConfiguration(){
        if (!configFile.exists())
            CoinAPI.getInstance().saveResource("config.yml",false);

        if (!dataFile.exists())
            CoinAPI.getInstance().saveResource("data.yml",false);
    }

    public void createAccount(Player player){
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        if (!player.hasPlayedBefore()){
            Coin.playerCoinHashMap.put(player.getUniqueId(),new Coin(0));
            dataConfig.set("data."+player.getUniqueId().toString()+".amount",0);
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                return;
            }


        }else {
            int playerCoin=dataConfig.getInt("data."+player.getUniqueId().toString()+".amount");
            Coin.playerCoinHashMap.put(player.getUniqueId(),new Coin(playerCoin));
        }

        if (mysqlResult==true){
            new MySQLManager().saveData(player.getUniqueId());
        }else if (mongodbResult==true){
            new MongoDBManager().saveData(player.getUniqueId());
        }
    }

    public void saveAccount(Player player){
        if (Coin.playerCoinHashMap.containsKey(player.getUniqueId())){
            Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
            dataConfig.set("data."+player.getUniqueId().toString()+".amount",coin.getCoin());
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                return;
            }
            Coin.playerCoinHashMap.remove(player.getUniqueId());
        }
    }

    public void deleteAccount(Player player){
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        if (Coin.playerCoinHashMap.containsKey(player.getUniqueId())){
            Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
            dataConfig.set("data."+player.getUniqueId().toString(),null);
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                return;
            }
            Coin.playerCoinHashMap.remove(player.getUniqueId());
        }

        if (mysqlResult==true){
            new MySQLManager().deleteData(player.getUniqueId());
        }else if (mongodbResult==true){
            //under constructor
        }
    }

    public void addCoin(OfflinePlayer player,int point){
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        if (player.isOnline()){
            Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
            coin.setCoin(coin.getCoin()+point);

            if (mysqlResult==true){
                new MySQLManager().updateData(player.getUniqueId(),coin.getCoin());
            }else if (mongodbResult==true){
                new MongoDBManager().updateData(player.getUniqueId(),coin.getCoin());
            }
        }
    }

    public void setCoin(OfflinePlayer player,int point){
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        if (player.isOnline()){
            Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
            coin.setCoin(point);

            if (mysqlResult==true){
                new MySQLManager().updateData(player.getUniqueId(),coin.getCoin());
            }else if (mongodbResult==true){
                new MongoDBManager().updateData(player.getUniqueId(),coin.getCoin());
            }
        }
    }

    public void removeCoin(OfflinePlayer player,int point){
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        if (player.isOnline()){
            Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
            coin.setCoin(coin.getCoin()-point);

            if (mysqlResult==true){
                new MySQLManager().updateData(player.getUniqueId(),coin.getCoin());
            }else if (mongodbResult==true){
                new MongoDBManager().updateData(player.getUniqueId(),coin.getCoin());
            }
        }
    }

    public void sendPlayerCoin(OfflinePlayer player,OfflinePlayer player2,int point){
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        if (player.isOnline()||player2.isOnline()){
            Coin playerCoin=Coin.playerCoinHashMap.get(player.getUniqueId());
            Coin playerTwoCoin=Coin.playerCoinHashMap.get(player2.getUniqueId());

            playerCoin.setCoin(playerCoin.getCoin()-point);
            playerTwoCoin.setCoin(playerTwoCoin.getCoin()+point);

            if (mysqlResult==true){
                new MySQLManager().updateData(player.getUniqueId(),playerCoin.getCoin());
                new MySQLManager().updateData(player2.getUniqueId(), playerTwoCoin.getCoin());
            }else if (mongodbResult==true){
                new MongoDBManager().updateData(player.getUniqueId(),playerCoin.getCoin());
                new MongoDBManager().updateData(player2.getUniqueId(),playerTwoCoin.getCoin());
            }

        }
    }

    public String lookPlayerCoin(OfflinePlayer player){
        if (player.isOnline()){
            Coin coin=Coin.playerCoinHashMap.get(player.getUniqueId());
            return CoinAPI.getInstance().getColor("&e"+player.getName()+" coin: "+coin.getCoin());
        }else{
            if (!dataConfig.contains("data."+player.getUniqueId().toString()+".amount"))return null;

            int coin=dataConfig.getInt("data."+player.getUniqueId().toString()+".amount");
            return CoinAPI.getInstance().getColor("&e"+player.getName()+" coin: "+coin);
        }
    }

    public void loadPlayerCoinData(){
        Boolean mysqlResult=configuration.getBoolean("database.mysql.connect");
        for (Player players : Bukkit.getOnlinePlayers()) {
            createAccount(players);
        }
    }
    public void backupPlayerCoinData(){
        for (Player players : Bukkit.getOnlinePlayers()) {
            saveAccount(players);
        }
    }

}
