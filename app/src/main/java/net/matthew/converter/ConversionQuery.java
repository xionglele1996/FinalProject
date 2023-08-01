package net.matthew.converter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "conversion_query_table")
public class ConversionQuery implements Serializable {

    /**
     * The ID of the query.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The source currency.
     */
    private String sourceCurrency;

    /**
     * The destination currency.
     */
    private String destinationCurrency;

    /**
     * The amount of the source currency.
     */
    private String amount;

    /**
     * The amount of the destination currency.
     */
    private String convertedAmount;

    public ConversionQuery(String sourceCurrency, String destinationCurrency, String amount, String convertedAmount) {
        this.sourceCurrency = sourceCurrency;
        this.destinationCurrency = destinationCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    /**
     * The ID of the query.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * The source currency.
     */
    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    /**
     * The destination currency.
     */
    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    /**
     * The destination currency.
     */
    public void setDestinationCurrency(String destinationCurrency) {
        this.destinationCurrency = destinationCurrency;
    }

    /**
     * The amount of the source currency.
     */
    public String getAmount() {
        /**
         * The amount of the source currency.
         */
        return amount;
    }

    /**
     * The amount of the source currency.
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * The amount of the destination currency.
     */
    public String getConvertedAmount() {
        return convertedAmount;
    }

    /**
     * The amount of the destination currency.
     */
    public void setConvertedAmount(String convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

}
