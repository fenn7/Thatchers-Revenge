package net.fenn7.thatchermod.commonside.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.List;
import java.util.Set;

public abstract class AbstractSpectreEntity extends HostileEntity implements IAnimatable, SmartBrainOwner<AbstractSpectreEntity> {
    protected final AnimationFactory factory = new AnimationFactory(this);
    protected final Set<DamageSource> weaknesses;
    protected int incorporealTicks = 0;
    protected AnimatedGeoModel<? extends AbstractSpectreEntity> geoModel;

    protected AbstractSpectreEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.weaknesses = Set.of(DamageSource.ON_FIRE, DamageSource.GENERIC, DamageSource.LIGHTNING_BOLT,
                DamageSource.OUT_OF_WORLD, DamageSource.MAGIC, DamageSource.WITHER, DamageSource.DRAGON_BREATH);
    }

    @Override
    public boolean hasNoDrag() { return false; }

    @Override
    public boolean hasNoGravity() { return true; }

    @Override
    public boolean collides() { return this.incorporealTicks == 0; }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;

        if (this.incorporealTicks > 0) {
            this.incorporealTicks--;
            if (this.incorporealTicks == 0) {
                this.setInvulnerable(false);
            }
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
        this.setInvulnerable(true);
    }

    public int getIncorporealTicks() {
        return this.incorporealTicks;
    }

    public AnimatedGeoModel<? extends AbstractSpectreEntity> getModel() { return this.geoModel; }

    public void setModel(AnimatedGeoModel<? extends AbstractSpectreEntity> geoModel) { this.geoModel = geoModel; }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public MoveControl getMoveControl() {
        return this.moveControl;
    }

    // brain logic
    @Override
    protected void mobTick() {
        this.tickBrain(this);
        super.mobTick();
    }

    @Override
    protected Brain.Profile<?> createBrainProfile() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<ExtendedSensor<AbstractSpectreEntity>> getSensors() {
        return List.of(
                new HurtBySensor<>(),
                new NearbyLivingEntitySensor<AbstractSpectreEntity>()
                        .setPredicate((target, entity) -> target instanceof CowEntity || target instanceof IronGolemEntity));
    }

    // animations
    private <E extends IAnimatable> PlayState gearPred(AnimationEvent<E> event) {
        var gearSpeed = this.isAttacking() ? "animation.model.gears_fast" : "animation.model.gears_slow";
        event.getController().setAnimation(new AnimationBuilder().addAnimation(gearSpeed, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState gnashPred(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(
                "animation.model.gnashing", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState upperTargetPred(AnimationEvent<E> event) {
        if (this.isAttacking() || event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(
                    "animation.model.targeting_upper", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState lowerTargetPred(AnimationEvent<E> event) {
        if (this.isAttacking() || event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(
                    "animation.model.targeting_lower", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "gears", 0, this::gearPred));
        animationData.addAnimationController(new AnimationController<>(this, "gnash", 0, this::gnashPred));
        animationData.addAnimationController(new AnimationController<>(this, "lowerTarget", 0, this::lowerTargetPred));
        animationData.addAnimationController(new AnimationController<>(this, "upperTarget", 0, this::upperTargetPred));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
