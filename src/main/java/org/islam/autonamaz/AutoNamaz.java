package org.islam.autonamaz;

import net.fabricmc.api.ModInitializer;
import java.util.concurrent.CompletableFuture;

public class AutoNamaz implements ModInitializer, Globals {
    public static NamazTimes namazTimes = new NamazTimes();

    @Override
    public void onInitialize() {
        CompletableFuture.runAsync(() -> {
            namazTimes.getClientLocation();
        });
    }
}
