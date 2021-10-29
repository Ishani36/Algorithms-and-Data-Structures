package sample;

import java.io.*;
import java.util.*;


public class ShortestPath<T> {
    Map<T, T> mainMap;
    static Graph<String> graph;
    static String src_node;

    public Map<T, Integer> calc_shortestPath(Graph<T> graph, T src_node) {
        BinaryMinHeap<T> minheap = new BinaryMinHeap<>();
        Map<T, Integer> distanceMap = new HashMap<>();
        mainMap = new HashMap<>();

        for (T vertex : graph.getAllVertices()) {
            minheap.add(Integer.MAX_VALUE, vertex);
        }
        minheap.updateKey(src_node, 0);
        distanceMap.put(src_node, 0);
        mainMap.put(src_node, null);

        while (!minheap.isEmpty()) {
            BinaryMinHeap<T>.Node<T> minNode = minheap.extractMin();
            T currentVertex = minNode.item;

            distanceMap.put(currentVertex, minNode.key);

            for (Graph.Edge edge : graph.getEdges(currentVertex)) {
                T adjacentVertex = getVertexForEdge(currentVertex, edge);

                if (!minheap.contains(adjacentVertex)) {
                    continue;
                }

                int newDistance = distanceMap.get(currentVertex) + edge.getWeight();

                if (minheap.getWeight(adjacentVertex) > newDistance) {
                    minheap.updateKey(adjacentVertex, newDistance);
                    mainMap.put(adjacentVertex, currentVertex);
                }

            }
        }

        return distanceMap;
    }

    public static void main(String[] args) {
        make_graph();
        ShortestPath<String> obj = new ShortestPath<>();
        Map<String, Integer> distMap = obj.calc_shortestPath(graph, src_node);
        System.out.println("Source Node: " + src_node);
        for (Map.Entry t : distMap.entrySet()) {
            System.out.print("Vertex: " + t.getKey() + ", Distance: " + t.getValue());
            System.out.println();
            System.out.print(" Path: ");
            obj.printPath((String) t.getKey());
            System.out.println();
        }
    }

    public void printPath(String current) {
        if (current.equals(src_node)) {
            System.out.print(current);
            return;
        }
        printPath((String) mainMap.get(current));
        System.out.print(" - " + current);

    }
    private T getVertexForEdge(T v, Graph.Edge e) {
        return (T) (e.getv1().equals(v) ? e.getv2() : e.getv1());
    }

    private static Graph<String> make_graph() {
        final String Graph1_undirected = "./Graph1_undirected";
        final String Graph2_undirected = "./Graph2_undirected";
        final String Graph3_directed = "./Graph1_directed";
        final String Graph4_directed = "./Graph2_directed";

        File file = null;
        Scanner myObj = new Scanner(System.in);

        System.out.println("1. Graph 1(Undirected)");
        System.out.println("2. Graph 2(Undirected)");
        System.out.println("3. Graph 3(Directed)");
        System.out.println("4. Graph 4(Directed)");
        System.out.println("Please Enter the serial number to select the graph from the list:");
        int selectedGraph=myObj.nextInt();
        if(selectedGraph==1){
            file = new File(Graph1_undirected);
        }else if(selectedGraph==2){
            file = new File(Graph2_undirected);
        }else if(selectedGraph==3){
            file = new File(Graph3_directed);
        }else if(selectedGraph==4){
            file = new File(Graph4_directed);
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
                    src_node = ruleAttributes[0];
                } else {
                    src_node = ruleAttributes[0];
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
        return null;
    }

}
class Graph<T> {
    private HashMap<T, LinkedList<Edge<T>>> mMap = new HashMap<>();
    private Set<Edge<T>> edgeSet = new HashSet<>();
    boolean directed = false;

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public void addEdge(T vertex1, T vertex2, int weight) {
        Edge edge = new Edge(vertex1, vertex2, weight);

        if (mMap.containsKey(vertex1)) {
            mMap.get(vertex1).addLast(edge);
        } else {
            LinkedList<Edge<T>> edgeList = new LinkedList<>();
            edgeList.addLast(edge);
            mMap.put(vertex1, edgeList);
        }

        if (mMap.containsKey(vertex2)) {
            if (!this.directed) {
                mMap.get(vertex2).addLast(edge);
            }
        } else {
            LinkedList<Edge<T>> edgeList = new LinkedList<>();
            if (!this.directed)
                edgeList.addLast(edge);
            mMap.put(vertex2, edgeList);
        }

        if (!edgeSet.contains(edge)) {
            edgeSet.add(edge);
        }
    }

    public Set<Edge<T>> getedgeSet() {
        return edgeSet;
    }

    public Set<T> getAllVertices() {
        return mMap.keySet();
    }

    public LinkedList<Edge<T>> getEdges(T node) {
        return mMap.get(node);
    }

    public static class Edge<T> {
        T v1;
        T v2;
        int weight;

        public Edge(T v1, T v2, int weight) {
            this.v1 = v1;
            this.v2 = v2;
            this.weight = weight;
        }

        public T getv1() {
            return v1;
        }

        public void setv1(T v1) {
            this.v1 = v1;
        }

        public T getv2() {
            return v2;
        }

        public void setv2(T v2) {
            this.v2 = v2;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "v1=" + v1 +
                    ", v2=" + v2 +
                    ", weight=" + weight +
                    '}';
        }
    }
}
class BinaryMinHeap<T> {

    List<Node> nodeList;
    HashMap<T, Integer> position;

    public BinaryMinHeap() {
        nodeList = new ArrayList<>();
        position = new HashMap<>();

        Node dummy = new Node();
        nodeList.add(dummy);
    }

    public boolean contains(T item) {
        return position.containsKey(item);
    }

    public Integer getWeight(T key) {
        Integer itemPosition = this.position.get(key);
        if (itemPosition == null) {
            return null;
        } else {
            return nodeList.get(itemPosition).key;
        }
    }

    public void add(int key, T item) {
        Node node = new Node(key, item);
        position.put(item, nodeList.size());
        nodeList.add(node);
        upHeap(nodeList.size() - 1);
    }


    public Node<T> extractMin() {
        Node<T> minNode = new Node();
        minNode.key = nodeList.get(1).key;
        minNode.item = (T) nodeList.get(1).item;
        int lastNodeKey = nodeList.get(nodeList.size() - 1).key;
        T lastNodeItem = (T) nodeList.get(nodeList.size() - 1).item;

        nodeList.get(1).item = lastNodeItem;
        nodeList.get(1).key = lastNodeKey;
        nodeList.remove(nodeList.size() - 1);

        position.remove(minNode.item);
        if (!isEmpty())
            position.put((T) nodeList.get(1).item, 1);

        downHeap(1);

        return minNode;
    }

    public boolean isEmpty() {
        return (nodeList.size() == 1);
    }

    public void updateKey(T item, int key) {
        Integer itemPosition = position.get(item);
        if (itemPosition == null) {
            System.out.println("No such element found");
            return;
        }

        int nodeKey = nodeList.get(itemPosition).key;
        nodeList.get(itemPosition).key = key;
        if (key < nodeKey) {
            upHeap(itemPosition);
        } else if (key > nodeKey) {
            downHeap(itemPosition);
        }
    }

    private void upHeap(int current) {
        while (current > 1 && (nodeList.get(current / 2).key > nodeList.get(current).key)) {
            swap(nodeList.get(current / 2), nodeList.get(current));
            updatePosition((T) nodeList.get(current / 2).item, (T) nodeList.get(current).item, current / 2, current);
            current = current / 2;
        }
    }

    private void downHeap(int current) {
        int parent = current;

        while (parent < nodeList.size()) {
            int child1 = 2 * parent;
            int child2 = 2 * parent + 1;
            if (child2 < nodeList.size()) { //the node has two children
                if (nodeList.get(parent).key <= nodeList.get(child1).key && nodeList.get(parent).key <= nodeList.get(child2).key) {
                    break;
                } else {
                    if (nodeList.get(child1).key < nodeList.get(child2).key) {
                        swap(nodeList.get(parent), nodeList.get(child1));
                        updatePosition((T) nodeList.get(parent).item, (T) nodeList.get(child1).item, parent, child1);
                        parent = child1;
                    } else {
                        swap(nodeList.get(parent), nodeList.get(child2));
                        updatePosition((T) nodeList.get(parent).item, (T) nodeList.get(child2).item, parent, child2);
                        parent = child2;
                    }
                }
            } else {
                if (child1 < nodeList.size() && nodeList.get(child1).key < nodeList.get(parent).key) { //check if node has one child
                    swap(nodeList.get(parent), nodeList.get(child1));
                    updatePosition((T) nodeList.get(parent).item, (T) nodeList.get(child1).item, parent, child1);
                    parent = child1;
                }
                parent++;
            }
        }
    }

    private void swap(Node node1, Node node2) {
        int tempKey = node1.key;
        T tempItem = (T) node1.item;

        node1.key = node2.key;
        node1.item = node2.item;

        node2.key = tempKey;
        node2.item = tempItem;

    }

    private void updatePosition(T item1, T item2, int pos1, int pos2) {
        position.remove(item1);
        position.remove(item2);
        position.put(item1, pos1);
        position.put(item2, pos2);
    }

    public void printHeap() {
        for (Node n : nodeList) {
            System.out.println(n.key + " " + n.item);
        }
    }

    public class Node<T> {
        public int key;
        public T item;

        public Node() {
        }

        public Node(int key, T item) {
            this.key = key;
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return key == node.key &&
                    Objects.equals(item, node.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, item);
        }
    }
}





