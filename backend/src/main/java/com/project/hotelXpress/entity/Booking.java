package com.project.hotelXpress.entity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name="bookings")

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotNull(message = "Checkin date is required!")
    private LocalDate checkInDate;

    @Future(message = "Checkout date must be in the future!")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Number of adults must not be less than 1")
    private int numOfAdults;

    @Min(value = 0, message = "Number of children must not be less than 0")
    private int numOfChildren;

    private int totalNumOfGuest;
    private String bookingConfirmationCode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public enum PaymentStatus {
        PENDING,
        PAID,
        FAILED
    }

    public void calculateTotalNumOfGuest() {
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumOfGuest();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumOfGuest();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuest=" + totalNumOfGuest +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
