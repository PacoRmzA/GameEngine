package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;


    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 20; //up or down
    private float yaw = 0;   //left or right
    private float roll;  //counterclockwise or clockwise

    private Player player;

    public Camera(Player player) {
        this.player = player;
        position.x = player.getPosition().x - distanceFromPlayer;
        position.z = player.getPosition().z;
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + 10;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        if (distanceFromPlayer - zoomLevel >= 10) {
            distanceFromPlayer -= zoomLevel;
        }
    }

    private void calculatePitch() {
        float pitchChange = Mouse.getDY() * 0.1f;
        if (Mouse.isButtonDown(1) && pitch - pitchChange >= 0 && pitch - pitchChange <= 90) {
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

}