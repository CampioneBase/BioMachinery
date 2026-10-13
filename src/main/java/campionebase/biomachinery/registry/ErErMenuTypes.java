package campionebase.biomachinery.registry;

import campionebase.biomachinery.Biomachinery;
import campionebase.biomachinery.inventory.BioControllerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErErMenuTypes {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Biomachinery.MODID);

    public static final RegistryObject<MenuType<BioControllerMenu>> BIO_CONTROLLER_MENU =
            REGISTER.register("bio_controller", () -> IForgeMenuType.create(BioControllerMenu::new));
}
