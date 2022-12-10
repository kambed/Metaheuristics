package pl.meta.task4.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleSwarmAlgorithm {
    private final int numOfParticles;
    private final int numOfIterations;
    private final List<Particle> particles = new ArrayList<>();

    public ParticleSwarmAlgorithm(int numOfIterations, int numOfParticles, double maxX, double minX, double maxY, double minY,
                                  String function, double inertion, double cognition, double social) {
        this.numOfParticles = numOfParticles;
        this.numOfIterations = numOfIterations;
        double bestAdaptation = Double.MAX_VALUE;
        Particle bestParticle = null;
        for (int i = 0; i < numOfParticles; i++) {
            Particle p = new Particle(maxX, maxY, minX, minY, function, inertion, cognition, social);
            particles.add(p);
            if (p.getAdaptation() < bestAdaptation) {
                bestAdaptation = p.getAdaptation();
                bestParticle = p;
            }
        }
        for (Particle p : particles) {
            p.setBestXInSwarm(bestParticle.getX());
            p.setBestYInSwarm(bestParticle.getY());
        }
    }

    public Map<String,Double> start() {
        double globalBestAdaptation = Double.MAX_VALUE;
        double globalBestX = -1;
        double globalBestY = -1;
        for (int i = 0; i < numOfIterations; i++) {
            double bestAdaptation = Double.MAX_VALUE;
            Particle bestParticle = null;
            for (Particle p : particles) {
                p.move();
                if (p.getAdaptation() < bestAdaptation) {
                    bestAdaptation = p.getAdaptation();
                    bestParticle = p;
                }
                if (p.getAdaptation() < globalBestAdaptation) {
                    globalBestAdaptation = p.getAdaptation();
                    globalBestX = p.getX();
                    globalBestY = p.getY();
                }
            }
            for (Particle p : particles) {
                p.setBestXInSwarm(bestParticle.getX());
                p.setBestYInSwarm(bestParticle.getY());
            }
        }
        Map<String, Double> bestResults = new HashMap<>();
        bestResults.put("X", globalBestX);
        bestResults.put("Y", globalBestY);
        bestResults.put("Adaptation", globalBestAdaptation);
        return bestResults;
    }
}
