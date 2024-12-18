package me.TreeOfSelf.easyautocrafting.mixin;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.TreeOfSelf.easyautocrafting.CraftingDropper;
import me.TreeOfSelf.easyautocrafting.DropperCache;

import java.util.List;

@Mixin(value = DropperBlockEntity.class, priority = 1500)
public class DropperBlockEntityMixin extends DispenserBlockEntity implements DropperCache
{
    @Unique
    private CraftingRecipe cachedRecipe;

    @Unique
    private List<ItemStack> cachedIngredients;

    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        if (this.world instanceof ServerWorld && CraftingDropper.hasTableNextToBlock((ServerWorld)this.world, this.pos))
        {
            return super.isValid(slot, stack);
        } else {
            return false;
        }
    }

    /*@Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    public void eac_isValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir)
    {
        if (this.world instanceof ServerWorld && CraftingDropper.hasTableNextToBlock((ServerWorld)this.world, this.pos))
        {
            cir.setReturnValue(this.getStack(slot).isEmpty());
        }
    }*/

    @Override
    public CraftingRecipe eac_getRecipe()
    {
        return this.cachedRecipe;
    }

    @Override
    public void eac_setRecipe(CraftingRecipe r)
    {
        this.cachedRecipe = r;
    }

    @Override
    public List<ItemStack> eac_getIngredients()
    {
        return this.cachedIngredients;
    }

    @Override
    public void eac_setIngredients(List<ItemStack> r)
    {
        this.cachedIngredients = r;
    }

    @Override
    public void eac_clearCache()
    {
        this.cachedRecipe = null;
        this.cachedIngredients = null;
    }

    @SuppressWarnings("ConstantConditions")
    public DropperBlockEntityMixin()
    {
        super(null, null);
    }
}
