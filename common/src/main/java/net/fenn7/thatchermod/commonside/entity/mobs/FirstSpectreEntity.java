package net.fenn7.thatchermod.commonside.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.function.Consumer;

public class FirstSpectreEntity extends AbstractSpectreEntity {
    private static final int MAXIMUM_BASIC_ATTACKS = 3;
    private int currentBasicAttacks = 0;

    public FirstSpectreEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.95D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isProjectile()) {
            this.turnIncorporealFor(40);
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean canWalkOnFluid(FluidState fluidState) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
    }

    // brain logic
    @Override
    public BrainActivityGroup<AbstractSpectreEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<AbstractSpectreEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new TargetOrRetaliate<>(),
                new SetWalkTargetToAttackTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<AbstractSpectreEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new FirstApplicableBehaviour<AbstractSpectreEntity>(
                    new AnimatableMeleeAttack<>(5).whenStarting(
                            entity -> {
                                ++this.currentBasicAttacks;
                                System.out.println(this.currentBasicAttacks);
                            }
                    )
                ));
    }
    
    // animations
    private <E extends IAnimatable> PlayState swingPred(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack_basic", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        super.registerControllers(animationData);
        animationData.addAnimationController(new AnimationController<>(this, "attacking", 0, this::swingPred));
    }
}
