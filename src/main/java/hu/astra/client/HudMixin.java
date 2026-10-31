package hu.astra.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void astra$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        AstraHud.draw(context);
    }
}
