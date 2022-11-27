package eu.shindapp.hanaya.listeners;

import eu.shindapp.hanaya.HanayaCore;
import net.dv8tion.jda.api.JDA;

public class ListenerManager {

    private JDA api;

    public void registerEvents() {

        api = HanayaCore.getApi();

        api.addEventListener(
                //... Coming Soon
        );
    }
}
