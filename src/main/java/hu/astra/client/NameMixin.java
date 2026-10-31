package hu.astra.client;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NameMixin {
    @Inject(method = "updateRenderState", at = @At("RETURN"))
    private void astra$badge(Entity entity, EntityRenderState state, float tickProgress, CallbackInfo ci) {
        if (entity instanceof PlayerEntity p && state.displayName != null
            && AstraClient.isAstraUser(p.getName().getString())) {
            state.displayName = Text.empty()
                .append(Text.literal("\u2726 ASTRA ").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD))
                .append(state.displayName);
        }
    }
}
