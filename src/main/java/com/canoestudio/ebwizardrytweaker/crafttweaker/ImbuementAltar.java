package com.canoestudio.ebwizardrytweaker.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import com.canoestudio.ebwizardrytweaker.EBWizardryTweaker;
import electroblob.wizardry.constants.Element;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * CraftTweaker entry point for the Electroblob's Wizardry imbuement altar (注灵祭坛).
 *
 * <p>ZenScript package: {@code mods.ebwizardrytweaker.ImbuementAltar}</p>
 *
 * <p>Element names: {@code magic}, {@code fire}, {@code ice}, {@code lightning},
 * {@code necromancy}, {@code earth}, {@code sorcery}, {@code healing}.
 * Use {@code empty}/{@code none}/{@code null}/"" for an empty receptacle.</p>
 *
 * <p>Default element matching is order-independent (multiset). Use
 * {@link #addOrderedRecipe} / {@link #removeOrderedRecipe} when the SWNE order matters
 * (South, West, North, East).</p>
 */
@ZenRegister
@ModOnly("ebwizardry")
@ZenClass("mods.ebwizardrytweaker.ImbuementAltar")
public final class ImbuementAltar {

    private ImbuementAltar() {
    }

    /**
     * Adds an imbuement recipe. Element order does not matter.
     *
     * @param input    center item on the altar
     * @param output   resulting item
     * @param elements exactly 4 element names for the surrounding receptacles
     */
    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, String[] elements) {
        addRecipeInternal(input, output, elements, false);
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, String south, String west, String north, String east) {
        addRecipeInternal(input, output, new String[]{south, west, north, east}, false);
    }

    /**
     * Adds an imbuement recipe that requires elements in SWNE order
     * (South, West, North, East).
     */
    @ZenMethod
    public static void addOrderedRecipe(IIngredient input, IItemStack output, String[] elements) {
        addRecipeInternal(input, output, elements, true);
    }

    @ZenMethod
    public static void addOrderedRecipe(IIngredient input, IItemStack output, String south, String west, String north, String east) {
        addRecipeInternal(input, output, new String[]{south, west, north, east}, true);
    }

    private static void addRecipeInternal(IIngredient input, IItemStack output, String[] elements, boolean ordered) {
        if (input == null) {
            CraftTweakerAPI.logError("ImbuementAltar: input cannot be null");
            return;
        }
        if (output == null) {
            CraftTweakerAPI.logError("ImbuementAltar: output cannot be null");
            return;
        }
        try {
            Element[] parsed = ImbuementAltarRecipe.parseElements(elements);
            String command = buildAddCommand(input, output, elements, ordered);
            ImbuementAltarRecipe recipe = new ImbuementAltarRecipe(input, output, parsed, ordered, command);
            CraftTweakerAPI.apply(new AddRecipeAction(recipe));
        } catch (IllegalArgumentException ex) {
            CraftTweakerAPI.logError("ImbuementAltar: " + ex.getMessage());
        }
    }

    /**
     * Suppresses vanilla (and other non-CT) imbuement results for the given input,
     * regardless of receptacle elements.
     */
    @ZenMethod
    public static void removeByInput(IIngredient input) {
        if (input == null) {
            CraftTweakerAPI.logError("ImbuementAltar: input cannot be null");
            return;
        }
        String command = "mods.ebwizardrytweaker.ImbuementAltar.removeByInput(" + input.toCommandString() + ");";
        CraftTweakerAPI.apply(new AddRemovalAction(new ImbuementAltarRegistry.Removal(input, null, false, command)));
    }

    /**
     * Suppresses vanilla (and other non-CT) imbuement results for the given input + elements.
     * Element matching is order-independent.
     */
    @ZenMethod
    public static void removeRecipe(IIngredient input, String[] elements) {
        removeRecipeInternal(input, elements, false);
    }

    @ZenMethod
    public static void removeRecipe(IIngredient input, String south, String west, String north, String east) {
        removeRecipeInternal(input, new String[]{south, west, north, east}, false);
    }

    /**
     * Suppresses vanilla (and other non-CT) imbuement results for the given input + ordered elements.
     */
    @ZenMethod
    public static void removeOrderedRecipe(IIngredient input, String[] elements) {
        removeRecipeInternal(input, elements, true);
    }

    @ZenMethod
    public static void removeOrderedRecipe(IIngredient input, String south, String west, String north, String east) {
        removeRecipeInternal(input, new String[]{south, west, north, east}, true);
    }

    private static void removeRecipeInternal(IIngredient input, String[] elements, boolean ordered) {
        if (input == null) {
            CraftTweakerAPI.logError("ImbuementAltar: input cannot be null");
            return;
        }
        try {
            Element[] parsed = ImbuementAltarRecipe.parseElements(elements);
            String command = buildRemoveCommand(input, elements, ordered);
            CraftTweakerAPI.apply(new AddRemovalAction(new ImbuementAltarRegistry.Removal(input, parsed, ordered, command)));
        } catch (IllegalArgumentException ex) {
            CraftTweakerAPI.logError("ImbuementAltar: " + ex.getMessage());
        }
    }

    /**
     * Suppresses every vanilla imbuement altar recipe. CraftTweaker-added recipes still work.
     *
     * @param value true to suppress all vanilla recipes
     */
    @ZenMethod
    public static void removeAllVanilla(@Optional(valueBoolean = true) boolean value) {
        CraftTweakerAPI.apply(new SetRemoveAllVanillaAction(value));
    }

    /**
     * Removes all CraftTweaker-added imbuement recipes (does not restore vanilla recipes / removals).
     */
    @ZenMethod
    public static void clear() {
        CraftTweakerAPI.apply(new ClearRecipesAction());
    }

    /**
     * Clears CT recipes, removal rules, and the removeAllVanilla flag.
     * Call this at the top of your script before re-adding recipes so repeated script execution does not stack duplicates.
     */
    @ZenMethod
    public static void clearAll() {
        CraftTweakerAPI.apply(new ClearAllAction());
    }

    private static String buildAddCommand(IIngredient input, IItemStack output, String[] elements, boolean ordered) {
        String method = ordered ? "addOrderedRecipe" : "addRecipe";
        return "mods.ebwizardrytweaker.ImbuementAltar." + method + "("
                + input.toCommandString() + ", "
                + output.toCommandString() + ", "
                + formatStringArray(elements) + ");";
    }

    private static String buildRemoveCommand(IIngredient input, String[] elements, boolean ordered) {
        String method = ordered ? "removeOrderedRecipe" : "removeRecipe";
        return "mods.ebwizardrytweaker.ImbuementAltar." + method + "("
                + input.toCommandString() + ", "
                + formatStringArray(elements) + ");";
    }

    private static String formatStringArray(String[] elements) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append('"').append(elements[i] == null ? "empty" : elements[i]).append('"');
        }
        return builder.append(']').toString();
    }

    private static final class AddRecipeAction implements IAction {

        private final ImbuementAltarRecipe recipe;

        private AddRecipeAction(ImbuementAltarRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            ImbuementAltarRegistry.INSTANCE.addRecipe(recipe);
            EBWizardryTweaker.LOGGER.info("Registered CraftTweaker imbuement altar recipe: {}", recipe);
        }

        @Override
        public String describe() {
            return "Adding imbuement altar recipe: " + recipe;
        }
    }

    private static final class AddRemovalAction implements IAction {

        private final ImbuementAltarRegistry.Removal removal;

        private AddRemovalAction(ImbuementAltarRegistry.Removal removal) {
            this.removal = removal;
        }

        @Override
        public void apply() {
            ImbuementAltarRegistry.INSTANCE.addRemoval(removal);
            EBWizardryTweaker.LOGGER.info("Registered CraftTweaker imbuement altar removal: {}", removal);
        }

        @Override
        public String describe() {
            return "Removing imbuement altar recipe: " + removal;
        }
    }

    private static final class SetRemoveAllVanillaAction implements IAction {

        private final boolean value;

        private SetRemoveAllVanillaAction(boolean value) {
            this.value = value;
        }

        @Override
        public void apply() {
            ImbuementAltarRegistry.INSTANCE.setRemoveAllVanilla(value);
        }

        @Override
        public String describe() {
            return (value ? "Suppressing" : "Restoring") + " all vanilla imbuement altar recipes";
        }
    }

    private static final class ClearRecipesAction implements IAction {

        @Override
        public void apply() {
            ImbuementAltarRegistry.INSTANCE.clearCustomRecipes();
        }

        @Override
        public String describe() {
            return "Clearing all CraftTweaker imbuement altar recipes";
        }
    }

    private static final class ClearAllAction implements IAction {

        @Override
        public void apply() {
            ImbuementAltarRegistry.INSTANCE.clearCustomRecipes();
            ImbuementAltarRegistry.INSTANCE.clearRemovals();
        }

        @Override
        public String describe() {
            return "Clearing all CraftTweaker imbuement altar recipes and removal rules";
        }
    }
}
