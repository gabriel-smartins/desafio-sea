package br.com.sea.tecnologia.desafio.model;

import br.com.sea.tecnologia.desafio.model.enums.UsuarioRole;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UsuarioRole role;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            id = UUID.randomUUID();
        }
    }
}
