package com.example.supplementseshopmanagmentsystem;

public class NotEnoughQuantityInStockException extends RuntimeException{
    public NotEnoughQuantityInStockException() {
    }

    public NotEnoughQuantityInStockException(String message) {
        super(message);
    }
}
