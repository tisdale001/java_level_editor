# Auto_Sand_L3 — Maze Image4 openings and encounters

## Latest revision: Maze Image4

Image4 uses8 pixels per tile with image origin(5,9). Three additional red regions are opened, preserving all Image3 cuts and closures:

| Region | Inclusive columns | Inclusive rows | Floor and encounter |
|---|---|---|---|
| Upper-right |98–127|22–29|Floor30; one dog ID45 at(112,29), patrol columns98–127|
| Middle-right |98–127|39–47|Floor48; one scorpion ID54 at(112,47), patrol columns98–127|
| Left |43–58|39–55|Floor56; one scorpion ID54 at(50,55), patrol columns43–58|

The left opening receives three2–3-thick ledges at columns51–55/floor40,43–47/floor45,and51–55/floor50. These preserve safe ascent/descent through its taller opening. Patrol support and full enemy sprite clearance are sampled across each entire30-tile or16-tile floor. Patrol markers sit immediately outside their floor endpoints:97/128 for the two right corridors and42/59 for the left corridor.

Exactly two WormManager records are added; runtime WormManager creates one Worm per record. Their SandTiles runs begin at(54,146), facing left, and(100,146), facing right. Each run spans5 tiles: columns54–58 and100–104 on row146. Sand terrain continues down through149, preserving the four-tile bottom boundary. The bottom passage remains clear for the3×2 cat on both approaches and across each run. Terrain collision remains solid beneath the worms.

The lift is unchanged: columns128–137, top stops16 and114, marker rows15 and115. Six lift boarding elevations now include the two new right-hand floors30 and48.

Final audit:106/106 supported surfaces reachable and returnable;249 directed transitions;75-surface entrance-to-exit route. Boundaries, allowed palette, no climbable tiles, full-body jump checks, enemy patrol support, worm approach clearance, and lift sweep checks pass. All29 editor/game asset pairs are SHA-256 verified.


150 rows ×150 columns. Terrain follows the red-open/yellow-close annotations in Maze Image3.png. The screenshot is cropped vertically: mapping is approximately x=(pixelX+4)/8 and y=(pixelY+436)/8. All coordinates below are inclusive, zero-based tile indices.

## Applied boxes

| Action | Columns | Rows |
|---|---|---|
| Open red1 | 38–74 | 59–69 |
| Open red2 | 101–127 | 59–68 |
| Open red3 | 95–106 | 79–120 |
| Open red4 | 104–127 | 80–90 |
| Open red5 | 66–86 | 89–94 |
| Open red6 | 72–86 | 94–136 |
| Open red7 | 30–49 | 99–106 |
| Open red8 | 111–128 | 107–115 |
| Open red9 | 93–127 | 121–130 |
| Open red10 | 35–47 | 131–145 |
| Open red11 | 48–112 | 136–145 |
| Close yellow1 | 47–66 | 95–98 |
| Close yellow2 | 30–32 | 121–130 |
| Close yellow3 | 126–138 | 116–120 |

The red regions remove rock, except for necessary thick traversal ledges added afterward. The yellow regions are fully solid. To make the new passages usable, the central vertical opening continues through row145 at columns72–86, and the right vertical opening continues through row130 at columns95–107. These small joins connect the drawn openings to the lower galleries.

New staggered2–3-tile-thick formations support ascent through the passages near columns79 and102. Two lower ledges at columns35–39/floor136 and43–47/floor141 connect the floor131 gallery with the newly opened bottom passage. Existing intact formations remain. Adjacent alternating ledges generally retain3 empty horizontal tiles; compact shaft mouths retain audited3×2 body clearance.

The edited openings and closures redirect the route through the new lower and vertical passages. The old18-waypoint construction graph is retained only as generator provenance; it does not describe the revised topology. Connectivity is checked against actual terrain surfaces. The Image4 revision adds three corridor enemies and two bottom-passage worms, described above.

## Lift and portals

The lift remains10 tiles wide at columns128–137. Its platform top travels from row16 to row114. At the lower stop its one-tile body occupies row114, leaving row115 empty above the new solid yellow closure beginning at row116. Marker rows15 and115 produce these limits in EnemyFactory/MovingPlatform.

MovingPlatforms record: 4096 512 10 43 4096 480 4096 3680 44. There is exactly one moving platform. Its upper start remains row16. Rider boarding positions16,30,48,69,90,and114 are used by the movement audit, allowing access to the new side openings.

Entry is unchanged at left rows10–15/floor16, safe spawn column9,row13. Exit remains right rows125–130/floor131. The lower yellow closure separates the upper lift sweep from the old lower shaft segment; the exit is reached through the new lower openings.

## Validation

-11 red-open and3 yellow-close regions applied.
-All106 route surfaces, including6 lift support positions, are reachable from the entrance and can return.
-249 directed movement transitions pass; selected exit route contains75 surfaces.
-Full3×2 cat collision checks use7-tile apex,11-tile unobstructed range, required rise at most6, horizontal release, and reduced falling arcs following ceiling hits. Edge launch/landing overlap may be0.5 tile.
-The full10-tile platform and centered3×2 rider envelope clear terrain throughout the new16–114 sweep, sampled every0.125 tile.
-Component palette, no climbable IDs214–223,7 side-boundary columns,4 top/bottom rows, and intended portals pass.
-All29 editor/game assets are byte-identical with SHA-256 hashes in Auto_Sand_L3.sync.json.

These checks validate geometry, not Android runtime physics. Lift timing, rider transport, and the revised route need gameplay testing. No Android registration changes are made.

## Files

Generator: tools/generate_auto_sand_l3.cjs. Coordinates and movement witnesses: tools/Auto_Sand_L3.validation.json. Synchronization hashes: tools/Auto_Sand_L3.sync.json. Terrain and moving-column assets retain the Auto_Sand_L3 name;27 companion files are supplied, with only MovingPlatforms nonempty.

Regenerate using node tools/generate_auto_sand_l3.cjs --replace-generated --sync-game. Preserve manual changes before regeneration.
