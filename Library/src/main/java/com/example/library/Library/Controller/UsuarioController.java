package com.example.library.Library.Controller;

import com.example.library.Library.Model.Livro;
import com.example.library.Library.Model.Usuario;
import com.example.library.Library.Service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController

@RequestMapping("/usuarioService")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario){
        return usuarioService.salvar(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuario() throws SQLException {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuarioId(@PathVariable int id) throws SQLException {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping
    public Usuario atualizarUsuario(@RequestBody Usuario usuario) throws SQLException {
        return usuarioService.atualizar(usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
        usuarioService.deletar(id);
    }
}
