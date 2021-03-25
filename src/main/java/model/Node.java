package model;

public class Node {
    private int id;
    private double lat;
    private double lon;
    private int serviceTime;
    private int load;
    private int arrival;
    private int departure;

    public Node(int id, double lat, double lon, int serviceTime, int load, int arrival, int departure) {
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

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getLoad() {
        return load;
    }

    public int getArrival() {
        return arrival;
    }

    public int getDeparture() {
        return departure;
    }
}
