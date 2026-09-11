// Autores:
//   Gabriela Campos - 
//   Danna Garcia - 202320823
//   Jose Manuel - 202422442
//
// ISIS2112 - Diseño de Algoritmos
// Semestre 2026-20
// Proyecto Parte 1 - Problema P1

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProblemaP1 {

    // Clase auxiliar para representar una arista del grafo
    static class Arista {
        int destino;
        int peso;

        public Arista(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    public static void main(String[] args) throws Exception {
        // TODO
    }

    public void resolverCaso(BufferedReader lectorLineas) throws Exception {
        // TODO
    }

    public int[] leerEnergiasOrbitas(BufferedReader lectorLineas, int numeroOrbitas) throws Exception {
        // TODO
        return null;
    }

    public int[][] leerPortales(BufferedReader lectorLineas, int numeroPortales) throws Exception {
        // TODO
        return null;
    }

    public List<List<Arista>> construirGrafo(int numeroOrbitas, int numeroPosiciones, int[] energiaOrbita, int[][] portales) {
        int totalNodos = numeroOrbitas * numeroPosiciones;
        List<List<Arista>> grafo = new ArrayList<>(totalNodos);
        for (int i = 0; i < totalNodos; i++) {
            grafo.add(new ArrayList<>());
        }

        for (int orbita = 1; orbita <= numeroOrbitas; orbita++) {
            int energia = energiaOrbita[orbita - 1];
            for (int posicion = 1; posicion < numeroPosiciones; posicion++) {
                int nodoA = calcularNumeroNodo(orbita, posicion, numeroPosiciones);
                int nodoB = calcularNumeroNodo(orbita, posicion + 1, numeroPosiciones);
                grafo.get(nodoA).add(new Arista(nodoB, energia));
                grafo.get(nodoB).add(new Arista(nodoA, energia));
            }
        }

        for (int[] portal : portales) {
            int xs = portal[0], ys = portal[1], xe = portal[2], ye = portal[3];
            int nodoOrigen = calcularNumeroNodo(xs, ys, numeroPosiciones);
            int nodoDestino = calcularNumeroNodo(xe, ye, numeroPosiciones);
            grafo.get(nodoOrigen).add(new Arista(nodoDestino, 0));
        }

        return grafo;
    }

    public int calcularNumeroNodo(int orbita, int posicion, int numeroPosiciones) {
        return (orbita - 1) * numeroPosiciones + (posicion - 1);
    }

    public long dijkstra(List<List<Arista>> grafo, int nodoInicio, int nodoDestino, int totalNodos) {
        // TODO
        return 0;
    }
}
