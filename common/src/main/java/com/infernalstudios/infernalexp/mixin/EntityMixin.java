package com.infernalstudios.infernalexp.mixin;

import com.infernalstudios.infernalexp.api.FireType;
import com.infernalstudios.infernalexp.api.FireTypeAccess;
import com.infernalstudios.infernalexp.module.ModFireTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements FireTypeAccess {
    @Shadow @Final protected SynchedEntityData entityData;

    @Shadow public abstract boolean isOnFire();

    @Shadow public abstract Level level();

    @Unique
    private static final EntityDataAccessor<String> FIRE_TYPE =
            SynchedEntityData.defineId(Entity.class, EntityDataSerializers.STRING);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<?> entityTypeIn, Level worldIn, CallbackInfo ci) {
        this.entityData.define(FIRE_TYPE, ModFireTypes.FIRE.getName().toString());
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putShort(Ljava/lang/String;S)V", ordinal = 0, shift = At.Shift.AFTER))
    private void writeCustomFires(CompoundTag tag, CallbackInfoReturnable<CompoundTag> ci) {
        tag.putString("fireType", this.infernalexp$getFireType().getName().toString());
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getShort(Ljava/lang/String;)S", ordinal = 0, shift = At.Shift.AFTER))
    private void readCustomFires(CompoundTag tag, CallbackInfo ci) {
        this.infernalexp$setFireType(FireType.getOrDefault(new ResourceLocation(tag.getString("fireType")), ModFireTypes.FIRE));
    }

    @Inject(method = "setSecondsOnFire", at = @At("HEAD"))
    private void setToDefaultFireType(int seconds, CallbackInfo ci) {
        this.infernalexp$setFireType(ModFireTypes.FIRE);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void infernalexp$resetFireTypeOnExtinguish(CallbackInfo ci) {
        if (!this.level().isClientSide && !this.isOnFire() && this.infernalexp$getFireType() != ModFireTypes.FIRE) {
            this.infernalexp$setFireType(ModFireTypes.FIRE);
        }
    }

    @Override
    public FireType infernalexp$getFireType() {
        return FireType.getOrDefault(new ResourceLocation(this.entityData.get(FIRE_TYPE)), ModFireTypes.FIRE);
    }

    @Override
    public void infernalexp$setFireType(FireType type) {
        this.entityData.set(FIRE_TYPE, type.getName().toString());
    }
}