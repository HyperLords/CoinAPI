package com.me.byezbercime.coinapi.listeners;

import com.me.byezbercime.coinapi.coinsapi.CoinManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void event(PlayerQuitEvent e){
        new CoinManager().saveAccount(e.getPlayer());
    }

}
