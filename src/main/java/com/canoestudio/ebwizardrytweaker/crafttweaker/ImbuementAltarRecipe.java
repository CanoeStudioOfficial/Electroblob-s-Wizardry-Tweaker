package com.canoestudio.ebwizardrytweaker.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import electroblob.wizardry.constants.Element;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A single imbuement altar recipe registered via CraftTweaker.
 */
public class ImbuementAltarRecipe {

    private final IIngredient input;
    private final Ingredient inputIngredient;
    private final IItemStack output;
    private final Element[] elements;
    private final boolean ordered;
    private final String commandString;

    public ImbuementAltarRecipe(IIngredient input, IItemStack output, Element[] elements, boolean ordered, String commandString) {
        this.input = Objects.requireNonNull(input, "input");
        Ingredient converted = CraftTweakerMC.getIngredient(input);
        this.inputIngredient = converted == null ? Ingredient.EMPTY : converted;
        this.output = Objects.requireNonNull(output, "output");
        this.elements = Arrays.copyOf(Objects.requireNonNull(elements, "elements"), elements.length);
        this.ordered = ordered;
        this.commandString = commandString;
    }

    public IIngredient getInput() {
        return input;
    }

    public IItemStack getOutput() {
        return output;
    }

    public Element[] getElements() {
        return Arrays.copyOf(elements, elements.length);
    }

    public boolean isOrdered() {
        return ordered;
    }

    public String getCommandString() {
        return commandString;
    }

    public boolean matches(ItemStack stack, Element[] receptacleElements) {
        if (stack == null || stack.isEmpty() || receptacleElements == null || receptacleElements.length != 4) {
            return false;
        }
        if (!matchesElements(receptacleElements)) {
            return false;
        }
        return this.inputIngredient.apply(stack);
    }

    public boolean matchesInput(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return this.inputIngredient.apply(stack);
    }

    public boolean matchesElements(Element[] receptacleElements) {
        if (receptacleElements == null || receptacleElements.length != 4 || elements.length != 4) {
            return false;
        }
        if (ordered) {
            return Arrays.equals(elements, receptacleElements);
        }
        return multisetEquals(elements, receptacleElements);
    }

    public ItemStack createOutput() {
        return CraftTweakerMC.getItemStack(output).copy();
    }

    public static boolean multisetEquals(Element[] a, Element[] b) {
        int size = Element.values().length + 1; // last bucket = null/empty
        int[] countsA = new int[size];
        int[] countsB = new int[size];
        for (Element element : a) {
            countsA[elementIndex(element)]++;
        }
        for (Element element : b) {
            countsB[elementIndex(element)]++;
        }
        return Arrays.equals(countsA, countsB);
    }

    private static int elementIndex(@Nullable Element element) {
        return element == null ? Element.values().length : element.ordinal();
    }

    /**
     * Parses an element name used in CraftTweaker scripts.
     * Empty / {@code none} / {@code empty} / {@code null} means an empty receptacle.
     */
    @Nullable
    public static Element parseElement(@Nullable String name) {
        if (name == null) {
            return null;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()
                || "none".equalsIgnoreCase(trimmed)
                || "empty".equalsIgnoreCase(trimmed)
                || "null".equalsIgnoreCase(trimmed)
                || "-".equals(trimmed)) {
            return null;
        }
        return Element.fromName(trimmed.toLowerCase(Locale.ROOT));
    }

    public static Element[] parseElements(String[] names) {
        if (names == null || names.length != 4) {
            throw new IllegalArgumentException("Imbuement altar recipes require exactly 4 element names (SWNE order)");
        }
        Element[] parsed = new Element[4];
        for (int i = 0; i < 4; i++) {
            parsed[i] = parseElement(names[i]);
        }
        return parsed;
    }

    public static String formatElements(Element[] elements) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(elements[i] == null ? "empty" : elements[i].getName());
        }
        return builder.append(']').toString();
    }

    public List<ItemStack> getDisplayStacks() {
        return getDisplayStacks(this.inputIngredient);
    }

    public static boolean matchesIngredient(@Nullable Ingredient ingredient, @Nullable ItemStack stack) {
        if (ingredient == null || stack == null || stack.isEmpty()) {
            return false;
        }
        return ingredient.apply(stack);
    }

    public static List<ItemStack> getDisplayStacks(@Nullable Ingredient ingredient) {
        List<ItemStack> stacks = new ArrayList<>();
        if (ingredient == null || ingredient == Ingredient.EMPTY) {
            return stacks;
        }
        for (ItemStack example : ingredient.getMatchingStacks()) {
            addDisplayStack(stacks, example);
        }
        return stacks;
    }

    private static void addDisplayStack(List<ItemStack> stacks, ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        for (ItemStack existing : stacks) {
            if (ItemStack.areItemsEqual(existing, stack) && ItemStack.areItemStackTagsEqual(existing, stack)) {
                return;
            }
        }
        stacks.add(stack.copy());
    }

    @Override
    public String toString() {
        return "ImbuementAltarRecipe{input=" + input + ", output=" + output
                + ", elements=" + formatElements(elements) + ", ordered=" + ordered + '}';
    }
}
