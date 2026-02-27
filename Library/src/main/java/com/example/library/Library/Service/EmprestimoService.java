package com.example.library.Library.Service;


import com.example.library.Library.DAO.EmprestimoDAO;
import com.example.library.Library.DAO.LivroDAO;
import com.example.library.Library.DAO.UsuarioDAO;
import com.example.library.Library.Model.Emprestimo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO;

    private final LivroDAO livroDAO;

    private final UsuarioDAO usuarioDAO;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, LivroDAO livroDAO, UsuarioDAO usuarioDAO) {
        this.emprestimoDAO = emprestimoDAO;
        this.livroDAO = livroDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public Emprestimo cadastrarEmprestimo(Emprestimo emprestimo) throws SQLException {

        if (emprestimo.getLivroId() == null || emprestimo.getLivroId() <= 0) {
            throw new RuntimeException("ID do livro inválido");
        }
        if (emprestimo.getUsuarioId() == null || emprestimo.getUsuarioId() <= 0) {
            throw new RuntimeException("ID do usuário inválido");
        }

        List<Emprestimo> emprestimosLivro = buscarPorLivro(emprestimo.getLivroId());
        for (Emprestimo e : emprestimosLivro) {
            if (e.getDataDevolucao() == null) {
                throw new RuntimeException("Livro já está emprestado");
            }
        }

        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }

        if (emprestimo.getDataDevolucao() != null &&
                emprestimo.getDataDevolucao().isBefore(emprestimo.getDataEmprestimo())) {
            throw new RuntimeException("Data de devolução não pode ser anterior à data de empréstimo");
        }

        return emprestimoDAO.salvar(emprestimo);
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        return emprestimoDAO.buscarTodos();
    }

    public Emprestimo buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        Emprestimo emprestimo = emprestimoDAO.buscarPorId(id);
        if (emprestimo == null) {
            throw new RuntimeException("Empréstimo não encontrado");
        }
        return emprestimo;
    }

    public List<Emprestimo> buscarPorUsuario(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new RuntimeException("ID do usuário inválido");
        }

        return emprestimoDAO.buscarPorUsuario(usuarioId);
    }

    public List<Emprestimo> buscarPorLivro(Long livroId) throws SQLException {
        List<Emprestimo> todos = emprestimoDAO.buscarTodos();
        return todos.stream()
                .filter(e -> e.getLivroId().equals(livroId))
                .toList();
    }

    public void atualizar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null || emprestimo.getId() <= 0) {
            throw new RuntimeException("ID inválido");
        }

        buscarPorId(emprestimo.getId());

        if (emprestimo.getDataEmprestimo() == null) {
            throw new RuntimeException("Data de empréstimo é obrigatória");
        }

        if (emprestimo.getDataDevolucao() != null &&
                emprestimo.getDataDevolucao().isBefore(emprestimo.getDataEmprestimo())) {
            throw new RuntimeException("Data de devolução não pode ser anterior à data de empréstimo");
        }

        emprestimoDAO.atualizar(emprestimo);
    }

    public void deletar(Long id) {
        emprestimoDAO.deletar(id);
    }

    public void registrarDevolucao(Long id) {
        Emprestimo emprestimo = buscarPorId(id);

        if (emprestimo.getDataDevolucao() != null) {
            throw new RuntimeException("Este livro já foi devolvido");
        }

        emprestimoDAO.registrarDevolucao(id, LocalDate.now());
    }
}