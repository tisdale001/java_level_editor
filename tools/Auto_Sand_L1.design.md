# Auto_Sand_L1 — first Sand level

100 rows × 150 columns. Coordinates below are zero-based tiles; companion files use 32-pixel editor tiles. Uses only terrain/object types found in `Wally_Sand_Components`.

## Route

Enter from the left through rows 80–85, with ground at row 86. Recommended player top-left spawn is column 9, row 83. Travel east through the lowest gallery, climb the right staircase, return west through the next gallery, and alternate directions until reaching the top gallery. Exit right through rows 10–15 with ground at row 16.

Gallery floors: **86 → 68 → 50 → 32 → 16**. The alternating stair chambers use four six-tile-wide treads per climb, with rises of four or five tiles. At each staircase top, a four-tile jump reaches a two-to-five-tile-thick return formation with a V-shaped underside; a six-tile drop at its inner end joins the next gallery. Formations are at rows 62, 44, 26 and 10. Ceiling recesses vary gallery height between nine and eleven tiles; connecting chambers are taller.

The surrounding rock is fully filled, including seven columns at each side and four rows at top/bottom. Only the two six-tile-tall edge portals interrupt the side borders.

## Encounters

- Three dogs: (30,85), (88,67), (103,15), each with separate patrol borders.
- Four floor scorpions: (59,67), (88,49), (69,31), (70,15), each with separate patrol borders.
- Two five-tile sand/worm runs: columns 64–68 at row 86; columns 58–62 at row 50. SandTiles spawn WormManager enemies in the game, so these are encounters, not merely scenery.
- One optional four-tile horizontal moving platform, initially (53,47), with border markers at (48,47) and (74,47). Its movement range is columns 49–70 for its left edge. It offers a route above the second sand patch; fixed terrain remains a complete route.

No boss, exit transition, or gameplay configuration is authored by this generator. Those are separate game-integration work.

## Validation

`node tools/generate_auto_sand_l1.cjs` creates the terrain, empty moving-column layer, 27 companion files and JSON validation report. It refuses to overwrite generated outputs unless `--replace-generated` is explicitly supplied.

Checks pass for exact dimensions, Component-only terrain/types/orientations, filled boundaries and portal exceptions. A geometry search checks the entire 3×2 body along sampled six-tile-apex jumps and swept walk-off drops. It finds an entrance-to-exit route with no required rise above five tiles and no tested horizontal displacement above eight tiles. The report records all route positions and stair/bridge coordinates.

This is not an Android physics simulation. Actual jump timing, enemy behavior, worm attacks, and moving-platform interaction still require playtesting. The independent reviewer and technical lead perform additional checks outside this generator.


## Platform revision — November 2, 2026

The four long single-row bridges are replaced by rock formations with a minimum two-tile body and V-shaped undersides reaching five tiles deep. Each is separated from the final stair tread by four empty columns horizontally. Stair rises are now four or five tiles. These continuous terrain stairs do not introduce isolated one-tile platforms. The detached return formations are six tiles above their destination gallery floors, preserving space around their thicker bodies.

The final gallery scorpion and its two patrol markers moved to supported ground at column 70 (markers 62 and 78), row 15. Enemy counts, type palette, portals, map dimensions and moving-platform behavior are unchanged. Validation now explicitly checks formation thickness, stair/formation spacing, ground support beneath enemies and the full 3-by-2 rider clearance over the moving platform sweep. It finds 584 reachable supported positions and a 147-edge entrance-to-exit route.

