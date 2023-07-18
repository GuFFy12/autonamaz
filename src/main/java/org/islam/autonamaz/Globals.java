package org.islam.autonamaz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

public interface Globals {
    MinecraftClient minecraftClient = MinecraftClient.getInstance();
    Gson gson = new GsonBuilder().setLenient().create();
}
