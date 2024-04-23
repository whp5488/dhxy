package com.kitty.system.game.scene.aoi;

class Node {
  long id;
  
  int x;
  
  int y;
  
  Node xPrev;
  
  Node xNext;
  
  Node yPrev;
  
  Node yNext;
  
  Node(long id, int x, int y) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.xPrev = this.xNext = null;
  }
}
