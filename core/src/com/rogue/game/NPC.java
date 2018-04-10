package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Svgood on 24.04.2017.
 */
public class NPC {
    Texture img;
    Texture img1;
    float animtriggercount;
    boolean animtrigger;


    Rectangle rect;

    float hp;
    float attack;
    int map_posx, map_posy;
    int move_to_x, move_to_y;
    int id;
    static int total_npc = 0;
    boolean triggered;


    //Pathfinfing
    int pathk, pathx, pathy, min, checkx, checky;
    boolean find, look;
    int counter;

    int rand;

    public NPC(int x, int y){
        total_npc += 1;
        id = total_npc;
        counter = 0;
        hp = 100;
        attack = 5;
        move_to_x = 0;
        move_to_y = 0;
        map_posx = x;
        map_posy = y;

        img = new Texture("alien.png");
        img1 = new Texture("alien1.png");
        animtrigger = true;
        animtriggercount = 0;

        rect = new Rectangle(x*32,y*32,img.getWidth(), img.getHeight());

        triggered = false;
        rand = (int)MathUtils.random(0, RDG.rooms.size-1);
    }

    void update(){
        if (Functions.distance(Player.map_posx, Player.map_posy, map_posx ,map_posy) < 7 && !triggered){
            triggered = true;
            LogWindow.push_message("Alien " + id + " saw u!");
        }

        if (Functions.distance(Player.map_posx, Player.map_posy, map_posx ,map_posy) <= 1.5) {
            rand = MathUtils.random(-1,1);
            Player.hp -= (attack - rand);
            LogWindow.push_message("Alien " + id + " attacked on " + (attack-rand) + " hp");
        }
        if (!triggered) {
            /*
            goTo(RDG.rooms.get(rand).midx, RDG.rooms.get(rand).midy);
            for (int i = map_posx - 1; i <= map_posx + 1; i++)
                for (int k = map_posy - 1; k <= map_posy + 1; k++)
                    if (i == RDG.rooms.get(rand).midx && k == RDG.rooms.get(rand).midy) {
                        rand = (int) MathUtils.random(0, RDG.rooms.size - 1);
                    }
                    */
        }
        else goTo(Player.map_posx, Player.map_posy);
        RDG.map[map_posx][map_posy] = "nG";
    }

    void goTo(int x, int y) {
        /*
        move_to_x = MathUtils.random(-1,1);
        move_to_y = MathUtils.random(-1,1);
        if (RDG.map[map_posx+move_to_x][map_posy+move_to_y].equals(".")){
            RDG.map[map_posx][map_posy] = ".";
            map_posx += move_to_x;
            map_posy += move_to_y;
            rect.x += move_to_x*32;
            rect.y += move_to_y*32;
        }
        */
        look = true;
        //Pathfinding to player
        for (int i = map_posx - 1; i <= map_posx + 1; i++)
            for (int k = map_posy - 1; k <= map_posy + 1; k++)
                if (i == x && k == y) {
                    look = false;
                }

        if (look) {
            find = true;
            pathk = 0;
            pathx = 0;
            pathy = 0;
            min = 999;
            RDG.pathmap[map_posx][map_posy] = "0";
            while (find) {
                for (int i = map_posx - pathk; i <= map_posx + pathk; i++)
                    for (int k = map_posy - pathk; k <= map_posy + pathk; k++) {
                        if (i + 1 < RDG.map_width && k < RDG.map_height && k >= 0 && i >= 0) {
                            if (RDG.pathmap[i + 1][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i + 1][k] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if ( i + 1 == x && k == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i < RDG.map_width && k + 1 < RDG.map_height && k >= 0 && i >= 0) {
                            if (RDG.pathmap[i][k + 1].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i][k + 1] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i == x && k + 1== y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i < RDG.map_width && k < RDG.map_height && k >= 0 && i - 1 >= 0) {
                            if (RDG.pathmap[i - 1][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i - 1][k] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i - 1 == x && k == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i < RDG.map_width && k < RDG.map_height && k - 1 >= 0 && i >= 0) {
                            if (RDG.pathmap[i][k - 1].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i][k - 1] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i == x && k - 1 == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i + 1 < RDG.map_width && k + 1 < RDG.map_height && k >= 0 && i >= 0) {
                            if (RDG.pathmap[i + 1][k + 1].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i + 1][k + 1] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i + 1 == x && k + 1 == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i < RDG.map_width && k < RDG.map_height && k - 1 >= 0 && i - 1 >= 0) {
                            if (RDG.pathmap[i - 1][k - 1].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i - 1][k - 1] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i - 1 == x && k - 1 == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i + 1 < RDG.map_width && k < RDG.map_height && k - 1 >= 0 && i >= 0) {
                            if (RDG.pathmap[i + 1][k - 1].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i + 1][k - 1] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i + 1 == x && k - 1 == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (i < RDG.map_width && k + 1 < RDG.map_height && k >= 0 && i - 1 >= 0) {
                            if (RDG.pathmap[i - 1][k + 1].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")
                                    && !RDG.pathmap[i][k].equals("@"))
                                RDG.pathmap[i - 1][k + 1] = "" + (Integer.parseInt(RDG.pathmap[i][k]) + 1);
                            else if (i - 1 == x && k + 1 == y && !RDG.pathmap[i][k].substring(0,1).equals("#")
                                    && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("n")) {
                                find = false;
                                pathx = x;
                                pathy = y;
                            }
                        }
                        if (pathk > 40) find = false;
                    }
                pathk += 1;
            }

            for (int i = x - 1; i <= x + 1; i++)
                for (int k = y - 1; k <= y + 1; k++)
                    if (RDG.pathmap[i][k].equals(".")) RDG.pathmap[i][k] = "999";

            find = true;
            checkx = pathx;
            checky = pathy;
            counter = 0;

            while (find) {
                int k = checky;
                int i = checkx;
                for (i = checkx - 1; i <= checkx + 1; i++)
                    if (i >= 0 && k >= 0 && i < RDG.map_width && k < RDG.map_height)
                        if (!RDG.pathmap[i][k].substring(0,1).equals("#") && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                && !RDG.pathmap[i][k].substring(0,1).equals("@") && !RDG.pathmap[i][k].substring(0,1).equals("n"))
                            if (Integer.parseInt(RDG.pathmap[i][k]) < min) {
                                if (i == map_posx && k == map_posy) {
                                    if (RDG.map[checkx][checky].equals(".")) {
                                        RDG.map[map_posx][map_posy] = ".";
                                        map_posx = checkx;
                                        map_posy = checky;
                                        rect.x = checkx * 32;
                                        rect.y = checky * 32;
                                        find = false;
                                    }
                                    find = false;
                                }
                                min = Integer.parseInt(RDG.pathmap[i][k]);
                                pathx = i;
                                pathy = k;
                            }
                i = checkx;
                for (k = checky - 1; k <= checky + 1; k++)
                    if (i >= 0 && k >= 0 && i < RDG.map_width && k < RDG.map_height)
                        if (!RDG.pathmap[i][k].substring(0,1).equals("#") && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                && !RDG.pathmap[i][k].substring(0,1).equals("@") && !RDG.pathmap[i][k].substring(0,1).equals("n"))
                            if (Integer.parseInt(RDG.pathmap[i][k]) < min) {
                                if (i == map_posx && k == map_posy) {
                                    if (RDG.map[checkx][checky].equals(".")) {
                                        RDG.map[map_posx][map_posy] = ".";
                                        map_posx = checkx;
                                        map_posy = checky;
                                        rect.x = checkx * 32;
                                        rect.y = checky * 32;
                                        find = false;
                                    }
                                    find = false;
                                }
                                min = Integer.parseInt(RDG.pathmap[i][k]);
                                pathx = i;
                                pathy = k;
                            }
                for (i = checkx - 1; i <= checkx + 1; i++)
                    for (k = checky - 1; k <= checky + 1; k++)
                        if (i >= 0 && k >= 0 && i < RDG.map_width && k < RDG.map_height)
                            if (!RDG.pathmap[i][k].substring(0,1).equals("#") && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("@") && !RDG.pathmap[i][k].substring(0,1).equals("n"))
                                if (Integer.parseInt(RDG.pathmap[i][k]) < min) {
                                    if (i == map_posx && k == map_posy) {
                                        if (RDG.map[checkx][checky].equals(".")) {
                                            RDG.map[map_posx][map_posy] = ".";
                                            map_posx = checkx;
                                            map_posy = checky;
                                            rect.x = checkx * 32;
                                            rect.y = checky * 32;
                                            find = false;
                                        }
                                        find = false;
                                    }
                                    min = Integer.parseInt(RDG.pathmap[i][k]);
                                    pathx = i;
                                    pathy = k;
                                }
                checkx = pathx;
                checky = pathy;
                counter += 1;
                //System.out.println("Going home " + counter);
                if (counter > 30) find = false;
            }

            RDG.resetPathMap();
        }
    }

    boolean checkPos(int xpos,int ypos) {
        if (xpos == map_posx && ypos == map_posy) return true;
        else return false;
    }

    void death(){
        LogWindow.push_message("Alien died!");
        rand = MathUtils.random(33, 55);
        Player.exp += rand;
        LogWindow.push_message("U gained " + rand + " exp");
        rand = MathUtils.random(0, 100);
        if (rand > 50) Main.items.add(new Items(1, map_posx, map_posy));
        RDG.map[map_posx][map_posy] = ".";
    }

    void attack(){
        Player.hp -= attack;
    }

    void draw(){
        if (FOV.fov_map[map_posx][map_posy].equals("V")){
            animtriggercount += Gdx.graphics.getDeltaTime();
            if (animtriggercount > 0.8) {
                animtriggercount = 0;
                if (animtrigger) animtrigger = false;
                else animtrigger = true;
            }
            if (animtrigger)
                Main.batch.draw(img, rect.x ,rect.y);
            else Main.batch.draw(img1, rect.x ,rect.y);
        }
    }

    void dispose(){

    }
}
