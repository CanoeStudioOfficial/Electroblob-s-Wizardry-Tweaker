package com.canoestudio.ebwizardrytweaker.client.jei;

import com.canoestudio.ebwizardrytweaker.crafttweaker.ImbuementAltarRegistry;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.integration.jei.ImbuementAltarRecipe;
import electroblob.wizardry.registry.WizardryItems;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
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
            List<ItemStack> centerStacks = recipe.getDisplayStacks();
            ItemStack outputStack = recipe.createOutput();
            if (centerStacks.isEmpty() || outputStack == null || outputStack.isEmpty()) {
                continue;
            }
            List<List<ItemStack>> dusts = new ArrayList<>(4);
            for (Element element : recipe.getElements()) {
                ItemStack dustStack = getDustStack(element);
                dusts.add(Collections.singletonList(dustStack == null ? ItemStack.EMPTY : dustStack));
            }
            recipes.add(new ImbuementAltarRecipeWithInputs(centerStacks, dusts, outputStack));
        }
        return recipes;
    }

    private static ItemStack getDustStack(Element element) {
        if (element == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(WizardryItems.spectral_dust, 1, element.ordinal());
    }

    private static final class ImbuementAltarRecipeWithInputs extends ImbuementAltarRecipe {

        private final List<List<ItemStack>> inputs;
        private final ItemStack output;

        private ImbuementAltarRecipeWithInputs(List<ItemStack> centerStacks, List<List<ItemStack>> dusts, ItemStack output) {
            super(centerStacks.get(0), dusts, output);
            this.inputs = new ArrayList<>();
            this.inputs.add(copyStacks(centerStacks));
            for (List<ItemStack> dustSlot : dusts) {
                this.inputs.add(copyStacks(dustSlot));
            }
            this.output = output.copy();
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
            ingredients.setOutput(VanillaTypes.ITEM, output);
        }

        private static List<ItemStack> copyStacks(List<ItemStack> stacks) {
            List<ItemStack> copies = new ArrayList<>();
            for (ItemStack stack : stacks) {
                if (stack != null && !stack.isEmpty()) {
                    copies.add(stack.copy());
                }
            }
            return copies;
        }
    }
}
