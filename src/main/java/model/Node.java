package model;

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
        return "{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", serviceTime=" + serviceTime +
                ", load=" + load +
                ", arrival=" + arrival +
                ", departure=" + departure +
                '}';
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
