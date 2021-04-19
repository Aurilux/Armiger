package aurilux.armiger.common.command;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.common.network.PacketHandler;
import aurilux.armiger.common.network.messages.PacketSyncArmiger;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandArmiger {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(ArmigerAPI.MOD_ID)
                        .requires(s -> s.hasPermissionLevel(2))
                        .then(Commands.literal("reset")
                            .executes(CommandArmiger::run))
        );
    }

    private static int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ArmigerAPI.getCapability(player).ifPresent(a -> {
            // We ignore the first 4 slots as those are available by default, then we check the remaining slots
            for (int i = 4; i < 11; i++) {
                player.dropItem(a.getStackInSlot(i), false, true);
                a.setStackInSlot(i, ItemStack.EMPTY);
            }
            a.unlockSets(1);
            PacketHandler.sendTo(new PacketSyncArmiger(a.serializeNBT()), player);
            context.getSource().sendFeedback(new TranslationTextComponent("commands.armiger.reset"), false);
        });
        return Command.SINGLE_SUCCESS;
    }
}
