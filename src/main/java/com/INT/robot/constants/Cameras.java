package com.INT.robot.constants;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;

public interface Cameras {
    
    public Camera[] LimelightCameras = new Camera[] {
        //inches, degrees (pitch assuming 0 is perpendicular) (yaw turning clockwise)
        new Camera("turret", new Pose3d(6.771, 8.047, 13.13, new Rotation3d(0, -34, 225))),
        new Camera("intake", new Pose3d(1.0, 12.828, 4.692, new Rotation3d(0, -34, 270)))
    };

    public static class Camera {
        private String name;
        private Pose3d location;

        public Camera(String name, Pose3d location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public Pose3d getLocation() {
            return location;
        }
    }
}