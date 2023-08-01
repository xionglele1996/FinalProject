package net.lele.flighttracker;

import java.io.Serializable;

public class Flight implements Serializable {
    /**
     * The flight number.
     */
    private String flightNumber;
    private String status;
    private String gate;

    /**
     * The delay in minutes.
     */
    private int delay;

    /**
     * The name of the airline.
     */
    private String airlineName;

    public Flight(String flightNumber, String status, String gate, int delay, String airlineName) {
        this.flightNumber = flightNumber;
        this.status = status;
        this.gate = gate;
        this.delay = delay;
        this.airlineName = airlineName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getGate() {
        return gate;
    }

    public int getDelay() {
        return delay;
    }

    public String getAirlineName() {
        return airlineName;
    }
}

