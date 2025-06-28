package com.bm3.accounting.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserResponseDTO {
    private Long idUser;
    private String username;
    private String email;
    private boolean isEnabled;
    private LocalDateTime createDate;
    private Set<String> roles;
}
