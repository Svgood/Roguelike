package com.rogue.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Svgood on 29.05.2017.
 */
public class Items {

    Rectangle rect;

    Texture img;

    String type,
            rarity,
            name,
            description;
    float add_str,
            add_int,
            add_agi,
            add_hp_regen,
            add_energregen,
            add_hp,
            add_energy,
            add_def,
            add_attack,
            stat_points;
    float heal_hp,
            heal_energy;
    int lvl;
    int rand;
    int map_posx, map_posy;
    boolean in_inventory;


    public Items(int item_level, int x, int y)
    {
        lvl = item_level;
        in_inventory = false;
        rand = MathUtils.random(0, 2);
        if (rand == 0)
            type = "Consumable_health";
        if (rand == 1)
            type = "Consumable_energy";
        if (rand == 2)
            type = "Equipment_head";
        map_posx = x;
        map_posy = y;
        rect = new Rectangle(map_posx*32, map_posy*32, 32, 32);

        if (type.equals("Consumable_health")){
            img = new Texture("hpotion.png");
            heal_hp = 50*item_level;
            name = "Healing potion";
        }
        if (type.equals("Consumable_energy")){
            img = new Texture("epotion.png");
            heal_energy = 50*item_level;
            name = "Energy potion";
        }
        if (type.equals("Equipment_head")){
            img = new Texture("helmet.png");
            rand = MathUtils.random(0, 100);
            if (rand >= 95) {
                rarity = "Legendary";
                rand = MathUtils.random(0, 2);
                if (rand == 0){
                    add_int = MathUtils.random(16 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 1){
                    add_agi = MathUtils.random(16 + lvl*5, 20 + lvl*5);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 2){
                    add_str = MathUtils.random(16 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
            }
            if (rand < 95 && rand >= 75) {
                rarity = "Epic";
                if (rand == 0){
                    add_int = MathUtils.random(12 + lvl*5, 16 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 1){
                    add_agi = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 2){
                    add_str = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
            }
            if (rand < 75 && rand >= 40) {
                rarity = "Rare";
                if (rand == 0){
                    add_int = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 1){
                    add_agi = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 2){
                    add_str = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
            }
            if (rand < 40) {
                rarity = "Common";
                if (rand == 0){
                    add_int = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 1){
                    add_agi = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_str = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
                if (rand == 2){
                    add_str = MathUtils.random(15 + lvl*5, 20 + lvl*5);
                    add_agi = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                    add_int = MathUtils.random(5 + lvl*4, 10 + lvl*4);
                }
            }
            name = rarity + " Helmet";
        }
    }

    public Items(Items item){
        in_inventory = true;
        if (item.type.equals("Equipment_head")){
            img = item.img;
            type = item.type;
            rarity = item.rarity;
            stat_points = item.stat_points;
            lvl = item.lvl;
            name = item.name;
        }
        if (item.type.equals("Consumable_energy")){
            img = item.img;
            type = item.type;
            heal_energy = item.heal_energy;
            lvl = item.lvl;
            name = item.name;
        }
        if (item.type.equals("Consumable_health")){
            img = item.img;
            type = item.type;
            heal_hp = item.heal_hp;
            lvl = item.lvl;
            name = item.name;
        }
    }

    public void draw(){
        if (!in_inventory)
            Main.batch.draw(img, map_posx*32, map_posy*32);
    }

    public void update(){

    }

    public void copyItem(Items item){
        if (item.type.equals("Equipment_head")){
            img = item.img;
            type = item.type;
            rarity = item.rarity;
            stat_points = item.stat_points;
        }
        if (item.type.equals("Consumable_energy")){
            img = item.img;
            type = item.type;
            heal_energy = item.heal_energy;
        }
    }
}
