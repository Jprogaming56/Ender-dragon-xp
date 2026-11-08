package com.example.dragonxp;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Makes the Ender Dragon drop the full first-kill XP (12000) on every kill.
 * Vanilla only drops 500 XP after the first kill, so we add the missing 11500
 * at the end of the death animation (200 ticks after death), like vanilla does.
 */
@Mod("dragonxp")
public class DragonXpMod {
    private static final int FULL_XP = 12000;
    private static final int VANILLA_REPEAT_XP = 500;
    private static final int DEATH_ANIMATION_TICKS = 200;

    private static class Pending {
        final ServerLevel level;
        final Vec3 pos;
        int ticksLeft = DEATH_ANIMATION_TICKS;

        Pending(ServerLevel level, Vec3 pos) {
            this.level = level;
            this.pos = pos;
        }
    }

    private final List<Pending> pending = new ArrayList<>();

    public DragonXpMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDragonDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon dragon)) return;
        if (!(dragon.level() instanceof ServerLevel level)) return;

        EndDragonFight fight = dragon.getDragonFight();
        // Only needed when vanilla would give the reduced amount.
        if (fight == null || !fight.hasPreviouslyKilledDragon()) return;

        pending.add(new Pending(level, dragon.position()));
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || pending.isEmpty()) return;

        Iterator<Pending> it = pending.iterator();
        while (it.hasNext()) {
            Pending p = it.next();
            if (--p.ticksLeft <= 0) {
                ExperienceOrb.award(p.level, p.pos, FULL_XP - VANILLA_REPEAT_XP);
                it.remove();
            }
        }
    }
}
