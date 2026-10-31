package hu.astra.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NameMixin {
    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void astra$badge(Entity entity, EntityRenderState state, float partialTick, CallbackInfo ci) {
        if (entity instanceof Player p && state.nameTag != null
            && AstraClient.isAstraUser(p.getName().getString())) {
            state.nameTag = Component.empty()
                .append(Component.literal("\u2726 ASTRA ").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD))
                .append(state.nameTag);
        }
    }
}
