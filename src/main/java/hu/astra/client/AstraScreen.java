package hu.astra.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class AstraScreen extends Screen {
    public AstraScreen() { super(Component.literal("Astra Client")); }

    @Override
    protected void init() {
        int x = width / 2 - 100, y = height / 4;
        toggle("Fullbright", () -> AstraClient.fullbright, v -> AstraClient.fullbright = v, x, y);
        toggle("Keystrokes", () -> AstraClient.keystrokes, v -> AstraClient.keystrokes = v, x, y + 24);
        toggle("CPS counter", () -> AstraClient.cps, v -> AstraClient.cps = v, x, y + 48);
        toggle("FPS counter", () -> AstraClient.fps, v -> AstraClient.fps = v, x, y + 72);
    }

    private void toggle(String name, BooleanSupplier get, Consumer<Boolean> set, int x, int y) {
        addRenderableWidget(Button.builder(label(name, get.getAsBoolean()), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(label(name, get.getAsBoolean()));
            AstraClient.save();
        }).bounds(x, y, 200, 20).build());
    }

    private static Component label(String name, boolean on) { return Component.literal(name + ": " + (on ? "ON" : "OFF")); }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
        g.drawCenteredString(font, "Astra Client - Settings", width / 2, height / 4 - 20, 0xFFFFFFFF);
        g.drawCenteredString(font, "Press Right Shift to close", width / 2, height / 4 + 104, 0xFF9AA0C0);
    }
}
