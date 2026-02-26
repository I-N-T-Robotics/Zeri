package com.INT.robot.constants;

import java.util.ArrayList;
import java.util.List;

import com.INT.robot.Robot;
import com.INT.robot.util.vision.AprilTag;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

public interface Field {
        
    public static final Field2d FIELD2D = new Field2d();

    double WIDTH = Units.inchesToMeters(317.69); // y
    double LENGTH = Units.inchesToMeters(651.22); // x

    public static Pose3d transformToOppositeAlliance(Pose3d pose) {
        Pose3d rotated = pose.rotateAround((new Translation3d(LENGTH / 2, WIDTH / 2, 0)),
                (new Rotation3d(0, 0, Math.PI)));

        return rotated;
    }

    public static Pose2d transformToOppositeAlliance(Pose2d pose) {
        Pose2d rotated = pose.rotateAround((new Translation2d(LENGTH / 2, WIDTH / 2)), new Rotation2d(-1, 0));

        return rotated;
    }

    public static Translation2d transformToOppositeAlliance(Translation2d translation) {
        return new Translation2d(LENGTH - translation.getX(), WIDTH - translation.getY());
    }

    public static List<Pose2d> transformToOppositeAlliance(List<Pose2d> poses) {
        List<Pose2d> newPoses = new ArrayList<>();
        for (Pose2d pose : poses) {
            newPoses.add(transformToOppositeAlliance(pose));
        }
        return newPoses;
    }

    public enum NamedTags {

        // =========================
        // RED ALLIANCE
        // =========================

        // ID 1–12 : Red Trench + Red Hub
        RED_TRENCH_TOP_LEFT, // ID 1
        RED_HUB_NORTH, // ID 2
        RED_HUB_NORTHWEST, // ID 3
        RED_HUB_WEST, // ID 4
        RED_HUB_SOUTHWEST, // ID 5
        RED_TRENCH_BOTTOM_LEFT, // ID 6
        RED_TRENCH_BOTTOM_RIGHT, // ID 7
        RED_HUB_SOUTHEAST, // ID 8
        RED_HUB_EAST, // ID 9
        RED_HUB_NORTHEAST, // ID 10
        RED_HUB_SOUTH, // ID 11
        RED_TRENCH_TOP_RIGHT, // ID 12

        // ID 13–16 : Red Outpost + Red Tower
        RED_OUTPOST_TOP, // ID 13
        RED_OUTPOST_BOTTOM, // ID 14
        RED_TOWER_TOP, // ID 15
        RED_TOWER_BOTTOM, // ID 16

        // =========================
        // BLUE ALLIANCE
        // =========================

        // ID 17–28 : Blue Trench + Blue Hub
        BLUE_TRENCH_BOTTOM_RIGHT, // ID 17
        BLUE_HUB_WEST, // ID 18
        BLUE_HUB_NORTHWEST, // ID 19
        BLUE_HUB_SOUTH, // ID 20
        BLUE_HUB_NORTH, // ID 21
        BLUE_TRENCH_TOP_LEFT, // ID 22
        BLUE_TRENCH_TOP_RIGHT, // ID 23
        BLUE_HUB_NORTHEAST, // ID 24
        BLUE_HUB_EAST, // ID 25
        BLUE_HUB_SOUTHEAST, // ID 26
        BLUE_HUB_SOUTHWEST, // ID 27
        BLUE_TRENCH_BOTTOM_LEFT, // ID 28

        // ID 29–30 : Blue Outpost
        BLUE_OUTPOST_BOTTOM, // ID 29
        BLUE_OUTPOST_TOP, // ID 30

        // ID 31–32 : Blue Tower
        BLUE_TOWER_BOTTOM, // ID 31
        BLUE_TOWER_TOP; // ID 32

        public final AprilTag tag;

        public int getID() {
            return tag.getID();
        }

        public Pose3d getLocation() {
            return Robot.isBlue()
                ? tag.getLocation()
                : transformToOppositeAlliance(tag.getLocation());
        }

        private NamedTags() {
            tag = APRILTAGS[ordinal()];
        }
    }

    AprilTag APRILTAGS[] = {
            new AprilTag(1, new Pose3d(
                    new Translation3d(Units.inchesToMeters(467.64), Units.inchesToMeters(292.31), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(2, new Pose3d(
                    new Translation3d(Units.inchesToMeters(469.11), Units.inchesToMeters(182.60), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(90)))),
            new AprilTag(3, new Pose3d(
                    new Translation3d(Units.inchesToMeters(445.35), Units.inchesToMeters(172.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(4, new Pose3d(
                    new Translation3d(Units.inchesToMeters(445.35), Units.inchesToMeters(158.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(5, new Pose3d(
                    new Translation3d(Units.inchesToMeters(469.11), Units.inchesToMeters(135.09), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(270)))),
            new AprilTag(6, new Pose3d(
                    new Translation3d(Units.inchesToMeters(467.64), Units.inchesToMeters(25.37), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(7, new Pose3d(
                    new Translation3d(Units.inchesToMeters(470.59), Units.inchesToMeters(25.37), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(8, new Pose3d(
                    new Translation3d(Units.inchesToMeters(483.11), Units.inchesToMeters(135.09), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(270)))),
            new AprilTag(9, new Pose3d(
                    new Translation3d(Units.inchesToMeters(492.88), Units.inchesToMeters(144.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(10, new Pose3d(
                    new Translation3d(Units.inchesToMeters(492.88), Units.inchesToMeters(158.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(11, new Pose3d(
                    new Translation3d(Units.inchesToMeters(483.11), Units.inchesToMeters(182.60), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(90)))),
            new AprilTag(12, new Pose3d(
                    new Translation3d(Units.inchesToMeters(470.59), Units.inchesToMeters(292.31), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(13, new Pose3d(
                    new Translation3d(Units.inchesToMeters(650.92), Units.inchesToMeters(291.47), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(14, new Pose3d(
                    new Translation3d(Units.inchesToMeters(650.92), Units.inchesToMeters(274.47), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(15, new Pose3d(
                    new Translation3d(Units.inchesToMeters(650.90), Units.inchesToMeters(170.22), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(16, new Pose3d(
                    new Translation3d(Units.inchesToMeters(650.90), Units.inchesToMeters(153.22), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(17, new Pose3d(
                    new Translation3d(Units.inchesToMeters(183.59), Units.inchesToMeters(25.37), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(18, new Pose3d(
                    new Translation3d(Units.inchesToMeters(182.11), Units.inchesToMeters(135.09), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(270)))),
            new AprilTag(19, new Pose3d(
                    new Translation3d(Units.inchesToMeters(205.87), Units.inchesToMeters(144.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(20, new Pose3d(
                    new Translation3d(Units.inchesToMeters(205.87), Units.inchesToMeters(158.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(21, new Pose3d(
                    new Translation3d(Units.inchesToMeters(182.11), Units.inchesToMeters(182.60), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(90)))),
            new AprilTag(22, new Pose3d(
                    new Translation3d(Units.inchesToMeters(183.59), Units.inchesToMeters(292.31), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(23, new Pose3d(
                    new Translation3d(Units.inchesToMeters(180.64), Units.inchesToMeters(292.31), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(24, new Pose3d(
                    new Translation3d(Units.inchesToMeters(168.11), Units.inchesToMeters(182.60), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(90)))),
            new AprilTag(25, new Pose3d(
                    new Translation3d(Units.inchesToMeters(158.34), Units.inchesToMeters(172.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(26, new Pose3d(
                    new Translation3d(Units.inchesToMeters(158.34), Units.inchesToMeters(158.84), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(27, new Pose3d(
                    new Translation3d(Units.inchesToMeters(168.11), Units.inchesToMeters(135.09), Units.inchesToMeters(44.25)),
                    new Rotation3d(0, 0, Units.degreesToRadians(270)))),
            new AprilTag(28, new Pose3d(
                    new Translation3d(Units.inchesToMeters(180.64), Units.inchesToMeters(25.37), Units.inchesToMeters(35.00)),
                    new Rotation3d(0, 0, Units.degreesToRadians(180)))),
            new AprilTag(29, new Pose3d(
                    new Translation3d(Units.inchesToMeters(0.30), Units.inchesToMeters(26.22), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(30, new Pose3d(
                    new Translation3d(Units.inchesToMeters(0.30), Units.inchesToMeters(43.22), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(31, new Pose3d(
                    new Translation3d(Units.inchesToMeters(0.32), Units.inchesToMeters(147.47), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0)))),
            new AprilTag(32, new Pose3d(
                    new Translation3d(Units.inchesToMeters(0.32), Units.inchesToMeters(164.47), Units.inchesToMeters(21.75)),
                    new Rotation3d(0, 0, Units.degreesToRadians(0))))
    };

        public static boolean isValidTag(int id) {
        for (AprilTag tag : APRILTAGS) {
            if (tag.getID() == id) {
                return true;
            }
        }
        return false;
    }

    public static AprilTag getTag(int id) {
        for (AprilTag tag : APRILTAGS) {
            if (tag.getID() == id) {
                return tag;
            }
        }
        return null;
    }

    double tag26x = NamedTags.BLUE_HUB_SOUTHEAST.getLocation().getX();
    double tag20x = NamedTags.BLUE_HUB_SOUTH.getLocation().getX();
    double tag18y = NamedTags.BLUE_HUB_NORTHWEST.getLocation().getY();
    double tag21y = NamedTags.BLUE_HUB_NORTH.getLocation().getY();

    //x value is the one on the horizontal to change 
    Translation2d BLUE_GOAL_CENTER = new Translation2d(Math.abs(tag26x - tag20x), Math.abs(tag18y - tag21y)); 
    Translation2d RED_GOAL_CENTER = transformToOppositeAlliance(BLUE_GOAL_CENTER);
    Translation2d hubCenter = Robot.isBlue() ?  BLUE_GOAL_CENTER :  RED_GOAL_CENTER;

    Translation2d BLUE_TOP_FERRY = new Translation2d(182.11/2 /* 1/2 ds to alliance line*/, 158.84/2 /*1/2 middle to top*/);
    Translation2d RED_TOP_FERRY = transformToOppositeAlliance(BLUE_TOP_FERRY);
    Translation2d topFerry = Robot.isBlue() ? BLUE_TOP_FERRY : RED_TOP_FERRY;

    Translation2d BLUE_BOTTOM_FERRY = new Translation2d(182.11/2, BLUE_TOP_FERRY.getY() + 158.84);
    Translation2d RED_BOTTOM_FERRY = transformToOppositeAlliance(BLUE_BOTTOM_FERRY);
    Translation2d bottomFerry = Robot.isBlue() ? BLUE_BOTTOM_FERRY : RED_BOTTOM_FERRY;
}