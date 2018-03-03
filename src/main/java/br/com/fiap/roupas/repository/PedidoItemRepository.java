package br.com.fiap.roupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.roupas.model.PedidoItem;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

}
