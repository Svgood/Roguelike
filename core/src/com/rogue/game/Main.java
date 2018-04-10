package com.rogue.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter {
	Stage stage;
	Table table;

	static SpriteBatch batch;
	OrthographicCamera camera;

	Player player;
	GUI gui;
	Inventory inventory;
	Pointer pointer;
	RDG rdg;
	LogWindow logWindow;

	float wait;
	float zoom_wait;

	static Array<Items> items;
	Array<Wall> walls;
	Array<Floor> floors;
	static Array<Projectiles> projectiles;
	static Array<NPC> npcs;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);
		camera.update();
		batch = new SpriteBatch();

		logWindow = new LogWindow();
		gui = new GUI();
		inventory = new Inventory();
		rdg = new RDG();
		pointer = new Pointer();

		items = new Array<Items>();
		npcs = new Array<NPC>();
		walls = new Array<Wall>();
		floors = new Array<Floor>();
		projectiles = new Array<Projectiles>();

		player = new Player(5,5);
		wait = 0;
		zoom_wait = 0;
		dungGen();
		gui.update();
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		wait += Gdx.graphics.getDeltaTime();
		pointer.update();
		zooming();
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		drawAll();
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			if (GlobalVars.FOVon) GlobalVars.FOVon = false;
			else GlobalVars.FOVon = true;
		}
		//System.out.println("X " + Gdx.input.getX() + " Y " + Gdx.input.getY());
		if (wait > 0.04) {
			dungGen();
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || !Pointer.onPlayer()) &&  wait > 0.06) {
			wait = 0;
			player.update();
			gui.update();
			if (Player.moved) {
				objectsUpdate();
				camera.position.set(Player.rect.x, Player.rect.y, 0);
				Player.moved = false;
			}
			//logging();
		}

	}

	public void objectsUpdate(){
		for(int i = 0; i < npcs.size; i++) {
			if (npcs.get(i).hp > 0)
				npcs.get(i).update();
			else {
				npcs.get(i).death();
				npcs.removeIndex(i);
			}
		}
	}

	public void drawAll(){
		for(int i = 0; i < walls.size; i++)
			walls.get(i).draw();
		for(int i = 0; i < floors.size; i++)
			floors.get(i).draw();
		for(int i = 0; i < npcs.size; i++)
			npcs.get(i).draw();

		//Projectiles
		for (int i = 0; i < projectiles.size; i++) {
			if (!projectiles.get(i).remove) projectiles.get(i).update();
			else projectiles.removeIndex(i);
		}
		for(int i = 0; i < projectiles.size; i++)
			projectiles.get(i).draw();
		for(int i = 0; i < items.size; i++)
			items.get(i).draw();
		logWindow.draw();
		player.draw();
		gui.draw();
		inventory.draw();
		pointer.draw();

	}

	void dungGen(){
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			wait = 0;
			walls.clear();
			floors.clear();
			rdg.generate();
			player.restart();
			for(int i = 0; i < RDG.map_width; i ++)
				for(int k = 0; k < RDG.map_height; k++) {
					if (RDG.map[i][k].equals("#")) walls.add(new Wall(i , k));
					if (RDG.map[i][k].equals(".")) floors.add(new Floor(i , k));
			}
			camera.position.set(Player.rect.x, Player.rect.y, 0);
			camera.update();
			gui.update();
		}
	}

	void logging(){
		Gdx.app.log("Java heap", Long.toString(Gdx.app.getJavaHeap() / (1024 * 1024)));
		Gdx.app.log("Native heap", Long.toString(Gdx.app.getNativeHeap() / (1024 * 1024)));
	}

	void zooming(){
		zoom_wait += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && zoom_wait > 0.05){
			camera.zoom += 0.1;
			zoom_wait = 0;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) && zoom_wait > 0.05){
			camera.zoom -= 0.1;
			zoom_wait = 0;
		}

	}
	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}
}
