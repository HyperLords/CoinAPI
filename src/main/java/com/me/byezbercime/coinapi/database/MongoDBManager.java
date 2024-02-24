package com.me.byezbercime.coinapi.database;

import com.me.byezbercime.coinapi.coinsapi.Coin;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class MongoDBManager {

    @Getter
    public static MongoCollection<Document> collection;

    private File configFile=new File("plugins/CoinAPI/config.yml");
    private FileConfiguration configuration= YamlConfiguration.loadConfiguration(configFile);

    @Getter
    private static MongoClient mongoClient;

    public void connect(){
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        if (mongodbResult==true){
            if (mongoClient!=null) return;
            String hostName=configuration.getString("database.mongodb.host");
            int hostPort=configuration.getInt("database.mongodb.port");
            String[] databaseSplit=configuration.getString("database.mongodb.host-database").split(",");

            String url="mongodb://<host_name>:<host_port>/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2"
                    .replace("<host_name>",hostName)
                    .replace("<host_port>",""+hostPort);

            mongoClient= MongoClients.create(url);
            collection=getMongoClient().getDatabase(databaseSplit[0]).getCollection(databaseSplit[1]);
            System.out.println("Success connected MongoDB");
        }
    }

    public void disconnect(){
        Boolean mongodbResult=configuration.getBoolean("database.mongodb.connect");
        if (mongodbResult==true){
            if (getMongoClient()==null) return;
            getMongoClient().close();
            System.out.println("Goodbye MongoDB");
        }
    }

    public void saveData(UUID uuid){
        if (getMongoClient()!=null) {
            Document data=getPlayerData(uuid);
            if (data!=null){
                processMongoData(uuid);
            }else {
                data=new Document("uuid",uuid.toString());
                data.put("coin",0);
                getCollection().insertOne(data);
                processMongoData(uuid);
            }
        }else {
            connect();
            saveData(uuid);
        }
    }

    public void updateData(UUID uuid,int point){
        if (getMongoClient()!=null){
            Document data=getPlayerData(uuid);
            if (data!=null){
                Bson mongoDBPlayerData=Filters.eq("uuid",uuid.toString());
                Bson updateCoin= Updates.set("coin",point);
                UpdateResult result=getCollection().updateMany(mongoDBPlayerData,updateCoin);
            }
        }
    }

    public void processMongoData(UUID uuid){
        if (getMongoClient()!=null){
            Document data=getPlayerData(uuid);
            if (data!=null){
                String dataUUID=data.getString("uuid");
                int dataCoin=data.getInteger("coin");

                if (Coin.playerCoinHashMap.containsKey(uuid))
                    Coin.playerCoinHashMap.remove(uuid);

                if (!Coin.playerCoinHashMap.containsKey(uuid))
                    Coin.playerCoinHashMap.put(UUID.fromString(dataUUID),new Coin(dataCoin));

            }
        }
    }

    public Document getPlayerData(UUID uuid){
        Bson data= Filters.eq("uuid",uuid.toString());
        if (data!=null){
            Document documentData=getCollection().find(data).first();
            return documentData;
        }
    return null;}

}
