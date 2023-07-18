package org.islam.autonamaz.mixin;

import com.batoulapps.adhan.Prayer;
import com.ibm.icu.text.SimpleDateFormat;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.islam.autonamaz.AutoNamaz;
import org.islam.autonamaz.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements Globals {
    @Inject(method = "isPressed", at = @At("RETURN"), cancellable = true)
    public void isPressed(CallbackInfoReturnable<Boolean> cir) {
        if (minecraftClient.world == null || minecraftClient.player == null || AutoNamaz.namazTimes == null) return;

        var isNamazTime = AutoNamaz.namazTimes.isNamazTime();
        if (isNamazTime != Prayer.NONE && cir.getReturnValue()) {
            minecraftClient.inGameHud.setTitle(Text.literal("ITS %s TIME!"
                    .formatted(isNamazTime.name()))
                    .styled(style -> style.withColor(Formatting.RED)));

            var remaining = AutoNamaz.namazTimes.namazTime - System.currentTimeMillis();
            var remaining_time = new SimpleDateFormat("mm:ss").format(remaining);
            minecraftClient.inGameHud.setSubtitle(Text.literal("TIME REMAINING: %s"
                    .formatted(remaining_time))
                    .styled(style -> style.withColor(Formatting.RED))
            );

            minecraftClient.player.playSound(
                SoundEvents.ENTITY_WITHER_SPAWN,
                SoundCategory.MASTER,
                1f,
                0.5f
            );

            cir.setReturnValue(false);
        }
    }
}
