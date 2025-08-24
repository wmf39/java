package simulation;

import java.util.Random;

public class GeneSimulation {
    public static void main(String[] args) {
        int numGenes = 10000;
        int numSimulations = 100;
        int maxChildren = 30;

        System.out.println("Kinder\tAnteil weitergegebener Gene (beide Allele)");

        for (int nChildren = 1; nChildren <= maxChildren; nChildren++) {
            double totalFraction = 0.0;

            for (int sim = 0; sim < numSimulations; sim++) {
                boolean[] bothAllelesPassed = new boolean[numGenes];
                Random rand = new Random();

                for (int gene = 0; gene < numGenes; gene++) {
                    boolean has0 = false;
                    boolean has1 = false;

                    for (int child = 0; child < nChildren; child++) {
                        int allele = rand.nextInt(2);  // 0 oder 1
                        if (allele == 0) has0 = true;
                        else has1 = true;
                        if (has0 && has1) break;  // beide schon vorhanden
                    }

                    bothAllelesPassed[gene] = has0 && has1;
                }

                // Zähle Gene, bei denen beide Allele weitergegeben wurden
                int countBothPassed = 0;
                for (boolean b : bothAllelesPassed) {
                    if (b) countBothPassed++;
                }

                double fraction = (double) countBothPassed / numGenes;
                totalFraction += fraction;
            }

            double averageFraction = totalFraction / numSimulations;
            //System.out.printf("%2d\t%.10f%n", nChildren, averageFraction);
            System.out.printf("%.10f%n", averageFraction);
        }
    }
}
