package com.me.byezbercime.coinapi.listeners;

import com.me.byezbercime.coinapi.coinsapi.CoinManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void event(PlayerJoinEvent e) {
        new CoinManager().createAccount(e.getPlayer());
    }

}
