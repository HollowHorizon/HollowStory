package ru.hollowhorizon.hollowstory.common.entities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import ru.hollowhorizon.hollowstory.common.npcs.NPCSettings;

import java.util.Optional;
import java.util.UUID;

public class PrivateNPCEntity extends NPCEntity {
    private static final DataParameter<Optional<UUID>> DATA_CALLER_UUID = EntityDataManager.defineId(PrivateNPCEntity.class, DataSerializers.OPTIONAL_UUID);

    public PrivateNPCEntity(NPCSettings options, World level) {
        super(options, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CALLER_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
    }

    public Optional<UUID> getCaller() {
        return this.entityData.get(DATA_CALLER_UUID);
    }

    public void setCaller(PlayerEntity player) {
        this.entityData.set(DATA_CALLER_UUID, Optional.of(player.getUUID()));
    }
}
