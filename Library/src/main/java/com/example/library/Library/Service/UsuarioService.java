package com.example.library.Library.Service;

import com.example.library.Library.DAO.UsuarioDAO;
import com.example.library.Library.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    public Usuario salvar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do usuário é obrigatório");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email do usuário é obrigatório");
        }
        if (!usuario.getEmail().contains("@")) {
            throw new RuntimeException("Email inválido");
        }

        return usuarioDAO.salvar(usuario);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioDAO.buscarTodos();
    }

    public Usuario buscarPorId(int id) throws SQLException {
        Usuario usuario = usuarioDAO.buscarPorId(id);

        return usuario;
    }

    public void atualizar(Usuario usuario) throws SQLException {

        buscarPorId(usuario.getId());

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do usuário é obrigatório");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email do usuário é obrigatório");
        }
        if (!usuario.getEmail().contains("@")) {
            throw new RuntimeException("Email inválido");
        }

        usuarioDAO.atualizar(usuario);
    }

    public void deletar(int id) {
        usuarioDAO.deletar(id);
    }
}