package com.infernalstudios.infernalexp.entities;

import com.infernalstudios.infernalexp.entities.ai.EatItemsGoal;
import com.infernalstudios.infernalexp.module.ModBlocks;
import com.infernalstudios.infernalexp.module.ModItems;
import com.infernalstudios.infernalexp.module.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class VolineEntity extends Monster implements IBucketable {
    public static final EntityDataAccessor<Integer> MAGMA_CREAM_EATEN = SynchedEntityData.defineId(VolineEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(VolineEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_EATING = SynchedEntityData.defineId(VolineEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(VolineEntity.class, EntityDataSerializers.BOOLEAN);

    private final Predicate<LivingEntity> TARGET_CONDITION = entity -> {
        if (this.entityData.get(MAGMA_CREAM_EATEN) > 0) return false;

        return entity.hasEffect(MobEffects.FIRE_RESISTANCE) && !entity.isHolding(Items.MAGMA_CREAM);
    };

    public VolineEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGMA_CREAM_EATEN, 0);
        this.entityData.define(IS_SLEEPING, false);
        this.entityData.define(IS_EATING, false);
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.MAGMA_CREAM), false));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.2D, 1.5D, entity -> entity.isHolding(Items.SNOWBALL)));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Piglin.class, 8.0F, 1.0D, 1.2D, entity -> !entity.hasEffect(MobEffects.FIRE_RESISTANCE)));
        this.goalSelector.addGoal(4, new EatItemsGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, TARGET_CONDITION));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, TARGET_CONDITION));
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (MAGMA_CREAM_EATEN.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return super.getDimensions(pose).scale(this.getSizeFactor());
    }

    public float getSizeFactor() {
        return 1.0F + (this.entityData.get(MAGMA_CREAM_EATEN) * 0.2F);
    }

    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        // Prevent suffocation when growing
        this.setPos(this.getX(), this.getY(), this.getZ());
    }

    public boolean isEating() {
        return this.entityData.get(IS_EATING);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 8) {
            this.entityData.set(IS_EATING, false);
        } else if (id == 9) {
            this.entityData.set(IS_EATING, true);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean wantsToEat(ItemStack stack) {
        return stack.is(Items.MAGMA_CREAM) || stack.is(Items.GOLD_NUGGET) || stack.is(Items.GOLD_INGOT);
    }

    public boolean isSleeping() {
        return this.entityData.get(IS_SLEEPING);
    }

    public void ate(ItemStack stack) {
        if (stack.is(Items.MAGMA_CREAM)) {
            int eaten = this.entityData.get(MAGMA_CREAM_EATEN) + 1;
            this.entityData.set(MAGMA_CREAM_EATEN, eaten);

            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, eaten - 1));
            this.setPersistenceRequired();
            this.setTarget(null);

            if (eaten >= 3) {
                this.entityData.set(IS_SLEEPING, true);
                this.getNavigation().stop();
                this.setDeltaMovement(0, 0, 0);
            }
        }
    }

    @Override
    public boolean isFromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean isFromBucket) {
        this.entityData.set(FROM_BUCKET, isFromBucket);
    }

    @Override
    public void copyToStack(ItemStack stack) {
        IBucketable.copyToStack(this, stack);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("MagmaCreamEaten", this.entityData.get(MAGMA_CREAM_EATEN));
        tag.putFloat("Size", this.getSizeFactor());
    }

    @Override
    public void copyFromAdditional(CompoundTag compound) {
        IBucketable.copyFromAdditional(this, compound);
        if (compound.contains("MagmaCreamEaten")) {
            this.entityData.set(MAGMA_CREAM_EATEN, compound.getInt("MagmaCreamEaten"));
        }
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ModItems.VOLINE_BUCKET.get());
    }

    @Override
    public SoundEvent getBucketedSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return IBucketable.tryBucketEntity(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.VOLINE_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.VOLINE_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.VOLINE_HURT.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof Snowball) {
            if (this.entityData.get(IS_SLEEPING)) {
                this.transformToGeyser();
                return false;
            }
            amount += 3.0F;
        }
        return super.hurt(source, amount);
    }

    private void transformToGeyser() {
        if (!this.level().isClientSide) {
            BlockPos pos = this.blockPosition();
            if (this.level().getBlockState(pos).isAir()) {
                this.level().setBlock(pos, ModBlocks.VOLATILE_GEYSER.get().defaultBlockState(), 3);
            }
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("MagmaCreamEaten", this.entityData.get(MAGMA_CREAM_EATEN));
        tag.putBoolean("IsSleeping", this.entityData.get(IS_SLEEPING));
        tag.putBoolean("FromBucket", this.isFromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(MAGMA_CREAM_EATEN, tag.getInt("MagmaCreamEaten"));
        this.entityData.set(IS_SLEEPING, tag.getBoolean("IsSleeping"));
        this.setFromBucket(tag.getBoolean("FromBucket"));
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.entityData.get(IS_SLEEPING)) return null;
        return this.getFirstPassenger() instanceof LivingEntity entity ? entity : null;
    }

    @Override
    public void aiStep() {
        if (this.entityData.get(IS_SLEEPING)) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            return;
        }
        super.aiStep();
    }
}