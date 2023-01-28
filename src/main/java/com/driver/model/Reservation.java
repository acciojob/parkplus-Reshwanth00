package com.driver.model;

import javax.persistence.*;

@Entity
@Table
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int numberOfHours;
    @ManyToOne
    @JoinColumn
    private User user;
    @ManyToOne
    @JoinColumn
    private Spot spot;
    @OneToMany(mappedBy = "reservation",cascade = CascadeType.ALL)
    private Payment payment;

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public Payment getPayment() {
        return payment;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
    public Reservation(){}
    public Reservation(int numberOfHours){
        setNumberOfHours(numberOfHours);
    }
}
