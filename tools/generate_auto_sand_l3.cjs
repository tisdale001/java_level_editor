const fs=require('fs'),path=require('path'),assert=require('assert');
const {auditJump,auditHeadHit}=require('./sand_jump_audit.cjs');
const root=path.resolve(__dirname,'..'),name='Auto_Sand_L3',component='Wally_Sand_Components';
const R=150,C=150,rocks=[100,101,102,103,107,119,126];
const grid=Array.from({length:R},(_,y)=>Array.from({length:C},(_,x)=>rocks[((x*37+y*53)^(x*y*11))%rocks.length]));
// Reference topology: one large winding U, then a right-hand descent.
const nodes=[],edges=[];
function node(x,floor){let n=nodes.find(n=>n.x===x&&n.floor===floor);if(!n){n={id:nodes.length,x,floor,row:floor};nodes.push(n);}return n.id;}
function link(a,b){edges.push([a,b]);}
const mainPoints=[[9,16],[50,16],[50,39],[22,39],[22,62],[22,85],[22,108],[22,131],[58,131],[58,108],[58,85],[82,85],[82,62],[82,39],[82,16],[128,16],[128,131],[134,131]];
const spine=mainPoints.map(([x,f])=>node(x,f));for(let i=1;i<spine.length;i++)link(spine[i-1],spine[i]);
const branchSpecs=[];
for(const[x,f,endX]of branchSpecs)link(node(x,f),node(endX,f));
assert(edges.length===nodes.length-1);
function carve(x0,y0,x1,y1){for(let y=y0;y<=y1;y++)for(let x=x0;x<=x1;x++)grid[y][x]=-1;}
for(const n of nodes)carve(Math.max(7,n.x-8),n.floor-10,Math.min(142,n.x+8),n.floor-1);
const shafts=[],formations=[];
function ledge(x0,x1,floor,style='V',maxDepth=3){
 const mid=(x0+x1)/2;
 for(let x=x0;x<=x1;x++){
  const depth=style==='V'?Math.min(maxDepth,2+Math.max(0,2-Math.floor(Math.abs(x-mid)))):2;
  for(let y=floor;y<floor+depth;y++)grid[y][x]=rocks[(x+y)%rocks.length];
 }
 formations.push({x0,x1,floor,style,minimumThickness:2,maximumThickness:maxDepth});
}
for(const [aId,bId]of edges){
 const a=nodes[aId],b=nodes[bId];
 if(a.row===b.row){carve(Math.max(7,Math.min(a.x,b.x)-8),a.floor-10,Math.min(142,Math.max(a.x,b.x)+8),a.floor-1);}
 else{
  if(a.x===128)continue;
  const upper=a.floor<b.floor?a:b,lower=a.floor<b.floor?b:a,c=upper.x,f=upper.floor;
  carve(c-7,f,c+7,lower.floor-1);
  shafts.push({center:c,upperFloor:f,lowerFloor:lower.floor,upperNode:upper.id,lowerNode:lower.id});
 }
}
// Install formations after all corridor excavation has finished.
for(const shaft of shafts){
 const c=shaft.center,f=shaft.upperFloor;
 ledge(c-7,c-3,f,'flat',2);ledge(c+3,c+7,f,'flat',2);
 ledge(c-7,c-3,f+18,'V',3);
 ledge(c+1,c+5,f+13,'V',3);
 ledge(c-7,c-3,f+8,'V',3);
 ledge(c+1,c+5,f+4,'V',2);
}
// Portals: left near row12 and right near row128 (about20 from bottom).
carve(7,6,17,15);carve(0,10,6,15);
carve(127,121,142,130);carve(143,125,149,130);
// Exact full-width lift shaft. Remove the unused pocket left of its exit.
for(let y=121;y<=130;y++)for(let x=120;x<128;x++)grid[y][x]=rocks[(x+y)%rocks.length];
carve(128,6,137,145);
const lift={x0:128,x1:137,width:10,topStop:16,bottomStop:114,upperMarkerRow:15,lowerMarkerRow:115};
const boxEdits={open:[[38,59,74,69],[101,59,127,68],[95,79,106,120],[104,80,127,90],[66,89,86,94],[72,94,86,136],[30,99,49,106],[111,107,128,115],[93,121,127,130],[35,131,47,145],[48,136,112,145]],close:[[47,95,66,98],[30,121,32,130],[126,116,138,120]]};
for(const b of boxEdits.open)carve(...b);
for(const[x0,y0,x1,y1]of boxEdits.close)for(let y=y0;y<=y1;y++)for(let x=x0;x<=x1;x++)grid[y][x]=rocks[(x+y)%rocks.length];
// Small extensions align the new vertical passages with their ledges and lower galleries.
carve(72,94,86,145);carve(95,90,107,130);
function newStairs(c,top,bottom){ledge(c-7,c-3,top,'flat',2);ledge(c+3,c+7,top,'flat',2);let i=0;for(let f=top+4;f<bottom;f+=5,i++){const left=i%2!==0;ledge(left?c-7:c+1,left?c-3:c+5,f,'V',Math.min(3,bottom-f));}}
newStairs(79,94,146);newStairs(102,90,131);
ledge(35,39,136,'V',3);ledge(43,47,141,'V',3);
// Image4 screenshot origin(5,9), eight pixels per tile.
const image4Open=[[98,22,127,29],[98,39,127,47],[43,39,58,55]];
for(const b of image4Open)carve(...b);
ledge(51,55,40,'V',3);ledge(43,47,45,'V',3);ledge(51,55,50,'V',3);
// Cap exposed terrain without introducing climbable IDs.
for(let y=1;y<R;y++)for(let x=0;x<C;x++)if(grid[y][x]>=0&&grid[y-1][x]===-1)grid[y][x]=x%2?122:123;
function clear(x,y,w=3,h=2){
 if(x<0||y<0||x+w>C||y+h>R)return false;
 for(let yy=Math.floor(y+1e-7);yy<Math.ceil(y+h-1e-7);yy++)for(let xx=Math.floor(x+1e-7);xx<Math.ceil(x+w-1e-7);xx++)if(grid[yy][xx]>=0)return false;
 return true;
}
// Extract actual supported surfaces, including corridors and shaft ledges.
const surfaces=[];
for(let f=2;f<R;f++){
 let start=null;
 for(let x=0;x<=C;x++){
  const good=x<C&&grid[f][x]>=0&&grid[f-1][x]===-1&&grid[f-2][x]===-1;
  if(good&&start===null)start=x;
  if(!good&&start!==null){if(x-start>=3&&!(f===146&&start>=128))surfaces.push({id:surfaces.length,x0:start,x1:x-1,floor:f});start=null;}
 }
}
function auditDrop(source,target){
 const drop=target.floor-source.floor;if(drop<=0||drop>6)return {pass:false};
 const duration=Math.sqrt(drop/28);
 for(const dir of [-1,1]){
  const startX=dir<0?source.x0-3:source.x1+1;
  if(!clear(startX,source.floor-2)||!clear(startX-dir*0.5,source.floor-2))continue;
  for(let speed=0;speed<=11;speed+=0.25){
   let ok=true;for(let n=0;n<=240;n++){let t=duration*n/240;if(!clear(startX+dir*speed*t,source.floor-2+28*t*t)){ok=false;break;}}
   const land=startX+dir*speed*duration;
   if(ok&&Math.min(land+3,target.x1+1)-Math.max(land,target.x0)>=0.5)return {pass:true,mode:'walk-off gravity drop',rise:-drop,takeoffLeft:startX,landingLeft:land,horizontalSpeed:speed,ceilingHits:0};
  }
 }
 return {pass:false};
}
const liftBase=surfaces.length;
const liftSurfaces=[16,30,48,69,90,114].map((floor,i)=>({id:liftBase+i,x0:128,x1:137,floor,virtualLift:true}));
surfaces.push(...liftSurfaces);
for(let y=16;y<=114;y+=0.125){assert(clear(128,y,10,1),'Lift swept terrain collision');assert(clear(131,y-2,3,2),'Lift rider headroom collision');}
const movement=[],adj=surfaces.map(()=>[]);
for(const a of liftSurfaces)for(const b of liftSurfaces)if(a!==b){adj[a.id].push(b.id);movement.push({from:a.id,to:b.id,pass:true,mode:'vertical moving-platform ride',sweep:[16,114],body:[3,2]});}
for(const a of surfaces)for(const b of surfaces){
 if(a.id===b.id||Math.abs(a.floor-b.floor)>6)continue;
 const gap=Math.max(b.x0-a.x1-1,a.x0-b.x1-1);
 if(gap>10)continue;
 let proof=auditJump(grid,a,b,{maxDrop:6});
 if(!proof.pass)proof=auditHeadHit(grid,a,b);
 if(!proof.pass)proof=auditDrop(a,b);
 if(proof.pass){adj[a.id].push(b.id);movement.push({from:a.id,to:b.id,...proof});}
}
const start=surfaces.find(p=>p.floor===16&&p.x0<=9&&p.x1>=11);
const end=surfaces.find(p=>p.floor===131&&p.x0<=147&&p.x1>=149);
assert(start&&end);
const visited=new Set([start.id]),queue=[start.id],parents=new Map();
for(let q=0;q<queue.length;q++)for(const n of adj[queue[q]])if(!visited.has(n)){visited.add(n);queue.push(n);parents.set(n,queue[q]);}
if(!visited.has(end.id))console.error('Unreachable exit; reached surfaces',surfaces.filter(s=>visited.has(s.id)),'all surfaces',surfaces);
assert(visited.has(end.id),'No complete full-body route');
const reversed=surfaces.map(()=>[]);for(const e of movement)reversed[e.to].push(e.from);
const canReturn=new Set([start.id]),rq=[start.id];for(let q=0;q<rq.length;q++)for(const n of reversed[rq[q]])if(!canReturn.has(n)){canReturn.add(n);rq.push(n);}


assert(surfaces.every(s=>visited.has(s.id)&&canReturn.has(s.id)),'A maze surface cannot be reached or returned from');
const nodeAdj=nodes.map(()=>[]);for(const[a,b]of edges){nodeAdj[a].push(b);nodeAdj[b].push(a);}
const deadEnds=nodes.filter(n=>nodeAdj[n.id].length===1&&n.id!==spine[0]&&n.id!==spine[spine.length-1]).map(n=>{
 let current=n.id,previous=-1,length=0;
 while(nodeAdj[current].length<3){const next=nodeAdj[current].find(id=>id!==previous);if(next===undefined)break;length+=Math.abs(nodes[current].x-nodes[next].x)+Math.abs(nodes[current].floor-nodes[next].floor);previous=current;current=next;}
 return {node:n.id,column:n.x,floor:n.floor,intersectionNode:current,lengthTiles:length};
});
assert(deadEnds.every(d=>d.lengthTiles>=15));
const intersections=nodes.filter(n=>nodeAdj[n.id].length>=3);
// Image4 encounters: one lift, three supported corridor patrols, exactly two worms.
const objects={Dogs:[],DogBorderBoxes:[],Scorpions:[],ScorpionBorderBoxes:[],SandTiles:[],MovingPlatforms:[[128,16,10,43,128,15,128,115,44]]};
const patrols=[];
for(const[type,x0,x1,floor,id]of [['Dogs',98,127,30,45],['Scorpions',98,127,48,54],['Scorpions',43,58,56,54]]){
 const width=type==='Dogs'?3.5715:2.8572;
 for(let x=x0;x<=x1+1-width;x+=0.125)assert(clear(x,floor-2.381,width,2.381),'Enemy patrol clearance');
 for(let x=x0;x<=x1;x++)assert(grid[floor][x]>=0,'Enemy patrol floor gap');
 objects[type].push([Math.floor((x0+x1)/2),floor-1,id]);
 objects[type==='Dogs'?'DogBorderBoxes':'ScorpionBorderBoxes'].push([x0-1,floor-1,type==='Dogs'?47:62],[x1+1,floor-1,type==='Dogs'?48:62]);
 patrols.push({type,columns:[x0,x1],floor,fullPatrolSupport:true});
}
for(const[x,y,direction]of [[54,146,0],[100,146,1]]){
 for(let yy=y;yy<R;yy++)for(let xx=x;xx<x+5;xx++){assert(grid[yy][xx]>=0);grid[yy][xx]=241;}
 for(let xx=x-3;xx<=x+5;xx+=0.25)assert(clear(xx,y-2),'Bottom worm traversal clearance');
 objects.SandTiles.push([x,y,direction,5]);
}
assert(objects.SandTiles.length===2);
const allowed=new Set(fs.readFileSync(path.join(root,'Assets/Levels/Sewer',component+'.lvl'),'utf8').trim().split(/\s+/).slice(3).map(Number));
assert(grid.flat().every(v=>allowed.has(v)&&!(v>=214&&v<=223)));
for(let y=0;y<R;y++)for(let x=0;x<C;x++)if(y<4||y>=R-4||x<7||x>=C-7){const portal=(x<7&&y>=10&&y<=15)||(x>=143&&y>=125&&y<=130);assert(portal?grid[y][x]===-1:grid[y][x]>=0);}
const spacingExceptions=[];
for(const shaft of shafts){const ps=formations.filter(p=>p.floor>=shaft.upperFloor&&p.floor<shaft.lowerFloor&&Math.abs((p.x0+p.x1)/2-shaft.center)<8);for(let i=0;i<ps.length;i++)for(let j=i+1;j<ps.length;j++){const a=ps[i],b=ps[j],gap=Math.max(b.x0-a.x1-1,a.x0-b.x1-1),upper=a.floor<=b.floor?a:b,lower=upper===a?b:a,vertical=lower.floor-upper.floor-upper.maximumThickness;if(gap<3&&vertical<4){assert(upper.floor===shaft.upperFloor&&lower.floor===shaft.upperFloor+4,'Unexpected close stationary formations');spacingExceptions.push({center:shaft.center,upperFloor:upper.floor,lowerFloor:lower.floor,reason:'Shaft-mouth lip; exact3x2 traversal independently audited'});}}}
const route=[];for(let id=end.id;id!==undefined;id=parents.get(id))route.push(id);route.reverse();
const validation={dimensions:'150x150 PASS',topology:'Maze Image3 retained plus three Image4 openings; full terrain movement graph validated',image4RedOpenBoxes:3,corridorEnemies:3,sandWormRuns:2,redOpenBoxes:11,yellowCloseBoxes:3,movingPlatforms:1,liftSweep:'columns128–137, top16–114; full platform and rider clearance PASS',boundary:'PASS',paletteAndNoClimbableIds:'PASS',fullBodyJumpArc:'7 high /11 horizontal, ceiling-hit reduction PASS',supportedSurfaces:surfaces.length,reachableSurfaces:visited.size,returnableSurfaces:canReturn.size,validatedTransitions:movement.length,routeSurfaceCount:route.length};
const report={image4Open,image4Mapping:{tilePixels:8,originPixelX:5,originPixelY:9},screenshotMapping:{tilePixels:8,xOffsetPixels:4,yOffsetPixels:436},boxEdits,lift,mainPoints,branchSpecs,spacingExceptions,name,component,rows:R,columns:C,entry:{edge:'left',rows:[10,15],floor:16},exit:{edge:'right',rows:[125,130],floor:131},spawn:{column:9,row:13},nodes,edges,intersections,deadEnds,shafts,formations,surfaces,movement,route,patrols,objects,validation,limitations:['Ballistic geometry audit is not Android gameplay simulation.','Jump audit permits horizontal release and at least0.5tile of launch/landing support.','Enemy behavior and maze pacing require playtesting.']};
const outputs=new Map();
outputs.set(`Assets/Levels/Sewer/${name}.lvl`,`${R} ${C} Sewer\n`+grid.map(r=>r.join(' ')).join('\n')+'\n');
outputs.set(`Assets/MovingColumnLevels/Sewer/${name}.lvl`,`${R} ${C} Sewer\n`+Array.from({length:R},()=>Array(C).fill(-1).join(' ')).join('\n')+'\n');
const beastieRoot=path.join(root,'Assets/Beasties/BeastieLevelData');
for(const type of fs.readdirSync(beastieRoot)){
 const src=path.join(beastieRoot,type,component+type+'.txt');if(!fs.existsSync(src))continue;
 const lines=fs.readFileSync(src,'utf8').trim().split(/\r?\n/),rows=objects[type]||[];
 assert(!rows.length||parseInt(lines[0])>0);
 if(rows.length&&!['SandTiles','MovingPlatforms'].includes(type)){const ids=new Set(lines.slice(1).map(l=>+l.split(/\s+/)[2]));assert(rows.every(r=>ids.has(r[2])));}
 const data=rows.map(r=>r.map((v,i)=>type==='MovingPlatforms'?([0,1,4,5,6,7].includes(i)?v*32:v):(i<2?v*32:v)).join(' '));
 outputs.set(`Assets/Beasties/BeastieLevelData/${type}/${name}${type}.txt`,`${rows.length} 32 32\n`+data.join('\n')+(rows.length?'\n':''));
}
outputs.set(`tools/${name}.validation.json`,JSON.stringify(report,null,2)+'\n');
for(const p of outputs.keys())if(fs.existsSync(path.join(root,p))&&!process.argv.includes('--replace-generated'))throw new Error('Refusing overwrite '+p);
for(const[p,data]of outputs){fs.mkdirSync(path.dirname(path.join(root,p)),{recursive:true});fs.writeFileSync(path.join(root,p),data);}
if(process.argv.includes('--sync-game')){
 const gameAssets=path.resolve(root,'../cat_game/Proto/app/src/main/assets');assert(fs.existsSync(gameAssets));let copied=0;const hashes=[];
 for(const[p,data]of outputs){if(!p.startsWith('Assets/'))continue;const relative=p.startsWith('Assets/Levels/Sewer/')?`${name}.lvl`:p.replace(/^Assets\/Beasties\/BeastieLevelData\//,'Beasties/').replace(/^Assets\//,'');const dest=path.join(gameAssets,relative);assert(dest.startsWith(gameAssets+path.sep)&&path.basename(dest).startsWith(name));fs.mkdirSync(path.dirname(dest),{recursive:true});fs.writeFileSync(dest,data);assert(fs.readFileSync(dest,'utf8')===fs.readFileSync(path.join(root,p),'utf8'));hashes.push({editor:p,game:dest,sha256:require('crypto').createHash('sha256').update(fs.readFileSync(dest)).digest('hex')});copied++;}assert(copied===29);fs.writeFileSync(path.join(root,'tools',name+'.sync.json'),JSON.stringify({files:copied,byteEquality:'PASS',hashes},null,2)+'\n');console.log(`Synced29 verified ${name} files.`);
}
console.log(JSON.stringify(validation,null,2));



