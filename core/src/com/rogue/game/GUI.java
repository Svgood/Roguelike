package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Svgood on 27.05.2017.
 */
public class GUI {
    Texture img;
    int pos_x, pos_y;
    BitmapFont font_red;
    BitmapFont font_lightgreen;
    BitmapFont font_white;
    BitmapFont font_yellow;

    public GUI(){
        img = new Texture("GUIbg.png");
        font_red = new BitmapFont(Gdx.files.internal("font_red.fnt"));
        font_lightgreen = new BitmapFont(Gdx.files.internal("font_lightgreen.fnt"));
        font_white = new BitmapFont(Gdx.files.internal("font_white.fnt"));
        font_yellow = new BitmapFont(Gdx.files.internal("font_yellow.fnt"));
        pos_x = Player.map_posx*32 - 640;
        pos_y = Player.map_posy*32 - 360;
    }

    public void update(){
        pos_x = Player.map_posx*32 - 640;
        pos_y = Player.map_posy*32 - 360;
    }

    public void draw(){
        Main.batch.draw(img, pos_x, pos_y);
        font_red.draw(Main.batch, "HEALTH:" + MathUtils.floor(Player.hp/Player.base_hp*100) + "%" ,pos_x+1070, pos_y +700);
        font_lightgreen.draw(Main.batch, "ENERGY:" + MathUtils.floor(Player.energy/Player.base_energy*100) + "%" ,pos_x+1070, pos_y +660);
        font_white.draw(Main.batch, "LEVEL:" + Player.lvl ,pos_x+1070, pos_y +620);
        font_yellow.draw(Main.batch, "EXP:" + Player.exp ,pos_x+1070, pos_y +580);
    }
}
