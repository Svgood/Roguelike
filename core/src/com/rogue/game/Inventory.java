package com.rogue.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Svgood on 29.05.2017.
 */
public class Inventory {
    int size;

    BitmapFont font_white12;
    Texture img, btn_drop, btn_use, inv_line;
    Rectangle btn_drop_rect, btn_use_rect;
    int pos_x, pos_y, cur_slot;
    static boolean toggle;
    float wait, item_use_wait;
    static Items player_head, player_chest, player_gloves, player_boots, player_weapon;
    static Boolean player_head_equiped, player_chest_equiped, player_gloves_equiped, player_boots_equiped, player_weapon_equiped;
    public static Array<Items> items = new Array<Items>();
    public Array<Rectangle> inv_slots;

    public Inventory(){
        player_boots_equiped = false;
        player_chest_equiped = false;
        player_gloves_equiped = false;
        player_head_equiped = false;

        cur_slot = 0;
        btn_drop = new Texture("btn_drop.png");
        btn_use = new Texture("btn_use.png");
        btn_drop_rect = new Rectangle(pos_x + 650, pos_y + 535, btn_drop.getWidth(), btn_drop.getHeight());
        btn_use_rect = new Rectangle(pos_x + 720, pos_y + 535, btn_drop.getWidth(), btn_drop.getHeight());

        font_white12 = new BitmapFont(Gdx.files.internal("font_white12.fnt"));
        toggle = false;
        wait = 0;
        item_use_wait = 0;
        img = new Texture("inv.png");
        pos_x = Player.map_posx*32 - 640;
        pos_y = Player.map_posy*32 - 360;

        inv_line = new Texture("inv_line.png");
        inv_slots = new Array<Rectangle>();
        for (int i = 0; i < 8; i++){
            inv_slots.add(new Rectangle(pos_x + 390, pos_y + 535 - i*40, inv_line.getWidth(), inv_line.getHeight()));
        }
    }

    public void update(){
        update_pos();
        inv_navigation();
        }

    public void inv_navigation(){
        item_use_wait += Gdx.graphics.getDeltaTime();
        if (toggle) {
            for (int i = 0; i < 8; i++) {
                inv_slots.get(i).x = pos_x + 390;
                inv_slots.get(i).y = pos_y + 535 - i * 40;
                if (inv_slots.get(i).contains(Gdx.input.getX() + pos_x, (720 - Gdx.input.getY()) + pos_y)) {
                    cur_slot = i;
                }
            }
            btn_drop_rect.x = pos_x + 720;
            btn_drop_rect.y = pos_y + 535 - cur_slot * 40;
            btn_use_rect.x = pos_x + 650;
            btn_use_rect.y = pos_y + 535 - cur_slot * 40;
            if (btn_drop_rect.contains(Gdx.input.getX() + pos_x, (720 - Gdx.input.getY()) + pos_y)&& Gdx.input.isTouched())  {

            }
            if (btn_use_rect.contains(Gdx.input.getX() + pos_x, (720 - Gdx.input.getY()) + pos_y) && Gdx.input.isTouched())
                if (cur_slot < items.size && item_use_wait > 0.7) {
                    item_use_wait = 0;
                    item_use(cur_slot);
                }
        }
    }

    public void update_pos(){
        pos_x = Player.map_posx*32 - 640;
        pos_y = Player.map_posy*32 - 360;
    }

    public void draw(){
        update();
        if (wait < 1) wait += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.I) && wait > 0.5) {
            wait = 0;
            if (toggle) toggle = false;
            else toggle = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && wait > 0.5) {
            wait = 0;
            toggle = false;
        }
        if (toggle) {
            Main.batch.draw(img, pos_x, pos_y);
            if (cur_slot < items.size) {
                Main.batch.draw(inv_line, pos_x + 390, pos_y + 535 - cur_slot * 40);
                Main.batch.draw(btn_use, btn_use_rect.x, btn_use_rect.y);
                Main.batch.draw(btn_drop, pos_x + 720, pos_y + 535 - cur_slot * 40);
            }
            for (int i = 0; i < items.size; i++){
                Main.batch.draw(items.get(i).img, pos_x + 400, pos_y + 535 - i * 40);
                font_white12.draw(Main.batch,items.get(i).name, pos_x + 460, pos_y + 555 - i * 40);
            }
            draw_equiped();
        }
    }

    public void item_use(int slot){
        if (items.get(slot).type.equals("Consumable_health")){
            Player.hp += items.get(slot).heal_hp;
            items.removeIndex(slot);
        }
        if (items.get(slot).type.equals("Consumable_energy")){
            Player.energy += items.get(slot).heal_energy;
            items.removeIndex(slot);
        }
        if (items.get(slot).type.equals("Equipment_head")){
            if (player_head_equiped){
                items.add(new Items(player_head));
                player_head = new Items(items.get(slot));
                items.removeIndex(slot);
            }
            if (!player_head_equiped) {
                player_head_equiped = true;
                player_head = new Items(items.get(slot));
                items.removeIndex(slot);
            }
        }

    }

    public void draw_equiped(){
        if (player_head_equiped)
            Main.batch.draw(player_head.img, pos_x + 235, pos_y + 480);
    }

    public void remove_item(){

    }
}
