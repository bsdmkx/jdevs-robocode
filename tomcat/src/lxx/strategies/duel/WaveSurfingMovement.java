/*
 * Copyright (c) 2011 Alexey Zhidkov (Jdev). All Rights Reserved.
 */

package lxx.strategies.duel;

import lxx.LXXRobotState;
import lxx.Tomcat;
import lxx.bullets.LXXBullet;
import lxx.bullets.PastBearingOffset;
import lxx.bullets.enemy.EnemyBulletManager;
import lxx.bullets.enemy.EnemyBulletPredictionData;
import lxx.office.Office;
import lxx.paint.LXXGraphics;
import lxx.paint.Painter;
import lxx.strategies.Movement;
import lxx.strategies.MovementDecision;
import lxx.targeting.Target;
import lxx.targeting.TargetManager;
import lxx.targeting.tomcat_eyes.TomcatEyes;
import lxx.utils.*;
import robocode.Rules;
import robocode.util.Utils;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;

public class WaveSurfingMovement implements Movement, Painter {

    private final Tomcat robot;
    private final TomcatEyes tomcatEyes;
    private final TargetManager targetManager;
    private final EnemyBulletManager enemyBulletManager;
    private final DistanceController distanceController;

    private OrbitDirection minDangerOrbitDirection = OrbitDirection.CLOCKWISE;
    private double distanceToTravel;
    private Target duelOpponent;
    private MovementDirectionPrediction prevPrediction;
    private BattleField battleField;
    private MovementDirectionPrediction clockwisePrediction;
    private MovementDirectionPrediction counterClockwisePrediction;

    public WaveSurfingMovement(Office office, TomcatEyes tomcatEyes) {
        this.robot = office.getRobot();
        this.targetManager = office.getTargetManager();
        this.enemyBulletManager = office.getEnemyBulletManager();
        this.tomcatEyes = tomcatEyes;

        distanceController = new DistanceController(office.getTargetManager(), tomcatEyes);
        battleField = robot.getState().getBattleField();
    }

    public MovementDecision getMovementDecision() {
        duelOpponent = targetManager.getDuelOpponent();
        final List<LXXBullet> lxxBullets = getBullets();
        if (needToReselectOrbitDirection(lxxBullets)) {
            selectOrbitDirection(lxxBullets);
        } else {
            distanceToTravel -= robot.getSpeed();
        }

        final Target.TargetState opponent = duelOpponent == null ? null : duelOpponent.getState();
        final APoint surfPoint = getSurfPoint(opponent, lxxBullets.get(0));
        final double desiredSpeed =
                (distanceToTravel > LXXUtils.getStopDistance(robot.getSpeed()) + Rules.MAX_VELOCITY ||
                        (duelOpponent != null && tomcatEyes.isRammingNow(duelOpponent)))
                        ? 8
                        : 0;

        return getMovementDecision(surfPoint, minDangerOrbitDirection, robot.getState(), opponent, desiredSpeed);
    }

    private boolean needToReselectOrbitDirection(List<LXXBullet> bullets) {
        return prevPrediction == null ||
                isBulletsUpdated(bullets) ||
                (duelOpponent != null && signum(duelOpponent.getAcceleration()) != prevPrediction.enemyAccelSign) ||
                (duelOpponent != null && duelOpponent.aDistance(robot) < prevPrediction.distanceBetween - 25) ||
                distanceToTravel <= LXXUtils.getStopDistance(robot.getSpeed()) + Rules.MAX_VELOCITY;
    }

    private boolean isBulletsUpdated(List<LXXBullet> newBullets) {
        return (newBullets.get(0).getAimPredictionData()).getPredictionRoundTime() !=
                prevPrediction.firstBulletPredictionTime;
    }

    private void selectOrbitDirection(List<LXXBullet> lxxBullets) {
        clockwisePrediction = predictMovementInDirection(lxxBullets, OrbitDirection.CLOCKWISE);
        counterClockwisePrediction = predictMovementInDirection(lxxBullets, OrbitDirection.COUNTER_CLOCKWISE);
        final int cmp = (int) signum(clockwisePrediction.minDanger.danger * (prevPrediction != null && prevPrediction.orbitDirection == OrbitDirection.CLOCKWISE ? 0.9 : 1)
                -
                counterClockwisePrediction.minDanger.danger * (prevPrediction != null && prevPrediction.orbitDirection == OrbitDirection.COUNTER_CLOCKWISE ? 0.9 : 1));
        if (cmp < 0) {
            setMovementParameters(clockwisePrediction);
        } else if (cmp > 0) {
            setMovementParameters(counterClockwisePrediction);
        } else if (prevPrediction != null && prevPrediction.orbitDirection == OrbitDirection.CLOCKWISE) {
            setMovementParameters(clockwisePrediction);
        } else {
            setMovementParameters(counterClockwisePrediction);
        }
    }

    private void setMovementParameters(MovementDirectionPrediction movementDirectionPrediction) {
        distanceToTravel = movementDirectionPrediction.distToMinDangerPoint;
        minDangerOrbitDirection = movementDirectionPrediction.orbitDirection;
        prevPrediction = movementDirectionPrediction;
    }

    private MovementDirectionPrediction predictMovementInDirection(List<LXXBullet> lxxBullets, OrbitDirection orbitDirection) {
        final MovementDirectionPrediction prediction = new MovementDirectionPrediction();
        prediction.enemyPos = duelOpponent != null ? duelOpponent.getPosition() : null;
        prediction.bullets = lxxBullets;
        prediction.firstBulletPredictionTime = lxxBullets.get(0).getAimPredictionData().getPredictionRoundTime();
        prediction.orbitDirection = orbitDirection;
        double distance = 0;
        APoint prevPoint = robot.getPosition();
        prediction.points = generatePoints(orbitDirection, lxxBullets, duelOpponent);
        prediction.enemyAccelSign = duelOpponent != null ? signum(duelOpponent.getAcceleration()) : 0;
        prediction.distanceBetween = duelOpponent != null ? duelOpponent.aDistance(robot) : 0;
        for (WSPoint pnt : prediction.points) {
            distance += prevPoint.aDistance(pnt);

            if (pnt.danger.danger < prediction.minDanger.danger) {
                prediction.minDanger = pnt.danger;
                prediction.distToMinDangerPoint = distance;
                prediction.minDangerPoint = pnt;
            }
            prevPoint = pnt;
        }

        return prediction;
    }

    private PointDanger getPointDanger(List<LXXBullet> lxxBullets, LXXRobotState robot, LXXRobotState duelOpponent) {
        final int bulletsSize = lxxBullets.size();
        final double firstWaveDng = bulletsSize == 0 ? 0 : getWaveDanger(robot, lxxBullets.get(0));
        final double secondWaveDng = bulletsSize == 1 ? 0 : getWaveDanger(robot, lxxBullets.get(1));
        final double distToEnemy = duelOpponent != null ? robot.aDistance(duelOpponent) : 5;
        double enemyAttackAngle = duelOpponent == null
                ? LXXConstants.RADIANS_90
                : LXXUtils.anglesDiff(duelOpponent.angleTo(robot), robot.getHeadingRadians());
        if (enemyAttackAngle > LXXConstants.RADIANS_90) {
            enemyAttackAngle = abs(enemyAttackAngle - LXXConstants.RADIANS_180);
        }
        return new PointDanger(firstWaveDng, secondWaveDng, distToEnemy, battleField.center.aDistance(robot),
                LXXConstants.RADIANS_90 - enemyAttackAngle);
    }

    private double getWaveDanger(APoint pnt, LXXBullet bullet) {
        final EnemyBulletPredictionData aimPredictionData = (EnemyBulletPredictionData) bullet.getAimPredictionData();
        final List<PastBearingOffset> predictedBearingOffsets = aimPredictionData.getPredictedBearingOffsets();
        if (predictedBearingOffsets.size() == 0) {
            return 0;
        }
        final APoint firePos = bullet.getFirePosition();
        final double alpha = firePos.angleTo(pnt);
        final double bearingOffset = Utils.normalRelativeAngle(alpha - bullet.noBearingOffset());
        final double robotWidthInRadians = LXXUtils.getRobotWidthInRadians(alpha, firePos.aDistance(pnt));

        double bulletsDanger = 0;
        final double hiEffectDist = robotWidthInRadians * 0.75;
        final double lowEffectDist = robotWidthInRadians * 2.55;
        for (PastBearingOffset bo : predictedBearingOffsets) {
            final double dist = abs(bearingOffset - bo.bearingOffset);
            if (dist < hiEffectDist) {
                bulletsDanger += (2 - (dist / hiEffectDist)) * bo.danger;
            } else {
                if (dist < lowEffectDist) {
                    bulletsDanger += (1 - (dist / lowEffectDist)) * bo.danger;
                }
            }
        }

        double intersection = 0;
        final IntervalDouble robotIval = new IntervalDouble(bearingOffset - robotWidthInRadians / 2, bearingOffset + robotWidthInRadians / 2);
        for (IntervalDouble shadow : bullet.getMergedShadows()) {
            if (robotIval.intersects(shadow)) {
                intersection += robotIval.intersection(shadow);
            }
        }
        bulletsDanger *= 1 - intersection / robotWidthInRadians;

        return bulletsDanger;
    }

    private List<LXXBullet> getBullets() {
        List<LXXBullet> bulletsOnAir = enemyBulletManager.getBulletsOnAir(2);
        if (bulletsOnAir.size() < 2 && duelOpponent != null) {
            bulletsOnAir.add(enemyBulletManager.createFutureBullet(duelOpponent));
        }
        if (bulletsOnAir.size() == 0) {
            bulletsOnAir = enemyBulletManager.getAllBulletsOnAir();
        }
        return bulletsOnAir;
    }

    private APoint getSurfPoint(LXXRobotState duelOpponent, LXXBullet bullet) {
        if (duelOpponent == null) {
            return bullet.getFirePosition();
        }

        return duelOpponent;
    }

    private List<WSPoint> generatePoints(OrbitDirection orbitDirection, List<LXXBullet> bullets, Target enemy) {
        final LXXBullet bullet = bullets.get(0);
        final List<WSPoint> points = new LinkedList<WSPoint>();

        final RobotImage robotImg = new RobotImage(robot.getPosition(), robot.getVelocity(), robot.getHeadingRadians(), robot.battleField, 0, robot.getEnergy());
        final RobotImage opponentImg = enemy == null ? null : new RobotImage(enemy.getPosition(), enemy.getVelocity(), enemy.getState().getHeadingRadians(), robot.battleField, 0,
                enemy.getEnergy());
        int time = 0;
        final APoint surfPoint = getSurfPoint(opponentImg, bullet);
        final double travelledDistance = bullet.getTravelledDistance();
        final APoint firePosition = bullet.getFirePosition();
        final double bulletSpeed = bullet.getSpeed();
        final double enemyDesiredVelocity;
        if (opponentImg != null) {
            enemyDesiredVelocity = Rules.MAX_VELOCITY * signum(opponentImg.getVelocity());
        } else {
            enemyDesiredVelocity = 0;
        }
        while (firePosition.aDistance(robotImg) - travelledDistance > bulletSpeed * time) {
            final MovementDecision md = getMovementDecision(surfPoint, orbitDirection, robotImg, opponentImg, 8);
            if (opponentImg != null) {
                opponentImg.apply(new MovementDecision(enemyDesiredVelocity, 0));
                for (WSPoint prevPoint : points) {
                    prevPoint.danger.distToEnemy = min(prevPoint.danger.distToEnemy, prevPoint.aDistance(opponentImg));
                    prevPoint.danger.calculateDanger();
                }
            }
            robotImg.apply(md);
            points.add(new WSPoint(robotImg, getPointDanger(bullets, robotImg, opponentImg)));
            time++;
        }

        return points;
    }

    private MovementDecision getMovementDecision(APoint surfPoint, OrbitDirection orbitDirection,
                                                 LXXRobotState robot, LXXRobotState opponent, double desiredSpeed) {
        double desiredHeading = distanceController.getDesiredHeading(surfPoint, robot, orbitDirection);
        desiredHeading = battleField.smoothWalls(robot, desiredHeading, orbitDirection == OrbitDirection.CLOCKWISE);

        double direction = robot.getAbsoluteHeadingRadians();
        if (LXXUtils.anglesDiff(direction, desiredHeading) > LXXConstants.RADIANS_90) {
            direction = Utils.normalAbsoluteAngle(direction + LXXConstants.RADIANS_180);
        }
        if (opponent != null) {
            double angleToOpponent = robot.angleTo(opponent);
            if (((LXXUtils.anglesDiff(direction, angleToOpponent) < LXXUtils.getRobotWidthInRadians(angleToOpponent, robot.aDistance(opponent)) * 1.1)) ||
                    LXXUtils.getBoundingRectangleAt(robot.project(direction, desiredSpeed), LXXConstants.ROBOT_SIDE_SIZE / 2 - 2).intersects(LXXUtils.getBoundingRectangleAt(opponent))) {
                desiredSpeed = 0;
            }
        }

        return MovementDecision.toMovementDecision(robot, desiredSpeed, desiredHeading);
    }

    public void paint(LXXGraphics g) {
        if (prevPrediction == null) {
            return;
        }

        drawPath(g, clockwisePrediction.points, new Color(0, 255, 0, 200));
        drawPath(g, counterClockwisePrediction.points, new Color(255, 0, 0, 200));

        g.setColor(new Color(0, 255, 0, 200));
        g.fillCircle(prevPrediction.minDangerPoint, 15);
    }

    private void drawPath(LXXGraphics g, List<WSPoint> points, Color color) {
        g.setColor(color);
        for (WSPoint pnt : points) {
            g.fillCircle(pnt, 3);
        }
    }

    private class WSPoint extends LXXPoint {

        private final PointDanger danger;

        private WSPoint(APoint point, PointDanger danger) {
            super(point);
            this.danger = danger;
        }
    }

    public enum OrbitDirection {

        CLOCKWISE(1),
        COUNTER_CLOCKWISE(-1);

        public final int sign;

        OrbitDirection(int sign) {
            this.sign = sign;
        }
    }

    public class MovementDirectionPrediction {

        private PointDanger MAX_POINT_DANGER = new PointDanger(100, 100, 5, 1000, LXXConstants.RADIANS_90);

        private PointDanger minDanger = MAX_POINT_DANGER;
        private APoint minDangerPoint;
        private double distToMinDangerPoint;
        private OrbitDirection orbitDirection;
        public LXXPoint enemyPos;
        public List<LXXBullet> bullets;
        public List<WSPoint> points;
        public double enemyAccelSign;
        public double distanceBetween;
        public long firstBulletPredictionTime;
    }

    private class PointDanger {

        public final double dangerOnFirstWave;
        public final double dangerOnSecondWave;
        public double distToEnemy;
        public final double distanceToCenter;
        public final double enemyAttackAngle;
        public double danger;

        private PointDanger(double dangerOnFirstWave, double dangerOnSecondWave, double distToEnemy, double distanceToWall,
                            double enemyAttackAngle) {
            this.dangerOnFirstWave = dangerOnFirstWave;
            this.dangerOnSecondWave = dangerOnSecondWave;
            this.distToEnemy = distToEnemy;
            this.distanceToCenter = distanceToWall;
            this.enemyAttackAngle = enemyAttackAngle;

            calculateDanger();
        }

        private void calculateDanger() {
            this.danger = dangerOnFirstWave * 120 +
                    dangerOnSecondWave * 10 +
                    distanceToCenter / 800 * 5 +
                    max(0, (500 - distToEnemy)) / distToEnemy * 15;
        }

        @Override
        public String toString() {
            return String.format("PointDanger (%s #1, %s #2, %3.3f, %3.3f)", dangerOnFirstWave, dangerOnSecondWave, distToEnemy, distanceToCenter);
        }
    }

}
