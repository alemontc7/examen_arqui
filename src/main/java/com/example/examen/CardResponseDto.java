package com.example.examen;


public class CardResponseDto {
    private int statusCode;
    private String message;


    public CardResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    

    public int  getTimestamp()
    {
        return statusCode;
    }

    public String getMessage()
    {
        return message;
    }
    
}
