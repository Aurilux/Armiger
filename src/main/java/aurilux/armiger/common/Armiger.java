package aurilux.armiger.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Armiger.MOD_ID, name = "Armiger", version = Armiger.MOD_VERSION)
public class Armiger {
    public static final String MOD_ID = "Armiger";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());
    public static final SimpleNetworkWrapper network = new SimpleNetworkWrapper(MOD_ID);

    @Mod.Instance(MOD_ID)
    public static Armiger instance;

    @SidedProxy(
            clientSide = "aurilux.armiger.client.ClientProxy",
            serverSide = "aurilux.armiger.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @Mod.EventHandler
    public void preInit(FMLPostInitializationEvent e) {

    }
}