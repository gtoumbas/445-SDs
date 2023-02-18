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
        double kStar = (T / (T + W)) * (1) + (W / (T + W)) * K;
        return kStar;
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
        double threshold = 0.8;


        // Case where N = 10, return top 10 schools
        if (N == 10) {
        for (int i = 0; i != 10; ++i) {

            ret[i] = truePrefs[i].index;
        }
        return ret;
        }

        int startingIndex = 0;

        // If W > T*diffInSize, just use K as index
        if (T / W < threshold) { 
        double K = averageNumStudentsAbove(N, S, T, W, aptitude, schools, synergies);
        startingIndex = (int) K;
        }
        else if (W / T < threshold) { // If T > W*diffInSize, just use K* as index
        startingIndex = 1;
        }
        else { // Compute weighted kStar
            double kStar = strategyScore(N, S, T, W, aptitude, schools, synergies);
            startingIndex = (int) kStar;
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