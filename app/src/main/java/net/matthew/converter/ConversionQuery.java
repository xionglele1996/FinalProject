package net.matthew.converter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "conversion_query_table")
public class ConversionQuery implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sourceCurrency;
    private String destinationCurrency;
    private String amount;
    private String convertedAmount;

    public ConversionQuery(String sourceCurrency, String destinationCurrency, String amount, String convertedAmount) {
        this.sourceCurrency = sourceCurrency;
        this.destinationCurrency = destinationCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    public void setDestinationCurrency(String destinationCurrency) {
        this.destinationCurrency = destinationCurrency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(String convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

}
