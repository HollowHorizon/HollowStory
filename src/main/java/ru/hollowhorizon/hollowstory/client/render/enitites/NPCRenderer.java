package ru.hollowhorizon.hollowstory.client.render.enitites;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import ru.hollowhorizon.hc.HollowCore;
import ru.hollowhorizon.hollowstory.common.entities.NPCEntity;
import ru.hollowhorizon.hollowstory.common.entities.PrivateNPCEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class NPCRenderer extends EntityRenderer<NPCEntity> {

    public NPCRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public boolean shouldRender(NPCEntity entity, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        if (entity instanceof PrivateNPCEntity) {
            Optional<UUID> callerId = ((PrivateNPCEntity) entity).getCaller();
            if (callerId.isPresent() && Minecraft.getInstance().player.getUUID().equals(callerId.get())) return false;
        }
        return super.shouldRender(entity, p_225626_2_, p_225626_3_, p_225626_5_, p_225626_7_);
    }

    @Override
    public void render(NPCEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packetLight) {
        super.render(entity, entityYaw, partialTicks, stack, buffer, packetLight);

        if (entity.getPuppet() != null) {
            Entity puppet = entity.getPuppet();

            EntityRenderer<? super Entity> manager = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(puppet);

            puppet.tickCount = entity.tickCount;

            puppet.setPos(entity.getX(), entity.getY(), entity.getZ());
            puppet.setDeltaMovement(entity.getDeltaMovement());

            puppet.xo = entity.xo;
            puppet.yo = entity.yo;
            puppet.zo = entity.zo;
            puppet.xOld = entity.xOld;
            puppet.yOld = entity.yOld;
            puppet.zOld = entity.zOld;

            puppet.yRot = entity.yRot;
            puppet.xRot = entity.xRot;
            puppet.xRotO = entity.xRotO;
            puppet.yRotO = entity.yRotO;

            puppet.setShiftKeyDown(entity.isShiftKeyDown());
            puppet.setSprinting(entity.isSprinting());

            if (puppet instanceof LivingEntity) {
                LivingEntity livingPuppet = (LivingEntity) puppet;

                livingPuppet.walkDist = entity.walkDist;
                livingPuppet.walkDistO = entity.walkDistO;

                livingPuppet.swinging = entity.swinging;
                livingPuppet.swingTime = entity.swingTime;
                livingPuppet.swingingArm = entity.swingingArm;

                livingPuppet.hurtTime = entity.hurtTime;
                livingPuppet.deathTime = entity.deathTime;
                livingPuppet.hurtDir = entity.hurtDir;
                livingPuppet.hurtMarked = entity.hurtMarked;

                livingPuppet.blocksBuilding = entity.blocksBuilding;

                livingPuppet.animationPosition = entity.animationPosition;
                livingPuppet.animationSpeedOld = entity.animationSpeedOld;
                livingPuppet.animationSpeed = entity.animationSpeed;
                livingPuppet.attackAnim = entity.attackAnim;

                livingPuppet.yBodyRot = entity.yBodyRot;
                livingPuppet.yBodyRotO = entity.yBodyRotO;

                livingPuppet.yHeadRot = entity.yHeadRot;
                livingPuppet.yHeadRotO = entity.yHeadRotO;

                Arrays.stream(EquipmentSlotType.values()).forEach(slot -> livingPuppet.setItemSlot(slot, entity.getItemBySlot(slot)));

                livingPuppet.setPose(entity.getPose());
            }

            manager.render(puppet, entityYaw, partialTicks, stack, renderType -> {
                final IVertexBuilder builder;

                if (renderType instanceof RenderType.Type) {
                    Optional<ResourceLocation> rs = ((RenderType.Type) renderType).state.textureState.texture;

                    if (rs.isPresent()) {
                        RenderType newType = RenderType.entityTranslucent(rs.get());

                        if (!newType.format().equals(renderType.format()))
                            return buffer.getBuffer(renderType);

                        builder = buffer.getBuffer(newType);
                    } else {
                        HollowCore.LOGGER.info("No texture found for render type: {}", renderType);
                        return buffer.getBuffer(renderType);
                    }
                } else {
                    HollowCore.LOGGER.info("Render type is not instance of RenderType.Type: {}", renderType);
                    return buffer.getBuffer(renderType);
                }

                return builder;
            }, packetLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity entity) {
        return null;
    }
}
