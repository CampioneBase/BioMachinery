package campionebase.biomachinery.blockentity;

import campionebase.biomachinery.api.IBioNutritionTank;
import campionebase.biomachinery.registry.ErErBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BioNutritionTankBlockEntity extends AbstractBioMachineBlockEntity implements IBioNutritionTank {
    public BioNutritionTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(ErErBlockEntities.BIO_NUTRITION_TANK.get(), pos, blockState);
    }


}
