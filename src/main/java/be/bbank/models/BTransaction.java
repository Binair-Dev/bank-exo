package be.bbank.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
public class BTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter @Setter
    private double amount;

    @ManyToMany(mappedBy = "transactions")
    private List<BBankAccount> bankAccounts;

    @Getter @Setter
    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date", nullable = false)
    private Date date;

    
    public BTransaction() {}

    public void addBankAccount(BBankAccount account) throws Exception {
        if(account != null)
            this.bankAccounts.add(account);
        else throw new Exception("Le compte spécifié n'existe pas ou est null!", null);
    }
}
