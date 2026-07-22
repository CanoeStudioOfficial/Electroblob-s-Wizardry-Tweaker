# Electroblob's Wizardry Tweaker

CraftTweaker support for [Electroblob's Wizardry](https://www.curseforge.com/minecraft/mc-mods/electroblobs-wizardry) on Minecraft 1.12.2.

## Requirements

- Minecraft 1.12.2 / Forge
- Electroblob's Wizardry **4.3.5+** (provides `ImbuementActivateEvent`)
- CraftTweaker 2

## Imbuement Altar (注灵祭坛)

ZenScript class: `mods.ebwizardrytweaker.ImbuementAltar`

Surrounding receptacles use **spectral dust** elements. Element name strings:

| Name | Meaning |
|------|---------|
| `magic` / `fire` / `ice` / `lightning` / `necromancy` / `earth` / `sorcery` / `healing` | Element |
| `empty` / `none` / `null` / `""` | Empty receptacle |

Default matching is **order-independent** (multiset of 4 elements).  
Ordered APIs use **SWNE** order: South -> West -> North -> East.

The 4-string form is recommended because it is friendlier to CraftTweaker 1.12 scripts. The array form still works.

### Add recipes

```zenscript
import mods.ebwizardrytweaker.ImbuementAltar;

// diamond + 4x fire dust -> emerald
ImbuementAltar.addRecipe(<minecraft:diamond>, <minecraft:emerald>, "fire", "fire", "fire", "fire");

// exact positions (South, West, North, East)
ImbuementAltar.addOrderedRecipe(
    <minecraft:iron_ingot>,
    <minecraft:gold_ingot>,
    "fire", "ice", "earth", "healing"
);

// array form is also accepted
ImbuementAltar.addRecipe(<minecraft:diamond>, <minecraft:emerald>, ["fire", "fire", "fire", "fire"]);
```

### Remove / suppress vanilla recipes

```zenscript
// suppress all vanilla results for this input
ImbuementAltar.removeByInput(<ebwizardry:magic_crystal>);

// suppress a specific element combination
ImbuementAltar.removeRecipe(<ebwizardry:magic_crystal>, "fire", "fire", "fire", "fire");

// suppress every vanilla imbuement recipe (CT recipes still work)
ImbuementAltar.removeAllVanilla();
```

### Clear CT rules

```zenscript
// recommended at the top of your script, especially before /ct reload
ImbuementAltar.clearAll();

// clears only CT-added recipes, leaving removal rules in place
ImbuementAltar.clear();
```

See also `examples/imbuement_altar.zs`.

## How it works

Wizardry's imbuement altar resolves results in `TileEntityImbuementAltar#getImbuementResult`, which posts `ImbuementActivateEvent`. This mod listens to that event, applies CraftTweaker recipes first, then optional vanilla suppressions. Cancelling the event with a result skips Wizardry's built-in recipes.

When a CraftTweaker action is applied, the mod logs `Registered CraftTweaker imbuement altar recipe` or `Registered CraftTweaker imbuement altar removal`. If that line is missing, the ZenScript method was not called.

This integration changes altar behavior only. JEI display is still handled by Electroblob's Wizardry itself, so custom CraftTweaker imbuement recipes are not added to JEI by this mod.

## Building

```bash
./gradlew build
```

Output jar: `build/libs/ebwizardrytweaker-<version>.jar`
