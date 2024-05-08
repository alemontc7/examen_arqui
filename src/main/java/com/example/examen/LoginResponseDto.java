package com.example.examen;


public class LoginResponseDto {
    public long timestamp;
    public String token;
    private String errorMessage;


    public LoginResponseDto(long timestamp, String token) {
        this.timestamp = timestamp;
        this.token = token;
        this.errorMessage = null;
    }

    public LoginResponseDto(long timestamp, String token, String errorMessage) {
        this.timestamp = timestamp; 
        this.token = token;
        this.errorMessage = errorMessage;
    }
    

    public long  getTimestamp()
    {
        return timestamp;
    }

    public String token()
    {
        return token;
    }
    
}
