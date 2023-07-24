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
import javax.persistence.ManyToOne;

import be.bbank.BBankUtils;
import lombok.Getter;
import lombok.Setter;

@Entity
public class BBankAccount {
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bank_account", length = 19, nullable = false)
    @Getter
    private String bankAccount;

    @ManyToOne
    @Getter @Setter
    private BTitular titular;

    @Setter
    private List<BTitular> titulars;

    @Setter
    @Column(name = "transactions", nullable = false)
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

    public void setBalance(double balance) throws Exception {
        if(this.titular == null) {
            throw new Exception("Il n'y a aucun titulaire pour ce compte, donc le compte est temporairement gelé!", null);
        } else {
            Date birthDate = this.titular.getBirthDate();
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

    public double getAvaillableBalance() throws Exception {
        if(this.titular == null) {
            throw new Exception("Il n'y a aucun titulaire pour ce compte, donc le compte est temporairement gelé!", null);
        } else {
            Date birthDate = this.titular.getBirthDate();
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

    public double removeMoney(double amount) throws Exception {
        if(this.accountType == EnumAccountType.COURANT) {
            if(amount <= getAvaillableBalance()) {
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

    public void sendToAccount(BBankAccount account, double amount) throws Exception {
        if(this.titular == null) {
            throw new Exception("Il n'y a aucun titulaire pour ce compte, donc le compte est temporairement gelé!", null);
        } else {
            if(this.accountType == EnumAccountType.COURANT) {
                if(amount <= getAvaillableBalance()) {
                    this.balance -= amount;
                    Date currentDate = new Date();
                    account.addTransactions(new BTransaction(this, account, amount, currentDate));
                    this.addTransactions(new BTransaction(account, this, amount, currentDate));
                } else {
                    throw new Exception("Vous n'avez pas assez d'argent dans votre compte!", null);
                }
            } else throw new Exception("Ceci n'est pas un compte courant! Aucun retrait ne peut donc être fait!", null);
        }
    }
}
