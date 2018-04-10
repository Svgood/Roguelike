package com.rogue.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Svgood on 29.04.2017.
 */
public class Floor {
    static Texture img = new Texture("floor1.png");
    static Texture img1 = new Texture("floor1_visited.png");
    Rectangle rect;
    int map_posx, map_posy;

    public Floor(int x, int y){
        map_posx = x;
        map_posy = y;
        rect = new Rectangle(x*32,y*32,img.getWidth(), img.getHeight());
    }

    void update() {

    }

    void draw(){
        if (FOV.fov_map[map_posx][map_posy].equals("V") )
            Main.batch.draw(img, rect.x, rect.y);
        if (FOV.fov_map[map_posx][map_posy].equals("S") )
            Main.batch.draw(img1, rect.x, rect.y);
    }
}
