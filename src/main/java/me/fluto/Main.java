package me.fluto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Neuron neuron = new Neuron();
    for (int x = 0; x < 3; x++) {
      int actual = linearFunc(x);
      int delta = neuron.calcDelta(x, actual);
      if (delta == 0) {
        // no need to weak for this value
        continue;
      }

      int bestFeat = -1;
      int bestDirection = -1;
      int bestDelta = Integer.MAX_VALUE;

      for (int feat = 0; feat < neuron.numFeatures(); feat++) {
        int originalValue = neuron.getFeature(feat);
        neuron.setFeature(feat, originalValue + 1);
        int posDelta = neuron.calcDelta(x, actual);
        neuron.setFeature(feat, originalValue);

        if (posDelta < bestDelta) {
          bestDirection = 1;
          bestDelta = posDelta;
          bestFeat = feat;
        }

        originalValue = neuron.getFeature(feat);
        neuron.setFeature(feat, originalValue - 1);
        int negDelta = neuron.calcDelta(x, actual);
        neuron.setFeature(feat, originalValue);

        if (negDelta < bestDelta) {
          bestDirection = -1;
          bestDelta = negDelta;
          bestFeat = feat;
        }
      }

      System.out.println("Best Feature Tweak: " + bestFeat);
      System.out.println("Best Direction: " + bestDirection);
      System.out.println("Best Delta: " + bestDelta);

      neuron.setFeature(bestFeat, neuron.getFeature(bestFeat) + 1);

      // try to iterate until you get to the least delta possible
      System.out.println(neuron);
      System.out.println("-----------------\n");
      while (true) {
        int originalFeatVal = neuron.getFeature(bestFeat);
        neuron.setFeature(bestFeat, originalFeatVal + bestDirection);
        int newDelta = neuron.calcDelta(x, actual);
        if (!(newDelta < bestDelta)) {
          neuron.setFeature(bestFeat, originalFeatVal); // revert
          break;
        }
        bestDelta = newDelta;
      }
      System.out.println(neuron);
    }
  }

  private static int deviation(int expected, int got) {
    return Math.abs(expected - got);
  }

  private static int linearFunc(int x) {
    return 3 + 2 * x;
  }

  static class Neuron {
    static int weight = 0;
    static int bias = 0;

    int pass(int x) {
      return x * weight + bias;
    }

    int calcDelta(int x, int expected) {
      return deviation(expected, pass(x));
    }


    int getFeature(int x) {
      if (x == 0) {
        return weight;
      }
      return bias;
    }

    void setFeature(int x, int value) {
      if (x == 0) {
        weight = value;
      } else {
        bias = value;
      }
    }

    int numFeatures() {
      return 2;
    }

    @Override
    public String toString() {
      return "(" + weight + "x" + " + " + bias + ")";
    }
  }

}