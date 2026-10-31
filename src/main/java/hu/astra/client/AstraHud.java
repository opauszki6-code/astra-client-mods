package hu.astra.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayDeque;
import java.util.Deque;

public class AstraHud {
    private static final Deque<Long> LEFT = new ArrayDeque<>(), RIGHT = new ArrayDeque<>();
    private static boolean lWas, rWas;
    private static int frames, fpsShown;
    private static long fpsT = System.currentTimeMillis();

    public static void draw(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        long now = System.currentTimeMillis();
        frames++;
        if (now - fpsT >= 500) { fpsShown = (int) (frames * 1000L / (now - fpsT)); frames = 0; fpsT = now; }

        boolean l = mc.options.keyAttack.isDown(), r = mc.options.keyUse.isDown();
        if (l && !lWas) LEFT.add(now);
        if (r && !rWas) RIGHT.add(now);
        lWas = l; rWas = r;
        LEFT.removeIf(t -> now - t > 1000);
        RIGHT.removeIf(t -> now - t > 1000);

        Font font = mc.font;
        int y = 4;
        if (AstraClient.fps) { g.drawString(font, fpsShown + " FPS", 4, y, 0xFFFFFFFF); y += 12; }
        if (AstraClient.cps) { g.drawString(font, LEFT.size() + " | " + RIGHT.size() + " CPS", 4, y, 0xFFFFFFFF); y += 14; }
        if (!AstraClient.keystrokes) return;
        key(g, font, "W", 26, y, mc.options.keyUp.isDown(), 20);
        key(g, font, "A", 4, y + 22, mc.options.keyLeft.isDown(), 20);
        key(g, font, "S", 26, y + 22, mc.options.keyDown.isDown(), 20);
        key(g, font, "D", 48, y + 22, mc.options.keyRight.isDown(), 20);
        key(g, font, "LMB", 4, y + 44, l, 32);
        key(g, font, "RMB", 38, y + 44, r, 32);
    }

    private static void key(GuiGraphics g, Font font, String n, int x, int y, boolean on, int w) {
        g.fill(x, y, x + w, y + 20, on ? 0xB0FFFFFF : 0x80000000);
        g.drawCenteredString(font, n, x + w / 2, y + 6, on ? 0xFF000000 : 0xFFFFFFFF);
    }
}
