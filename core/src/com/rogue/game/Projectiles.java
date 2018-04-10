package com.rogue.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by svgood on 25.05.17.
 */
public class Projectiles {

    int map_posx, map_posy;
    int move_to_x, move_to_y;
    boolean remove;
    Texture img;

    public Projectiles(int x1, int y1, int x2, int y2){
        map_posx = x1;
        map_posy = y1;
        move_to_x = x2;
        move_to_y = y2;
        img = new Texture("troll.png");
        remove = false;
    }

    void update(){
        move_to();
    }

    void draw(){
        Main.batch.draw(img, map_posx*32, map_posy*32);
    }

    void move_to(){
        if (map_posx < move_to_x) map_posx += 1;
        if (map_posx > move_to_x) map_posx -= 1;
        if (map_posy < move_to_y) map_posy += 1;
        if (map_posy > move_to_y) map_posy -= 1;
        if (map_posx == move_to_x && map_posy == move_to_y) remove = true;
    }
}
