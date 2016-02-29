package com.fruitcrm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orders")
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "details")
    private String details;
    
    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    @NotNull
    @Column(name = "first_delivery", nullable = false)
    private LocalDate firstDelivery;
    
    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "fruit_pack_id")
    private FruitPack fruitPack;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private Week week;

    @ManyToOne
    @JoinColumn(name = "delivery_day_id")
    private DeliveryDay deliveryDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getFirstDelivery() {
        return firstDelivery;
    }
    
    public void setFirstDelivery(LocalDate firstDelivery) {
        this.firstDelivery = firstDelivery;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public FruitPack getFruitPack() {
        return fruitPack;
    }

    public void setFruitPack(FruitPack fruitPack) {
        this.fruitPack = fruitPack;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public DeliveryDay getDeliveryDay() {
        return deliveryDay;
    }

    public void setDeliveryDay(DeliveryDay deliveryDay) {
        this.deliveryDay = deliveryDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        if(orders.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id=" + id +
            ", details='" + details + "'" +
            ", orderDate='" + orderDate + "'" +
            ", firstDelivery='" + firstDelivery + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
