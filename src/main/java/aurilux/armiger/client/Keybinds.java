package aurilux.armiger.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;


public class Keybinds {
    public static final KeyBinding openArmiger = createKey("openArmiger", GLFW_KEY_G);

    private static KeyBinding createKey(String desc, int keycode) {
        return new KeyBinding("key.armiger." + desc, keycode, "keybind.armiger.category");
    }

    public static void init() {
        ClientRegistry.registerKeyBinding(openArmiger);
    }
}
