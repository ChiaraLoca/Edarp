package model;

import java.util.Objects;

public class Node {
    private int id;
    private double lat;
    private double lon;
    private double serviceTime;
    private double load;
    private double arrival;
    private double departure;
    private NodeType nodeType = NodeType.NONE;

    public Node(int id, double lat, double lon, double serviceTime, double load, double arrival, double departure) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.serviceTime = serviceTime;
        this.load = load;
        this.arrival = arrival;
        this.departure = departure;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", arrival=" + arrival +
                ", departure=" + departure +
                '}';
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id &&
                Double.compare(node.lat, lat) == 0 &&
                Double.compare(node.lon, lon) == 0 &&
                Double.compare(node.serviceTime, serviceTime) == 0 &&
                Double.compare(node.load, load) == 0 &&
                Double.compare(node.arrival, arrival) == 0 &&
                Double.compare(node.departure, departure) == 0 &&
                nodeType == node.nodeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon, serviceTime, load, arrival, departure, nodeType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }

    public double getArrival() {
        return arrival;
    }

    public void setArrival(double arrival) {
        this.arrival = arrival;
    }

    public double getDeparture() {
        return departure;
    }

    public void setDeparture(double departure) {
        this.departure = departure;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }
}
