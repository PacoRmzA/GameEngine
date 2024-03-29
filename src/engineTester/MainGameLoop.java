package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        ModelData data = OBJFileLoader.loadOBJ("steve_rotated");

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        RawModel steve = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), 
                data.getNormals(), data.getIndices());

        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        RawModel model2 = OBJLoader.loadObjModel("fern", loader);
        RawModel model3 = OBJLoader.loadObjModel("grassModel", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("blue"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("fern"));
        texture2.setNumberOfRows(2);
        ModelTexture texture3 = new ModelTexture(loader.loadTexture("grassTexture"));
        ModelTexture mando = new ModelTexture(loader.loadTexture("L3H_TheCubicJedi"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel texturedModel2 = new TexturedModel(model2, texture2);
        TexturedModel texturedModel3 = new TexturedModel(model3, texture3);
        TexturedModel playerModel = new TexturedModel(steve, mando);
        texturedModel2.getTexture().setHasTransparency(true);
        texturedModel2.getTexture().setUseFakeLighting(true);
        texturedModel3.getTexture().setHasTransparency(true);
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

        Entity entity = new Entity(texturedModel, new Vector3f(20, terrain.getHeightOfTerrain(0, -100), -100), 0, 0, 0, 1);
        Random random = new Random(8544889);
        Entity entity2 = new Entity(texturedModel2, random.nextInt(4), new Vector3f(20, terrain.getHeightOfTerrain(20, -150), -150), 0, 0, 0, 1);
        Entity entity3 = new Entity(texturedModel3, new Vector3f(20, terrain.getHeightOfTerrain(20, -100), -100), 0, 0, 0, 1);
        Player player = new Player(playerModel, new Vector3f(50, terrain.getHeightOfTerrain(50, -200) + 3, -200), 0, 0, 0, 1);

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(0.2f, 0.2f, 0.2f));
        List<Light> lights = new ArrayList<Light>();
        lights.add(light);
        lights.add(new Light(new Vector3f(50, 50, -200), new Vector3f(2, 2, 0), new Vector3f(0.1f, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(20, 50, -100), new Vector3f(0, 2, 2), new Vector3f(0.1f, 0.01f, 0.002f)));

        Camera camera = new Camera(player);

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("testgui"), new Vector2f(0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        MasterRenderer renderer = new MasterRenderer();
        
        while(!Display.isCloseRequested()){
            camera.move();
            player.move(terrain);
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            renderer.processEntity(entity);
            renderer.processEntity(entity2);
            renderer.processEntity(entity3);
            renderer.render(lights, camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        guiRenderer.CleanUp();
        renderer.CleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}