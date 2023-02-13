package net.fenn7.thatchermod.commonside.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Set;

public abstract class AbstractSpectreEntity extends HostileEntity implements IAnimatable {
    protected final AnimationFactory factory = new AnimationFactory(this);
    protected final Set<DamageSource> weaknesses;
    protected int incorporealTicks = 0;

    protected AbstractSpectreEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.weaknesses = Set.of(DamageSource.IN_FIRE, DamageSource.ON_FIRE, DamageSource.LAVA, DamageSource.GENERIC,
                DamageSource.OUT_OF_WORLD, DamageSource.MAGIC, DamageSource.WITHER, DamageSource.DRAGON_BREATH);
    }

    @Override
    protected void initGoals() {
    }

    @Override
    public boolean hasNoDrag() { return true; }

    @Override
    public boolean hasNoGravity() { return true; }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;

        this.incorporealTicks--;
        if (this.incorporealTicks == 0) {
            this.setInvulnerable(false);
            this.setInvisible(false);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return this.weaknesses.contains(source) &&
                source == DamageSource.GENERIC ? super.damage(source, amount * 0.8F) : super.damage(source, amount * 1.2F);
    }

    protected void turnIncorporealFor(int incorporealTicks) {
        this.world.sendEntityStatus(this, (byte) 60);
        this.world.sendEntityStatus(this, (byte) 46);
        this.incorporealTicks = incorporealTicks;
        this.setInvisible(true);
        this.setInvulnerable(true);
    }

    public int getIncorporealTicks() {
        return this.incorporealTicks;
    }

    // animations
    private <E extends IAnimatable> PlayState gearPred(AnimationEvent<E> event) {
        var gearSpeed = this.isAttacking() ? "animation.model.gears_fast" : "animation.model.gears_slow";
        event.getController().setAnimation(new AnimationBuilder().addAnimation(gearSpeed, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState gnashPred(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.gnashing", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "gears", 0, this::gearPred));
        animationData.addAnimationController(new AnimationController(this, "gnash", 0, this::gnashPred));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
