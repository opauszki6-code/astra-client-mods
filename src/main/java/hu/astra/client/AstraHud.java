package hu.astra.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

public class AstraHud {
    private static final Deque<Long> LEFT = new ArrayDeque<>(), RIGHT = new ArrayDeque<>();
    private static boolean lWas, rWas;

    public static void draw(DrawContext c) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;
        long w = mc.getWindow().getHandle(), now = System.currentTimeMillis();
        boolean l = GLFW.glfwGetMouseButton(w, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean r = GLFW.glfwGetMouseButton(w, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if (l && !lWas) LEFT.add(now);
        if (r && !rWas) RIGHT.add(now);
        lWas = l; rWas = r;
        LEFT.removeIf(t -> now - t > 1000);
        RIGHT.removeIf(t -> now - t > 1000);

        TextRenderer tr = mc.textRenderer;
        int y = 4;
        if (AstraClient.fps) { c.drawTextWithShadow(tr, mc.getCurrentFps() + " FPS", 4, y, 0xFFFFFFFF); y += 12; }
        if (AstraClient.cps) { c.drawTextWithShadow(tr, LEFT.size() + " | " + RIGHT.size() + " CPS", 4, y, 0xFFFFFFFF); y += 14; }
        if (!AstraClient.keystrokes) return;
        key(c, tr, "W", 26, y, mc.options.forwardKey.isPressed());
        key(c, tr, "A", 4, y + 22, mc.options.leftKey.isPressed());
        key(c, tr, "S", 26, y + 22, mc.options.backKey.isPressed());
        key(c, tr, "D", 48, y + 22, mc.options.rightKey.isPressed());
        key(c, tr, "LMB", 4, y + 44, l, 32);
        key(c, tr, "RMB", 38, y + 44, r, 32);
    }

    private static void key(DrawContext c, TextRenderer tr, String n, int x, int y, boolean on) { key(c, tr, n, x, y, on, 20); }

    private static void key(DrawContext c, TextRenderer tr, String n, int x, int y, boolean on, int w) {
        c.fill(x, y, x + w, y + 20, on ? 0xB0FFFFFF : 0x80000000);
        c.drawCenteredTextWithShadow(tr, n, x + w / 2, y + 6, on ? 0xFF000000 : 0xFFFFFFFF);
    }
}
