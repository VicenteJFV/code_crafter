package com.perfulandia.service.orden.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class orderModel {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(unique = true, nullable = false)
private String orderNumber;
@Column(nullable = false)
private Long userId;

@OneToMany(cascade = CascadeTy´pe.ALL, orphanRemoval = true)
@JoinColumn(name = "order_id")
private List<orderItemModel> items;

@Embedded
private orderAddressModel address;

@Enumerated(EnumType.STRING)
private paymentMethod paymentMethod;

@Enumerated(EnumType.STRING)
private paymentStatus paymentStatus = paymentStatus.PENDING;

@Enumerated(EnumType.STRING)
private orderStatus orderStatus = orderStatus.Created;

@Column(nullable=false)
private Double montoTotal;

@Column(nullable=false)
private  Double montoIVA;

@Column(nullable=false)
Private Double costoEnvio;

@Temporal(TemporalType.TIMESTAMP)
Private Date createdAt = new Date();

@Temporal(TemporalType.TIMESTAMP)
Private Date updatedAt = new Date();

// getters y setters



    /**
     * @param items
     */
    public orderModel(List<orderItemModel> items) {
        this.items = items;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
    this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return
     */
    public List<orderItemModel> getItems() {
        return items;
    }

    /**
     * @param items
     */
    public void setItems(List<orderItemModel> items) {
        this.items = items;
    }

    /**
     * @return
     */
    public orderAddressModel getAddress() {
        return address;
    }

    public void setAddress(orderAddressModel address) {
        this.address = address;
    }

    public paymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(paymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public paymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(paymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public orderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(orderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getMontoIVA() {
        return montoIVA;
    }

    public void setMontoIVA(Double montoIVA) {
        this.montoIVA = montoIVA;
    }

    public Private getDouble() {
        return Double;
    }

    public void setDouble(Private Double) {
        this.Double = Double;
    }

    public Private getDate() {
        return Date;
    }

    /**
     * @param Date
     */
    public void setDate(Private Date) {
        this.Date = Date;
    }


