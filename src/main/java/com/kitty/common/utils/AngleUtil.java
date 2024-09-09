package com.kitty.common.utils;

public class AngleUtil
{
    public static final int CIRCLE_ANGLE = 360;
    public static final int HALF_CIRCLE_ANGLE = 180;
    
    private AngleUtil() {
    }
    
    public static void main(String[] args) {
        System.out.println(isInCircle(0.0, 0.0, 0.0, 0.0, 0.0));
    }
    
    public static double relativePosition2Angle(double x, double y) {
        return Math.toDegrees(Math.atan2(y, x));
    }
    
    public static double position2Angle(double sourceX, double sourceY, double targetX, double targetY) {
        return circleAngle(relativePosition2Angle(targetX - sourceX, targetY - sourceY));
    }
    
    public static boolean isInCircle(double sourceX, double sourceY, double targetX, double targetY, double radius) {
        checkNotNegative(radius, "圆半径");
        return square(radius) >= square(targetX - sourceX) + square(targetY - sourceY);
    }
    
    public static boolean isInRing(double sourceX, double sourceY, double targetX, double targetY, double innerRadius, double outerRadius) {
        checkNotNegative(innerRadius, "内圆半径");
        checkNotNegative(outerRadius, "外圆半径");
        double inner = (innerRadius <= outerRadius) ? innerRadius : outerRadius;
        double outer = (inner == innerRadius) ? outerRadius : innerRadius;
        double distanceSquare = square(targetX - sourceX) + square(targetY - sourceY);
        return square(inner) <= distanceSquare && distanceSquare <= square(outer);
    }
    
    public static boolean isInSector(double sourceX, double sourceY, double targetX, double targetY, double radius, double axisAngle, double extendAngle) {
        checkNotNegative(radius, "扇形半径");
        if (extendAngle >= 180.0) {
            return isInCircle(sourceX, sourceY, targetX, targetY, radius);
        }
        double radiusSquare = square(radius);
        double distanceSquare = square(targetX - sourceX) + square(targetY - sourceY);
        if (distanceSquare <= radiusSquare) {
            double maxAngle = circleAngle(axisAngle + extendAngle);
            double minAngle = circleAngle(axisAngle - extendAngle);
            double realAngel = circleAngle(position2Angle(sourceX, sourceY, targetX, targetY));
            boolean across = minAngle > maxAngle;
            return maxAngle >= realAngel && (realAngel >= minAngle || across);
        }
        return false;
    }
    
    public static boolean isInRect(double sourceX, double sourceY, double targetX, double targetY, double axisAngle, double width, double length) {
        checkNotNegative(length, "矩形长度");
        checkNotNegative(width, "矩形宽度");
        if (sourceX == targetX && sourceY == targetY) {
            return true;
        }
        if (length == 0.0 || width == 0.0) {
            return false;
        }
        double distance = distance(sourceX, sourceY, targetX, targetY);
        double targetAngle = position2Angle(sourceX, sourceY, targetX, targetY);
        double relativeAngle = circleAngle(targetAngle - axisAngle);
        double targetRelativeX = angleToXRatio(relativeAngle) * distance;
        double targetRelativeY = angleToYRatio(relativeAngle) * distance;
        double halfWid = width / 2.0;
        return 0.0 <= targetRelativeX && targetRelativeX <= length && -halfWid <= targetRelativeY && targetRelativeY <= halfWid;
    }
    
    private static double circleAngle(double angle) {
        double a = (angle < 0.0) ? (angle + 360.0) : angle;
        return a % 360.0;
    }
    
    public static double distance(double sourceX, double sourceY, double targetX, double targetY) {
        return Math.sqrt(square(targetX - sourceX) + square(targetY - sourceY));
    }
    
    public static double angleToYRatio(double angle) {
        double a = circleAngle(angle);
        if (a == 0.0) {
            return 0.0;
        }
        if (a % 90.0 == 0.0) {
            return (a == 180.0) ? 0.0 : ((double)((a == 270.0) ? -1 : 1));
        }
        return Math.sin(Math.toRadians(a));
    }
    
    public static double angleToXRatio(double angle) {
        double a = circleAngle(angle);
        if (a == 0.0) {
            return 1.0;
        }
        if (a % 90.0 == 0.0) {
            return (a == 180.0) ? -1.0 : 0.0;
        }
        return Math.cos(Math.toRadians(a));
    }
    
    public static double square(double num) {
        return Math.pow(num, 2.0);
    }
    
    private static void checkNotNegative(double num, String errorMessage) {
        if (num < 0.0) {
            throw new IllegalArgumentException("[" + errorMessage + "]不能小于0");
        }
        return;
    }
}
