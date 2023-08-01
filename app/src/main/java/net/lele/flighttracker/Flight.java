package net.lele.flighttracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Flight implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String flightNumber;
    private String destinationAirport;
    private String terminal;
    private String gate;
    private int delay;



    public Flight(String destinationAirport, String terminal, String gate, int delay, String flightNumber) {
        this.flightNumber = flightNumber;
        this.destinationAirport = destinationAirport;
        this.terminal = terminal;
        this.gate = gate;
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getGate() {
        return gate;
    }

    public int getDelay() {
        return delay;
    }
}
