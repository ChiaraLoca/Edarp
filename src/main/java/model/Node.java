package model;

public class Node {
    private int id;
    private double lat;
    private double lon;
    private int service;
    private int time;
    private int load;
    private int arrival;
    //TODO chiedere per mancanza dell'ottavo dato
    private int departure;

    public Node(int id, double lat, double lon, int service, int time, int load, int arrival, int departure) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.service = service;
        this.time = time;
        this.load = load;
        this.arrival = arrival;
        this.departure = departure;
    }
}
