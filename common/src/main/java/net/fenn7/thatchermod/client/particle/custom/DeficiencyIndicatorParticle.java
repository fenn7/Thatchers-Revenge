package net.fenn7.thatchermod.client.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class DeficiencyIndicatorParticle extends SpriteBillboardParticle {
    protected DeficiencyIndicatorParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                                          SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.0F;
        this.scale(1.0F);
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1 / (float) maxAge) * age + 1);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            DeficiencyIndicatorParticle dip = new DeficiencyIndicatorParticle(level, x, y, z, this.sprites, dx, dy, dz);
            dip.setAlpha(1.0F);
            dip.setVelocity(0, 0, 0);
            dip.setMaxAge(1);
            return dip;
        }
    }
}
