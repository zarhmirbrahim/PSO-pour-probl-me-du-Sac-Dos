import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;

public class PSO_Sac_à_Dos {

    private static int NUM_PARTICLES;
    private static int NUM_ITERATIONS;
    private static double C1;
    private static double C2;
    private static double INERTIA_WEIGHT;
    private static double MAX_VELOCITY;

    private static int[] weights;
    private static int[] values;
    private static int MAX_WEIGHT;

    private static int[] bestGlobalPosition;
    private static int bestGlobalValue;

    public static String runAlgorithm(double c1, double c2, double inertiaWeight, int numIterations, int numParticles, int maxWeight, String filePath) {
        // Initialiser les variables statiques avec les paramètres fournis
        C1 = c1;
        C2 = c2;
        INERTIA_WEIGHT = inertiaWeight;
        NUM_ITERATIONS = numIterations;
        NUM_PARTICLES = numParticles;
        MAX_WEIGHT = maxWeight;

        // Lecture des poids et des valeurs à partir du fichier
        readFromFile(filePath);

        // Initialisation de bestGlobalPosition
        bestGlobalPosition = new int[weights.length];

        // Création des particules et initialisation
        Random random = new Random();
        Particle[] particles = new Particle[NUM_PARTICLES];
        for (int i = 0; i < NUM_PARTICLES; i++) {
            particles[i] = new Particle(random);

            if (particles[i].getValue() > bestGlobalValue) {
                bestGlobalPosition = particles[i].getPosition();
                bestGlobalValue = particles[i].getValue();
            }
        }

        // Création de l'objet chronoo pour mesurer le temps d'exécution
        chronoo chrono = new chronoo();
        chrono.start();

        // Boucle principale d'optimisation PSO
        for (int iteration = 0; iteration < NUM_ITERATIONS; iteration++) {
            for (Particle particle : particles) {
                updateVelocity(particle, random);
                updatePosition(particle);
                if (particle.getValue() > particle.getBestValue()) {
                    particle.setBestValue(particle.getValue());
                    particle.setBestPosition(particle.getPosition());
                }
                if (particle.getValue() > bestGlobalValue) {
                    bestGlobalValue = particle.getValue();
                    bestGlobalPosition = particle.getPosition();
                }
            }
        }

        // Arrêt du chronomètre
        chrono.stop();

        // Construction de la chaîne de résultat
        StringBuilder result = new StringBuilder();
        result.append("Meilleure solution trouvée :\n");
        for (int i = 0; i < weights.length; i++) {
            if (bestGlobalPosition[i] == 1) {
                result.append("Item ").append(i).append(" - Poids : ").append(weights[i]).append(", Valeur : ").append(values[i]).append("\n");
            }
        }
        result.append("Poids total : ").append(getTotalWeight(bestGlobalPosition)).append("\n");
        result.append("Valeur totale : ").append(bestGlobalValue).append("\n");
        result.append("Temps d'exécution : ").append(chrono.getMilliSec()).append(" ms\n");
        return result.toString();
    }

    private static void readFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            int numItems = 0;
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                numItems++;
            }

            weights = new int[numItems];
            values = new int[numItems];

            scanner = new Scanner(file);
            int index = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                weights[index] = Integer.parseInt(parts[0]);
                values[index] = Integer.parseInt(parts[1]);
                index++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé : " + e.getMessage());
        }
    }

    private static void updateVelocity(Particle particle, Random random) {
        int[] velocity = particle.getVelocity();
        int[] position = particle.getPosition();
        int[] bestPosition = particle.getBestPosition();

        for (int i = 0; i < position.length; i++) {
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();
            velocity[i] = (int) (INERTIA_WEIGHT * velocity[i]
                    + C1 * r1 * (bestPosition[i] - position[i])
                    + C2 * r2 * (bestGlobalPosition[i] - position[i]));
            if (velocity[i] > MAX_VELOCITY) {
                velocity[i] = (int) MAX_VELOCITY;
            } else if (velocity[i] < -MAX_VELOCITY) {
                velocity[i] = (int) -MAX_VELOCITY;
            }
        }
    }

    private static void updatePosition(Particle particle) {
        int[] position = particle.getPosition();
        int[] velocity = particle.getVelocity();

        for (int i = 0; i < position.length; i++) {
            position[i] = velocity[i] + position[i];
            if (position[i] < 0) {
                position[i] = 0;
            } else if (position[i] > 1) {
                position[i] = 1;
            }
        }
    }

    private static int getTotalWeight(int[] solution) {
        int totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalWeight += weights[i];
            }
        }
        return totalWeight;
    }

    private static class Particle {
        private int[] position;
        private int[] velocity;
        private int[] bestPosition;
        private int value;
        private int bestValue;

        public Particle(Random random) {
            position = new int[weights.length];
            velocity = new int[weights.length];
            bestPosition = new int[weights.length];

            // Generate initial positions
            int totalWeight = 0;
            for (int i = 0; i < position.length; i++) {
                if (random.nextDouble() < 0.5) {
                    if (totalWeight + weights[i] <= MAX_WEIGHT) {
                        position[i] = 1;
                        velocity[i] = 1;
                        totalWeight += weights[i];
                    } else {
                        position[i] = 0;
                        velocity[i] = -1;
                    }
                } else {
                    position[i] = 0;
                    velocity[i] = -1;
                }
            }

            value = evaluate(position);
            bestPosition = Arrays.copyOf(position, position.length);
            bestValue = value;
        }

        public int[] getPosition() {
            return position;
        }

        public int[] getVelocity() {
            return velocity;
        }

        public int getValue() {
            return value;
        }

        public int getBestValue() {
            return bestValue;
        }

        public int[] getBestPosition() {
            return bestPosition;
        }

        public void setBestPosition(int[] bestPosition) {
            System.arraycopy(bestPosition, 0, this.bestPosition, 0, weights.length);
        }

        public void setBestValue(int bestValue) {
            this.bestValue = bestValue;
        }

        private int evaluate(int[] solution) {
            int totalWeight = 0;
            int totalValue = 0;

            for (int i = 0; i < solution.length; i++) {
                if (solution[i] == 1) {
                    totalWeight += weights[i];
                    totalValue += values[i];
                }
            }

            if (totalWeight > MAX_WEIGHT) {
                return 0;
            } else {
                return totalValue;
            }
        }
    }

    public static void main(String[] args) {
        new PSIOTemplate_Sac_à_Dos();
    }

    static class chronoo {
        Calendar m_start = new GregorianCalendar();
        Calendar m_stop = new GregorianCalendar();

        public chronoo() {
        }

        //Lancer le chronometre
        public void start() {
            m_start.setTime(new Date());
        }

        //Arreter le chronometre
        public void stop() {
            m_stop.setTime(new Date());
        }

        //Retourner le nombre de millisecondes separant l'appel des methode start() et stop()
        public long getMilliSec() {
            return (m_stop.getTimeInMillis() - m_start.getTimeInMillis());
        }

        //Afficher le nombre de millisecondes separant l'appel des methode start() et stop() sur la sortie standard
        public void printMilliSec() {
            if (getMilliSec() <= 0) {
                System.out.println("Vous n'avez pas arrêté le chronomètre");
            } else {
                System.out.println("Temps d'exécution : " + getMilliSec() + " ms");
            }
        }
    }
}
