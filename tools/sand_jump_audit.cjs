// Normalized ballistic arc: y=28*t*(1-t), x gain <=11*t.
// Full-speed apex: (5.5,7). Horizontal release is permitted, never overspeed.
// A half-height head hit yields 5.5 horizontal tiles at original height:
// 11*(0.5*(1-sqrt(0.5))+sqrt(0.5)/2).
function auditJump(grid,source,target,options={}){
 const rise=source.floor-target.floor,H=7,range=11,step=1/220,W=3,BH=2;
 if(rise<-(options.maxDrop||0)||rise>6)return {pass:false,reason:'outside required rise'};
 const direction=(target.x0+target.x1>source.x0+source.x1)?1:-1;
 const end=(1+Math.sqrt(1-rise/H))/2;
 function clear(x,y){
  if(x<0||y<0||x+W>grid[0].length||y+BH>grid.length)return false;
  for(let yy=Math.floor(y+1e-7);yy<Math.ceil(y+BH-1e-7);yy++)for(let xx=Math.floor(x+1e-7);xx<Math.ceil(x+W-1e-7);xx++)if(grid[yy][xx]>=0)return false;
  return true;
 }
 let states=new Map();
 for(let x=source.x0-2.5;x<=source.x1+0.5;x+=0.25){
  if(clear(x,source.floor-2))states.set(x.toFixed(6),{x,takeoff:x,moved:0,held:0});
 }
 let previousTime=0;
 for(let n=1;n<=Math.ceil(end/step);n++){
  const time=Math.min(end,n*step),delta=time-previousTime,next=new Map();
  for(const state of states.values())for(const moving of [true,false]){
   const nx=state.x+(moving?direction*range*delta:0),key=nx.toFixed(6);
   if(next.has(key))continue;
   let ok=true;
   for(const fraction of [0.5,1]){
    const t=previousTime+delta*fraction,x=state.x+(nx-state.x)*fraction;
    if(!clear(x,source.floor-BH-4*H*t*(1-t))){ok=false;break;}
   }
   if(ok)next.set(key,{x:nx,takeoff:state.takeoff,moved:state.moved+(moving?1:0),held:state.held+(moving?0:1)});
  }
  states=next;previousTime=time;
  if(!states.size)return {pass:false,reason:'all full-body trajectories obstructed before landing',rise,atArcTime:time};
 }
 const landings=[...states.values()].filter(s=>Math.min(s.x+W,target.x1+1)-Math.max(s.x,target.x0)>=0.5-1e-6);
 if(!landings.length)return {pass:false,reason:'no supported landing at target height',rise,remainingStates:states.size};
 landings.sort((a,b)=>a.held-b.held);
 const best=landings[0];
 return {pass:true,rise,takeoffLeft:+best.takeoff.toFixed(3),landingLeft:+best.x.toFixed(3),horizontalGain:+Math.abs(best.x-best.takeoff).toFixed(3),arcTime:+end.toFixed(4),horizontalHoldSteps:best.held,stepSecondsNormalized:step,ceilingHits:0,minimumLaunchAndLandingSupport:0.5};
}
function auditHeadHit(grid,source,target){
 const direction=(target.x0+target.x1>source.x0+source.x1)?1:-1,dt=1/1000;
 let best=null;
 function colliding(x,y){
  const tiles=[];
  if(x<0||x+3>grid[0].length||y<0||y+2>grid.length)return [[-1,-1]];
  for(let yy=Math.floor(y+1e-7);yy<Math.ceil(y+2-1e-7);yy++)for(let xx=Math.floor(x+1e-7);xx<Math.ceil(x+3-1e-7);xx++)if(grid[yy][xx]>=0)tiles.push([xx,yy]);
  return tiles;
 }
 for(let launch=source.x0-2.5;launch<=source.x1+0.5;launch+=0.25){
  let oldTop=source.floor-2,hit=null;
  if(colliding(launch,oldTop).length)continue;
  for(let n=1;n<=1200;n++){
   const t=n*dt,x=launch+direction*11*t;
   let top=hit?hit.top+28*(t-hit.time)**2:source.floor-2-28*t*(1-t);
   const descending=hit||t>0.5;
   if(descending&&oldTop+2<=target.floor+1e-7&&top+2>=target.floor-1e-7){
    const landTime=hit?hit.time+Math.sqrt(Math.max(0,(target.floor-2-hit.top)/28)):(1+Math.sqrt(1-(source.floor-target.floor)/7))/2;
    const landX=launch+direction*11*landTime;
    if(hit&&Math.min(landX+3,target.x1+1)-Math.max(landX,target.x0)>=0.5&&!colliding(landX,target.floor-2).length){
     const h=source.floor-(hit.top+2),remaining=Math.sqrt(h/28)*11;
     const overlap=Math.min(landX+3,target.x1+1)-Math.max(landX,target.x0);
     const candidate={pass:true,rise:source.floor-target.floor,takeoffLeft:launch,landingLeft:+landX.toFixed(3),horizontalGain:+Math.abs(landX-launch).toFixed(3),ceilingHits:1,clippedApex:+h.toFixed(3),headHitTime:+hit.time.toFixed(4),sameHeightRangeAfterHeadHit:+(11*hit.time+remaining).toFixed(3),landingSupport:+overlap.toFixed(3),minimumLaunchAndLandingSupport:0.5};
     if(!best||overlap>best.landingSupport)best=candidate;
     break;
    }
   }
   const collisions=colliding(x,top);
   if(collisions.length){
    if(!hit&&t<0.5&&collisions.every(([,y])=>y+1<=oldTop+0.03)){
     const ceiling=Math.max(...collisions.map(([,y])=>y+1));
     const fraction=(oldTop-ceiling)/(oldTop-top);
     hit={top:ceiling,time:t-dt+dt*Math.max(0,Math.min(1,fraction))};top=ceiling;
     if(colliding(x,top).length)break;
    }else break;
   }
   oldTop=top;
  }
 }
 return best||{pass:false,reason:'no supported landing after a physical head-hit fall'};
}
function auditPlatforms(grid,platforms){
 const result=[];
 for(const side of ['left','right']){
  const ps=platforms.filter(p=>side==='left'?p.x1<55:p.x0>=70).sort((a,b)=>b.floor-a.floor);
  for(let i=0;i<ps.length-1;i++){
   const source=ps[i],target=ps[i+1];
   let audit=auditJump(grid,source,target);
   if(!audit.pass){const clipped=auditHeadHit(grid,source,target);if(clipped.pass)audit=clipped;}
   result.push({side,sourceFloor:source.floor,targetFloor:target.floor,sourceColumns:[source.x0,source.x1],targetColumns:[target.x0,target.x1],...audit});
  }
 }
 return {model:{apexHeight:7,fullSpeedApexHorizontal:5.5,fullSpeedSameHeightRange:11,body:[3,2],horizontalControl:'full speed or release; no horizontal overspeed',headCollisionPolicy:'upward velocity becomes zero at contact; gravity starts the falling arc at the clipped height',halfHeightHeadHitSameHeightRange:5.5},transitions:result};
}
module.exports={auditJump,auditHeadHit,auditPlatforms};
