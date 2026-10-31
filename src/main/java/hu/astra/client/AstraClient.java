package hu.astra.client;

import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.lwjgl.glfw.GLFW;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AstraClient implements ClientModInitializer {
    public static boolean fullbright, keystrokes = true, cps = true, fps = true;
    private static final Set<String> USERS = ConcurrentHashMap.newKeySet();
    private static boolean wasDown, fbApplied;

    @Override
    public void onInitializeClient() {
        load();
        startUserFetch();
        HudRenderCallback.EVENT.register((graphics, tickCounter) -> AstraHud.draw(graphics));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            long window = GLFW.glfwGetCurrentContext();
            boolean down = window != 0 && GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (down && !wasDown) {
                if (mc.screen == null && mc.level != null) mc.setScreen(new AstraScreen());
                else if (mc.screen instanceof AstraScreen) mc.setScreen(null);
            }
            wasDown = down;
            if (mc.player == null) return;
            if (fullbright) {
                mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000, 0, false, false));
                fbApplied = true;
            } else if (fbApplied) {
                mc.player.removeEffect(MobEffects.NIGHT_VISION);
                fbApplied = false;
            }
        });
    }

    public static boolean isAstraUser(String name) { return USERS.contains(name.toLowerCase()); }

    private static Path cfg() { return FabricLoader.getInstance().getConfigDir().resolve("astra-client.properties"); }

    public static void load() {
        try (var in = Files.newInputStream(cfg())) {
            Properties p = new Properties(); p.load(in);
            fullbright = Boolean.parseBoolean(p.getProperty("fullbright", "false"));
            keystrokes = Boolean.parseBoolean(p.getProperty("keystrokes", "true"));
            cps = Boolean.parseBoolean(p.getProperty("cps", "true"));
            fps = Boolean.parseBoolean(p.getProperty("fps", "true"));
        } catch (Exception ignored) {}
    }

    public static void save() {
        try (var out = Files.newOutputStream(cfg())) {
            Properties p = new Properties();
            p.setProperty("fullbright", "" + fullbright); p.setProperty("keystrokes", "" + keystrokes);
            p.setProperty("cps", "" + cps); p.setProperty("fps", "" + fps);
            p.store(out, "Astra Client");
        } catch (Exception ignored) {}
    }

    // The launcher writes the server address into config/astra-server.txt
    private static void startUserFetch() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    String server = Files.readString(FabricLoader.getInstance().getConfigDir().resolve("astra-server.txt")).trim();
                    HttpResponse<String> r = HttpClient.newHttpClient().send(
                        HttpRequest.newBuilder(URI.create(server + "/users")).build(), HttpResponse.BodyHandlers.ofString());
                    var names = JsonParser.parseString(r.body()).getAsJsonArray();
                    USERS.clear();
                    names.forEach(e -> USERS.add(e.getAsString().toLowerCase()));
                } catch (Exception ignored) {}
                try { Thread.sleep(60_000); } catch (InterruptedException e) { return; }
            }
        }, "astra-users");
        t.setDaemon(true);
        t.start();
    }
}
