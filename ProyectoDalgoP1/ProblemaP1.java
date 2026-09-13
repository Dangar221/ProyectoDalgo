// Autores:
//   Gabriela Campos - 202410122
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
import java.util.Arrays;
import java.util.PriorityQueue;

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

    //clase auxiliar para representar un nodo en la cola de prioridad
    static class nodoDijkstra implements Comparable<nodoDijkstra> {
        int nodo;
        long distancia; //menor distancia conocida desde el nodo inicial

        public nodoDijkstra(int nodo, long distancia) {
            this.nodo = nodo;
            this.distancia = distancia;
        }

        // define como se comparan dos nodos en la PQ, compara las distancias para que la menor quede primero
        // priority queue la usa para ordenarlos
        @Override
        public int compareTo(nodoDijkstra otro) {
            return Long.compare(this.distancia, otro.distancia);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader lectorLineas = new BufferedReader(new InputStreamReader(System.in));
        int casosDePrueba = Integer.parseInt(lectorLineas.readLine().trim());

        ProblemaP1 solucion = new ProblemaP1();
        StringBuilder salida = new StringBuilder();

        for(int i = 0; i < casosDePrueba; i++){
            String respuesta = solucion.resolverCaso(lectorLineas);
            salida.append(respuesta).append("\n");
        }
        System.out.print(salida);
    }

    public String resolverCaso(BufferedReader lectorLineas) throws Exception {
        String[] primeraLinea = lectorLineas.readLine().split(" ");
        int n = Integer.parseInt(primeraLinea[0]); // numeroOrbitas
        int m = Integer.parseInt(primeraLinea[1]); // numeroPosiciones
        int p = Integer.parseInt(primeraLinea[2]); // numeroPortales

        int[] energias = leerEnergiasOrbitas(lectorLineas, n);
        int[][] portales = leerPortales(lectorLineas, p);
        List<List<Arista>> grafo = construirGrafo(n, m, energias, portales);

        int totalNodos = n*m;
        int nodoInicio = calcularNumeroNodo(1, 1, m);
        int nodoDestino = calcularNumeroNodo(n, m, m);

        long resultado = dijkstra(grafo, nodoInicio, nodoDestino, totalNodos);

        if(resultado == Long.MAX_VALUE){
            return "NO EXISTE";
        } else{
            return String.valueOf(resultado);
        }

    }

    public int[] leerEnergiasOrbitas(BufferedReader lectorLineas, int numeroOrbitas) throws Exception {
        String[] tokens = lectorLineas.readLine().split(" ");
        int[] energias = new int[numeroOrbitas];
        for(int i = 0; i < numeroOrbitas; i++){
            energias[i] = Integer.parseInt(tokens[i]);
        }
        return energias;
    }

    public int[][] leerPortales(BufferedReader lectorLineas, int numeroPortales) throws Exception {
        int[][] portales = new int[numeroPortales][4];
        for(int i = 0; i < numeroPortales; i++){
            String[] tokens = lectorLineas.readLine().split(" ");
            portales[i][0] = Integer.parseInt(tokens[0]); // xs
            portales[i][1] = Integer.parseInt(tokens[1]); // ys
            portales[i][2] = Integer.parseInt(tokens[2]); // xe
            portales[i][3] = Integer.parseInt(tokens[3]); // ye
        }
        return
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
        long[] distancias = new long[totalNodos]; // guarda el menor costo conocido desde el nodo inicial a cada nodo
        Arrays.fill(distancias, Long.MAX_VALUE); //todas las distancias como inifinito porque no sabemos el camino aun
        boolean[] visitado = new boolean[totalNodos]; 
        PriorityQueue<nodoDijkstra> minheap = new PriorityQueue<>(); //cola de prioridad para escoger el nodo con menor distancia conocida
        distancias[nodoInicio] = 0;
        minheap.add(new nodoDijkstra(nodoInicio, 0));

        while(!minheap.isEmpty() && visitado[nodoDestino] == false) { //recorrer mientras la cola no este vacia y no hayamos visitado el destino
            nodoDijkstra actual = minheap.poll(); //extraer el nodo con menor distancia conocida
            int nodoActual = actual.nodo;
            if (!visitado[nodoActual]) { //procesa el nodo solo si no ha sido visitado
                visitado[nodoActual] = true; 
            for (Arista arista : grafo.get(nodoActual)) {
                int vecino = arista.destino;
                long nuevaDistancia = distancias[nodoActual] + arista.peso; //costo de llegar al vecino pasando por el nodo actual

                if (nuevaDistancia < distancias[vecino]) { //si encontramos un camino mas corto al vecino
                    distancias[vecino] = nuevaDistancia; 
                    minheap.add(new nodoDijkstra(vecino, nuevaDistancia)); //agregar otra vez al vecino con su distancia nueva
                                                                        //al usar minheap siempre se procesa el nodo con menor distancia primero
                }

            }   
        }
    }
        return distancias[nodoDestino]; //la menor distancia conocida al  destino
    }
}

