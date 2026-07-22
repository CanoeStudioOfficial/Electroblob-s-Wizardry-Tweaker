package com.canoestudio.ebwizardrytweaker.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import com.canoestudio.ebwizardrytweaker.EBWizardryTweaker;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.ImbuementActivateEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Holds CraftTweaker imbuement altar recipes and applies them through {@link ImbuementActivateEvent}.
 */
public final class ImbuementAltarRegistry {

    public static final ImbuementAltarRegistry INSTANCE = new ImbuementAltarRegistry();

    private final List<ImbuementAltarRecipe> recipes = new ArrayList<>();
    private final List<Removal> removals = new ArrayList<>();
    private boolean removeAllVanilla;

    private ImbuementAltarRegistry() {
    }

    public synchronized void addRecipe(ImbuementAltarRecipe recipe) {
        recipes.add(recipe);
    }

    public synchronized boolean removeRecipe(ImbuementAltarRecipe recipe) {
        return recipes.remove(recipe);
    }

    public synchronized void addRemoval(Removal removal) {
        removals.add(removal);
    }

    public synchronized void clearCustomRecipes() {
        recipes.clear();
    }

    public synchronized void clearRemovals() {
        removals.clear();
        removeAllVanilla = false;
    }

    public synchronized void setRemoveAllVanilla(boolean removeAllVanilla) {
        this.removeAllVanilla = removeAllVanilla;
    }

    public synchronized boolean isRemoveAllVanilla() {
        return removeAllVanilla;
    }

    public synchronized List<ImbuementAltarRecipe> getRecipes() {
        return Collections.unmodifiableList(new ArrayList<>(recipes));
    }

    public synchronized List<Removal> getRemovals() {
        return Collections.unmodifiableList(new ArrayList<>(removals));
    }

    /**
     * Finds the first matching custom recipe for the given altar state.
     */
    @Nullable
    public synchronized ImbuementAltarRecipe findRecipe(ItemStack input, Element[] elements) {
        for (ImbuementAltarRecipe recipe : recipes) {
            if (recipe.matches(input, elements)) {
                return recipe;
            }
        }
        return null;
    }

    public synchronized boolean isRemoved(ItemStack input, Element[] elements) {
        if (removeAllVanilla) {
            return true;
        }
        for (Removal removal : removals) {
            if (removal.matches(input, elements)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onImbuementActivate(ImbuementActivateEvent event) {
        ItemStack input = event.input;
        Element[] elements = event.receptacleElements;

        ImbuementAltarRecipe recipe = findRecipe(input, elements);
        if (recipe != null) {
            event.result = recipe.createOutput();
            event.setCanceled(true);
            EBWizardryTweaker.LOGGER.debug("Matched CraftTweaker imbuement altar recipe: {}", recipe);
            return;
        }

        if (isRemoved(input, elements)) {
            event.result = ItemStack.EMPTY;
            event.setCanceled(true);
            EBWizardryTweaker.LOGGER.debug("Suppressed imbuement altar recipe for input {} with elements {}", input, ImbuementAltarRecipe.formatElements(elements));
        }
    }

    /**
     * Describes a vanilla (or otherwise non-CT) recipe suppression rule.
     */
    public static final class Removal {

        private final IIngredient input;
        @Nullable
        private final Element[] elements; // null = any elements
        private final boolean ordered;
        private final String commandString;

        public Removal(IIngredient input, @Nullable Element[] elements, boolean ordered, String commandString) {
            this.input = input;
            this.elements = elements == null ? null : Arrays.copyOf(elements, elements.length);
            this.ordered = ordered;
            this.commandString = commandString;
        }

        public boolean matches(ItemStack stack, Element[] receptacleElements) {
            if (stack == null || stack.isEmpty()) {
                return false;
            }
            IItemStack ctStack = CraftTweakerMC.getIItemStack(stack);
            if (ctStack == null || !input.matches(ctStack)) {
                return false;
            }
            if (elements == null) {
                return true;
            }
            if (receptacleElements == null || receptacleElements.length != 4) {
                return false;
            }
            if (ordered) {
                return Arrays.equals(elements, receptacleElements);
            }
            return ImbuementAltarRecipe.multisetEquals(elements, receptacleElements);
        }

        public String getCommandString() {
            return commandString;
        }

        @Override
        public String toString() {
            return "Removal{input=" + input
                    + ", elements=" + (elements == null ? "any" : ImbuementAltarRecipe.formatElements(elements))
                    + ", ordered=" + ordered + '}';
        }
    }
}
