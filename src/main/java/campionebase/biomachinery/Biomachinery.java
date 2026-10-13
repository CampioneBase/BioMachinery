package campionebase.biomachinery;

import campionebase.biomachinery.network.BioMachineryNetwork;
import campionebase.biomachinery.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Biomachinery.MODID)
public class Biomachinery {
    public static final String MODID = "biomachinery";
    public static final Logger LOGGER = LogManager.getLogger(Biomachinery.class);

    public Biomachinery(final FMLJavaModLoadingContext context) {
        this.init(context);
        context.getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void init(final FMLJavaModLoadingContext context){
        IEventBus bus = context.getModEventBus();
        ErErEntities.REGISTER.register(bus);
        ErErBlocks.REGISTER.register(bus);
        ErErBlockEntities.REGISTER.register(bus);
        ErErItems.REGISTER.register(bus);
        ErErTabs.REGISTER.register(bus);
        ErErMenuTypes.REGISTER.register(bus);
        ErErFluids.register(bus);

        BioMachineTypes.REGISTER.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        BioMachineryNetwork.register();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
