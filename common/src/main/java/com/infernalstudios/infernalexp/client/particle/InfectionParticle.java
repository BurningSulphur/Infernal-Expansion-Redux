package com.infernalstudios.infernalexp.client.particle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class InfectionParticle extends TextureSheetParticle {
    private final SpriteSet spriteWithAge;

    protected InfectionParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet spriteWithAge) {
        super(world, x, y, z, 0.5 - Math.random(), motionY, 0.5 - Math.random());

        this.spriteWithAge = spriteWithAge;
        this.yd *= -0.1;

        if (motionX == 0 && motionY == 0) {
            this.xd *= 0.1;
            this.zd *= 0.1;
        }

        this.quadSize *= 0.75;
        this.lifetime = (int) (16 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteWithAge);

        this.rCol = 0.5f;
        this.gCol = (float) Math.random() * 0.1f;
        this.bCol = (float) Math.random() * 0.1f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.spriteWithAge);
            this.yd -= 0.001;
            this.move(this.xd, this.yd, this.zd);

            if (this.y == this.yo) {
                this.xd *= 1.1;
                this.zd *= 1.1;
            }

            this.xd *= 0.96;
            this.yd *= 0.96;
            this.zd *= 0.96;

            if (this.onGround) {
                this.xd *= 0.7;
                this.zd *= 0.7;
            }

            this.rCol = 0.7f + (0.2f * Mth.sin(this.age / 10f));
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            InfectionParticle infectionParticle = new InfectionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
//            infectionParticle.setColor(0.8f, 0, 0);

            return infectionParticle;
        }
    }
}