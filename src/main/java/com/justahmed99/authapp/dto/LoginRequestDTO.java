package com.justahmed99.authapp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRequestDTO {
  private String username;
  private String email;
  private String password;
}
