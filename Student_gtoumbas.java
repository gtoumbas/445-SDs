// Created By Viraj Nadkarni, Harpreet Kaur, and George Toumbas
// Uses code from Student_holist.java
//  Student_holist.java: sample implementation for Student
//  COS 445 HW1, Spring 2018
//  Created by Andrew Wonnacott

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

    // Number of students that a university is expected to prefer
    public static double getExpNumStudentsAbove(
        int N,
        double S,
        double W,
        double aptitude,
        double school_quality,
        double synergy) {

        double r_u = (aptitude + synergy) / (S + W);
        double K = ((1 - r_u) * N);
        return K;
    }

    // Average number of expected preferred students across all universities
    public static double getAvgNumStudentsAbove(
        int N,
        double S,
        double W,
        double aptitude,
        List<Double> schools,
        List<Double> synergies) {

        double total = 0;
        for (int i = 0; i < schools.size(); i++) {
            total += getExpNumStudentsAbove(N, S, W, aptitude, schools.get(i), synergies.get(i));
        }
        return total / schools.size();
    }

    // Gets starting index for 10 schools to apply to
    public static double getStartingIndex(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        List<Double> schools,
        List<Double> synergies) {

        double avgK = getAvgNumStudentsAbove(N, S, W, aptitude, schools, synergies);
        double kStar = (W / (T + W)) * (1) + (T / (T + W)) * avgK;
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
        int[] ret = new int[10];
        double howAggressive = 0.20; // 20% lower starting index if possible
        for (int i = 0; i != synergies.size(); ++i) {
            truePrefs[i] = new School(i, schools.get(i) + synergies.get(i));
        }
        Arrays.sort(truePrefs);

        // Case where N = 10, return top 10 schools
        if (N == 10) {
            for (int i = 0; i != 10; ++i) {
                ret[i] = truePrefs[i].index;
            }
            return ret;
        }

        int startingIndex = (int) getStartingIndex(N, S, T, W, aptitude, schools, synergies);
        int aggresiveIndex = (int) (startingIndex - (N * howAggressive));
        if (aggresiveIndex > 0) {
            startingIndex = aggresiveIndex;
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
