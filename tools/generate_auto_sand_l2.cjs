// Component-limited Sand descent/crossing/ascent. Run with Node from any folder.
const fs=require('fs'),path=require('path'),assert=require('assert');
const root=path.resolve(__dirname,'..'),name='Auto_Sand_L2',component='Wally_Sand_Components';
const R=150,C=125,rocks=[100,101,102,103,107,119,126];
const grid=Array.from({length:R},(_,y)=>Array.from({length:C},(_,x)=>rocks[((x*37+y*53)^(x*y*11))%rocks.length]));
function carve(x0,y0,x1,y1){for(let y=y0;y<=y1;y++)for(let x=x0;x<=x1;x++)grid[y][x]=-1;}
// The central solid sand wall forces the low crossing before the ascent.
for(let y=4;y<138;y++)for(let x=55;x<=69;x++)grid[y][x]=241;
carve(7,4,54,137);carve(70,4,117,137);carve(7,128,117,137);
// Keep the mandatory four-row cap, then add 3/4/5 rows of natural ceiling.
// Short five-column shoulders and wider recessed-middle formations vary it.
const ceilingProfile=[];
for(const [start,end]of [[7,54],[70,117]])for(let x=start;x<=end;x++){
 const local=x-start;
 let addedDepth=3;
 if((local>=5&&local<=9)||(local>=38&&local<=42))addedDepth=4;
 if(local>=18&&local<=30)addedDepth=(local>=22&&local<=26)?5:4;
 const filledDepth=4+addedDepth;
 for(let y=4;y<filledDepth;y++)grid[y][x]=rocks[(x+y)%rocks.length];
 ceilingProfile.push({column:x,additionalDepth:addedDepth,totalFilledDepth:filledDepth});
}
carve(0,22,6,27);carve(118,12,124,17);
const platforms=[];
function formation(x0,x1,floor,style='V'){
  for(let x=x0;x<=x1;x++){
    const mid=(x0+x1)/2,dist=Math.abs(x-mid);
    const depth=style==='V'?2+Math.max(0,2-Math.floor(dist/2)):2+(dist>4?2:dist>2?1:0);
    for(let y=floor;y<floor+depth;y++)grid[y][x]=rocks[(x+y)%rocks.length];
  }
  platforms.push({x0,x1,floor,minimumThickness:2,maximumThickness:4,style});
}
formation(7,27,28,'U');
const smallPlatformFloors={left:[43,73,123],right:[113,78,33]};
const connectors=[];
const leftConnections=new Map([[33,'divider'],[48,'outer'],[63,'divider'],[88,'outer'],[103,'divider'],[118,'outer']]);
const rightConnections=new Map([[128,'outer'],[113,'divider'],[98,'outer'],[83,'divider'],[68,'outer'],[53,'divider'],[38,'outer'],[23,'divider']]);
for(let i=1;i<=21;i++){
 const floor=28+i*5,small=smallPlatformFloors.left.includes(floor);
 let x0=i%2?34:14,x1=i%2?(small?42:46):26;
 const wall=leftConnections.get(floor);
 if(wall==='outer'){x0=7;x1=30;}
 if(wall==='divider'){x0=floor===33?31:30;x1=54;}
 formation(x0,x1,floor,i%3?'V':'U');
 if(wall)connectors.push({side:'left',wall,floor,columns:[x0,x1]});
}
for(let i=1;i<=24;i++){
 const floor=138-i*5,small=smallPlatformFloors.right.includes(floor);
 let x0=i%2?(small?81:77):93,x1=i%2?89:(small?101:105);
 const wall=rightConnections.get(floor);
 if(wall==='outer')x1=117;
 if(wall==='divider')x0=70;
 formation(x0,x1,floor,'V');
 if(wall)connectors.push({side:'right',wall,floor,columns:[x0,x1]});
}
// Join the final ledge to the right portal without climbable supports.
for(let x=106;x<C;x++)for(let y=18;y<22;y++)grid[y][x]=rocks[(x+y)%rocks.length];
// Walls remain straight apart from the explicit, documented connected shelves.
for(let y=1;y<R;y++)for(let x=0;x<C;x++)if(grid[y][x]>=0&&grid[y-1][x]===-1)grid[y][x]=x%2?122:123;
// Relieve only thick U-shaped lips that stop the upward arc before a usable
// landing. Keep the top two rows, wall attachments and all tread coordinates.
const jumpClearanceRepairs=[
 {overheadFloor:88,columns:[24,30],removedRows:[90,91],sourceFloor:98,targetFloor:93},
 {overheadFloor:103,columns:[30,36],removedRows:[105,106],sourceFloor:113,targetFloor:108},
 {overheadFloor:118,columns:[24,30],removedRows:[120,121],sourceFloor:128,targetFloor:123}
];
for(const repair of jumpClearanceRepairs)for(const y of repair.removedRows)for(let x=repair.columns[0];x<=repair.columns[1];x++)grid[y][x]=-1;
const preservedTiles=JSON.parse(fs.readFileSync(path.join(__dirname,'Auto_Sand_L2.preserved_tiles.json'),'utf8')).tiles;
for(const tile of preservedTiles)grid[tile.row][tile.column]=tile.id;
const sand=[[56,138,0,5],[64,138,1,5]];
for(const [x,y,,len]of sand)for(let yy=y;yy<y+5;yy++)for(let xx=x;xx<x+len;xx++)grid[yy][xx]=241;
const objects={
 Dogs:[[36,82,45],[98,87,46]],
 DogBorderBoxes:[[33,82,47],[47,82,48],[92,87,47],[106,87,48]],
 Scorpions:[[20,57,55],[20,107,54],[98,47,55]],
 ScorpionBorderBoxes:[[13,57,62],[27,57,62],[13,107,62],[27,107,62],[92,47,62],[106,47,62]],
 SandTiles:sand,
 // Game bounds exclude border-marker tiles; four-tile-wide carriers.
 MovingPlatforms:[[48,61,4,43,49,44,49,63,44],[72,108,4,43,73,91,73,110,44],[56,133,4,43,50,133,75,133,44]]
};
const allowed=new Set(fs.readFileSync(path.join(root,'Assets/Levels/Sewer',component+'.lvl'),'utf8').trim().split(/\s+/).slice(3).map(Number));
assert(grid.flat().every(v=>allowed.has(v)&&!(v>=214&&v<=223)),'Disallowed terrain');
for(let y=0;y<R;y++)for(let x=0;x<C;x++)if(y<4||y>=R-4||x<7||x>=C-7){
 const portal=(x<7&&y>=22&&y<=27)||(x>=118&&y>=12&&y<=17);
 assert(portal?grid[y][x]===-1:grid[y][x]>=0,`Boundary ${x},${y}`);
}
function clear(x,y){
 if(x<0||y<0||x+3>C||y+2>R)return false;
 for(let yy=Math.floor(y+1e-6);yy<Math.ceil(y+2-1e-6);yy++)for(let xx=Math.floor(x+1e-6);xx<Math.ceil(x+3-1e-6);xx++)if(grid[yy][xx]>=0)return false;
 return true;
}
function stand(x,f){return clear(x,f-2)&&[0,1,2].every(dx=>grid[f]?.[x+dx]>=0);}
const jumpAudit=require('./sand_jump_audit.cjs').auditPlatforms(grid,platforms);
assert(jumpAudit.transitions.every(t=>t.pass),'Seven-high/eleven-wide full-body jump audit failed');
const barelyReference=jumpAudit.transitions.find(t=>t.side==='left'&&t.sourceFloor===38&&t.targetFloor===33);
assert(barelyReference?.pass&&barelyReference.ceilingHits===0,'BARELY reference changed');
for(const p of platforms)for(let x=p.x0;x<=p.x1;x++)assert(grid[p.floor][x]>=0&&grid[p.floor+1][x]>=0);
for(let i=0;i<platforms.length;i++)for(let j=i+1;j<platforms.length;j++){
 const a=platforms[i],b=platforms[j],h=Math.max(b.x0-a.x1-1,a.x0-b.x1-1);
 const upper=a.floor<b.floor?a:b,lower=a.floor<b.floor?b:a;
 const vertical=lower.floor-(upper.floor+upper.maximumThickness);
 assert(h>=3||vertical>=4,`Platform spacing ${i}/${j}`);
}
for(const type of ['Dogs','Scorpions'])for(const[x,y]of objects[type])assert(grid[y][x]===-1&&grid[y+1][x]>=0,`Unsupported ${type}`);
const patrols=[];
// Border boxes occupy one tile. Placing them immediately outside the ledge
// lets the sprite reach both edges before its collision reverses direction.
for(const [type,borderType,width,height]of [['Dogs','DogBorderBoxes',3.5715,2.381],['Scorpions','ScorpionBorderBoxes',2.8572,2.381]]){
 for(const[x,y]of objects[type]){
  const p=platforms.find(p=>p.floor===y+1&&x>=p.x0&&x<=p.x1);
  assert(p&&p.x1-p.x0+1>=13,'Enemy must be on a long formation');
  const borders=objects[borderType].filter(b=>b[1]===y).map(b=>b[0]).sort((a,b)=>a-b);
  assert(borders.length===2&&borders[0]===p.x0-1&&borders[1]===p.x1+1,'Patrol does not span full platform');
  const right=p.x1+1-width,top=p.floor-height;
  for(let px=p.x0;px<=right;px+=0.125){
   for(let yy=Math.floor(top);yy<p.floor;yy++)for(let xx=Math.floor(px);xx<Math.ceil(px+width);xx++)assert(grid[yy][xx]===-1,'Enemy patrol headroom blocked');
  }
  patrols.push({type,floor:p.floor,platformColumns:[p.x0,p.x1],borderColumns:borders,spriteLeftEdgeRange:[p.x0,right]});
 }
}
assert(platforms.filter(p=>p.x1-p.x0+1===9).length===5,'Expected five remaining short platforms');
// Preserve the user's exact eleven top tiles, thickening underneath them.
for(let x=44;x<=54;x++)assert(grid[33][x]===(x%2?122:123)&&grid[34][x]>=0,'Manual platform1 edit lost');
for(const c of connectors){
 const attached=c.wall==='outer'?(c.side==='left'?c.columns[0]===7:c.columns[1]===117):(c.side==='left'?c.columns[1]===54:c.columns[0]===70);
 assert(attached,'Disconnected shelf');
}
for(const side of ['left','right']){
 const floors=connectors.filter(c=>c.side===side).map(c=>c.floor).sort((a,b)=>a-b);
 for(let i=1;i<floors.length;i++)assert((floors[i]-floors[i-1])/5>=2&&(floors[i]-floors[i-1])/5<=5,'Connector interval outside2–5 platforms');
}
// Every possible three-column vertical fall corridor in the left shaft meets
// an intentional baffle between the entry and bottom crossing.
for(let x=7;x<=52;x++)assert(connectors.filter(c=>c.side==='left'&&c.floor>=48&&c.floor<=118).some(c=>x<c.columns[1]+1&&x+3>c.columns[0]),'Uninterrupted descent lane');
for(const p of ceilingProfile){
 for(let y=0;y<p.totalFilledDepth;y++)assert(grid[y][p.column]>=0,'Broken ceiling cap');
 assert(grid[p.totalFilledDepth][p.column]===-1,'Ceiling profile differs');
}
assert(ceilingProfile.filter(p=>p.additionalDepth===3).length>ceilingProfile.length/2,'Three-row ceiling depth must predominate');
for(const p of objects.MovingPlatforms){
 const[x,y,n,,minX,minY,maxX,maxY]=p;
 const vertical=minX===maxX;
 const lo=vertical?minY+1:minX+1,hi=vertical?maxY-1:maxX-n;
 for(let s=lo;s<=hi;s+=0.25){
  const px=vertical?x:s,py=vertical?s:y;
  for(let dx=0;dx<n;dx++)assert(grid[Math.floor(py)][Math.floor(px+dx)]===-1,'Carrier intersects terrain');
  for(let rider=0;rider<=n-3;rider+=0.5)assert(clear(px+rider,py-2),'Rider headroom');
 }
}
function jump(a,b){
 const rise=a.f-b.f;if(rise<0||rise>5||Math.abs(a.x-b.x)>10)return false;
 const apex=6,tEnd=(1+Math.sqrt(1-rise/apex))/2;
 for(let k=0;k<=120;k++){const t=k/120*tEnd;if(!clear(a.x+(b.x-a.x)*t/tEnd,a.f-2-4*apex*t*(1-t)))return false;}
 return true;
}
function drop(a,b){
 if(b.f<=a.f||b.f-a.f>5||Math.abs(a.x-b.x)>10)return false;
 const direction=Math.sign(b.x-a.x);
 for(let gap=a.x+direction;direction>0?gap<=b.x:gap>=b.x;gap+=direction){
  if(![0,1,2].every(dx=>grid[a.f]?.[gap+dx]===-1))continue;
  let ok=true;
  for(let k=0;k<=60;k++)if(!clear(a.x+(gap-a.x)*k/60,a.f-2)){ok=false;break;}
  if(!ok)continue;
  // Leave the ledge fully, descend two rows to clear the previous formation's
  // underside, then steer toward the next landing during the remaining fall.
  for(let k=0;k<=120;k++){
   const fallen=(b.f-a.f)*k/120;
   const steer=Math.max(0,(fallen-2)/(b.f-a.f-2));
   if(!clear(gap+(b.x-gap)*steer,a.f-2+fallen)){ok=false;break;}
  }
  if(ok)return true;
 }
 return false;
}
const states=[];for(let f=2;f<R;f++)for(let x=0;x<=C-3;x++)if(stand(x,f))states.push({x,f});
const intendedFloors=new Set([...platforms.map(p=>p.floor),138]);
assert(states.every(s=>intendedFloors.has(s.f)),'Incidental reachable wall ledge');
const byKey=new Map(states.map((s,i)=>[s.x+','+s.f,i])),start=byKey.get('9,28'),goal=byKey.get('122,18');
assert(start!==undefined&&goal!==undefined);
const queue=[start],visited=new Set(queue),parents=new Map();
for(let q=0;q<queue.length;q++){
 const i=queue[q],a=states[i];
 for(let dx=-10;dx<=10;dx++)for(let rise=-5;rise<=5;rise++){
  if(dx===0)continue;const j=byKey.get((a.x+dx)+','+(a.f-rise));if(j===undefined||visited.has(j))continue;
  const b=states[j],walk=rise===0&&Math.abs(dx)===1&&clear((a.x+b.x)/2,a.f-2);
  if(walk||drop(a,b)||jump(a,b)){visited.add(j);queue.push(j);parents.set(j,i);}
 }
}
if(!visited.has(goal))console.error('Right reachable:',[...visited].map(i=>states[i]).filter(s=>s.x>70&&s.f<138),'Reachable floors:',[...new Set([...visited].map(i=>states[i].f))].sort((a,b)=>a-b));
assert(visited.has(goal),'No fixed-terrain full-body route');
const route=[];for(let i=goal;i!==undefined;i=parents.get(i))route.push(states[i]);route.reverse();
assert(route.some(s=>s.f===138&&s.x>=55&&s.x<=69),'Route does not cross bottom divider');
const validation={dimensions:'150 rows x125 columns PASS',componentPalette:'PASS',noClimbableIds:'PASS',boundary:'PASS',jumpArcAudit:`${jumpAudit.transitions.length} adjacent upward jumps PASS (7-high/11-wide arc, full3x2body, clipped head-hit falls)`,barelyReference:'floor38 to33 unchanged PASS',manualPlatform1:'eleven original row33 tiles preserved; row34 support PASS',wallConnections:'six left, eight right; intervals2–5 platforms PASS',noStraightDescentLane:'every3-wide vertical lane meets a left baffle PASS',ceilingProfile:'4-row cap plus mostly3, some4, central5 rows PASS',shorterPlatforms:'five empty9-tile ledges PASS',fullLengthEnemyPatrols:'all five enemies on13-tile platforms PASS',noIncidentalWallLedges:'PASS',stationaryThickness:'2–4 tiles PASS',stationarySpacing:'>=3 horizontal empty columns OR>=4 vertical empty rows PASS',enemySupport:'PASS',movingPlatformSweepAndRider:'PASS',fullBodyRoute:'PASS',body:[3,2],maxJumpRise:5,maxHorizontalDisplacement:10,maxDrop:5,reachableStandingPositions:visited.size,routeEdges:route.length-1};
const report={name,component,rows:R,columns:C,entry:{edge:'left',rows:[22,27],floor:28},exit:{edge:'right',rows:[12,17],floor:18},spawn:{column:9,row:25},bottomCrossing:{floor:138,dividerColumns:[55,69],ceiling:128},preservedManualTiles:{row:33,columns:[44,54],addedSupportRow:34},jumpClearanceRepairs,jumpAudit,connectors,ceilingProfile,smallPlatformFloors,patrols,platforms,objects,validation,route,limitations:['Sampled geometry validation is not Android physics simulation.','Jump audit allows edge launch/landing with at least0.5tile support and horizontal release; head hits zero vertical velocity and shorten the arc.','Moving platforms are optional alternatives; fixed ledges provide the validated route.','Worm attacks, enemy behavior, lift boarding and pacing require playtesting.']};
const outputs=new Map();
outputs.set(`Assets/Levels/Sewer/${name}.lvl`,`${R} ${C} Sewer\n`+grid.map(r=>r.join(' ')).join('\n')+'\n');
outputs.set(`Assets/MovingColumnLevels/Sewer/${name}.lvl`,`${R} ${C} Sewer\n`+Array.from({length:R},()=>Array(C).fill(-1).join(' ')).join('\n')+'\n');
const beastieRoot=path.join(root,'Assets/Beasties/BeastieLevelData');
for(const type of fs.readdirSync(beastieRoot)){
 const src=path.join(beastieRoot,type,component+type+'.txt');if(!fs.existsSync(src))continue;
 const lines=fs.readFileSync(src,'utf8').trim().split(/\r?\n/),rows=objects[type]||[];
 assert(!rows.length||parseInt(lines[0])>0,`Disallowed ${type}`);
 if(rows.length&&!['SandTiles','MovingPlatforms'].includes(type)){const ids=new Set(lines.slice(1).map(l=>+l.split(/\s+/)[2]));assert(rows.every(r=>ids.has(r[2])));}
 const data=rows.map(r=>r.map((v,i)=>type==='MovingPlatforms'?([0,1,4,5,6,7].includes(i)?v*32:v):(i<2?v*32:v)).join(' '));
 outputs.set(`Assets/Beasties/BeastieLevelData/${type}/${name}${type}.txt`,`${rows.length} 32 32\n`+data.join('\n')+(rows.length?'\n':''));
}
outputs.set(`tools/${name}.validation.json`,JSON.stringify(report,null,2)+'\n');
for(const p of outputs.keys())if(fs.existsSync(path.join(root,p))&&!process.argv.includes('--replace-generated'))throw new Error('Refusing overwrite '+p);
for(const[p,data]of outputs){fs.mkdirSync(path.dirname(path.join(root,p)),{recursive:true});fs.writeFileSync(path.join(root,p),data);}
if(process.argv.includes('--sync-game')){
 const gameAssets=path.resolve(root,'../cat_game/Proto/app/src/main/assets');
 assert(fs.existsSync(gameAssets),'Expected sibling Cat_Game assets folder');
 let copied=0;
 for(const[p,data]of outputs){
  if(!p.startsWith('Assets/'))continue;
  const relative=p.startsWith('Assets/Levels/Sewer/')?`${name}.lvl`:p.replace(/^Assets\/Beasties\/BeastieLevelData\//,'Beasties/').replace(/^Assets\//,'');
  const dest=path.join(gameAssets,relative);
  assert(dest.startsWith(gameAssets+path.sep)&&path.basename(dest).startsWith(name),'Unexpected sync destination');
  fs.mkdirSync(path.dirname(dest),{recursive:true});fs.writeFileSync(dest,data);
  assert(fs.readFileSync(dest,'utf8')===fs.readFileSync(path.join(root,p),'utf8'),'Game/editor copy differs');copied++;
 }
 assert(copied===29);console.log(`Synced and verified ${copied} Auto_Sand_L2 game files.`);
}
console.log(JSON.stringify({files:outputs.size,...validation},null,2));


