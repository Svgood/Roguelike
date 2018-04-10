package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Svgood on 28.05.2017.
 */
public class LogWindow {

    BitmapFont font;
    static String str1, str2, str3, str4, str5, str6;
    int pos_x, pos_y;

    public LogWindow(){
        pos_x = Player.map_posx*32 - 640;
        pos_y = Player.map_posy*32 - 360;
        str1 = "";
        str2 = "";
        str3 = "";
        str4 = "";
        str5 = "";
        str6 = "";
        font = new BitmapFont(Gdx.files.internal("font_white20.fnt"));
    }

    public void update(){
        pos_x = Player.map_posx*32 - 640;
        pos_y = Player.map_posy*32 - 360;
    }

    public void draw(){
        update();

        font.draw(Main.batch, str1, pos_x + 50, pos_y + 50);
        font.draw(Main.batch, str2, pos_x + 50, pos_y + 80);
        font.draw(Main.batch, str3, pos_x + 50, pos_y + 110);
        font.draw(Main.batch, str4, pos_x + 50, pos_y + 140);
        font.draw(Main.batch, str5, pos_x + 50, pos_y + 170);
        font.draw(Main.batch, str6, pos_x + 50, pos_y + 200);
    }

    public static void push_message(String msg){
        str6 = str5;
        str5 = str4;
        str4 = str3;
        str3 = str2;
        str2 = str1;
        str1 = msg;
    }
}
