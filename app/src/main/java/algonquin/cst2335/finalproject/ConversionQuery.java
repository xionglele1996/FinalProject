package algonquin.cst2335.finalproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conversion_query_table")
public class ConversionQuery {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sourceCurrency;
    private String destinationCurrency;
    private String amount;

    public ConversionQuery(String sourceCurrency, String destinationCurrency, String amount) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency){
        this.sourceCurrency = sourceCurrency;
    }

    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    public void setDestinationCurrency(String destinationCurrency){
        this.destinationCurrency = destinationCurrency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    // Constructor, getters and setters...
}
