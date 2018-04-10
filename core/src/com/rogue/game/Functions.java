package com.rogue.game;

/**
 * Created by Svgood on 25.04.2017.
 */
public class Functions {

    static int max(int a, int b){
        if (a > b) return a;
        if (a < b) return b;
        return a;
    }

    static int min(int a, int b){
        if (a > b) return b;
        if (a < b) return a;
        return a;
    }

    static float distance(int x1,int y1,int x2,int y2) {
        return (float)(Math.sqrt(Math.pow(max(x2,x1) - min(x1,x2), 2) + Math.pow(max(y1, y2) - min(y1,y2), 2)));
    }
}
