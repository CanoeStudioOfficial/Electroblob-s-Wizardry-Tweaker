package com.canoestudio.ebwizardrytweaker.proxy;

import com.canoestudio.ebwizardrytweaker.EBWizardryTweaker;
import com.canoestudio.ebwizardrytweaker.Tags;
import com.canoestudio.ebwizardrytweaker.crafttweaker.ImbuementAltarRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ImbuementAltarRegistry.INSTANCE);
        EBWizardryTweaker.LOGGER.info("{} loaded - Imbuement Altar CraftTweaker support enabled (mods.ebwizardrytweaker.ImbuementAltar)", Tags.MOD_NAME);
    }
}
