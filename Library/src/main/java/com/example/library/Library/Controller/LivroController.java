package com.example.library.Library.Controller;


import com.example.library.Library.Model.Livro;
import com.example.library.Library.Service.LivroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService){
        this.livroService = livroService;
    }

    @PostMapping
    public Livro criarLivro(@RequestBody Livro livro){
        return livroService.salvar(livro);
    }

    @GetMapping
    public List<Livro> buscarLivro(){
        return livroService.listarTodos();
    }

    @GetMapping("/{id}")
    public Livro buscarLivroId(@PathVariable int id){
        return livroService.buscarPorId(id);
    }

    @PutMapping
    public void editarLivro(@RequestBody Livro livro){
        livroService.atualizar(livro);
    }

    @DeleteMapping("/{id}")
    public void excluirLivro(@PathVariable int id){
        livroService.deletar(id);
    }

}
