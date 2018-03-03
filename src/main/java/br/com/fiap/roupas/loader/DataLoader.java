package br.com.fiap.roupas.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.fiap.roupas.model.Cliente;
import br.com.fiap.roupas.model.Pedido;
import br.com.fiap.roupas.model.PedidoItem;
import br.com.fiap.roupas.model.Produto;
import br.com.fiap.roupas.repository.ClienteRepository;
import br.com.fiap.roupas.repository.PedidoRepository;
import br.com.fiap.roupas.repository.ProdutoRepository;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		for(int i = 1; i <= 100; i++) {
			Cliente cliente = new Cliente();
			cliente.setNome("Denis " + i);
			cliente.setSobrenome("Romano " + i);
			cliente.setCnpj("12345678" + i);
			
			clienteRepository.save(cliente);
			
			Produto produto = new Produto();
			produto.setNome("Teste " + i);
			produto.setValor((double)(100 + i));
			
			produtoRepository.save(produto);
			
			Pedido pedido = new Pedido();
			pedido.setCliente(cliente);
			pedido.setCriacao(new Date());
			
			pedidoRepository.save(pedido);
			
			PedidoItem item = new PedidoItem();
			item.setProduto(produto);
			item.setQuantidade((long)i);
			item.setPedido(pedido);
			List<PedidoItem> items = new ArrayList<>();
			items.add(item);
			pedido.setItems(items);
			
			pedidoRepository.save(pedido);

		}
		
		for(Pedido pedido : pedidoRepository.findAll()) {
			System.out.println(pedido.getId());
		}
		
	}

}
