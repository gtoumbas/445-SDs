// Student_holist.java: sample implementation for Student
// COS 445 HW1, Spring 2018
// Created by Andrew Wonnacott

import java.util.Arrays;
import java.util.List;

public class Student_multi_strat implements Student {
  private class School implements Comparable<School> {
    public School(int i, double q) {
      index = i;
      quality = q;
    }

    private int index;
    private double quality;

    public int compareTo(School n) { // smaller pairs are higher quality
      int ret = Double.compare(n.quality, quality);
      return (ret == 0) ? (Integer.compare(index, n.index)) : ret;
    }
  }

    public static double expectedNumStudentsAbove(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        double school_quality,
        double synergy) {

        double r_u = (aptitude + synergy) / (S + W);
        double K = ((1 - r_u) * N);
        return K;
    }

    public static double averageNumStudentsAbove(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        List<Double> schools,
        List<Double> synergies) {

        double total = 0;
        for (int i = 0; i < schools.size(); i++) {
            total += expectedNumStudentsAbove(N, S, T, W, aptitude, schools.get(i), synergies.get(i));
        }
        return total / schools.size();
    }

    public static double strategyScore(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        List<Double> schools,
        List<Double> synergies) {

        double K = averageNumStudentsAbove(N, S, T, W, aptitude, schools, synergies);
        double kStar = (W / (T + W)) * (1) + (T / (T + W)) * K;
        return kStar;
    }

    public static double[] strategyScores(
            int N,
            double S,
            double T,
            double W,
            double aptitude,
            double[] schoolsTruePref,
            List<Double> synergies) {

        double[] schoolScores = new double[N];
        for (int i = 0; i < N; i++) {
            double studsAbove = expectedNumStudentsAbove(N, S, T, W, aptitude, schoolsTruePref[i], synergies.get(i));
            double chances = (W / (T + W)) * (1 / N) + (T / (T + W)) * (studsAbove / N);// studsAbove/N
            schoolScores[i] = (N - i) * (1 - chances);
        }
        return schoolScores;
    }

    public int[] getApplications(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        List<Double> schools,
        List<Double> synergies) {
          
        School[] truePrefs = new School[schools.size()];
        School[] scorePrefs = new School[schools.size()];
        for (int i = 0; i != synergies.size(); ++i) {
        truePrefs[i] = new School(i, schools.get(i) + synergies.get(i));
        }
        Arrays.sort(truePrefs);
        int[] ret = new int[10];
        double threshold = 0.0;
        // Print out the true preferences
        // for (int i = 0; i != truePrefs.length; ++i) {
        // System.out.println(truePrefs[i].index + " " + truePrefs[i].quality);
        // }
        


        // Case where N = 10, return top 10 schools
        if (N == 10) {
        for (int i = 0; i != 10; ++i) {

            ret[i] = truePrefs[i].index;
        }
        return ret;
        }

        int startingIndex = 0;

        // T is more dominant, use K
        if ((W / T) < threshold) { 
        double K = averageNumStudentsAbove(N, S, T, W, aptitude, schools, synergies);
        startingIndex = (int) K;
        }
        else if ((T / W) < threshold) { // W is more dominant, use 1
        startingIndex = 1;
        }
        else { // Compute weighted kStar
            double kStar = strategyScore(N, S, T, W, aptitude, schools, synergies);
            startingIndex = (int) kStar;
            // Try to be more aggresive
            double howAggressive = 0.05; // 5% more aggressive
            int aggresiveIndex = (int) (startingIndex - (N * howAggressive));

            if (aggresiveIndex > 0) {
                startingIndex = aggresiveIndex;
            }

            // if(startingIndex + 10 < N-4){
            //     // Print N
            //     System.out.println("N: " + N);
            //     startingIndex -= 5;
            // }
        }
        
        // Check if K* + 10 > N
        // If so, return last 10 schools
        if (startingIndex + 10 > N) {
        for (int i = 0; i != 10; ++i) {
            ret[i] = truePrefs[truePrefs.length - 1 - i].index;
        }
        return ret;
        }

        // Otherwise, return startingIndex to startingIndex + 10
        for (int i = 0; i != 10; ++i) {
        ret[i] = truePrefs[startingIndex + i].index;
        }
        return ret;
    }
}
