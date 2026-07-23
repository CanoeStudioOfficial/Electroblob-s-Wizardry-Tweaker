package com.canoestudio.ebwizardrytweaker.client.jei;

import com.canoestudio.ebwizardrytweaker.crafttweaker.ImbuementAltarRegistry;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.integration.jei.ImbuementAltarRecipe;
import electroblob.wizardry.registry.WizardryItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.IJeiRuntime;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JEIPlugin
public class CTImbuementAltarJeiPlugin implements IModPlugin {

    private static final String IMBUEMENT_ALTAR_UID = "ebwizardry:imbuement_altar";

    @Override
    public void register(IModRegistry registry) {
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        if (!Wizardry.settings.jeiIntegration) {
            return;
        }
        for (ImbuementAltarRecipe recipe : generateRecipes()) {
            runtime.getRecipeRegistry().addRecipe(recipe, IMBUEMENT_ALTAR_UID);
        }
    }

    private static List<ImbuementAltarRecipe> generateRecipes() {
        List<ImbuementAltarRecipe> recipes = new ArrayList<>();
        for (com.canoestudio.ebwizardrytweaker.crafttweaker.ImbuementAltarRecipe recipe : ImbuementAltarRegistry.INSTANCE.getRecipes()) {
            ItemStack centerStack = getDisplayStack(recipe.getInput());
            ItemStack outputStack = recipe.createOutput();
            if (centerStack == null || centerStack.isEmpty() || outputStack == null || outputStack.isEmpty()) {
                continue;
            }
            List<List<ItemStack>> dusts = new ArrayList<>(4);
            for (Element element : recipe.getElements()) {
                ItemStack dustStack = getDustStack(element);
                dusts.add(Collections.singletonList(dustStack == null ? ItemStack.EMPTY : dustStack));
            }
            recipes.add(new ImbuementAltarRecipe(centerStack.copy(), dusts, outputStack.copy()));
        }
        return recipes;
    }

    private static ItemStack getDisplayStack(IIngredient ingredient) {
        if (ingredient == null) {
            return ItemStack.EMPTY;
        }
        for (crafttweaker.api.item.IItemStack example : ingredient.getItems()) {
            ItemStack stack = CraftTweakerMC.getItemStack(example);
            if (stack != null && !stack.isEmpty()) {
                return stack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getDustStack(Element element) {
        if (element == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(WizardryItems.spectral_dust, 1, element.ordinal());
    }
}
