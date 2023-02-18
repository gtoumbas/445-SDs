// Student_holist.java: sample implementation for Student
// COS 445 HW1, Spring 2018
// Created by Andrew Wonnacott

import java.util.Arrays;
import java.util.List;

public class Student_testing123 implements Student {
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

    public static double[] strategyScores(
        int N,
        double S,
        double T,
        double W,
        double aptitude,
        double[] schools,
        List<Double> synergies) {

        double[] schoolScores = new double[N];
        for (int i = 0; i < N; i++) {
            double studsAbove = expectedNumStudentsAbove(N, S, T, W, aptitude, schools[i], synergies.get(i));
            schoolScores[i] = (N-i)*(1-studsAbove/N);//(N-i)/((W / (T + W)) * (1) + (T / (T + W)) * studsAbove);
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

        // Case where N = 10, return top 10 schools
        if (N == 10) {
        for (int i = 0; i != 10; ++i) {
            ret[i] = truePrefs[i].index;
        }
        return ret;
        }
        double[] truePrefArray = new double[schools.size()];
        for (int i = 0; i != schools.size(); ++i) {
            truePrefArray[i] = truePrefs[i].quality;
        }
        // Computer K*
        double[] studScores = new double[schools.size()];
        studScores = strategyScores(N, S, T, W, aptitude, truePrefArray, synergies);
        for (int i = 0; i != synergies.size(); ++i) {
        scorePrefs[i] = new School(i, studScores[i]);
        }
        Arrays.sort(scorePrefs);
        int[] top10 = new int[10];
        for (int i = 0; i != 10; ++i) {
            top10[i] = scorePrefs[i].index;
        }
        
        int idx = 0;
        for (int i = 0; i != schools.size(); ++i) {
            if (Arrays.asList(top10).contains(truePrefs[i].index)) {
                ret[idx] = truePrefs[i].index;
                idx+=1;
            }
        }
        return top10;

        // // Check if K* + 10 > N
        // // If so, return last 10 schools
        // if (kStar + 10 > N) {
        // for (int i = 0; i != 10; ++i) {
        //     ret[i] = truePrefs[truePrefs.length - 1 - i].index;
        // }
        // return ret;
        // }

        // // Otherwise, return K - K* + 10 schools
        // for (int i = 0; i != 10; ++i) {
        // ret[i] = truePrefs[(int) kStar + i].index;
        // }
        // return ret;
    }
}
