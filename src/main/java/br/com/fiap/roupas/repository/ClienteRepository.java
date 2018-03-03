package br.com.fiap.roupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.roupas.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
