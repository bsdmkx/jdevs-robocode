/*
 * Copyright (c) 2011 Alexey Zhidkov (Jdev). All Rights Reserved.
 */

package lxx.utils;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ResAnaliser {

    private static final String str1 = "| [[lxx.Tomcat]] 3.52 || [[Author]] || Algorithm ||45,13 ||52,97 ||63,21 ||57,05 ||70,35 ||59,76 ||54,65 ||52,87 ||73,31 ||55,93 ||62,65 ||71,36 ||64,09 ||61,88 ||65,56 ||61,92 ||63,59 ||65,25 ||66,17 ||68,41 ||65,71 ||65,57 ||63,03 ||64,50 ||58,80 ||61,94 ||56,78 ||71,73 ||62,65 ||58,84 ||64,68 ||60,28 ||65,09 ||61,82 ||76,22 ||62,97 ||76,72 ||73,42 ||59,71 ||73,80 ||68,16 ||63,08 ||66,90 ||62,59 ||76,00 ||66,90 ||62,75 ||77,07 ||63,74 ||68,19 ||71,18 ||69,62 ||72,48 ||65,35 ||62,22 ||68,97 ||67,87 ||72,10 ||69,88 ||70,99 ||63,79 ||75,87 ||60,80 ||63,48 ||69,50 ||68,40 ||76,85 ||82,26 ||69,07 ||68,92 ||71,14 ||72,14 ||67,48 ||73,31 ||82,51 ||62,92 ||64,59 ||72,24 ||71,89 ||70,15 ||80,68 ||77,29 ||79,78 ||82,05 ||79,86 ||85,27 ||75,77 ||77,63 ||70,60 ||77,48 ||76,19 ||73,66 ||76,13 ||67,25 ||73,31 ||81,34 ||62,75 ||72,20 ||77,80 ||63,86 ||75,67 ||87,31 ||85,98 ||71,94 ||74,76 ||82,58 ||86,65 ||81,44 ||83,11 ||71,07 ||84,08 ||69,63 ||77,56 ||79,62 ||83,49 ||70,48 ||71,73 ||73,00 ||75,47 ||86,06 ||71,54 ||79,97 ||78,58 ||66,35 ||89,54 ||81,90 ||74,24 ||82,33 ||79,23 ||75,61 ||77,04 ||67,33 ||83,66 ||82,97 ||76,99 ||81,30 ||79,68 ||69,06 ||75,55 ||74,78 ||64,41 ||97,64 ||67,22 ||62,64 ||84,01 ||68,02 ||84,79 ||82,03 ||74,21 ||79,44 ||74,23 ||86,66 ||70,73 ||92,06 ||78,80 ||79,49 ||79,21 ||72,83 ||66,58 ||78,55 ||77,97 ||70,52 ||78,38 ||90,39 ||90,88 ||89,94 ||72,80 ||78,94 ||87,22 ||80,37 ||81,43 ||80,27 ||76,91 ||79,11 ||73,85 ||79,16 ||84,15 ||88,24 ||87,39 ||88,20 ||84,97 ||73,74 ||72,66 ||87,17 ||74,17 ||70,47 ||83,05 ||72,93 ||82,66 ||63,19 ||75,68 ||81,69 ||73,27 ||69,45 ||73,73 ||80,32 ||78,38 ||85,45 ||80,34 ||71,30 ||74,64 ||80,86 ||81,20 ||73,56 ||79,66 ||74,09 ||77,63 ||79,79 ||80,59 ||81,35 ||85,40 ||74,71 ||78,87 ||82,37 ||84,90 ||87,15 ||80,37 ||77,83 ||88,15 ||78,94 ||82,86 ||77,95 ||84,88 ||88,89 ||86,17 ||88,62 ||89,93 ||75,59 ||88,56 ||83,81 ||82,16 ||77,56 ||86,27 ||74,83 ||72,94 ||78,78 ||79,21 ||97,77 ||80,36 ||86,08 ||79,24 ||85,17 ||77,57 ||64,53 ||84,71 ||79,81 ||79,47 ||85,21 ||77,90 ||83,51 ||84,42 ||80,92 ||79,02 ||78,92 ||87,11 ||77,84 ||75,33 ||78,89 ||80,17 ||75,11 ||79,59 ||84,16 ||78,98 ||85,06 ||99,64 ||89,81 ||81,19 ||75,78 ||83,49 ||75,68 ||98,19 ||78,02 ||81,98 ||75,66 ||79,92 ||81,20 ||84,14 ||86,61 ||86,19 ||98,90 ||88,08 ||82,26 ||84,04 ||77,38 ||83,14 ||91,98 ||90,84 ||82,34 ||79,60 ||80,91 ||84,53 ||82,14 ||71,84 ||79,38 ||78,06 ||84,34 ||71,08 ||84,38 ||91,44 ||90,52 ||75,28 ||82,68 ||88,46 ||82,92 ||81,00 ||82,79 ||83,24 ||81,52 ||71,49 ||77,12 ||90,05 ||74,40 ||87,41 ||82,54 ||82,60 ||82,59 ||78,22 ||78,46 ||77,57 ||91,34 ||78,64 ||76,90 ||77,50 ||72,63 ||86,49 ||78,01 ||83,86 ||87,57 ||82,43 ||74,50 ||97,67 ||77,28 ||81,51 ||87,84 ||84,58 ||88,16 ||72,84 ||78,77 ||88,56 ||78,01 ||86,21 ||83,54 ||68,84 ||88,10 ||91,75 ||90,76 ||77,03 ||90,73 ||67,78 ||80,16 ||66,66 ||87,35 ||87,52 ||84,17 ||83,12 ||91,58 ||79,00 ||81,70 ||83,02 ||87,18 ||91,73 ||84,66 ||88,92 ||76,06 ||68,44 ||87,64 ||78,24 ||81,39 ||94,69 ||86,64 ||89,49 ||67,80 ||76,24 ||83,90 ||76,27 ||98,65 ||83,84 ||78,67 ||85,52 ||85,59 ||90,10 ||90,39 ||87,48 ||79,63 ||84,12 ||92,92 ||91,85 ||87,64 ||90,49 ||95,82 ||86,29 ||97,34 ||85,10 ||85,11 ||97,80 ||94,39 ||92,09 ||91,95 ||90,63 ||83,38 ||86,31 ||91,23 ||86,18 ||89,68 ||88,33 ||97,89 ||87,78 ||67,69 ||89,92 ||98,89 ||86,23 ||79,53 ||72,19 ||81,31 ||93,56 ||95,02 ||84,72 ||86,58 ||85,39 ||99,62 ||92,64 ||84,02 ||87,05 ||90,22 ||98,15 ||86,51 ||86,82 ||89,71 ||78,49 ||93,07 ||93,91 ||85,62 ||98,19 ||95,68 ||99,56 ||88,61 ||88,63 ||85,76 ||88,96 ||88,85 ||96,01 ||69,19 ||91,65 ||99,84 ||92,02 ||85,86 ||97,45 ||74,68 ||92,08 ||97,48 ||87,35 ||87,15 ||80,00 ||88,35 ||98,72 ||91,97 ||96,50 ||98,06 ||91,22 ||88,09 ||98,62 ||76,10 ||90,97 ||85,08 ||92,95 ||85,67 ||95,99 ||97,31 ||96,98 ||98,06 ||84,46 ||91,45 ||98,23 ||82,51 ||97,71 ||93,47 ||74,55 ||89,90 ||97,95 ||87,43 ||88,19 ||88,86 ||72,43 ||94,64 ||85,46 ||98,56 ||92,08 ||99,73 ||96,66 ||87,01 ||94,70 ||91,75 ||91,80 ||97,74 ||98,21 ||95,50 ||99,11 ||99,79 ||91,95 ||88,68 ||92,36 ||90,13 ||93,70 ||81,59 ||99,08 ||98,61 ||98,72 ||99,28 ||97,07 ||99,15 ||90,14 ||91,26 ||99,40 ||98,77 ||88,54 ||98,57 ||98,80 ||99,77 ||95,32 ||82,47 ||89,56 ||86,47 ||91,36 ||83,74 ||97,03 ||86,16 ||70,88 ||98,48 ||95,42 ||85,70 ||88,39 ||88,15 ||90,91 ||98,80 ||97,21 ||95,18 ||92,24 ||86,05 ||99,64 ||95,35 ||90,64 ||96,89 ||99,76 ||97,89 ||82,36 ||94,45 ||99,50 ||96,58 ||98,60 ||98,06 ||98,47 ||98,11 ||87,80 ||95,43 ||91,71 ||97,93 ||89,21 ||93,53 ||98,17 ||92,14 ||86,84 ||94,35 ||97,15 ||93,95 ||99,60 ||81,97 ||98,05 ||94,01 ||91,38 ||97,25 ||97,67 ||94,87 ||95,35 ||98,91 ||94,76 ||93,17 ||93,54 ||91,70 ||97,98 ||87,96 ||99,75 ||98,22 ||87,74 ||90,82 ||88,33 ||99,28 ||93,59 ||98,71 ||95,14 ||92,88 ||96,38 ||97,38 ||98,28 ||94,81 ||91,69 ||100,00 ||98,77 ||91,39 ||94,92 ||99,55 ||87,63 ||99,41 ||99,19 ||90,04 ||99,65 ||94,54 ||98,28 ||89,54 ||99,55 ||94,60 ||93,47 ||93,58 ||85,45 ||98,39 ||95,29 ||100,00 ||93,71 ||99,24 ||99,50 ||93,39 ||95,57 ||97,12 ||97,16 ||98,26 ||99,98 ||99,75 ||90,70 ||99,75 ||99,28 ||86,16 ||98,43 ||98,87 ||99,45 ||98,01 ||97,96 ||94,29 ||95,80 ||95,29 ||98,75 ||95,96 ||95,49 ||96,43 ||87,19 ||99,83 ||89,31 ||98,33 ||99,83 ||99,60 ||90,44 ||94,07 ||98,87 ||92,58 ||96,37 ||98,98 ||98,97 ||97,48 ||99,54 ||92,38 ||98,03 ||98,11 ||98,05 ||95,69 ||95,95 ||99,06 ||88,20 ||95,23 ||99,44 ||94,68 ||96,14 ||98,59 ||99,55 ||94,19 ||94,40 ||92,91 ||99,70 ||98,83 ||99,82 ||91,71 ||98,61 ||90,71 ||95,26 ||90,17 ||98,29 ||96,14 ||98,56 ||92,85 ||95,04 ||91,04 ||98,05 ||99,50 ||93,16 ||99,59 ||99,79 ||96,67 ||87,11 ||99,72 ||98,47 ||83,78 ||99,48 ||99,95 ||95,39 ||98,06 ||98,53 ||90,02 ||96,39 ||99,37 ||98,44 ||99,83 ||94,23 ||88,25 ||95,84 ||97,52 ||92,79 ||100,00 ||98,92 ||93,95 ||99,76 ||94,88 ||91,84 ||93,17 ||95,02 ||98,96 ||84,33 ||99,01 ||97,98 ||98,36 ||95,35 ||97,04 ||95,83 ||99,33 ||98,95 ||99,77 ||99,74 ||86,44 ||99,63 ||96,30 ||97,63 ||99,60 ||97,71 ||97,97 ||96,99 ||99,16 ||99,96 ||99,64 ||96,64 ||97,69 ||99,27 ||99,25 ||76,18 ||98,08 ||97,04 ||97,40 ||96,79 ||98,15 ||99,34 ||94,33 ||99,47 ||98,23 ||98,92 ||94,74 ||97,16 ||99,19 ||99,49 ||96,90 ||97,12 ||99,23 ||96,60 ||99,38 ||99,20 ||99,66 ||99,91 ||96,71 ||99,70 ||95,92 ||96,71 ||96,43 ||93,29 ||99,61 ||99,99 ||99,57 ||99,64 ||98,84 ||99,61 ||99,95 ||99,78 ||99,66 ||93,84 ||99,92 ||98,45 ||98,53 ||99,84 ||95,70 ||84,14 ||99,98 ||100,00 ||98,65 ||99,12 ||99,74 ||99,76 ||100,00 ||98,42 ||99,71 ||99,57 ||99,14 ||100,00 ||99,89 ||99,96 ||96,93 ||99,91 ||99,98 ||98,32 ||100,00 ||100,00 ||100,00 ||100,00 ||99,96 ||99,56 ||100,00 ||99,64 ||99,97 ||99,11 ||99,77 ||98,77 ||99,98 ||99,99 ||99,78 ||100,00 ||99,91 ||98,17 ||99,99 ||99,98 ||99,95 ||100,00 ||100,00 ||100,00 ||100,00 ||'''86,79 (87,88)''' ||'''86,79 (87,88)''' ||4 seasons";
    private static final String str2 = "| [[lxx.Tomcat]] 3.52b.knn.movement || [[Author]] || Algorithm ||41,51 ||61,48 ||56,41 ||56,77 ||60,96 ||48,05 ||52,23 ||64,19 ||59,13 ||46,90 ||68,49 ||58,59 ||70,41 ||61,11 ||69,35 ||58,09 ||63,60 ||68,17 ||67,66 ||62,03 ||57,83 ||67,47 ||55,61 ||65,33 ||49,80 ||66,56 ||54,36 ||55,98 ||63,68 ||49,98 ||65,38 ||54,02 ||69,12 ||57,21 ||66,23 ||61,58 ||68,41 ||59,42 ||70,27 ||66,89 ||62,03 ||58,32 ||67,65 ||55,21 ||70,30 ||62,96 ||59,24 ||67,19 ||63,69 ||65,73 ||62,26 ||67,25 ||71,52 ||68,25 ||63,10 ||67,48 ||66,74 ||65,69 ||58,75 ||64,80 ||66,35 ||73,29 ||56,68 ||67,19 ||76,41 ||74,93 ||66,58 ||78,64 ||66,90 ||65,23 ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||82,50 ||92,43 ||87,86 ||89,25 ||91,17 ||95,46 ||82,53 ||97,48 ||85,18 ||84,48 ||98,53 ||93,25 ||93,98 ||91,42 ||92,69 ||84,49 ||84,41 ||91,70 ||83,81 ||91,71 ||92,73 ||93,70 ||88,78 ||69,32 ||85,24 ||99,42 ||84,84 ||76,51 ||73,28 ||87,90 ||90,95 ||96,92 ||88,27 ||83,12 ||82,95 ||98,69 ||89,35 ||83,38 ||86,59 ||87,88 ||98,91 ||84,02 ||83,57 ||91,31 ||82,21 ||96,34 ||96,58 ||82,56 ||96,45 ||95,50 ||99,18 ||93,35 ||93,28 ||88,84 ||91,59 ||92,29 ||96,88 ||77,57 ||89,74 ||99,65 ||93,42 ||83,65 ||97,45 ||77,84 ||91,78 ||98,35 ||90,43 ||83,40 ||79,46 ||81,44 ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||NaN ||97,50 ||100,00 ||98,81 ||99,81 ||99,43 ||96,96 ||99,43 ||96,51 ||94,09 ||97,00 ||94,32 ||99,90 ||99,73 ||99,78 ||99,64 ||98,50 ||99,78 ||100,00 ||99,53 ||98,70 ||93,19 ||99,56 ||98,27 ||98,83 ||100,00 ||97,39 ||83,94 ||99,71 ||100,00 ||98,14 ||99,86 ||99,79 ||100,00 ||100,00 ||98,96 ||99,72 ||99,64 ||99,57 ||100,00 ||100,00 ||99,84 ||99,68 ||99,93 ||100,00 ||97,69 ||100,00 ||100,00 ||100,00 ||100,00 ||99,97 ||99,66 ||100,00 ||98,56 ||100,00 ||99,18 ||98,81 ||99,55 ||100,00 ||100,00 ||99,71 ||100,00 ||100,00 ||99,66 ||99,93 ||99,93 ||99,97 ||100,00 ||100,00 ||100,00 ||100,00 ||'''83,67 (0,00)''' ||'''83,67 (0,00)''' ||4 seasons";

    public static void main(String[] args) {
        String[] str1Arr = str1.split("\\|\\|");
        String[] str2Arr = str2.split("\\|\\|");
        AvgValue diff = new AvgValue(10000);
        AvgValue avgAps1 = new AvgValue(10000);
        AvgValue avgAps2 = new AvgValue(10000);
        AvgValue avgDelta1 = new AvgValue(10000);
        AvgValue avgDelta2 = new AvgValue(10000);
        double minDiff = Integer.MAX_VALUE;
        double maxDiff = Integer.MIN_VALUE;
        Double prevAps1 = null;
        Double prevAps2 = null;
        int cnt = 0;
        for (int i = 0; i < str1Arr.length; i++) {
            try {
                final double aps1 = new Double(str1Arr[i].trim().replace(",", "."));
                final double aps2 = new Double(str2Arr[i].trim().replace(",", "."));
                if (Double.isNaN(aps1) || Double.isNaN(aps2)) {
                    continue;
                }
                avgAps1.addValue(aps1);
                avgAps2.addValue(aps2);
                final double d = aps2 - aps1;
                diff.addValue(d);
                minDiff = min(minDiff, d);
                maxDiff = max(maxDiff, d);
                if (prevAps1 != null) {
                    avgDelta1.addValue(aps1 - prevAps1);
                    avgDelta2.addValue(aps2 - prevAps2);
                }
                prevAps1 = aps1;
                prevAps2 = aps2;
                cnt++;
            } catch (NumberFormatException ignore) {
            }
        }

        System.out.println("Diff = " + diff.getCurrentValue());
        System.out.println("Ref APS = " + avgAps1.getCurrentValue());
        System.out.println("Chel aps = " + avgAps2.getCurrentValue());
        System.out.println("Wrost change = " + minDiff);
        System.out.println("best change =" + maxDiff);
        System.out.println("Cnt = " + cnt);
    }

}
