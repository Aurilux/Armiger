package aurilux.armiger.common.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ArmigerConfig {
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<Client, ForgeConfigSpec> specPairClient = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();

        final Pair<Common, ForgeConfigSpec> specPairCommon = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPairCommon.getRight();
        COMMON = specPairCommon.getLeft();

        final Pair<Server, ForgeConfigSpec> specPairServer = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPairServer.getRight();
        SERVER = specPairServer.getLeft();
    }

    // For client only options. Primarily rendering.
    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {
            //NOOP
        }
    }

    // These options will be available for single-player and server worlds. Most config options should be here.
    public static class Common {
        public final ForgeConfigSpec.IntValue setUnlockCost;

        public Common(ForgeConfigSpec.Builder builder) {
            setUnlockCost = builder
                    .comment("How many levels it costs to unlock the next armiger set. A value of 0 means all armiger sets are unlocked by default.")
                    .defineInRange("setUnlockCost", 30, 0, Integer.MAX_VALUE);
        }
    }

    // For server only options. These options will not be available on single-player worlds.
    public static class Server {
        public Server(ForgeConfigSpec.Builder builder) {
        }
    }
}
