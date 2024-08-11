package com.plusls.carpet.util.rule.dispenserCollectXp;

import com.plusls.carpet.PluslsCarpetAdditionSettings;
import com.plusls.carpet.util.dispenser.MyFallibleItemDispenserBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.phys.AABB;

import java.lang.reflect.Method;
import java.util.List;

//#if MC > 12001
//$$ import net.minecraft.core.dispenser.BlockSource;
//#else
import net.minecraft.core.BlockSource;
//#endif

public class GlassBottleDispenserBehavior extends MyFallibleItemDispenserBehavior {
    private final DefaultDispenseItemBehavior fallbackBehavior = new DefaultDispenseItemBehavior();

    public GlassBottleDispenserBehavior(DispenseItemBehavior oldDispenserBehavior) {
        super(oldDispenserBehavior);
    }

    public static void init() {
        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE,
                new GlassBottleDispenserBehavior(DispenserBlock.DISPENSER_REGISTRY.get(Items.GLASS_BOTTLE)));
    }

    private ItemStack replaceItem(BlockSource pointer, ItemStack oldItem, ItemStack newItem) {
        oldItem.shrink(1);
        if (oldItem.isEmpty()) {
            return newItem.copy();
        }

        try {
            // 获取 DispenserBlockEntity 类的实例
            DispenserBlockEntity dispenser = (DispenserBlockEntity) pointer.getEntity();

            // 尝试获取 addItem 方法
            Method addItemMethod = DispenserBlockEntity.class.getMethod("addItem", ItemStack.class);

            // 如果找到 addItem 方法，调用它
            if ((int) addItemMethod.invoke(dispenser, newItem.copy()) < 0) {
                this.fallbackBehavior.dispense(pointer, newItem.copy());
            }
        } catch (NoSuchMethodException e) {
            // 如果没有找到 addItem 方法，尝试获取 insertItem 方法
            try {
                Method insertItemMethod = DispenserBlockEntity.class.getMethod("insertItem", int.class, ItemStack.class, boolean.class);
                if ((int) insertItemMethod.invoke(pointer.getEntity(), 0, newItem.copy(), false) < 0) {
                    this.fallbackBehavior.dispense(pointer, newItem.copy());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // 处理方法未找到或反射调用失败的情况
                this.fallbackBehavior.dispense(pointer, newItem.copy());
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            // 处理反射调用失败的情况
            this.fallbackBehavior.dispense(pointer, newItem.copy());
        }

        return oldItem;
    }

    @Override
    public ItemStack dispenseSilently(BlockSource pointer, ItemStack itemStack) {
        if (!PluslsCarpetAdditionSettings.dispenserCollectXp) {
            return itemStack;
        }
        BlockPos faceBlockPos = pointer.getPos().relative(pointer.getBlockState().getValue(DispenserBlock.FACING));

        List<ExperienceOrb> xpEntityList = pointer.getLevel().getEntitiesOfClass(ExperienceOrb.class,
                new AABB(faceBlockPos), Entity::isAlive);

        int currentXp = 0;
        // 运算次数不多，所以多循环几次也无所谓（放弃思考.jpg
        for (ExperienceOrb xpEntity : xpEntityList) {
            //#if MC > 11605
            //$$ for (; xpEntity.count > 0; --xpEntity.count) {
            //#else
            for (; xpEntity.value > 0; --xpEntity.value) {
            //#endif
                currentXp += xpEntity.getValue();
                // 有残留经验也无所谓，直接把经验球销毁
                // 付出点代价很合理
                //#if MC > 11605
                //$$ if (xpEntity.count == 1) {
                //$$     xpEntity.discard();
                //#else
                if (xpEntity.value == 1) {
                    xpEntity.remove();
                //#endif
                }
                if (currentXp >= 8) {
                    setSuccess(true);
                    return this.replaceItem(pointer, itemStack, new ItemStack(Items.EXPERIENCE_BOTTLE));
                }
            }
        }
        return itemStack;
    }
}