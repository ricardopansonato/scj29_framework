package br.com.fiap.roupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.roupas.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
