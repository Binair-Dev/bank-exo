package be.bbank.models;

import java.util.ArrayList;
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
public class BTitular {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", length = 50,nullable = false)
    @Getter @Setter
    private String firstName;

    @Column(name = "last_name", length = 50,nullable = false)
    @Getter @Setter
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date birthDate;

    @Getter @Setter
    @ManyToMany(mappedBy = "titulars")
    private List<BBankAccount> accounts;

    public BTitular() {
        this.accounts = new ArrayList<>();
    }
}
