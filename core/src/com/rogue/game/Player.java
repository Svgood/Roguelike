package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Svgood on 22.04.2017.
 */
public class Player {

    static Rectangle rect;
    static int map_posx, map_posy;
    static int last_turn_x, last_turn_y;

    //Animation
    Texture img, img1;
    float animtriggercount;
    boolean animtrigger;
    float wait;

    //stats
    static boolean moving;
    static float hp, base_hp, energy, base_energy;
    float base_attack, def, current_attack;
    float stat_int, stat_agi, stat_str;
    float hp_regen, energ_regen;
    static int lvl;
    static float exp;

    //pathfinding
    int pathk, pathx, pathy, min, checkx, checky;
    boolean find, look;
    int counter;

    //fov
    FOV fov;
    static boolean moved;

    public Player(int x, int y) {
        base_attack = 10;
        def = 5;
        base_hp = 100;
        base_energy = 100;
        hp = base_hp;
        energy = base_energy;
        lvl = 1;
        exp = 0;
        stat_int = 10;
        stat_agi = 8;
        stat_str = 12;
        hp_regen = 1;
        energ_regen = 4;


        current_attack = (float)(base_attack + stat_int*0.1 + stat_agi*0.2 + stat_str*0.3);

        img = new Texture("player.png");
        img1 = new Texture("player1.png");
        animtriggercount = 0;
        animtrigger = true;

        rect = new Rectangle(x*32,y*32,img.getWidth(), img.getHeight());
        wait = 0;
        map_posx = x;
        map_posy = y;
        moved = false;

        fov = new FOV();
    }

    void update() {
        Player.moved = false;
        if (!Inventory.toggle) {
            mouse_check();
            goTo(Pointer.gomap_posx, Pointer.gomap_posy);
        }
        key_check();
        FOV.FOV_update();
        items_interacting();
        regeneration();

        if (last_turn_x == map_posx && last_turn_y == map_posy){
            Pointer.gomap_posx = map_posx;
            Pointer.gomap_posy = map_posy;
        }

        rect.x = map_posx*32;
        rect.y = map_posy*32;

        last_turn_x = map_posx;
        last_turn_y = map_posy;
    }

    void draw() {
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

    void mouse_check(){
        if (Gdx.input.isTouched()) {
            if (Pointer.onNPC())
                for (int i = 0; i < Main.npcs.size; i++)
                    for (int k = map_posx - 1; k <= map_posx + 1; k++)
                        for (int j = map_posy - 1; j <= map_posy + 1; j++)
                            if (k == Main.npcs.get(i).map_posx && j == Main.npcs.get(i).map_posy
                                    && Main.npcs.get(i).map_posy == Pointer.gomap_posy
                                    && Main.npcs.get(i).map_posx == Pointer.gomap_posx) {
                                Main.npcs.get(i).hp -= current_attack;
                                LogWindow.push_message("You attack Alien for " + current_attack + " dmg");
                                Pointer.gomap_posx = map_posx;
                                Pointer.gomap_posy = map_posy;
                                Player.moved = true;
                            }
        }
    }

    void key_check(){
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (RDG.map[map_posx][map_posy + 1].equals(".")) {
                move(0, 1);
                Pointer.gomap_posy += 1;
                Player.moved = true;
                log();
            } else if (RDG.map[map_posx][map_posy + 1].substring(0,1).equals("n")) {
                for (int i = 0; i < Main.npcs.size; i++)
                    if (Main.npcs.get(i).checkPos(map_posx, map_posy + 1))
                        Main.npcs.get(i).hp -= current_attack;
                        Player.moved = true;
                        LogWindow.push_message("You attack Alien for " + current_attack + " dmg");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (RDG.map[map_posx - 1][map_posy].equals(".")) {
                move(-1, 0);
                Pointer.gomap_posx -= 1;
                log();
            } else if (RDG.map[map_posx - 1][map_posy].substring(0,1).equals("n")) {
                for (int i = 0; i < Main.npcs.size; i++)
                    if (Main.npcs.get(i).checkPos(map_posx - 1, map_posy))
                        Main.npcs.get(i).hp -= current_attack;
                        Player.moved = true;
                        LogWindow.push_message("You attack Alien for " + current_attack + " dmg");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (RDG.map[map_posx][map_posy - 1].equals(".")) {
                move(0, -1);
                Pointer.gomap_posy -= 1;
                log();
            } else if (RDG.map[map_posx][map_posy - 1].substring(0,1).equals("n")) {
                for (int i = 0; i < Main.npcs.size; i++)
                    if (Main.npcs.get(i).checkPos(map_posx, map_posy - 1))
                        Main.npcs.get(i).hp -= current_attack;
                        Player.moved = true;
                        LogWindow.push_message("You attack Alien for " + current_attack + " dmg");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            if (RDG.map[map_posx+1][map_posy].equals(".")){
                move(1, 0);
                Pointer.gomap_posx += 1;
                log();
            }
            else if (RDG.map[map_posx+1][map_posy].substring(0,1).equals("n")) {
                for (int i = 0; i < Main.npcs.size; i++)
                    if (Main.npcs.get(i).checkPos(map_posx+1, map_posy))
                        Main.npcs.get(i).hp -= current_attack;
                        Player.moved = true;
                        LogWindow.push_message("You attack Alien for " + current_attack + " dmg");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F1)){
            if (energy > 5) {
                Main.projectiles.add(new Projectiles(map_posx, map_posy, Pointer.map_posx, Pointer.map_posy));
                energy -= 5;
            }

        }
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
        /*
        for (int i = map_posx - 1; i <= map_posx + 1; i++)
            for (int k = map_posy - 1; k <= map_posy + 1; k++)
                if (i == x && k == y) {
                    look = false;
                }
        */

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
                //System.out.println("Looking for...");
            }

            for (int i = x - 1; i <= x + 1; i++)
                for (int k = y - 1; k <= y + 1; k++)
                    if (RDG.pathmap[i][k].equals(".")) RDG.pathmap[i][k] = "999";

            find = true;
            checkx = pathx;
            checky = pathy;
            counter = 0;

            /*
            for (int i = 0; i < RDG.map_height; i++) {
                System.out.println();
                for (int k = 0; k < RDG.map_width; k++)
                    System.out.print(RDG.pathmap[k][i]);
            }*/

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
                                        RDG.map[map_posx][map_posy] = "@";
                                        rect.x = checkx * 32;
                                        rect.y = checky * 32;
                                        find = false;
                                        Player.moved = true;
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
                                        RDG.map[map_posx][map_posy] = "@";
                                        rect.x = checkx * 32;
                                        rect.y = checky * 32;
                                        find = false;
                                        Player.moved = true;
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
                                            RDG.map[map_posx][map_posy] = "@";
                                            rect.x = checkx * 32;
                                            rect.y = checky * 32;
                                            find = false;
                                            Player.moved = true;
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
        /*
        while (find) {
                for (int i = checkx - 1; i <= checkx + 1; i++)
                    for (int k = checky - 1; k <= checky + 1; k++)
                        if (i >= 0 && k >= 0 && i < RDG.map_width && k < RDG.map_height)
                            if (!RDG.pathmap[i][k].substring(0,1).equals("#") && !RDG.pathmap[i][k].substring(0,1).equals(".")
                                    && !RDG.pathmap[i][k].substring(0,1).equals("@") && !RDG.pathmap[i][k].substring(0,1).equals("n"))
                                if (Integer.parseInt(RDG.pathmap[i][k]) < min) {
                                    if (i == map_posx && k == map_posy) {
                                        if (RDG.map[checkx][checky].equals(".")) {
                                            RDG.map[map_posx][map_posy] = ".";
                                            map_posx = checkx;
                                            map_posy = checky;
                                            RDG.map[map_posx][map_posy] = "@";
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
         */
    }

    void move(int x,int y){
        Player.moved = true;
        RDG.map[map_posx][map_posy] = ".";
        map_posy += y;
        map_posx += x;
        rect.x += x*32;
        rect.y += y*32;
        RDG.map[map_posx][map_posy] = "@";
    }

    void regeneration(){
        if (Player.moved) {
            if (hp + hp_regen <= base_hp) hp += hp_regen;
            if (hp + hp_regen > base_hp) hp = base_hp;
            if (energy + energ_regen <= base_energy) energy += energ_regen;
            if (energy + energ_regen > base_energy) energy = base_energy;
        }
    }

    void restart(){
        hp = base_hp;
        energy = base_energy;
        lvl = 1;
        exp = 0;
    }

    void log(){
        System.out.println("map x:" + map_posx + " map y:" + map_posy);
        System.out.println("hp: " + hp);
    }

    void items_interacting(){
        for (int i = 0; i < Main.items.size; i++)
            if (rect.overlaps(Main.items.get(i).rect) && !Main.items.get(i).in_inventory){
                Inventory.items.add(new Items(Main.items.get(i)));
                Main.items.removeIndex(i);
            }
    }

    void dispose() {
        img.dispose();
    }
}
