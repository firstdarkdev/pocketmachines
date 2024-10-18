package com.hypherionmc.pocketmachines.mixin.accessor;

import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SimpleContainer.class)
public interface SimpleContainerAccessor {

    @Accessor("listeners")
    List<ContainerListener> getListeners();

}
