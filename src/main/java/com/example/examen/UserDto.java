package com.example.examen;

public class UserDto {
   private String email;
   private String password;
public UserDto(String email, String password) {
    this.email = email;
    this.password = password;
}

public String getEmail() {
    return email;
}
public String getPassword() {
    return password;
}

}