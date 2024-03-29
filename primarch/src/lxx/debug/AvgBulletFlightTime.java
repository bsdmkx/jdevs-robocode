/*
 * Copyright (c) 2011 Alexey Zhidkov (Jdev). All Rights Reserved.
 */

package lxx.debug;

import lxx.Primarch;
import lxx.office.Office;
import lxx.targeting.Target;
import lxx.utils.AvgValue;
import lxx.wave.Wave;
import lxx.wave.WaveCallback;
import robocode.Rules;

/**
 * User: jdev
 * Date: 01.10.2010
 */
public class AvgBulletFlightTime implements Debugger, WaveCallback {

    private static final AvgValue value = new AvgValue(100000);

    private Office office;

    public void roundStarted(Office office) {
        this.office = office;
    }

    public void roundEnded() {
        System.out.println("Avg bullet flight time: " + value.getCurrentValue());
    }

    public void battleEnded() {
    }

    public void tick() {
        final Primarch primarch = office.getPrimarch();
        if (!office.getTargetManager().hasDuelOpponent()) {
            return;
        }
        final Target target = office.getTargetManager().getDuelOpponent();
        if (target.getState() != null) {
            office.getWaveManager().launchWave(primarch.getState(), target, Rules.getBulletSpeed(primarch.firePower()), this);
        }
    }

    public void wavePassing(Wave w) {
        value.addValue(office.getTime() - w.getLaunchTime());
    }

    public void waveBroken(Wave w) {
    }
}
