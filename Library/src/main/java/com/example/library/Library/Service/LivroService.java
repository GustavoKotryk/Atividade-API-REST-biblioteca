package com.example.library.Library.Service;


import com.example.library.Library.DAO.LivroDAO;
import com.example.library.Library.Model.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {


    @Autowired
    private LivroDAO livroDAO;

    public Livro salvar(Livro livro){
        if(livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()){
            throw new RuntimeException("Título é obrigatório");
        }
        return livroDAO.salvar(livro);
    }

    public List<Livro> listarTodos(){
        return livroDAO.buscarTodos();
    }

    public Livro buscarPorId(int id){
        Livro livro = livroDAO.buscarPorId(id);
        if (livro == null) {
            throw new RuntimeException("Livro não encontrado");
        }
        return livro;
    }

    public void atualizar(Livro livro){
        livroDAO.atualizar(livro);
    }

    public void deletar(int id){
        livroDAO.deletar(id);
    }
}
