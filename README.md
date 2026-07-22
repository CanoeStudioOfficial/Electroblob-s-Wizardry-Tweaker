# Electroblob's Wizardry Tweaker

CraftTweaker support for [Electroblob's Wizardry](https://www.curseforge.com/minecraft/mc-mods/electroblobs-wizardry) on Minecraft 1.12.2.

## Requirements

- Minecraft 1.12.2 / Forge
- Electroblob's Wizardry **4.3.5+** (provides `ImbuementActivateEvent`)
- CraftTweaker 2

## Imbuement Altar (注灵祭坛)

ZenScript class: `mods.ebwizardry.ImbuementAltar`

Surrounding receptacles use **spectral dust** elements. Element name strings:

| Name | Meaning |
|------|---------|
| `magic` / `fire` / `ice` / `lightning` / `necromancy` / `earth` / `sorcery` / `healing` | Element |
| `empty` / `none` / `null` / `""` | Empty receptacle |

Default matching is **order-independent** (multiset of 4 elements).  
Ordered APIs use **SWNE** order: South → West → North → East.

### Add recipes

```zenscript
import mods.ebwizardry.ImbuementAltar;

// diamond + 4x fire dust -> emerald
ImbuementAltar.addRecipe(<minecraft:diamond>, <minecraft:emerald>, ["fire", "fire", "fire", "fire"]);

// exact positions (South, West, North, East)
ImbuementAltar.addOrderedRecipe(
    <minecraft:iron_ingot>,
    <minecraft:gold_ingot>,
    ["fire", "ice", "earth", "healing"]
);
```

### Remove / suppress vanilla recipes

```zenscript
// suppress all vanilla results for this input
ImbuementAltar.removeByInput(<ebwizardry:magic_crystal>);

// suppress a specific element combination
ImbuementAltar.removeRecipe(<ebwizardry:magic_crystal>, ["fire", "fire", "fire", "fire"]);

// suppress every vanilla imbuement recipe (CT recipes still work)
ImbuementAltar.removeAllVanilla();
```

### Clear CT recipes

```zenscript
ImbuementAltar.clear();
```

See also `examples/imbuement_altar.zs`.

## How it works

Wizardry's imbuement altar resolves results in `TileEntityImbuementAltar#getImbuementResult`, which posts `ImbuementActivateEvent`. This mod listens to that event, applies CraftTweaker recipes first, then optional vanilla suppressions. Cancelling the event with a result skips Wizardry's built-in recipes.

## Building

```bash
./gradlew build
```

Output jar: `build/libs/ebwizardrytweaker-<version>.jar`
