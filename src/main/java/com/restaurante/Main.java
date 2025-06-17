package com.restaurante;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.restaurante.model.Cliente;
import com.restaurante.model.ItemCardapio;
import com.restaurante.model.ItemPedido;
import com.restaurante.model.Mesa;
import com.restaurante.model.Pedido;
import com.restaurante.model.Reserva;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class Main {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("restaurantePU");
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== SISTEMA DE RESTAURANTE ===");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Mesas");
            System.out.println("3. Gerenciar Reservas");
            System.out.println("4. Gerenciar Cardápio");
            System.out.println("5. Gerenciar Pedidos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuMesas();
                case 3 -> menuReservas();
                case 4 -> menuCardapio();
                case 5 -> menuPedidos();
                case 0 -> {
                    emf.close();
                    System.out.println("Sistema encerrado.");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ========== MENU CLIENTES ==========
    private static void menuClientes() {
        while (true) {
            System.out.println("\n=== CLIENTES ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Editar Cliente");
            System.out.println("4. Remover Cliente");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> listarClientes();
                case 3 -> editarCliente();
                case 4 -> removerCliente();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarCliente() {
        System.out.println("\n--- CADASTRAR CLIENTE ---");
        Cliente cliente = new Cliente();

        System.out.print("CPF: ");
        cliente.setCpf(scanner.nextLine());

        System.out.print("Nome: ");
        cliente.setNome(scanner.nextLine());

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            System.out.println("Cliente cadastrado com sucesso!");
        }
    }

    private static void listarClientes() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
            
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado!");
                return;
            }

            System.out.println("\n--- CLIENTES CADASTRADOS ---");
            clientes.forEach(c -> System.out.printf(
                "ID: %d | CPF: %s | Nome: %s%n",
                c.getId(), c.getCpf(), c.getNome()
            ));
        }
    }

    private static void editarCliente() {
        System.out.println("\n--- EDITAR CLIENTE ---");
        listarClientes();
        
        System.out.print("\nID do Cliente para editar: ");
        Long clienteId = scanner.nextLong();
        scanner.nextLine();

        try (EntityManager em = emf.createEntityManager()) {
            Cliente cliente = em.find(Cliente.class, clienteId);
            if (cliente == null) {
                System.out.println("Cliente não encontrado!");
                return;
            }

            System.out.print("Novo Nome: ");
            String novoNome = scanner.nextLine();

            em.getTransaction().begin();
            cliente.setNome(novoNome);
            em.getTransaction().commit();
            System.out.println("Cliente atualizado com sucesso!");
        }
    }

    private static void removerCliente() {
        System.out.println("\n--- REMOVER CLIENTE ---");
        listarClientes();
        
        System.out.print("\nID do Cliente para remover: ");
        Long clienteId = scanner.nextLong();

        try (EntityManager em = emf.createEntityManager()) {
            Cliente cliente = em.find(Cliente.class, clienteId);
            if (cliente == null) {
                System.out.println("Cliente não encontrado!");
                return;
            }

            em.getTransaction().begin();
            em.remove(cliente);
            em.getTransaction().commit();
            System.out.println("Cliente removido com sucesso!");
        }
    }

    // ========== MENU MESAS ==========
    private static void menuMesas() {
        while (true) {
            System.out.println("\n=== MESAS ===");
            System.out.println("1. Cadastrar Mesa");
            System.out.println("2. Listar Mesas");
            System.out.println("3. Editar Mesa");
            System.out.println("4. Remover Mesa");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarMesa();
                case 2 -> listarMesas();
                case 3 -> editarMesa();
                case 4 -> removerMesa();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarMesa() {
        System.out.println("\n--- CADASTRAR MESA ---");
        Mesa mesa = new Mesa();

        System.out.print("Número da mesa: ");
        mesa.setNumero(scanner.nextInt());

        System.out.print("Capacidade: ");
        mesa.setCapacidade(scanner.nextInt());
        scanner.nextLine();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(mesa);
            em.getTransaction().commit();
            System.out.println("Mesa cadastrada com sucesso!");
        }
    }

    private static void listarMesas() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Mesa> mesas = em.createQuery("SELECT m FROM Mesa m", Mesa.class).getResultList();
            
            if (mesas.isEmpty()) {
                System.out.println("Nenhuma mesa cadastrada!");
                return;
            }

            System.out.println("\n--- MESAS CADASTRADAS ---");
            mesas.forEach(m -> System.out.printf(
                "ID: %d | Nº %d | Capacidade: %d lugares%n",
                m.getId(), m.getNumero(), m.getCapacidade()
            ));
        }
    }

    private static void editarMesa() {
        System.out.println("\n--- EDITAR MESA ---");
        listarMesas();
        
        System.out.print("\nID da Mesa para editar: ");
        Long mesaId = scanner.nextLong();
        scanner.nextLine();

        try (EntityManager em = emf.createEntityManager()) {
            Mesa mesa = em.find(Mesa.class, mesaId);
            if (mesa == null) {
                System.out.println("Mesa não encontrada!");
                return;
            }

            System.out.print("Nova Capacidade: ");
            int novaCapacidade = scanner.nextInt();
            scanner.nextLine();

            em.getTransaction().begin();
            mesa.setCapacidade(novaCapacidade);
            em.getTransaction().commit();
            System.out.println("Mesa atualizada com sucesso!");
        }
    }

    private static void removerMesa() {
        System.out.println("\n--- REMOVER MESA ---");
        listarMesas();
        
        System.out.print("\nID da Mesa para remover: ");
        Long mesaId = scanner.nextLong();

        try (EntityManager em = emf.createEntityManager()) {
            Mesa mesa = em.find(Mesa.class, mesaId);
            if (mesa == null) {
                System.out.println("Mesa não encontrada!");
                return;
            }

            em.getTransaction().begin();
            em.remove(mesa);
            em.getTransaction().commit();
            System.out.println("Mesa removida com sucesso!");
        }
    }

    // ========== MENU RESERVAS ==========
    private static void menuReservas() {
        while (true) {
            System.out.println("\n=== RESERVAS ===");
            System.out.println("1. Nova Reserva");
            System.out.println("2. Listar Reservas");
            System.out.println("3. Editar Reserva");
            System.out.println("4. Cancelar Reserva");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> fazerReserva();
                case 2 -> listarReservas();
                case 3 -> editarReserva();
                case 4 -> cancelarReserva();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void fazerReserva() {
        System.out.println("\n--- NOVA RESERVA ---");
        try (EntityManager em = emf.createEntityManager()) {
            // Listar clientes
            System.out.println("\nClientes disponíveis:");
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
            clientes.forEach(c -> System.out.println(c.getId() + " - " + c.getNome()));

            System.out.print("ID do Cliente: ");
            Long clienteId = scanner.nextLong();
            Cliente cliente = em.find(Cliente.class, clienteId);

            // Listar mesas
            System.out.println("\nMesas disponíveis:");
            List<Mesa> mesas = em.createQuery("SELECT m FROM Mesa m", Mesa.class).getResultList();
            mesas.forEach(m -> System.out.println(m.getId() + " - Nº" + m.getNumero() + " (Capacidade: " + m.getCapacidade() + ")"));

            System.out.print("ID da Mesa: ");
            Long mesaId = scanner.nextLong();
            scanner.nextLine();
            Mesa mesa = em.find(Mesa.class, mesaId);

            System.out.print("Data e Hora (dd/MM/yyyy HH:mm): ");
            String dataHoraStr = scanner.nextLine();
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, formatter);

            Reserva reserva = new Reserva();
            reserva.setCliente(cliente);
            reserva.setMesa(mesa);
            reserva.setDataHora(dataHora);

            em.getTransaction().begin();
            em.persist(reserva);
            em.getTransaction().commit();
            System.out.println("Reserva realizada com sucesso!");
        }
    }

    private static void listarReservas() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Reserva> query = em.createQuery(
                "SELECT r FROM Reserva r ORDER BY r.dataHora", Reserva.class);
            List<Reserva> reservas = query.getResultList();

            if (reservas.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada.");
                return;
            }

            System.out.println("\n--- RESERVAS ---");
            reservas.forEach(r -> System.out.printf(
                "ID: %d | Cliente: %s | Mesa: %d | Data: %s%n",
                r.getId(),
                r.getCliente().getNome(),
                r.getMesa().getNumero(),
                r.getDataHora().format(formatter)
            ));
        }
    }

    private static void editarReserva() {
        System.out.println("\n--- EDITAR RESERVA ---");
        listarReservas();
        
        System.out.print("\nID da Reserva para editar: ");
        Long reservaId = scanner.nextLong();
        scanner.nextLine();

        try (EntityManager em = emf.createEntityManager()) {
            Reserva reserva = em.find(Reserva.class, reservaId);
            if (reserva == null) {
                System.out.println("Reserva não encontrada!");
                return;
            }

            System.out.print("Nova Data e Hora (dd/MM/yyyy HH:mm): ");
            String novaDataHoraStr = scanner.nextLine();
            LocalDateTime novaDataHora = LocalDateTime.parse(novaDataHoraStr, formatter);

            em.getTransaction().begin();
            reserva.setDataHora(novaDataHora);
            em.getTransaction().commit();
            System.out.println("Reserva atualizada com sucesso!");
        }
    }

    private static void cancelarReserva() {
        System.out.println("\n--- CANCELAR RESERVA ---");
        listarReservas();
        
        System.out.print("\nID da Reserva para cancelar: ");
        Long reservaId = scanner.nextLong();

        try (EntityManager em = emf.createEntityManager()) {
            Reserva reserva = em.find(Reserva.class, reservaId);
            if (reserva == null) {
                System.out.println("Reserva não encontrada!");
                return;
            }

            em.getTransaction().begin();
            em.remove(reserva);
            em.getTransaction().commit();
            System.out.println("Reserva cancelada com sucesso!");
        }
    }

    // ========== MENU CARDÁPIO ==========
    private static void menuCardapio() {
        while (true) {
            System.out.println("\n=== CARDÁPIO ===");
            System.out.println("1. Cadastrar Item");
            System.out.println("2. Listar Itens");
            System.out.println("3. Editar Item");
            System.out.println("4. Remover Item");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarItemCardapio();
                case 2 -> listarItensCardapio();
                case 3 -> editarItemCardapio();
                case 4 -> removerItemCardapio();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarItemCardapio() {
        System.out.println("\n--- NOVO ITEM ---");
        ItemCardapio item = new ItemCardapio();

        System.out.print("Nome: ");
        item.setNome(scanner.nextLine());

        System.out.print("Descrição: ");
        item.setDescricao(scanner.nextLine());

        System.out.print("Preço: ");
        item.setPreco(scanner.nextDouble());
        scanner.nextLine();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
            System.out.println("Item cadastrado com sucesso!");
        }
    }

    private static void listarItensCardapio() {
        try (EntityManager em = emf.createEntityManager()) {
            List<ItemCardapio> itens = em.createQuery("SELECT i FROM ItemCardapio i", ItemCardapio.class).getResultList();
            
            if (itens.isEmpty()) {
                System.out.println("Nenhum item no cardápio!");
                return;
            }

            System.out.println("\n--- CARDÁPIO ---");
            itens.forEach(item -> System.out.printf(
                "ID: %d | %s - %s | R$ %.2f%n",
                item.getId(), item.getNome(), item.getDescricao(), item.getPreco()
            ));
        }
    }

    private static void editarItemCardapio() {
        System.out.println("\n--- EDITAR ITEM ---");
        listarItensCardapio();
        
        System.out.print("\nID do Item para editar: ");
        Long itemId = scanner.nextLong();
        scanner.nextLine();

        try (EntityManager em = emf.createEntityManager()) {
            ItemCardapio item = em.find(ItemCardapio.class, itemId);
            if (item == null) {
                System.out.println("Item não encontrado!");
                return;
            }

            System.out.print("Novo Nome: ");
            item.setNome(scanner.nextLine());

            System.out.print("Nova Descrição: ");
            item.setDescricao(scanner.nextLine());

            System.out.print("Novo Preço: ");
            item.setPreco(scanner.nextDouble());
            scanner.nextLine();

            em.getTransaction().begin();
            em.merge(item);
            em.getTransaction().commit();
            System.out.println("Item atualizado com sucesso!");
        }
    }

    private static void removerItemCardapio() {
        System.out.println("\n--- REMOVER ITEM ---");
        listarItensCardapio();
        
        System.out.print("\nID do Item para remover: ");
        Long itemId = scanner.nextLong();

        try (EntityManager em = emf.createEntityManager()) {
            ItemCardapio item = em.find(ItemCardapio.class, itemId);
            if (item == null) {
                System.out.println("Item não encontrado!");
                return;
            }

            em.getTransaction().begin();
            em.remove(item);
            em.getTransaction().commit();
            System.out.println("Item removido com sucesso!");
        }
    }

    // ========== MENU PEDIDOS ==========
    private static void menuPedidos() {
        while (true) {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1. Novo Pedido");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Adicionar Item ao Pedido");
            System.out.println("4. Fechar Pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> criarPedido();
                case 2 -> listarPedidos();
                case 3 -> adicionarItemPedido();
                case 4 -> fecharPedido();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void criarPedido() {
        System.out.println("\n--- NOVO PEDIDO ---");
        try (EntityManager em = emf.createEntityManager()) {
            // Listar reservas ativas
            System.out.println("\nReservas ativas:");
            List<Reserva> reservas = em.createQuery(
                "SELECT r FROM Reserva r WHERE r.dataHora >= CURRENT_TIMESTAMP", 
                Reserva.class).getResultList();

            if (reservas.isEmpty()) {
                System.out.println("Nenhuma reserva ativa encontrada!");
                return;
            }

            reservas.forEach(r -> System.out.printf(
                "ID: %d | %s - Mesa %d (%s)%n",
                r.getId(), r.getCliente().getNome(), r.getMesa().getNumero(), 
                r.getDataHora().format(formatter)
            ));

            System.out.print("ID da Reserva: ");
            Long reservaId = scanner.nextLong();
            Reserva reserva = em.find(Reserva.class, reservaId);

            if (reserva == null) {
                System.out.println("Reserva não encontrada!");
                return;
            }

            Pedido pedido = new Pedido();
            pedido.setReserva(reserva);

            em.getTransaction().begin();
            em.persist(pedido);
            em.getTransaction().commit();
            
            System.out.println("Pedido criado com ID: " + pedido.getId());
        }
    }

    private static void adicionarItemPedido() {
        System.out.println("\n--- ADICIONAR ITEM ---");
        try (EntityManager em = emf.createEntityManager()) {
            // Listar pedidos abertos
            System.out.println("\nPedidos abertos:");
            List<Pedido> pedidos = em.createQuery(
                "SELECT p FROM Pedido p WHERE p.dataFechamento IS NULL", 
                Pedido.class).getResultList();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido aberto!");
                return;
            }

            pedidos.forEach(p -> System.out.printf(
                "ID: %d | Reserva: %s - Mesa %d%n",
                p.getId(), p.getReserva().getCliente().getNome(), 
                p.getReserva().getMesa().getNumero()
            ));

            System.out.print("ID do Pedido: ");
            Long pedidoId = scanner.nextLong();
            Pedido pedido = em.find(Pedido.class, pedidoId);

            // Listar cardápio
            listarItensCardapio();

            System.out.print("ID do Item: ");
            Long itemId = scanner.nextLong();
            ItemCardapio item = em.find(ItemCardapio.class, itemId);

            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setItemCardapio(item);
            itemPedido.setQuantidade(quantidade);

            em.getTransaction().begin();
            em.persist(itemPedido);
            em.getTransaction().commit();
            
            System.out.println("Item adicionado com sucesso!");
        }
    }

    private static void listarPedidos() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Pedido> pedidos = em.createQuery(
                "SELECT p FROM Pedido p ORDER BY p.dataAbertura DESC", 
                Pedido.class).getResultList();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado!");
                return;
            }

            System.out.println("\n--- TODOS OS PEDIDOS ---");
            pedidos.forEach(p -> {
                System.out.printf("\nPedido ID: %d | Mesa: %d | Cliente: %s%n",
                    p.getId(), p.getReserva().getMesa().getNumero(), 
                    p.getReserva().getCliente().getNome());
                
                System.out.println("Itens:");
                p.getItens().forEach(i -> System.out.printf(
                    " - %d x %s (R$ %.2f cada) = R$ %.2f%n",
                    i.getQuantidade(), i.getItemCardapio().getNome(),
                    i.getItemCardapio().getPreco(),
                    i.getQuantidade() * i.getItemCardapio().getPreco()
                ));
                
                double total = p.getItens().stream()
                    .mapToDouble(i -> i.getQuantidade() * i.getItemCardapio().getPreco())
                    .sum();
                
                System.out.printf("Total: R$ %.2f | Status: %s%n",
                    total, p.getDataFechamento() == null ? "ABERTO" : "FECHADO");
            });
        }
    }

    private static void fecharPedido() {
        System.out.println("\n--- FECHAR PEDIDO ---");
        try (EntityManager em = emf.createEntityManager()) {
            // Listar pedidos abertos
            List<Pedido> pedidos = em.createQuery(
                "SELECT p FROM Pedido p WHERE p.dataFechamento IS NULL", 
                Pedido.class).getResultList();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido aberto!");
                return;
            }

            pedidos.forEach(p -> System.out.printf(
                "ID: %d | Mesa %d | Cliente: %s | %d itens%n",
                p.getId(), p.getReserva().getMesa().getNumero(),
                p.getReserva().getCliente().getNome(), p.getItens().size()
            ));

            System.out.print("ID do Pedido para fechar: ");
            Long pedidoId = scanner.nextLong();
            Pedido pedido = em.find(Pedido.class, pedidoId);

            if (pedido == null || pedido.getDataFechamento() != null) {
                System.out.println("Pedido não encontrado ou já fechado!");
                return;
            }

            em.getTransaction().begin();
            pedido.setDataFechamento(LocalDateTime.now());
            em.getTransaction().commit();
            
            System.out.println("Pedido fechado com sucesso!");
        }
    }
}