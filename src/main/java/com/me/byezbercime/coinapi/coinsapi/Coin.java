package com.me.byezbercime.coinapi.coinsapi;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Coin {

    public static HashMap<UUID,Coin> playerCoinHashMap=new HashMap<>();

    private int coin;

    public Coin(int coin){
        this.coin=coin;
    }


}
