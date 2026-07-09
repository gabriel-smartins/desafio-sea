package br.com.sea.tecnologia.desafio.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Cliente {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Embedded
    private Endereco endereco;

    @Builder.Default
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Telefone> telefones = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Email> emails = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public void addTelefone(Telefone telefone) {
        telefones.add(telefone);
        telefone.setCliente(this);
    }

    public void removeTelefone(Telefone telefone) {
        telefones.remove(telefone);
        telefone.setCliente(null);
    }

    public void addEmail(Email email) {
        emails.add(email);
        email.setCliente(this);
    }

    public void removeEmail(Email email) {
        emails.remove(email);
        email.setCliente(null);
    }

    public void atualizarDadosBasicos(Cliente novosDados) {
        if(novosDados.getNome() != null) this.nome = novosDados.getNome();
        if(novosDados.getCpf() != null) this.cpf = novosDados.getCpf();
        if(novosDados.getEndereco() != null) this.endereco = novosDados.getEndereco();
    }
}
