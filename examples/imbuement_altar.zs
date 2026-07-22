#loader crafttweaker
#priority 100

// Example CraftTweaker scripts for Electroblob's Wizardry Tweaker
// Place this file (or parts of it) in your instance's scripts/ folder.

import mods.ebwizardrytweaker.ImbuementAltar;

// Recommended at the top of your script so repeated script execution does not stack duplicates
ImbuementAltar.clearAll();

// ---------------------------------------------------------------------------
// Element names (receptacle spectral dust):
//   magic, fire, ice, lightning, necromancy, earth, sorcery, healing
// Empty receptacle: "empty" / "none" / "null" / ""
// Default matching is order-independent (multiset of 4 elements).
// SWNE order for ordered recipes: South, West, North, East.
//
// Common parameters:
//   input  = item/ingredient placed in the center altar slot
//   output = item produced after imbuement finishes
//   south, west, north, east = four receptacle element names
// ---------------------------------------------------------------------------

// Add a custom recipe: diamond + 4x fire dust -> emerald
// Parameters: addRecipe(input, output, south, west, north, east)
// Element order does not matter for addRecipe.
// ImbuementAltar.addRecipe(<minecraft:diamond>, <minecraft:emerald>, "fire", "fire", "fire", "fire");

// Ordered recipe (exact SWNE positions)
// Parameters: addOrderedRecipe(input, output, south, west, north, east)
// ImbuementAltar.addOrderedRecipe(
//     <minecraft:iron_ingot>,
//     <minecraft:gold_ingot>,
//     "fire", "ice", "earth", "healing"
// );

// Array form is also accepted; the array must contain exactly 4 element names.
// ImbuementAltar.addRecipe(<minecraft:diamond>, <minecraft:emerald>, ["fire", "fire", "fire", "fire"]);

// Suppress all vanilla imbuements of magic crystals (any element combination)
// Parameters: removeByInput(input)
// ImbuementAltar.removeByInput(<ebwizardry:magic_crystal>);

// Suppress only the fire-crystal vanilla recipe
// Parameters: removeRecipe(input, south, west, north, east)
// Element order does not matter for removeRecipe.
// ImbuementAltar.removeRecipe(<ebwizardry:magic_crystal>, "fire", "fire", "fire", "fire");

// Suppress every vanilla imbuement altar recipe (CT-added recipes still work)
// ImbuementAltar.removeAllVanilla();
