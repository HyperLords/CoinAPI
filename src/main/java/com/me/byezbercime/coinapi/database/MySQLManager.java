package com.me.byezbercime.coinapi.database;

import com.me.byezbercime.coinapi.coinsapi.Coin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class MySQLManager {

    @Getter
    private static Connection connection;
    private File configFile=new File("plugins/CoinAPI/config.yml");
    private FileConfiguration configuration= YamlConfiguration.loadConfiguration(configFile);

    public void connect(){
        Boolean connectResult=configuration.getBoolean("database.mysql.connect");
        if (connectResult==true){
            if (getConnection()!=null)return;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String hostName=configuration.getString("database.mysql.host");
                String hostUsername=configuration.getString("database.mysql.username");
                String hostPassword=configuration.getString("database.mysql.password");
                int hostPort=configuration.getInt("database.mysql.port");
                String[] databaseSpilt=configuration.getString("database.mysql.host-database").split(",");

                String url="jdbc:mysql://<host_name>:<host_port>/"
                        .replace("<host_name>",""+hostName)
                        .replace("<host_port>",""+hostPort);

                connection=DriverManager.getConnection(url,hostUsername,hostPassword);

                mysqlDatabaseCMD("create database if not exists "+databaseSpilt[0]+";");
                mysqlDatabaseCMD("use "+databaseSpilt[0]+";");
                mysqlDatabaseCMD("create table if not exists "+databaseSpilt[1]+"(" +
                        "uuid varchar(50) primary key," +
                        "coin int(50) not null);");

                System.out.println("Success connected MySQL");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(){
        Boolean connectResult=configuration.getBoolean("database.mysql.connect");
        if (connectResult==true){
            if (getConnection()==null)return;
            try {
                getConnection().close();
                System.out.println("Goodbye MySQL");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mysqlDatabaseCMD(String command){
        try {
            Statement statement=getConnection().createStatement();
            statement.executeUpdate(command);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveData(UUID uuid){
        if (getConnection()!=null){
            if (getPlayerData(uuid)!=null){
                processPlayerData(uuid);
            }else {
                String[] databaseSpilt=configuration.getString("database.mysql.host-database").split(",");
                try {
                    PreparedStatement preparedStatement=getConnection().prepareStatement("insert into "+databaseSpilt[1]+"(uuid,coin) values (?,?);");
                    preparedStatement.setString(1,uuid.toString());
                    preparedStatement.setInt(2,0);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    processPlayerData(uuid);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else {
            connect();
            saveData(uuid);
        }
    }

    public void deleteData(UUID uuid){
        if (getConnection()!=null){
            if (getPlayerData(uuid)!=null){
                String[] databaseSpilt=configuration.getString("database.mysql.host-database").split(",");
                try {
                    PreparedStatement preparedStatement=getConnection().prepareStatement("delete from "+databaseSpilt[1]+" where uuid=?");
                    preparedStatement.setString(1,uuid.toString());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateData(UUID uuid,int point){
        if (getConnection()!=null){
            if(getPlayerData(uuid)!=null){
                String[] databaseSpilt=configuration.getString("database.mysql.host-database").split(",");
                try {
                    PreparedStatement preparedStatement=getConnection().prepareStatement("update "+databaseSpilt[1]+" set coin=? where uuid=?;");
                    preparedStatement.setInt(1,point);
                    preparedStatement.setString(2,uuid.toString());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void processPlayerData(UUID uuid){
        if (getConnection()!=null){
            if (getPlayerData(uuid)!=null){
                ResultSet data=getPlayerData(uuid);
                try {
                    String dataUUID=data.getString("uuid");
                    int dataCoin=data.getInt("coin");

                    if (Coin.playerCoinHashMap.containsKey(uuid))
                        Coin.playerCoinHashMap.remove(uuid);

                    if (!Coin.playerCoinHashMap.containsKey(uuid))
                        Coin.playerCoinHashMap.put(UUID.fromString(dataUUID),new Coin(dataCoin));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else {
            connect();
            processPlayerData(uuid);
        }
    }

    public ResultSet getPlayerData(UUID uuid){
        try {
            PreparedStatement st=getConnection().prepareStatement("select uuid,coin from data where uuid=?");
            st.setString(1,uuid.toString());
            ResultSet profileData=st.executeQuery();
            while (profileData.next()){
                if (profileData!=null){
                    return profileData;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;}

}
