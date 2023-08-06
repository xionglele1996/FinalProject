package net.lele.flighttracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
/**
 * Flight entity representing the flight information.
 * This class holds details about the flight, including
 * flight number, destination airport, terminal, gate, and delay information.
 */
@Entity
public class Flight implements Serializable {
    /**
     * The unique identifier for this flight.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    /**
     * The flight number of this flight.
     */
    private String flightNumber;
    /**
     * The destination airport for this flight.
     */
    private String destinationAirport;
    /**
     * The terminal from which this flight will depart.
     */
    private String terminal;
    /**
     * The gate from which this flight will depart.
     */
    private String gate;
    /**
     * The delay time for this flight in minutes.
     */
    private int delay;

    /**
     * Constructs a new Flight with the given details.
     *
     * @param destinationAirport The destination airport for this flight.
     * @param terminal           The terminal for this flight.
     * @param gate               The gate for this flight.
     * @param delay              The delay for this flight in minutes.
     * @param flightNumber       The flight number for this flight.
     */
    public Flight(String destinationAirport, String terminal, String gate, int delay, String flightNumber) {
        this.flightNumber = flightNumber;
        this.destinationAirport = destinationAirport;
        this.terminal = terminal;
        this.gate = gate;
        this.delay = delay;
    }

    /**
     * @return The unique identifier for this flight.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this flight.
     *
     * @param id The unique identifier for this flight.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The flight number for this flight.
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * @return The destination airport for this flight.
     */
    public String getDestinationAirport() {
        return destinationAirport;
    }

    /**
     * @return The terminal from which this flight will depart.
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * @return The gate from which this flight will depart.
     */
    public String getGate() {
        return gate;
    }

    /**
     * @return The delay time for this flight in minutes.
     */
    public int getDelay() {
        return delay;
    }
}
