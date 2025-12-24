package com.infernalstudios.infernalexp.entities.ai;

import com.infernalstudios.infernalexp.entities.VolineEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;
import java.util.List;

public class EatItemsGoal extends Goal {
    private final VolineEntity mob;
    private ItemEntity targetItem;
    private int eatAnimationTick;

    public EatItemsGoal(VolineEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<ItemEntity> list = this.mob.level().getEntitiesOfClass(ItemEntity.class,
                this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
                item -> mob.wantsToEat(item.getItem()));

        if (!list.isEmpty()) {
            this.targetItem = list.get(0);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if (this.targetItem == null || !this.targetItem.isAlive()) {
            this.stop();
            return;
        }

        if (this.eatAnimationTick > 0) {
            this.eatAnimationTick--;

            if (this.eatAnimationTick % 5 == 0) {
                this.mob.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            }

            if (this.eatAnimationTick == 0) {
                this.mob.ate(this.targetItem.getItem());
                this.targetItem.discard();
                this.stop();
            }
            return;
        }

        this.mob.getNavigation().moveTo(this.targetItem, 1.2D);
        if (this.mob.distanceToSqr(this.targetItem) < 1.0D) {
            this.mob.playEatingAnimation();
            this.eatAnimationTick = 20;
        }
    }

    @Override
    public void stop() {
        this.eatAnimationTick = 0;
        super.stop();
    }
}