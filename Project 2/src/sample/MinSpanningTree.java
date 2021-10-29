package sample;

import java.io.*;
import java.util.*;

public class MinSpanningTree<T> {

    static Graph<String> graph;
    public List<Graph.Edge<T>> getMinimumSpanningTree(Graph<T> g) {
        List<Graph.Edge<T>> mst = new ArrayList<>();
        List<Graph.Edge<T>> allEdges = new ArrayList<>(g.getedgeSet());
        Disjointset<T> djSet = new Disjointset<>();

        EdgeComparator comparator = new EdgeComparator();
        Collections.sort(allEdges, comparator);

        for (T edge : g.getAllVertices()) {
            djSet.createSet((T) edge);
        }

        for (Graph.Edge<T> edge : allEdges) {
            T set1 = djSet.findSet(edge.getv1());
            T set2 = djSet.findSet(edge.getv2());

            if (set1 == set2) {
                continue;
            } else {
                mst.add(edge);
                djSet.union(set1, set2);
            }
        }
        return mst;
    }
    public class EdgeComparator implements Comparator<Graph.Edge<T>> {

        @Override
        public int compare(Graph.Edge<T> e1, Graph.Edge<T> e2) {
            if (e1.getWeight() <= e2.getWeight()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static void main(String[] args) {
        make_graph();
        MinSpanningTree<String> minST = new MinSpanningTree<>();
        List<Graph.Edge<String>> mst = minST.getMinimumSpanningTree(graph);
        int totalCost = 0;
        System.out.println("Edges of Tree:");
        for (Graph.Edge<String> edge : mst) {
            System.out.println(edge.getv1() + " -- " + edge.getv2());
            totalCost += edge.getWeight();
        }
        System.out.println("Total Cost of MST: " + totalCost);
    }

    private static void make_graph() {
        final String Graph1_undirected = "./Graph1_undirected";
        final String Graph2_undirected = "./Graph2_undirected";
        final String Graph3_undirected = "./Graph3_undirected";
        final String Graph4_undirected = "./Graph4_undirected";

        File file = null;
        Scanner myObj = new Scanner(System.in);

        System.out.println("1. Graph 1(Undirected)");
        System.out.println("2. Graph 2(Undirected)");
        System.out.println("3. Graph 3(Undirected)");
        System.out.println("4. Graph 4(Undirected)");
        System.out.println("Please Enter the serial number to select the graph from the list:");
        int selectedGraph=myObj.nextInt();
        if(selectedGraph==1){
            file = new File(Graph1_undirected);
        }else if(selectedGraph==2){
            file = new File(Graph2_undirected);
        }else if(selectedGraph==3){
            file = new File(Graph3_undirected);
        }else if(selectedGraph==4){
            file = new File(Graph4_undirected);
        }else{
            System.out.println("Please Enter a valid number from the List");
        }

        BufferedReader br = null;
        String line = "";
        try {
            int count = 0;
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] ruleAttributes = line.split(" ");
                if (count == 0) {
                    boolean directed = ruleAttributes[2].equalsIgnoreCase("D");
                    graph = new Graph<>(directed);
                } else if (ruleAttributes.length == 1) {
                } else {
                    graph.addEdge(ruleAttributes[0], ruleAttributes[1], Integer.parseInt(ruleAttributes[2]));
                }
                count++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

class Disjointset<T> {
    private Map<T, Node> setMap = new HashMap<>();
    class Node<T> {
        Node main;
        int pos;
        T data;

    }
    public T findSet(T data) {
        return (T) findSet(setMap.get(data)).data;
    }

    private Node<T> findSet(Node<T> node) {
        Node parent = node.main;
        if (parent == node) {
            return node;
        }
        node.main = findSet(parent);
        return node.main;
    }

    public void createSet(T data) {
        Node<T> node = new Node<>();
        node.data = data;
        node.pos = 0;
        node.main = node;
        setMap.put(data, node);
    }

    public void union(T data1, T data2) {
        Node parent1 = findSet(setMap.get(data1));
        Node parent2 = findSet(setMap.get(data2));

        if (parent1 == parent2) {
            return;
        }

        if (parent1.pos >= parent2.pos) {
            parent1.pos = (parent1.pos == parent2.pos) ? parent1.pos + 1 : parent1.pos;
            parent2.main = parent1;
        } else {
            parent1.main = parent2;
        }
    }



    public static void main(String[] args) {
    }
}
