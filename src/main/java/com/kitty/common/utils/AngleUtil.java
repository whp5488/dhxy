
package com.kitty.common.utils;

public class AngleUtil
{
    public static final int CIRCLE_ANGLE = 360;
    public static final int HALF_CIRCLE_ANGLE = 180;
    
    private AngleUtil() {
    }
    
    public static void main(final String[] args) {
        System.out.println(isInCircle(0.0, 0.0, 0.0, 0.0, 0.0));
    }
    
    public static double relativePosition2Angle(final double x, final double y) {
        return Math.toDegrees(Math.atan2(y, x));
    }
    
    public static double position2Angle(final double sourceX, final double sourceY, final double targetX, final double targetY) {
        return circleAngle(relativePosition2Angle(targetX - sourceX, targetY - sourceY));
    }
    
    public static boolean isInCircle(final double sourceX, final double sourceY, final double targetX, final double targetY, final double radius) {
        checkNotNegative(radius, "圆半径");
        return square(radius) >= square(targetX - sourceX) + square(targetY - sourceY);
    }
    
    public static boolean isInRing(final double sourceX, final double sourceY, final double targetX, final double targetY, final double innerRadius, final double outerRadius) {
        checkNotNegative(innerRadius, "内圆半径");
        checkNotNegative(outerRadius, "外圆半径");
        final double inner = (innerRadius <= outerRadius) ? innerRadius : outerRadius;
        final double outer = (inner == innerRadius) ? outerRadius : innerRadius;
        final double distanceSquare = square(targetX - sourceX) + square(targetY - sourceY);
        return square(inner) <= distanceSquare && distanceSquare <= square(outer);
    }
    
    public static boolean isInSector(final double sourceX, final double sourceY, final double targetX, final double targetY, final double radius, final double axisAngle, final double extendAngle) {
        checkNotNegative(radius, "扇形半径");
        if (extendAngle >= 180.0) {
            return isInCircle(sourceX, sourceY, targetX, targetY, radius);
        }
        final double radiusSquare = square(radius);
        final double distanceSquare = square(targetX - sourceX) + square(targetY - sourceY);
        if (distanceSquare <= radiusSquare) {
            final double maxAngle = circleAngle(axisAngle + extendAngle);
            final double minAngle = circleAngle(axisAngle - extendAngle);
            final double realAngel = circleAngle(position2Angle(sourceX, sourceY, targetX, targetY));
            final boolean across = minAngle > maxAngle;
            return maxAngle >= realAngel && (realAngel >= minAngle || across);
        }
        return false;
    }
    
    public static boolean isInRect(final double sourceX, final double sourceY, final double targetX, final double targetY, final double axisAngle, final double width, final double length) {
        checkNotNegative(length, "矩形长度");
        checkNotNegative(width, "矩形宽度");
        if (sourceX == targetX && sourceY == targetY) {
            return true;
        }
        if (length == 0.0 || width == 0.0) {
            return false;
        }
        final double distance = distance(sourceX, sourceY, targetX, targetY);
        final double targetAngle = position2Angle(sourceX, sourceY, targetX, targetY);
        final double relativeAngle = circleAngle(targetAngle - axisAngle);
        final double targetRelativeX = angleToXRatio(relativeAngle) * distance;
        final double targetRelativeY = angleToYRatio(relativeAngle) * distance;
        final double halfWid = width / 2.0;
        return 0.0 <= targetRelativeX && targetRelativeX <= length && -halfWid <= targetRelativeY && targetRelativeY <= halfWid;
    }
    
    private static double circleAngle(final double angle) {
        final double a = (angle < 0.0) ? (angle + 360.0) : angle;
        return a % 360.0;
    }
    
    public static double distance(final double sourceX, final double sourceY, final double targetX, final double targetY) {
        return Math.sqrt(square(targetX - sourceX) + square(targetY - sourceY));
    }
    
    public static double angleToYRatio(final double angle) {
        final double a = circleAngle(angle);
        if (a == 0.0) {
            return 0.0;
        }
        if (a % 90.0 == 0.0) {
            return (a == 180.0) ? 0.0 : ((a == 270.0) ? -1 : 1);
        }
        return Math.sin(Math.toRadians(a));
    }
    
    public static double angleToXRatio(final double angle) {
        final double a = circleAngle(angle);
        if (a == 0.0) {
            return 1.0;
        }
        if (a % 90.0 == 0.0) {
            return (a == 180.0) ? -1.0 : 0.0;
        }
        return Math.cos(Math.toRadians(a));
    }
    
    public static double square(final double num) {
        return Math.pow(num, 2.0);
    }
    
    private static void checkNotNegative(final double num, final String errorMessage) {
        if (num < 0.0) {
            throw new IllegalArgumentException("[" + errorMessage + "]不能小于0");
        }
    }
}
