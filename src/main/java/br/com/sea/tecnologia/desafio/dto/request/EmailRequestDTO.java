package br.com.sea.tecnologia.desafio.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailRequestDTO {
    @NotBlank
    @Email
    private String email;
}