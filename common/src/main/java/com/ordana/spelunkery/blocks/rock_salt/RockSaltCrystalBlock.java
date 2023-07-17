package com.ordana.spelunkery.blocks.rock_salt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RockSaltCrystalBlock extends Block implements SimpleWaterloggedBlock, RockSalt {
    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    protected final VoxelShape northAabb;
    protected final VoxelShape southAabb;
    protected final VoxelShape eastAabb;
    protected final VoxelShape westAabb;
    protected final VoxelShape upAabb;
    protected final VoxelShape downAabb;

    public RockSaltCrystalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false).setValue(LIGHT, 0).setValue(FACING, Direction.UP).setValue(ILLUMINATED, false));
        this.upAabb = Block.box(3, 0.0D, 3, 13, 12, 13);
        this.downAabb = Block.box(3, 4, 3, 13, 16.0D, 13);
        this.northAabb = Block.box(3, 3, 4, 13, 13, 16.0D);
        this.southAabb = Block.box(3, 3, 0.0D, 13, 13, 12);
        this.eastAabb = Block.box(0.0D, 3, 3, 12, 13, 13);
        this.westAabb = Block.box(4, 3, 3, 16.0D, 13, 13);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case NORTH -> this.northAabb;
            case SOUTH -> this.southAabb;
            case EAST -> this.eastAabb;
            case WEST -> this.westAabb;
            case DOWN -> this.downAabb;
            case UP -> this.upAabb;
        };
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        return level.getBlockState(blockPos).isFaceSturdy(level, blockPos, direction);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        int i = getDistanceAt(neighborState) - 1;
        if (i != 1 || state.getValue(LIGHT) != i) {
            level.scheduleTick(currentPos, this, 1);
        }

        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var light = context.getLevel().getLightEmission(context.getClickedPos());
        boolean bl = light > 0;
        LevelAccessor levelAccessor = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = this.defaultBlockState().setValue(LIGHT, light).setValue(ILLUMINATED, bl).setValue(WATERLOGGED, levelAccessor.getFluidState(blockPos).getType() == Fluids.WATER).setValue(FACING, context.getClickedFace());
        return updateDistance(blockState, context.getLevel(), context.getClickedPos());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, updateDistance(state, level, pos), 3);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, ILLUMINATED, LIGHT);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        onEntityStepOn(state, entity);
        super.stepOn(world, pos, state, entity);
    }


    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int i = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        Direction[] var5 = Direction.values();

        for (Direction direction : var5) {
            mutableBlockPos.setWithOffset(pos, direction);
            i = Math.max(i, getDistanceAt(level.getBlockState(mutableBlockPos)) - 1);
            if (i == 16) {
                break;
            }
        }

        return state.setValue(LIGHT, i).setValue(ILLUMINATED, i > 0);
    }

    private static int getDistanceAt(BlockState neighbor) {
        if (neighbor.getLightEmission() > 1) {
            return neighbor.getLightEmission();
        } else {
            return neighbor.hasProperty(ILLUMINATED) ? neighbor.getValue(LIGHT) : 0;
        }
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        FACING = BlockStateProperties.FACING;
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (state.getValue(LIGHT));
    }
}

