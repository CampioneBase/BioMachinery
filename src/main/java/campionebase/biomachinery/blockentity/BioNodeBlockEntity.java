package campionebase.biomachinery.blockentity;

import campionebase.biomachinery.api.IBioConnector;
import campionebase.biomachinery.api.IBioMachine;
import campionebase.biomachinery.registry.ErErBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BioNodeBlockEntity extends BlockEntity implements IBioConnector {
    public BioNodeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ErErBlockEntities.BIO_NODE.get(), pos, blockState);
    }

    @Override
    public @Nullable IBioMachine getMachine() {
        return null;
    }
}
