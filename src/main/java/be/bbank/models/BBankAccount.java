package be.bbank.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import be.bbank.BBankUtils;
import lombok.Getter;
import lombok.Setter;

@Entity
public class BBankAccount {
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "bank_account", length = 19, nullable = false)
    @Getter
    private String bankAccount;

    @Setter
    @ManyToMany
    @JoinTable(
        name = "temp_titular",
        joinColumns = @JoinColumn(name = "account_id"), // FK column for BBankAccount
        inverseJoinColumns = @JoinColumn(name = "titular_id") // FK column for BTitular
    )
    private List<BTitular> titulars;

    @Setter
    @ManyToMany
    @JoinTable(
        name = "temp_transaction",
        joinColumns = @JoinColumn(name = "transaction_id"), // FK column for BBankAccount
        inverseJoinColumns = @JoinColumn(name = "titular_id") // FK column for BTitular
    )
    private List<BTransaction> transactions;

    private double balance;

    @Column(name = "available_balance")
    private double availableBalance;

    @Getter @Setter
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private EnumAccountType accountType;

    public BBankAccount() {
        this.titulars = new java.util.ArrayList<>();
        this.transactions = new java.util.ArrayList<>();
    }

    public void setBankAccount(String bankAccount) throws Exception {
        if (bankAccount.matches("^BE\\d{2}\\s\\d{4}\\s\\d{4}\\s\\d{4}$")) {
            this.bankAccount = bankAccount;
        } else {
            throw new Exception("Vous avez du vous trompé dans l'écriture du numéro de compte, il est invalide!", null);
        }
    }

    public void setBalance(BTitular btitular, double balance) throws Exception {
        if(this.titulars == null) {
            throw new Exception("Il n'y a aucun titulaire pour ce compte, donc le compte est temporairement gelé!", null);
        } else {
            Date birthDate = btitular.getBirthDate();
            if(balance >= 0) {
                this.balance = balance;
            } else {
                if(this.accountType == EnumAccountType.EPARGNE) {
                    if(balance >= 0) {
                        this.balance = balance;
                    } else {
                        throw new Exception("Un compte épargne ne peut pas passer en négatif!", null);
                    }
                } else {
                    if(BBankUtils.calculateDifferenceInYears(birthDate, new Date()) >= 21) {
                        if(balance >= -1000) {
                            this.balance = balance;
                        } else {
                            throw new Exception("Un compte épargne ne peut pas passer en dessous de -1000€!", null);
                        }
                    } else {
                        if(balance >= 0) {
                            this.balance = balance;
                        } else {
                            throw new Exception("Un compte épargne ne peut pas passer en négatif temps que tu n'as pas 21 ans!", null);
                        }
                    }
                }
            }
        }
    }

    public double getAvaillableBalance(BTitular btitular) throws Exception {
        if(btitular == null) {
            throw new Exception("Il n'y a aucun titulaire pour ce compte, donc le compte est temporairement gelé!", null);
        } else {
            Date birthDate = btitular.getBirthDate();
            if(this.accountType == EnumAccountType.EPARGNE) {
                return balance;
            } else {
                if((BBankUtils.calculateDifferenceInYears(birthDate, new Date()) >= 21)) {
                    if(this.balance >= 0) {
                        return this.balance + 1000;
                    } else {
                        return -1000 - this.balance;
                    }
                } else {
                    return balance;
                }
            }
        }
    }

    public double removeMoney(BTitular btitular, double amount) throws Exception {
        if(this.accountType == EnumAccountType.COURANT) {
            if(amount <= getAvaillableBalance(btitular)) {
                this.balance -= amount;
                return amount;
            }
            else {
                throw new Exception("Vous n'avez pas assez d'argent dans votre compte!", null);
            }
        } else throw new Exception("Ceci n'est pas un compte courant! Aucun retrait ne peut donc être fait!", null);
    }

    public List<BTitular> getTitulaire() {
        return List.copyOf(titulars);
    }

    public List<BTransaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public void addTitular(BTitular titular) {
        this.titulars.add(titular);
    }

    public void addTransactions(BTransaction transaction) {
        this.transactions.add(transaction);
        if(this.transactions.size() >= 5) {
            this.transactions.remove(0);
        }
    }

    public void removeTitular(BTitular titular) {
        this.titulars.remove(titular);
    }

    public void sendToAccount(BTitular btitular, BBankAccount account, double amount) throws Exception {
        if(btitular == null) {
            throw new Exception("Il n'y a aucun titulaire pour ce compte, donc le compte est temporairement gelé!", null);
        } else {
            if(this.accountType == EnumAccountType.COURANT) {
                if(amount <= getAvaillableBalance(btitular)) {
                    this.balance -= amount;
                    Date currentDate = new Date();

                    BTransaction transaction = new BTransaction();
                    try {
                        transaction.addBankAccount(this);
                        transaction.addBankAccount(account);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    transaction.setDate(currentDate);
                    transaction.setAmount(amount);

                    this.addTransactions(transaction);
                    account.addTransactions(transaction);
                } else {
                    throw new Exception("Vous n'avez pas assez d'argent dans votre compte!", null);
                }
            } else throw new Exception("Ceci n'est pas un compte courant! Aucun retrait ne peut donc être fait!", null);
        }
    }
}
