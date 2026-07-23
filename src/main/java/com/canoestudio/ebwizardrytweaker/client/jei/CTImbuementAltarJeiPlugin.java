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
import java.lang.reflect.Array;
import java.lang.reflect.Method;

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
        ItemStack direct = CraftTweakerMC.getItemStack(ingredient);
        if (direct != null && !direct.isEmpty()) {
            return direct.copy();
        }
        try {
            Object examples = CraftTweakerMC.class.getMethod("getExamples", IIngredient.class).invoke(null, ingredient);
            if (examples instanceof Iterable) {
                for (Object example : (Iterable<?>) examples) {
                    ItemStack stack = asItemStack(example);
                    if (stack != null && !stack.isEmpty()) {
                        return stack.copy();
                    }
                }
            } else if (examples != null && examples.getClass().isArray()) {
                int length = Array.getLength(examples);
                for (int i = 0; i < length; i++) {
                    ItemStack stack = asItemStack(Array.get(examples, i));
                    if (stack != null && !stack.isEmpty()) {
                        return stack.copy();
                    }
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getDustStack(Element element) {
        if (element == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(WizardryItems.spectral_dust, 1, element.ordinal());
    }

    private static ItemStack asItemStack(Object value) {
        return value instanceof ItemStack ? (ItemStack) value : null;
    }
}
