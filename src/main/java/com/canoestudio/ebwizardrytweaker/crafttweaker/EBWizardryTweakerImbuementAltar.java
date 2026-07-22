package com.canoestudio.ebwizardrytweaker.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ModOnly("ebwizardry")
@ZenClass("mods.ebwizardrytweaker.ImbuementAltar")
public final class EBWizardryTweakerImbuementAltar {

    private EBWizardryTweakerImbuementAltar() {
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, String[] elements) {
        ImbuementAltar.addRecipe(input, output, elements);
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, String south, String west, String north, String east) {
        ImbuementAltar.addRecipe(input, output, south, west, north, east);
    }

    @ZenMethod
    public static void addOrderedRecipe(IIngredient input, IItemStack output, String[] elements) {
        ImbuementAltar.addOrderedRecipe(input, output, elements);
    }

    @ZenMethod
    public static void addOrderedRecipe(IIngredient input, IItemStack output, String south, String west, String north, String east) {
        ImbuementAltar.addOrderedRecipe(input, output, south, west, north, east);
    }

    @ZenMethod
    public static void removeByInput(IIngredient input) {
        ImbuementAltar.removeByInput(input);
    }

    @ZenMethod
    public static void removeRecipe(IIngredient input, String[] elements) {
        ImbuementAltar.removeRecipe(input, elements);
    }

    @ZenMethod
    public static void removeRecipe(IIngredient input, String south, String west, String north, String east) {
        ImbuementAltar.removeRecipe(input, south, west, north, east);
    }

    @ZenMethod
    public static void removeOrderedRecipe(IIngredient input, String[] elements) {
        ImbuementAltar.removeOrderedRecipe(input, elements);
    }

    @ZenMethod
    public static void removeOrderedRecipe(IIngredient input, String south, String west, String north, String east) {
        ImbuementAltar.removeOrderedRecipe(input, south, west, north, east);
    }

    @ZenMethod
    public static void removeAllVanilla(@Optional(valueBoolean = true) boolean value) {
        ImbuementAltar.removeAllVanilla(value);
    }

    @ZenMethod
    public static void clear() {
        ImbuementAltar.clear();
    }

    @ZenMethod
    public static void clearAll() {
        ImbuementAltar.clearAll();
    }
}
