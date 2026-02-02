package com.INT.robot.constants;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;

public interface Cameras {
    
    public Camera[] LimelightCameras = new Camera[] {
        new Camera("turret", new Pose3d(0, 0, 0, new Rotation3d(0, 0, 0))),
        new Camera("intake", new Pose3d(0, 0, 0, new Rotation3d(0, 0, 0)))
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

//TODO: give camera location constant (do math for turret one)