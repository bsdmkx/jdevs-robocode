/*
 * Copyright (c) 2011 Alexey Zhidkov (Jdev). All Rights Reserved.
 */

package lxx.utils;

import robocode.util.Utils;

import java.awt.*;

/**
 * User: jdev
 * Date: 17.02.2011
 */
public class BattleField {

    public final APoint availableLeftBottom;
    public final APoint availableLeftTop;
    public final APoint availableRightTop;
    public final APoint availableRightBottom;

    private final APoint leftBottom;
    private final APoint leftTop;
    private final APoint rightTop;
    private final APoint rightBottom;

    public final Wall bottom;
    public final Wall left;
    public final Wall top;
    public final Wall right;

    public final int availableBottomY;
    public final int availableTopY;
    public final int availableLeftX;
    public final int availableRightX;

    public final Rectangle battleField;
    public final Rectangle availableBattleFieldRectangle;

    public final APoint center;

    public final int width;
    public final int height;

    public BattleField(int x, int y, int width, int height) {
        availableBottomY = y;
        availableTopY = y + height;
        availableLeftX = x;
        availableRightX = x + width;

        availableLeftBottom = new LXXPoint(availableLeftX, availableBottomY);
        availableLeftTop = new LXXPoint(availableLeftX, availableTopY);
        availableRightTop = new LXXPoint(availableRightX, availableTopY);
        availableRightBottom = new LXXPoint(availableRightX, availableBottomY);

        final int bottomY = 0;
        final int topY = y * 2 + height;
        final int leftX = 0;
        final int rightX = x * 2 + width;

        leftBottom = new LXXPoint(leftX, bottomY);
        leftTop = new LXXPoint(leftX, topY);
        rightTop = new LXXPoint(rightX, topY);
        rightBottom = new LXXPoint(rightX, bottomY);

        bottom = new Wall(WallType.BOTTOM, availableRightBottom, availableLeftBottom);
        left = new Wall(WallType.LEFT, availableLeftBottom, availableLeftTop);
        top = new Wall(WallType.TOP, availableLeftTop, availableRightTop);
        right = new Wall(WallType.RIGHT, availableRightTop, availableRightBottom);
        bottom.clockwiseWall = left;
        bottom.counterClockwiseWall = right;
        left.clockwiseWall = top;
        left.counterClockwiseWall = bottom;
        top.clockwiseWall = right;
        top.counterClockwiseWall = left;
        right.clockwiseWall = bottom;
        right.counterClockwiseWall = top;

        battleField = new Rectangle(0, 0, width + x * 2, height + y * 2);
        availableBattleFieldRectangle = new Rectangle(x, y, width, height);

        center = new LXXPoint(width / 2, height / 2);

        this.width = width;
        this.height = height;
    }

    // this method is called very often, so keep it optimal
    public Wall getWall(APoint pos, double heading) {
        final double alphaToLeftBottomAngle = pos.angleTo(leftBottom);
        if (alphaToLeftBottomAngle > heading) {
            final double alphaToRightBottomAngle = pos.angleTo(rightBottom);
            if (heading >= alphaToRightBottomAngle) {
                return bottom;
            } else {
                final double alphaToRightTopAngle = pos.angleTo(rightTop);
                if (heading >= alphaToRightTopAngle) {
                    return right;
                } else {
                    return top;
                }
            }
        } else {
            double alphaToLeftTopAngle = pos.angleTo(leftTop);
            if (alphaToLeftTopAngle < alphaToLeftBottomAngle) {
                alphaToLeftTopAngle = LXXConstants.RADIANS_360;
            }
            if (heading < alphaToLeftTopAngle) {
                return left;
            } else {
                return top;
            }
        }
    }

    public double getBearingOffsetToWall(APoint pnt, double heading) {
        return Utils.normalRelativeAngle(getWall(pnt, heading).wallType.fromCenterAngle - heading);
    }

    public double getDistanceToWall(Wall wall, APoint pnt) {
        switch (wall.wallType) {
            case TOP:
                return availableTopY - pnt.getY();
            case RIGHT:
                return availableRightX - pnt.getX();
            case BOTTOM:
                return pnt.getY() - availableBottomY;
            case LEFT:
                return pnt.getX() - availableLeftX;
            default:
                throw new IllegalArgumentException("Unknown wallType: " + wall.wallType);
        }
    }

    private double getWallSmoothStickLength(APoint pos, double heading) {
        final Wall wall = getWall(pos, heading);
        final double targetAngle = LXXUtils.anglesDiff(heading, wall.wallType.clockwiseAngle) <
                LXXUtils.anglesDiff(heading, wall.wallType.counterClockwiseAngle) ? wall.wallType.clockwiseAngle : wall.wallType.counterClockwiseAngle;

        return LXXUtils.getTurnDistance(heading, targetAngle);
    }

    public double smoothWalls(APoint pos, double heading, boolean isClockwise) {
        return smoothWall(getWall(pos, heading), pos, heading, isClockwise);
    }

    private double smoothWall(Wall wall, APoint pos, double heading, boolean isClockwise) {
        final double hypotenuse = getWallSmoothStickLength(pos, heading);
        final double adjacentLeg = getDistanceToWall(wall, pos) - 2;
        if (hypotenuse < adjacentLeg) {
            return heading;
        }
        final double smoothAngle = (QuickMath.acos((adjacentLeg) / (hypotenuse))) *
                (isClockwise ? 1 : -1);
        final double baseAngle = wall.wallType.fromCenterAngle;
        double smoothedAngle = Utils.normalAbsoluteAngle(baseAngle + smoothAngle);
        if (!contains(pos.project(smoothedAngle, hypotenuse))) {
            final Wall secondWall = isClockwise ? wall.clockwiseWall : wall.counterClockwiseWall;
            return smoothWall(secondWall, pos, smoothedAngle, isClockwise);
        }
        return smoothedAngle;
    }

    public boolean contains(APoint point) {
        return availableBattleFieldRectangle.contains(point.getX(), point.getY());
    }

    public class Wall {

        public final WallType wallType;
        public final APoint ccw;
        public final APoint cw;

        private Wall clockwiseWall;
        private Wall counterClockwiseWall;

        private Wall(WallType wallType, APoint ccw, APoint cw) {
            this.wallType = wallType;
            this.ccw = ccw;
            this.cw = cw;
        }
    }

}
