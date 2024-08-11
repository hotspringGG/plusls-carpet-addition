package com.plusls.carpet.mixin.rule.superLead;

import com.plusls.carpet.PluslsCarpetAdditionSettings;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractVillager.class)
public abstract class MixinAbstractVillager extends AgableMob {
    protected MixinAbstractVillager(EntityType<? extends AgableMob> entityType, Level level) {
        super(entityType, level);
    }

    //#if MC<12100
    @Inject(
            method = "canBeLeashed",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void postCanBeLeashed(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (PluslsCarpetAdditionSettings.superLead) {
            cir.setReturnValue(!isLeashed());
        }
    }
    //#else
    //#endif
}

