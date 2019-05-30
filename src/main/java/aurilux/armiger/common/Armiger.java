package aurilux.armiger.common;

import aurilux.armiger.common.capability.ArmigerImpl;
import aurilux.armiger.common.network.PacketDispatcher;
import aurilux.armiger.common.network.messages.PacketOpenArmigerGui;
import aurilux.armiger.common.network.messages.PacketSyncArmorSlots;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Armiger.MOD_ID, name = "Armiger", version = Armiger.MOD_VERSION)
public class Armiger {
    public static final String MOD_ID = "armiger";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());

    @Mod.Instance(value=Armiger.MOD_ID)
    public static Armiger instance;

    @SidedProxy(
            clientSide = "aurilux.armiger.client.ClientProxy",
            serverSide = "aurilux.armiger.common.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Run before anything else.
     * Read your config.
     * Register blocks, items, tile entities, and entities.
     * Assign ore dictionary names.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        ArmigerImpl.register();
        proxy.registerHandlers();
    }

    /**
     * Register world generators, recipes, event handlers, network (network).
     * Send IMC messages
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

        PacketDispatcher.registerServerMessage(PacketOpenArmigerGui.class);
        PacketDispatcher.registerServerMessage(PacketSyncArmorSlots.class);
    }

    /**
     * Handle mod compatibility or anything which depends on other mods’ init phases being finished.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    }

    public static void console(Object o, boolean warning) {
        logger.log(warning ? Level.WARN : Level.INFO, o.toString());
    }

    public static void console(Object o) {
        console(o, false);
    }
}