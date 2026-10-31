package hu.astra.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class AstraScreen extends Screen {
    public AstraScreen() { super(Text.literal("Astra Client")); }

    @Override
    protected void init() {
        int x = width / 2 - 100, y = height / 4;
        toggle("Fullbright", () -> AstraClient.fullbright, v -> AstraClient.fullbright = v, x, y);
        toggle("Keystrokes", () -> AstraClient.keystrokes, v -> AstraClient.keystrokes = v, x, y + 24);
        toggle("CPS counter", () -> AstraClient.cps, v -> AstraClient.cps = v, x, y + 48);
        toggle("FPS counter", () -> AstraClient.fps, v -> AstraClient.fps = v, x, y + 72);
    }

    private void toggle(String name, BooleanSupplier get, Consumer<Boolean> set, int x, int y) {
        addDrawableChild(ButtonWidget.builder(label(name, get.getAsBoolean()), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(label(name, get.getAsBoolean()));
            AstraClient.save();
        }).dimensions(x, y, 200, 20).build());
    }

    private static Text label(String name, boolean on) { return Text.literal(name + ": " + (on ? "ON" : "OFF")); }

    @Override
    public void render(DrawContext c, int mx, int my, float delta) {
        super.render(c, mx, my, delta);
        c.drawCenteredTextWithShadow(textRenderer, "Astra Client - Settings", width / 2, height / 4 - 20, 0xFFFFFFFF);
        c.drawCenteredTextWithShadow(textRenderer, "Press Right Shift to close", width / 2, height / 4 + 104, 0xFF9AA0C0);
    }

    @Override
    public boolean shouldPause() { return false; }
}
