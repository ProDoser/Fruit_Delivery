package com.fruitcrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DeliveryDay.
 */
@Entity
@Table(name = "delivery_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deliveryday")
public class DeliveryDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @Min(value = 1)
    @Max(value = 7)
    @Column(name = "weekday", nullable = false)
    private Integer weekday;
    
    @OneToMany(mappedBy = "deliveryDay")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Orders> orderss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeekday() {
        return weekday;
    }
    
    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Set<Orders> getOrderss() {
        return orderss;
    }

    public void setOrderss(Set<Orders> orderss) {
        this.orderss = orderss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliveryDay deliveryDay = (DeliveryDay) o;
        if(deliveryDay.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deliveryDay.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeliveryDay{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", weekday='" + weekday + "'" +
            '}';
    }
}
