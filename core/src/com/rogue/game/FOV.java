package com.rogue.game;

/**
 * Created by svgood on 25.05.17.
 */
public class FOV {


    //H - Hidden
    //S - Seen
    //V - Visible
    static int FOV_RANGE = 8;
    static int FOV_KOEF = 0;
    static String [][] fov_map = new String[RDG.map_width][RDG.map_height];

    public FOV(){
        for(int i = 0; i < RDG.map_width; i++)
            for (int k = 0 ; k < RDG.map_height; k++)
                fov_map[i][k] = "H";
    }

    public static void FOV_reset(){
        for(int i = 0; i < RDG.map_width; i++)
            for (int k = 0 ; k < RDG.map_height; k++)
                fov_map[i][k] = "H";
    }

    public static void FOV_update(){
        FOV_KOEF = 0;

        for(int i = 0; i < RDG.map_width; i++)
            for (int k = 0 ; k < RDG.map_height; k++)
                if (fov_map[i][k].equals("V")) fov_map[i][k] = "S";
        fov_map[Player.map_posx][Player.map_posy] = "V";
        mark_visible_around(Player.map_posx, Player.map_posy);

        while (FOV_KOEF < FOV_RANGE) {
            for (int i = Player.map_posx - FOV_KOEF; i <= Player.map_posx + FOV_KOEF; i++) {
                if (Player.map_posy - FOV_KOEF > 0 && i > 0 && i < RDG.map_width)
                    if (!RDG.map[i][Player.map_posy - FOV_KOEF].substring(0, 1).equals("#") && fov_map[i][Player.map_posy - FOV_KOEF].equals("V"))
                        mark_visible_around(i,Player.map_posy - FOV_KOEF);
                        //fov_map[i][Player.map_posy - FOV_KOEF] = "V";
                if (Player.map_posy + FOV_KOEF < RDG.map_height && i > 0 && i < RDG.map_width)
                    if (!RDG.map[i][Player.map_posy + FOV_KOEF].substring(0, 1).equals("#")&& fov_map[i][Player.map_posy + FOV_KOEF].equals("V"))
                        mark_visible_around(i,Player.map_posy + FOV_KOEF);
                        //fov_map[i][Player.map_posy + FOV_KOEF] = "V";
            }
            for (int i = Player.map_posy - FOV_KOEF; i <= Player.map_posy + FOV_KOEF; i++) {
                if (Player.map_posx - FOV_KOEF > 0 && i > 0 && i < RDG.map_height)
                    if (!RDG.map[Player.map_posx - FOV_KOEF][i].substring(0, 1).equals("#")&& fov_map[Player.map_posx - FOV_KOEF][i].equals("V"))
                        mark_visible_around(Player.map_posx - FOV_KOEF,i);
                        //fov_map[Player.map_posx - FOV_KOEF][i] = "V";
                if (Player.map_posx + FOV_KOEF < RDG.map_width && i > 0 && i < RDG.map_height)
                  if (!RDG.map[Player.map_posx + FOV_KOEF][i].substring(0, 1).equals("#")&& fov_map[Player.map_posx + FOV_KOEF][i].equals("V"))
                      mark_visible_around(Player.map_posx + FOV_KOEF,i);
                     //fov_map[Player.map_posx + FOV_KOEF][i] = "V";
            }
            FOV_KOEF += 1;
        }

    }

    public static void mark_visible_around(int x, int y){
        for(int u = x - 1; u <= x+1; u++)
            for (int o = y - 1; o <= y+1; o++)
                fov_map[u][o] = "V";
    }
}
