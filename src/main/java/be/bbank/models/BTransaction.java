package be.bbank.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
public class BTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "from_account")
    @Getter
    private BBankAccount from;
    
    @ManyToOne
    @JoinColumn(name = "to_account")
    @Getter
    private BBankAccount to;

    @Getter @Setter
    private double amount;

    @Getter @Setter
    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date", nullable = false)
    private Date date;

    public BTransaction(BBankAccount from, BBankAccount to, double amount, Date date) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
    }
}
