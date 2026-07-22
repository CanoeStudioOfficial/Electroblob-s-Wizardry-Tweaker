package com.canoestudio.ebwizardrytweaker;

import com.canoestudio.ebwizardrytweaker.crafttweaker.ImbuementAltarRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:ebwizardry@[4.3.5,);required-after:crafttweaker"
)
public class EBWizardryTweaker {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ImbuementAltarRegistry.INSTANCE);
        LOGGER.info("{} loaded — Imbuement Altar CraftTweaker support enabled (mods.ebwizardry.ImbuementAltar)", Tags.MOD_NAME);
    }
}
