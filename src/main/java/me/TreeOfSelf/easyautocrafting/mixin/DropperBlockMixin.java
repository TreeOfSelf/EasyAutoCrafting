package me.TreeOfSelf.easyautocrafting.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.TreeOfSelf.easyautocrafting.CraftingDropper;

@Mixin(value = DispenserBlock.class, priority = 1500)
public class DropperBlockMixin extends Block
{
    @Inject(method = "dispense", at = @At("HEAD"), cancellable = true)
    protected void eac_dispense(ServerWorld world, BlockState state, BlockPos pos, CallbackInfo ci)
    {
        CraftingDropper.dispense(world, state, pos, ci);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return super.getComparatorOutput(state, world, pos);
    }

    @Inject(method = "getComparatorOutput", at = @At("HEAD"), cancellable = true)
    public void eac_getComparatorOutput(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (world instanceof ServerWorld serverWorld
                && CraftingDropper.hasTableNextToBlock(serverWorld, pos)
                && world.getBlockEntity(pos) instanceof DropperBlockEntity dropper)
        {
            int stackCount = 0;
            for (int i = 0; i < dropper.size(); i++) {
                if (!dropper.getStack(i).isEmpty()) {
                    stackCount++;
                }
            }
            cir.setReturnValue(stackCount);
        }
    }

    @Override
    @Unique(silent = true)
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify)
    {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    @Inject(method = { "neighborUpdate" }, at = @At("HEAD"))
    public void eac_neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci)
    {
        if (sourceBlock == Blocks.CRAFTING_TABLE || world.getBlockState(sourcePos).getBlock() == Blocks.CRAFTING_TABLE)
        {
            world.updateComparators(pos, state.getBlock());
        }
    }

    @SuppressWarnings("ConstantConditions")
    public DropperBlockMixin()
    {
        super(null);
    }
}
