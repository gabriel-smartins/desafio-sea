package br.com.sea.tecnologia.desafio.model;

import br.com.sea.tecnologia.desafio.model.enums.TipoTelefone;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "telefones")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Telefone {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTelefone tipo;

    @Column(nullable = false, length = 11)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
