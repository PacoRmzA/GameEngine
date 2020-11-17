package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
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

public class MainGameLoop {
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        ModelData data = OBJFileLoader.loadOBJ("steve");

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
        ModelTexture texture3 = new ModelTexture(loader.loadTexture("grassTexture"));
        ModelTexture mando = new ModelTexture(loader.loadTexture("L3H_TheCubicJedi"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel texturedModel2 = new TexturedModel(model2, texture2);
        TexturedModel texturedModel3 = new TexturedModel(model3, texture3);
        TexturedModel player = new TexturedModel(steve, mando);
        texturedModel2.getTexture().setHasTransparency(true);
        texturedModel2.getTexture().setUseFakeLighting(true);
        texturedModel3.getTexture().setHasTransparency(true);
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -100), 0, 0, 0, 1);
        Entity entity2 = new Entity(texturedModel2, new Vector3f(-20, 0, -100), 0, 0, 0, 1);
        Entity entity3 = new Entity(texturedModel3, new Vector3f(20, 0, -100), 0, 0, 0, 1);
        Entity playerEntity = new Entity(player, new Vector3f(-40, 35, -100), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(1000, 2000, 2000), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();
        
          while(!Display.isCloseRequested()){
            //entity.increaseRotation(0, 1, 0);
            camera.move();
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.processEntity(entity2);
            renderer.processEntity(entity3);
            renderer.processEntity(playerEntity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.CleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}