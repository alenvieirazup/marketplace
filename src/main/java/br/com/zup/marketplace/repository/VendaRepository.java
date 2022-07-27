package br.com.zup.marketplace.repository;

import br.com.zup.marketplace.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
