package aurilux.armiger.common.core;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.common.container.ArmigerContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModObjects {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ArmigerAPI.MOD_ID);

    public static final RegistryObject<ContainerType<ArmigerContainer>> ARMIGER_CONTAINER = CONTAINERS.register("armiger_container", () -> new ContainerType<>(ArmigerContainer::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}