package br.com.zup.marketplace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Pagamento {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String forma;
    private String status;

    public Pagamento() {
    }

    public Pagamento(UUID id, String forma, String status) {
        this.id = id;
        this.forma = forma;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

}
