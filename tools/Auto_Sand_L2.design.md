# Auto_Sand_L2 — the divided sand caverns

150 rows × 125 columns, using `Wally_Sand_Components`. All coordinates are zero-based tiles; object files use 32-pixel editor coordinates.

## Route and portals

Enter at the left edge through rows 22–27, with ground at row 28. Safe player top-left spawn: column 9, row 25. Descend the left cavern on alternating broad rock formations. Their floors progress 28, 33, 38 … 133, then join the bottom floor at row 138.

The solid central sand divider occupies columns 55–69 down to row 127. Its only through-route is the bottom passage, rows 128–137. Cross that passage through two five-tile worm patches, with rock footing between and beside them.

Ascend the right cavern on alternating ledges at floors 133, 128, 123 … 18. The final ledge connects to the right edge exit at rows 12–17, floor 18. The route therefore descends approximately 110 tiles, crosses the bottom, and climbs 120 tiles before exiting.

Seven filled columns frame both side edges except their six-tile-tall portals. Four filled rows protect the top and bottom. Above each cavern, the four-row top cap has a mostly three-row additional ceiling profile, several five-column-wide sections with four additional rows, and one wider section whose middle has five additional rows. Total roof thickness is therefore seven, eight or nine tiles, never three. Rock beneath the bottom crossing continues to the map bottom. Shaft walls remain straight except for the intentional connected shelves documented below.

## Platforms and encounters

- 46 stationary formations, including the entrance ledge: minimum thickness two tiles, up to four tiles at their shaped undersides. Five unoccupied formations remain nine tiles wide: left floors 43, 73 and 123; right floors 78 and 33. The former short platform at right floor 113 is now connected to the divider. The descent mixes V and U silhouettes; the ascent uses V shapes with thinner edges to preserve jumping headroom. Each alternating ledge is separated by at least three empty columns horizontally, or by at least four empty rows vertically from overlapping formations.
- Two four-tile-wide vertical lifts: left shaft at column 48, moving through rows 45–62; right shaft at column 72, moving through rows 92–109. Both were moved sideways to clear the new connected shelves. Both remain optional alternatives beside the fixed ledges.
- One four-tile horizontal carrier at row 133 above the bottom crossing, with left-edge travel columns 51–71. It offers a moving route over the worm encounter.
- Two dogs: (36,82) and (98,87), each on a thirteen-tile platform. The first dog moved from the bottom crossing to the left descent.
- Three floor scorpions: (20,57), (20,107), and (98,47), each on a thirteen-tile platform. All dog/scorpion patrol markers sit immediately outside their platforms so the sprite can reach both edges. Full patrol sweeps are checked using the game's approximate sprite dimensions. No enemies occupy the six shortened ledges.
- Two five-tile SandTiles runs: columns 56–60 and 64–68 at row 138. These create WormManager enemies with burrowing/projectile behavior; they are not decorative sand.

## Intentional wall connections

The user's platform-1 addition was found in the saved level before regeneration: eleven surface tiles at row 33, columns 44–54. Those exact IDs are retained, with solid row-34 support added beneath them. The entire first platform now spans columns 31–54 and meets the central divider. A pre-edit terrain backup is stored at `.cache/Auto_Sand_L2.before_wall_connections.lvl`.

| Shaft | Floor row | Attachment | Platform columns |
| --- | ---: | --- | --- |
| Left | 33 | Central divider; user's platform 1 | 31–54 |
| Left | 48 | Outer left wall; three platforms below platform 1 | 7–30 |
| Left | 63 | Central divider | 30–54 |
| Left | 88 | Outer left wall | 7–30 |
| Left | 103 | Central divider | 30–54 |
| Left | 118 | Outer left wall | 7–30 |
| Right | 128 | Outer right wall | 93–117 |
| Right | 113 | Central divider | 70–89 |
| Right | 98 | Outer right wall | 93–117 |
| Right | 83 | Central divider | 70–89 |
| Right | 68 | Outer right wall | 93–117 |
| Right | 53 | Central divider | 70–89 |
| Right | 38 | Outer right wall | 93–117 |
| Right | 23 | Central divider | 70–89 |

Connections are separated by two to five platform intervals. Left outer-wall shelves extend through column 30, and later divider shelves start at column 30; this overlap blocks the old uninterrupted three-column fall lane. Nearby left ledges shifted to columns 14–26 or 34–46 to preserve spacing and route clearance. Short ledges in that shaft use columns 34–42. The player must change horizontal position between baffles to continue descending. Some shelf ends form optional pockets, while the fixed-terrain route remains complete.

Enemy sprites stay at their established coordinates. The first dog's patrol borders moved to columns 33 and 47; the two left scorpions' borders moved to columns 13 and 27, matching their shifted thirteen-tile platforms. All five enemies still patrol long platforms.

No climbable terrain IDs 214–223 occur anywhere. Fixed geometry alone provides a complete route, so no climb ability or mandatory lift timing is assumed.

## Validation and files

Run `node tools/generate_auto_sand_l2.cjs`. Existing generated outputs are protected unless `--replace-generated` is supplied.

The generator creates terrain, an empty moving-column layer, 27 object companion files, and `Auto_Sand_L2.validation.json`. It checks exact dimensions, Component-only terrain and object orientations, excluded climbable IDs, filled boundaries, preservation and support of the manual platform tiles, connector attachment and frequency, blocked straight descent lanes, the varying ceiling profile, formation thickness and spacing, full enemy patrol clearance, absence of incidental supporting wall ledges, full moving-platform sweeps, and 3×2 rider headroom.

A supported-position search checks the full 3×2 cat body along sampled parabolic jumps and controlled falls. It finds 753 reachable positions and a 66-edge path from entrance to exit that passes through the bottom divider crossing. No tested jump rises more than five tiles or travels more than ten tiles horizontally. The validated descent uses drops of at most five tiles.

Add `--sync-game` when intentionally refreshing the sibling Cat_Game assets. The generator copies only this level's 29 terrain/object files and verifies the game and editor contents match. The latest wall-connection revision was generated with `--replace-generated --sync-game`.

These are geometric checks, not an Android physics simulation. Actual jumping, lift boarding, enemy reversal timing, and worm/projectile difficulty require playtesting. Game registration remains separate technical-lead work.

## Seven-tile jump-arc audit and targeted repairs

The explicit jump audit in `sand_jump_audit.cjs` uses a seven-tile apex at 5.5 horizontal tiles, and eleven horizontal tiles over a full same-height jump. It checks the entire 3×2 body throughout flight, including edge takeoff/landing and horizontal release. The model uses `height = 28t(1−t)` and a maximum horizontal speed of eleven tiles per normalized full jump. A ceiling hit zeros upward velocity and begins a gravity-driven fall from the contact height; it never grants the remainder of an unobstructed jump. A head hit at half the normal apex produces about 5.5 horizontal tiles at the original landing height.

The audit found the two reported failures plus one additional leftward return failure. Exactly 42 underside tiles were removed:

| Jump, by source/target floor | Overhead platform | Removed tiles |
| --- | ---: | --- |
| 98 → 93: six left-column platforms below BARELY | 88 | columns 24–30, rows 90–91 |
| 113 → 108: additional blocked leftward return | 103 | columns 30–36, rows 105–106 |
| 128 → 123: bottom-left jump | 118 | columns 24–30, rows 120–121 |

All three overhead lips retain two solid rows, their original top surfaces and wall connections. The repaired jumps may still clip the ceiling, but can now rise six tiles before contact; the shortened falling arc reaches the target five tiles above takeoff. Their modeled landing requires edge positioning, so playtesting remains important.

The BARELY reference is floor 38 → 33. Its geometry is unchanged, and the model finds an edge takeoff with a brief horizontal release inside the exact three-tile opening. It remains intentionally tight. The entire right cavern is byte-for-byte unchanged by these repairs, and all its upward transitions still pass. Existing divider-bottom rock tiles are retained through `Auto_Sand_L2.preserved_tiles.json`.

All 44 adjacent upward platform transitions pass the explicit arc audit. The report includes takeoff, landing, horizontal gain and any clipped apex for each transition. Edge launches/landings require at least half a tile of ground overlap; this is separate from the three-tile-wide collision clearance. The existing supported-position route search remains an additional geometric check. All 29 editor/game files were synchronized and verified after the repairs.
