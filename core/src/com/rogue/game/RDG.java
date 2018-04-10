package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Svgood on 22.04.2017.
 */
public class RDG {

    class Room {
        int x1, y1, x2, y2, midx, midy;

        public Room(int x, int y, int w, int h){
            x1 = x;
            y1 = y;
            x2 = x+w;
            y2 = y+h;
            midx = (int)((x1+x2)/2);
            midy = (int)((y1+y2)/2);
        }

        public Boolean overlaps(int x,int y,int w,int h){
            //System.out.println("Overlap checking");
            if (x1 < x+w && x2 > x && y1 < y+h && y2 > y)
                return true;
            else return false;
        }

        void fillRoom() {
            for (int i = x1+1; i < x2-1; i++)
                for (int k = y1+1; k < y2-1; k++) {
                    map[i][k] = ".";
                }
            //if ((int)MathUtils.random(0,2) == 1)
                Main.npcs.add(new NPC((int)MathUtils.random(x1+1,x2-1), (int)MathUtils.random(y1+1,y2-1)));
        }

    }

    static String map[][];
    static String pathmap[][];
    static String nativemap[][];
    static int map_width, map_height;

    int rooms_count;
    int max_width, min_width;
    int max_height, min_height;
    static Array<Room> rooms;
    int x, y, w, h;

    public RDG(){
        map_width = 60;
        map_height = 40;
        map = new String[map_width][map_height];
        nativemap = new String[map_width][map_height];
        pathmap = new String[map_width][map_height];
        clearMap();
        resetPathMap();
        rooms_count = 20;
        max_width = 8;
        max_height = 8;
        min_width = 4;
        min_height = 4;
        rooms = new Array<Room>();
    }

    void generate(){
        rooms.clear();
        Main.npcs.clear();
        clearMap();
        resetPathMap();
        FOV.FOV_reset();

        boolean gen = true;
        boolean exit = false;
        for(int i = 0; i < rooms_count; i++) {
            while (gen) {
                //Room creating
                exit = false;
                x = (int) MathUtils.random(1, map_width - max_width - 1);
                y = (int) MathUtils.random(1, map_height - max_height - 1);
                w = (int) MathUtils.random(min_width, max_width);
                h = (int) MathUtils.random(min_height, max_height);
                for (int k = 0; k < rooms.size; k++) {
                    if (rooms.get(k).overlaps(x, y, w, h)) {
                        exit = true;
                        break;
                    }
                }
                if (rooms.size == 0) {
                    Player.rect.x = (int)((2*x+w)/2*32);
                    Player.rect.y = (int)((2*y+h)/2*32);
                    Player.map_posx = (int)((2*x+w)/2);
                    Player.map_posy = (int)((2*y+h)/2);
                    map[Player.map_posx][Player.map_posy] = "@";
                    //gen = false;
                }
                //Map filling
                if (!exit) {
                    //System.out.println("Room generated");
                    gen = false;
                    rooms.add(new Room(x, y, w, h));
                    for(int xgen = x; xgen < x+w; xgen++)
                        for(int ygen = y; ygen < y+h; ygen++)
                            map[xgen][ygen] = ".";
                }
            }
            gen = true;

        }

        //creating tunnels
        for (int i = 1; i < rooms.size; i++){
            if (rooms.get(i-1).midx <= rooms.get(i).midx){
                for (int k = rooms.get(i-1).midx; k <= rooms.get(i).midx; k++){
                    map[k][rooms.get(i-1).midy] = ".";
                }
                if (rooms.get(i-1).midy <= rooms.get(i).midy){
                    for (int k = rooms.get(i-1).midy; k <= rooms.get(i).midy; k++){
                        map[rooms.get(i).midx][k] = ".";
                    }
                }
                else
                    for (int k = rooms.get(i).midy; k <= rooms.get(i-1).midy; k++){
                        map[rooms.get(i).midx][k] = ".";
                    }
            }
            else {
                for (int k = rooms.get(i).midx; k <= rooms.get(i-1).midx; k++){
                    map[k][rooms.get(i).midy] = ".";
                }
                if (rooms.get(i-1).midy <= rooms.get(i).midy){
                    for (int k = rooms.get(i-1).midy; k <= rooms.get(i).midy; k++){
                        map[rooms.get(i-1).midx][k] = ".";
                    }
                }
                else
                    for (int k = rooms.get(i).midy; k <= rooms.get(i-1).midy; k++){
                        map[rooms.get(i-1).midx][k] = ".";
                    }
            }
        }

        //create path map
        resetPathMap();
        //clearing inside rooms
        for (int i = 0; i < rooms.size; i++){
            rooms.get(i).fillRoom();
        }

        //Poinerts
        Pointer.gomap_posy = Player.map_posy;
        Pointer.gomap_posx = Player.map_posx;
        FOV.FOV_update();
        //Gdx.input.setCursorPosition((Player.map_posx - 20)*32, (Player.map_posy - 30)*32);
        /*
        for (int i = 0; i < map_width; i++) {
            System.out.println();
            for (int k = 0; k < map_height; k++)
                System.out.print(map[i][k]);
        }
        */
    }

    void clearMap() {
        for (int i = 0; i < map_width; i++)
            for (int k = 0; k < map_height; k++)
                map[i][k] = "#";
    }

    static void resetPathMap(){
        for (int i = 0; i < map_width; i++)
            for (int k = 0; k < map_height; k++) {
                pathmap[i][k] = map[i][k];
            }
    }
}
