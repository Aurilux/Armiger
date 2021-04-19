package aurilux.armiger.common;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.api.IArmiger;
import aurilux.armiger.client.Keybinds;
import aurilux.armiger.client.gui.ArmigerScreen;
import aurilux.armiger.common.capability.ArmigerCapImpl;
import aurilux.armiger.common.command.CommandArmiger;
import aurilux.armiger.common.core.ArmigerConfig;
import aurilux.armiger.common.core.ModObjects;
import aurilux.armiger.common.network.PacketHandler;
import aurilux.armiger.common.util.CapabilityHelper;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArmigerAPI.MOD_ID)
public class ArmigerMod {
    public static final Logger LOG = LogManager.getLogger(ArmigerAPI.MOD_ID.toUpperCase());

    public ArmigerMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ArmigerConfig.COMMON_SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        ModObjects.register(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::registerCommands);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityHelper.registerDummyCapability(IArmiger.class, ArmigerCapImpl::new);
        PacketHandler.init();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        Keybinds.init();
        ScreenManager.registerFactory(ModObjects.ARMIGER_CONTAINER.get(), ArmigerScreen::new);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandArmiger.register(event.getDispatcher());
    }
}