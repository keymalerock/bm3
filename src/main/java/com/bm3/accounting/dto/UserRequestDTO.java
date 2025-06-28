package com.bm3.accounting.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    private String username;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "El formato del correo electrónico no es válido.")
    @Size(max = 60)
    private String email;

    // La contraseña es opcional en la actualización, por eso no lleva @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String key;

    private boolean isEnabled;
}