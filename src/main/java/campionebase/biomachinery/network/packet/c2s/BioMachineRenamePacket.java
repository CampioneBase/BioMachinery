package campionebase.biomachinery.network.packet.c2s;

import campionebase.biomachinery.api.IBioMachine;
import campionebase.biomachinery.item.RenameTagItem;
import campionebase.biomachinery.network.BioMachineryNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record BioMachineRenamePacket(BlockPos machinePos, String name) {

    public static void encode(BioMachineRenamePacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.machinePos);
        buf.writeUtf(packet.name);
    }

    public static BioMachineRenamePacket decode(FriendlyByteBuf buf) {
        return new BioMachineRenamePacket(
                buf.readBlockPos(),
                buf.readUtf()
        );
    }

    public static void handle(BioMachineRenamePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();
        if (sender == null) return;
        context.enqueueWork(() -> {
            ServerLevel level = sender.serverLevel();
            if (!level.hasChunkAt(packet.machinePos)) {
                return;
            }
            if (sender.distanceToSqr(packet.machinePos.getCenter()) > 64.0D) {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from renaming bio-machine[{}]: player is too far away",
                        sender.getName().getString(), packet.machinePos
                );
                return;
            }
            if (!(sender.getMainHandItem().getItem() instanceof RenameTagItem)
                    && !(sender.getOffhandItem().getItem() instanceof RenameTagItem)) {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from renaming bio-machine[{}]: rename tag is not held",
                        sender.getName().getString(), packet.machinePos
                );
                return;
            }
            if (level.getBlockEntity(packet.machinePos) instanceof IBioMachine machine) {
                machine.setName(packet.name.length() > 32 ? packet.name.substring(0, 32) : packet.name);
            } else {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from renaming bio-machine[{}]: bio-machine invalid",
                        sender.getName().getString(), packet.machinePos
                );
            }
        });
        context.setPacketHandled(true);
    }
}
