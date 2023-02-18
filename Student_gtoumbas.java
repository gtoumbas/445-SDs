// Student_holist.java: sample implementation for Student
// COS 445 HW1, Spring 2018
// Created by Andrew Wonnacott

import java.util.Arrays;
import java.util.List;

public class Student_gtoumbas implements Student {
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
        double k_star = (T / (T + W)) * (1 + (W / (T + W))) * K;
        return k_star;
    }

    public int[] getApplications(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        List<Double> schools,
        List<Double> synergies) {
        School[] true_prefs = new School[schools.size()];
        for (int i = 0; i != synergies.size(); ++i) {
        true_prefs[i] = new School(i, schools.get(i) + synergies.get(i));
        }
        Arrays.sort(true_prefs);
        int[] ret = new int[10];

        // Case where N = 10, return top 10 schools
        if (N == 10) {
        for (int i = 0; i != 10; ++i) {
            ret[i] = true_prefs[i].index;
        }
        return ret;
        }

        // Computer K*
        double K_star = strategyScore(N, S, T, W, aptitude, schools, synergies);

        // Check if K* + 10 > N
        // If so, return last 10 schools
        if (K_star + 10 > N) {
        for (int i = 0; i != 10; ++i) {
            ret[i] = true_prefs[true_prefs.length - 1 - i].index;
        }
        return ret;
        }

        // Otherwise, return K - K* + 10 schools
        for (int i = 0; i != 10; ++i) {
        ret[i] = true_prefs[(int) K_star + i].index;
        }
        return ret;
    }
}