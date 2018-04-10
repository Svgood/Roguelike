package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Svgood on 29.04.2017.
 */
public class Pointer {
    static int map_posx, map_posy, gomap_posx, gomap_posy;
    Texture img;
    static float wait;
    static boolean touch;

    public Pointer() {
        map_posx = 0;
        map_posy = 0;
        gomap_posx = 10;
        gomap_posy = 10;
        img = new Texture("pointer.png");
    }

    void update() {
        wait += Gdx.graphics.getDeltaTime();

        checkTouch();
        setGo();

        //map_posx = (int)Gdx.input.getX()/32;
        map_posx = Player.map_posx - 20 + (int)Gdx.input.getX()/32;
        //map_posy =  -(int)Gdx.input.getY()/32;
        map_posy = Player.map_posy - 30 + RDG.map_height - (int)Gdx.input.getY()/32;
        // System.out.println("x " + map_posx + "y " + map_posy);
    }

    static boolean onPlayer(){
        if (gomap_posx == Player.map_posx && gomap_posy == Player.map_posy)
            return true;
        else return false;
    }

    static boolean onNPC(){
        for (int i = 0; i < Main.npcs.size; i++)
            if (Main.npcs.get(i).checkPos(gomap_posx, gomap_posy))
                return true;
        return false;
    }

    static void setGo() {
        if (Gdx.input.isTouched() && wait > 0.1){
            if (!onPlayer()){
                wait = 0;
                gomap_posx = Player.map_posx;
                gomap_posy = Player.map_posy;
            }
            else {
                wait = 0;
                Player.moving = true;
                gomap_posx = map_posx;
                gomap_posy = map_posy;
            }
        }
    }

    void checkTouch() {
        if (!Gdx.input.isTouched()) touch = false;
        else touch = true;
    }


    void draw() {
        if (!Inventory.toggle)
            Main.batch.draw(img, map_posx*32, map_posy*32);
    }

}
