package com.rogue.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Svgood on 31.05.2017.
 */
public class Chests {
    Texture img, img1;
    int map_posx, map_posy;
    boolean opened;

    public Chests(int x, int y){
        opened = false;
        map_posx = x;
        map_posy = y;
        img = new Texture("chest1.png");
        img1 = new Texture("chest2.png");
    }

    public void draw(){
        if (!opened)
            Main.batch.draw(img, map_posx*32, map_posy*32);
        else Main.batch.draw(img1, map_posx*32, map_posy*32);
    }

    public void update(){

    }
}
