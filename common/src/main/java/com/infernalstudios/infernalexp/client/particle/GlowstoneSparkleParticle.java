package com.infernalstudios.infernalexp.client.particle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class GlowstoneSparkleParticle extends TextureSheetParticle {

    private GlowstoneSparkleParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.xd = ((motionX + random.nextFloat() - 0.5) / 3) / 4;
        this.yd = ((motionY + random.nextFloat()) / 5) / 4;
        this.zd = ((motionZ + random.nextFloat() - 0.5) / 3) / 4;
        this.quadSize *= 0.75F;
        this.lifetime = 120 + this.random.nextInt(12);
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.x += this.xd;
            this.y += this.yd;
            this.z += this.zd;
            if (this.xd > 0.1) this.xd *= 0.9;
            if (this.yd > 0.1) this.yd *= 0.9;
            if (this.zd > 0.1) this.zd *= 0.9;
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public int getLightColor(float partialTick) {
        return 15728880;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GlowstoneSparkleParticle glowstoneSparkleParticle = new GlowstoneSparkleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            glowstoneSparkleParticle.pickSprite(this.spriteSet);
            glowstoneSparkleParticle.scale(0.5F);
            return glowstoneSparkleParticle;
        }
    }
}