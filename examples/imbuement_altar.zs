#loader crafttweaker
#priority 100

// Example CraftTweaker scripts for Electroblob's Wizardry Tweaker
// Place this file (or parts of it) in your instance's scripts/ folder.

import mods.ebwizardry.ImbuementAltar;

// Recommended at the top of your script so /ct reload does not stack duplicates
ImbuementAltar.clearAll();

// ---------------------------------------------------------------------------
// Element names (receptacle spectral dust):
//   magic, fire, ice, lightning, necromancy, earth, sorcery, healing
// Empty receptacle: "empty" / "none" / "null" / ""
// Default matching is order-independent (multiset of 4 elements).
// SWNE order for ordered recipes: South, West, North, East.
// ---------------------------------------------------------------------------

// Add a custom recipe: diamond + 4x fire dust -> emerald
// ImbuementAltar.addRecipe(<minecraft:diamond>, <minecraft:emerald>, ["fire", "fire", "fire", "fire"]);

// Ordered recipe (exact SWNE positions)
// ImbuementAltar.addOrderedRecipe(
//     <minecraft:iron_ingot>,
//     <minecraft:gold_ingot>,
//     ["fire", "ice", "earth", "healing"]
// );

// Suppress all vanilla imbuements of magic crystals (any element combination)
// ImbuementAltar.removeByInput(<ebwizardry:magic_crystal>);

// Suppress only the fire-crystal vanilla recipe
// ImbuementAltar.removeRecipe(<ebwizardry:magic_crystal>, ["fire", "fire", "fire", "fire"]);

// Suppress every vanilla imbuement altar recipe (CT-added recipes still work)
// ImbuementAltar.removeAllVanilla();