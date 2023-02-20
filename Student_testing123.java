// Created by Viraj 
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
        double[] schoolsTruePref,
        List<Double> synergies) {

        double[] schoolScores = new double[N];
        for (int i = 0; i < N; i++) {
            double studsAbove = expectedNumStudentsAbove(N, S, T, W, aptitude, schoolsTruePref[i], synergies.get(i));
            double chances = (W / (T + W)) * (1/N) + (T / (T + W)) * (studsAbove/N);//studsAbove/N
            schoolScores[i] = (N-i)*(1-chances);
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
        if (N <= 10) {
            for (int i = 0; i != N; ++i) {
                ret[i] = truePrefs[i].index;
            }
            return ret;
        }
        double[] truePrefArray = new double[schools.size()];
        for (int i = 0; i != N; ++i) {
            truePrefArray[i] = truePrefs[i].quality;
        }
        // Computer K*
        double[] schoolScores = new double[schools.size()];
        schoolScores = strategyScores(N, S, T, W, aptitude, truePrefArray, synergies);
        for (int i = 0; i != N; ++i) {
            scorePrefs[i] = new School(truePrefs[i].index, schoolScores[i]);
        }
        Arrays.sort(scorePrefs);
        
        int[] top10 = new int[10];
        for (int i = 0; i != 10; ++i) {
            top10[i] = scorePrefs[i].index;
        }

        int ind = 0; 
        for (int i = 0; i != N; ++i) {
            if (top10[0]==truePrefs[i].index) {
                //ret[idx] = truePrefs[i].index;
                ind = i;
                break;
            }
        }

        if (ind + 10 > N) {
            for (int i = 0; i != 10; ++i) {
                ret[i] = truePrefs[truePrefs.length - 1 - i].index;
            }
        }
        else{
            for (int i = 0; i != 10; ++i) {
                ret[i] = truePrefs[ind + i].index;
            }
        }
        // int idx = 0;
        // for (int i = 0; i != N; ++i) {
        //     if (Arrays.asList(top10).contains(truePrefs[i].index)) {
        //         ret[idx] = truePrefs[i].index;
        //         idx+=1;
        //     }
        // }
        return ret;

        // Check if K* + 10 > N
        // If so, return last 10 schools
        

        // // Otherwise, return K - K* + 10 schools
       
    }
}
