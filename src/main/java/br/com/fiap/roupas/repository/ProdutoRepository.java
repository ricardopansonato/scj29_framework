package br.com.fiap.roupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.roupas.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
