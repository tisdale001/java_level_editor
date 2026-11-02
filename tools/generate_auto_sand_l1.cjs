// Deterministic, component-limited first Sand level. Run from repository root.
// Refuses to overwrite an existing level unless --replace-generated is supplied.
const fs = require('fs');
const path = require('path');
const assert = require('assert');
const root = path.resolve(__dirname, '..');
const name = 'Auto_Sand_L1';
const component = 'Wally_Sand_Components';
const R = 100, C = 150;
const rocks = [100,101,102,103,107,119,126];
const grid = Array.from({length:R}, (_,y) => Array.from({length:C}, (_,x) => rocks[((x*37+y*53)^(x*y*11)) % rocks.length]));
const carve = (x0,y0,x1,y1) => {for(let y=y0;y<=y1;y++) for(let x=x0;x<=x1;x++) grid[y][x]=-1;};
// Broad galleries with irregular, shallow ceiling recesses.
const galleries = [86,68,50,32,16];
for (let i=0;i<galleries.length;i++) {
  const f=galleries[i];
  for(let x=7;x<=142;x++) {
    const depth = 9 + ((Math.floor((x+i*7)/13)+i)%3);
    carve(x,Math.max(4,f-depth),x,f-1);
  }
}
// Four stair chambers, four broad terrain treads per climb.
const climbs = [];
for(let i=0;i<4;i++) {
  const low=galleries[i], high=galleries[i+1], right=i%2===0;
  carve(right?102:7,high-10,right?142:47,low-1);
  const rises = low-high===18 ? [5,5,5,5] : [4,4,5,5];
  let floor=low;
  const steps=[];
  for(let j=0;j<4;j++) {
    floor-=rises[j];
    const width=j===3?6:8;
    const x0=right?108+j*8:34-j*8+(j===3?2:0);
    for(let x=x0;x<x0+width;x++) for(let y=floor;y<low;y++) grid[y][x]=rocks[(x+y)%rocks.length];
    steps.push({x0,x1:x0+width-1,floor,rise:rises[j]});
  }
  // Fill behind the final tread, preventing a narrow dead-end shaft.
  const far0=right?138:7, far1=right?142:11;
  for(let x=far0;x<=far1;x++) for(let y=high-2;y<low;y++) grid[y][x]=rocks[(x+y)%rocks.length];
  // Return rock formation above the upper gallery. Six tiles of elevation keep its
  // shaped underside clear of lower stair jumps. Raised top tread is a four-tile jump
  // to this formation; the other end drops six tiles onto the upper gallery.
  const bridgeY=high-6, bridgeX0=right?102:22, bridgeX1=right?127:47;
  for(let x=bridgeX0;x<=bridgeX1;x++) {
    const depth=2+Math.max(0,3-Math.floor(Math.abs(x-(right?114:35))/3));
    for(let y=bridgeY;y<bridgeY+depth;y++) grid[y][x]=rocks[(x+y)%rocks.length];
  }
  // Open a generous chamber around the catwalk's gallery-side drop.
  carve(right?89:48,high-10,right?101:60,high-1);
  climbs.push({right,low,high,steps,returnBridge:{floor:bridgeY,x0:right?102:22,x1:right?127:47},galleryDrop:6});
}
// Edge portals are six tiles tall and extend through the entire side boundary.
carve(0,80,6,85);
carve(143,10,149,15);
// Add surface caps to exposed terrain and short wooden ceiling supports.
for(let y=1;y<R;y++) for(let x=0;x<C;x++) if(grid[y][x]>=0 && grid[y-1][x]===-1) grid[y][x]=(x%2?122:123);
for(const [x,y] of [[47,75],[77,57],[43,39],[72,21]]) {
  // Beams replace rock only; never insert an obstacle into clear space.
  for(let k=0;k<5;k++) if(grid[y][x+k]>=0) grid[y][x+k]=214+k;
}
// Sand surface objects create worm encounters. Keep five-tile runs matching
// the component's lengths, with solid sand body beneath and flat approaches.
const sand = [[64,86,0,5],[58,50,1,5]];
for(const [x,y,,len] of sand) for(let yy=y;yy<y+4;yy++) for(let xx=x;xx<x+len;xx++) grid[yy][xx]=241;
const objects={
  Dogs:[[30,85,45],[88,67,46],[103,15,46]],
  DogBorderBoxes:[[24,85,47],[39,85,48],[80,67,47],[97,67,48],[94,15,47],[113,15,48]],
  Scorpions:[[59,67,55],[88,49,54],[69,31,55],[70,15,54]],
  ScorpionBorderBoxes:[[52,67,62],[67,67,62],[81,49,62],[97,49,62],[61,31,62],[77,31,62],[62,15,62],[78,15,62]],
  SandTiles:sand,
  // Optional horizontal platform above the second sand patch. All mandatory
  // traversal remains possible using permanent terrain.
  MovingPlatforms:[[53,47,4,43,48,47,74,47,44]]
};
function entry(type,row) {
  if(type==='MovingPlatforms') return row.map((v,i)=>[0,1,4,5,6,7].includes(i)?v*32:v);
  return row.map((v,i)=>i<2?v*32:v);
}
const componentText=fs.readFileSync(path.join(root,'Assets/Levels/Sewer',component+'.lvl'),'utf8').trim().split(/\s+/).slice(3).map(Number);
const allowed=new Set(componentText);
assert(grid.flat().every(v=>allowed.has(v)),'Terrain outside component palette');
assert(grid.length===R && grid.every(row=>row.length===C));
for(let y=0;y<R;y++) for(let x=0;x<C;x++) {
  if(y<4||y>=R-4||x<7||x>=C-7) {
    const portal=(x<7&&y>=80&&y<=85)||(x>=143&&y>=10&&y<=15);
    assert(portal?grid[y][x]===-1:grid[y][x]>=0,`Boundary violation ${x},${y}`);
  }
}
// Full three-by-two body clearance, continuous tile collision during sampled
// parabolic jumps. This verifies geometry, not the Android physics/timing.
function clear(x,y) {
  if(x<0||y<0||x+3>C||y+2>R) return false;
  for(let yy=Math.floor(y+1e-6);yy<Math.ceil(y+2-1e-6);yy++)
    for(let xx=Math.floor(x+1e-6);xx<Math.ceil(x+3-1e-6);xx++) if(grid[yy][xx]>=0) return false;
  return true;
}
function stand(x,floor){return clear(x,floor-2) && [0,1,2].every(dx=>grid[floor]?.[x+dx]>=0);}
// New platform-shape rules: no one-row return formations and generous gaps
// from each detached formation to the final stair landing.
for(const climb of climbs) {
  const b=climb.returnBridge;
  assert(climb.steps.every(s=>s.rise>=4 && s.rise<=5));
  for(let x=b.x0;x<=b.x1;x++)assert(grid[b.floor][x]>=0&&grid[b.floor+1][x]>=0,'Thin fixed platform');
  const last=climb.steps.at(-1);
  const horizontalEmpty=climb.right?last.x0-b.x1-1:b.x0-last.x1-1;
  assert(horizontalEmpty>=3,'Detached platform spacing');
}
for(const type of ['Dogs','Scorpions'])for(const [x,y]of objects[type])assert(grid[y][x]===-1&&grid[y+1][x]>=0,`Unsupported ${type} at ${x},${y}`);
// The optional moving platform must have full rider headroom over its sweep.
for(let x=49;x<=70;x++)assert(clear(x,45),'Moving platform rider clearance');
function jump(a,b) {
  if(Math.abs(a.x-b.x)>8 || a.f-b.f>5) return false;
  // Six-tile nominal apex; land on the target while descending.
  const rise=a.f-b.f, apex=6;
  const tEnd=(1+Math.sqrt(1-rise/apex))/2;
  for(let k=0;k<=120;k++) {
    const t=k/120*tEnd, x=a.x+(b.x-a.x)*t/tEnd;
    const y=a.f-2-4*apex*t*(1-t);
    if(!clear(x,y)) return false;
  }
  return true;
}
function drop(a,b) {
  if(b.f<=a.f||b.f-a.f>6||Math.abs(a.x-b.x)>8)return false;
  // Walk clear of a ledge, then settle onto the floor below. Full body is
  // collision-checked along the entire horizontal and vertical sweep.
  for(let k=0;k<=120;k++)if(!clear(a.x+(b.x-a.x)*k/120,a.f-2))return false;
  for(let k=0;k<=120;k++)if(!clear(b.x,a.f-2+(b.f-a.f)*k/120))return false;
  return true;
}
const states=[];
for(let f=2;f<R;f++) for(let x=0;x<=C-3;x++) if(stand(x,f)) states.push({x,f});
const byKey=new Map(states.map((s,i)=>[s.x+','+s.f,i]));
const start=byKey.get('9,86');
assert(start!==undefined,'Spawn lacks support');
const visited=new Set([start]), queue=[start], parent=new Map();
for(let q=0;q<queue.length;q++) {
  const i=queue[q],a=states[i];
  for(let dx=-8;dx<=8;dx++) for(let rise=-6;rise<=5;rise++) {
    if(dx===0) continue;
    const j=byKey.get((a.x+dx)+','+(a.f-rise));
    if(j===undefined||visited.has(j))continue;
    const b=states[j];
    const walk=rise===0&&Math.abs(dx)===1&&clear((a.x+b.x)/2,a.f-2);
    if(walk||drop(a,b)||jump(a,b)){visited.add(j);queue.push(j);parent.set(j,i);}
  }
}
const goal=byKey.get('147,16');
if(!visited.has(goal))console.error('Reachability diagnostic:', [...new Set([...visited].map(i=>states[i].f))].sort((a,b)=>a-b), 'last', states[queue.at(-1)]);
assert(visited.has(goal),'No conservative full-body route from spawn to exit');
const route=[];for(let i=goal;i!==undefined;i=parent.get(i))route.push(states[i]);route.reverse();
const report={name,component,rows:R,columns:C,entrance:{columns:[0,6],rows:[80,85],floor:86},exit:{columns:[143,149],rows:[10,15],floor:16},spawn:{column:9,row:83},galleryFloors:galleries,climbs,objects,validation:{allowedTerrain:[...new Set(grid.flat())].sort((a,b)=>a-b),boundary:'PASS',fullBodyRoute:'PASS',stationaryPlatformThickness:'2–5 tiles, PASS',stationaryTransitionSpacing:'four empty columns to final tread; stair rises4–5, PASS',enemyGroundSupport:'PASS',movingPlatformRiderClearance:'PASS',bodyTiles:[3,2],sampledJumpApex:6,maxTestedJumpRise:5,maxTestedHorizontalDisplacement:8,reachableStandingPositions:visited.size,routeEdges:route.length-1},route,limitations:['Static geometry check uses sampled parabolas and swept walk-off drops, not Android physics.','Enemy AI, worm projectiles and moving platform timing require playtesting.','The horizontal moving platform is optional.']};
const outputs=new Map();
outputs.set('Assets/Levels/Sewer/'+name+'.lvl',`${R} ${C} Sewer\n`+grid.map(r=>r.join(' ')).join('\n')+'\n');
outputs.set('Assets/MovingColumnLevels/Sewer/'+name+'.lvl',`${R} ${C} Sewer\n`+Array.from({length:R},()=>Array(C).fill(-1).join(' ')).join('\n')+'\n');
const beastieRoot=path.join(root,'Assets/Beasties/BeastieLevelData');
for(const type of fs.readdirSync(beastieRoot)) {
  const src=path.join(beastieRoot,type,component+type+'.txt');
  if(!fs.existsSync(src))continue;
  const rows=objects[type]||[];
  // A populated type must exist in the component; orientations are checked.
  const srcLines=fs.readFileSync(src,'utf8').trim().split(/\r?\n/);
  assert(!rows.length||parseInt(srcLines[0])>0,`Disallowed object type ${type}`);
  if(rows.length && !['SandTiles','MovingPlatforms'].includes(type)){
    const ids=new Set(srcLines.slice(1).map(l=>+l.split(/\s+/)[2]));
    assert(rows.every(row=>ids.has(row[2])),`Disallowed orientation ${type}`);
  }
  outputs.set(`Assets/Beasties/BeastieLevelData/${type}/${name}${type}.txt`,`${rows.length} 32 32\n`+rows.map(r=>entry(type,r).join(' ')).join('\n')+(rows.length?'\n':''));
}
outputs.set('tools/Auto_Sand_L1.validation.json',JSON.stringify(report,null,2)+'\n');
for(const relative of outputs.keys()) if(fs.existsSync(path.join(root,relative))&&!process.argv.includes('--replace-generated'))throw new Error('Refusing overwrite: '+relative);
for(const [relative,data]of outputs){fs.mkdirSync(path.dirname(path.join(root,relative)),{recursive:true});fs.writeFileSync(path.join(root,relative),data);}
console.log(JSON.stringify({files:outputs.size,...report.validation},null,2));


