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

### Parameter reference

| Parameter | Type | Meaning |
|-----------|------|---------|
| `input` | `IIngredient` | The item placed in the center of the imbuement altar. This can be a normal item stack like `<minecraft:diamond>` or any CraftTweaker ingredient expression supported by CT. |
| `output` | `IItemStack` | The item produced after the imbuement finishes, for example `<minecraft:emerald>`. |
| `south`, `west`, `north`, `east` | `String` | The four receptacle elements around the altar. These names correspond to spectral dust elements. |
| `elements` | `String[]` | Array form of the four element strings. It must contain exactly 4 entries. |

For `addRecipe` and `removeRecipe`, element order does not matter. For example, four entries containing one `fire`, one `ice`, one `earth`, and one `healing` will match those four elements in any receptacle positions.

For `addOrderedRecipe` and `removeOrderedRecipe`, element order does matter and must be written as `south`, `west`, `north`, `east`.

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

The first example means:

| Argument | Value | Meaning |
|----------|-------|---------|
| `input` | `<minecraft:diamond>` | Put a diamond in the center altar slot. |
| `output` | `<minecraft:emerald>` | The altar will output an emerald. |
| elements | `"fire", "fire", "fire", "fire"` | All four receptacles need fire spectral dust. |

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
