
package com.kitty.system.game.scene.aoi;

class Node
{
    long id;
    int x;
    int y;
    Node xPrev;
    Node xNext;
    Node yPrev;
    Node yNext;
    
    Node(final long id, final int x, final int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        final Node node = null;
        this.xNext = node;
        this.xPrev = node;
    }
}
