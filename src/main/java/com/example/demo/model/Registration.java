package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id"})
})
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDetail user;  

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


    @Column(name = "amount",  nullable = false)
    private int paymentAmount;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "razorpay_id", nullable = false)
    private String razorPayId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDetail getUser() {
		return user;
	}

	public void setUser(UserDetail user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(int paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getRazorPayId() {
		return razorPayId;
	}

	public void setRazorPayId(String razorPayId) {
		this.razorPayId = razorPayId;
	}

	@Override
	public String toString() {
		return "Registration [id=" + id + ", user=" + user + ", event=" + event + ", paymentAmount=" + paymentAmount
				+ ", paymentStatus=" + paymentStatus + ", razorPayId=" + razorPayId + "]";
	}

	
}
