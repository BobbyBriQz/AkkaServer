package akkaserver.transactions.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transaction {
    @JsonProperty("id")
    private int id;
    @JsonProperty("address")
    private String address;
    @JsonProperty("amount")
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(int id, String address, BigDecimal amount) {
        this.id = id;
        this.address = address;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
