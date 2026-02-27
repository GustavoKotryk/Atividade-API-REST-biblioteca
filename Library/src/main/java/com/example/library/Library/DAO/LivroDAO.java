package com.example.library.Library.DAO;

import com.example.library.Library.Model.Livro;
import com.example.library.Library.database.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LivroDAO {

    public Livro salvar(Livro livro){
        String sql = "INSERT INTO livro (titulo, autor, ano_publicacao) VALUES (?,?,?)";

        try (Connection conn = Conexao.conexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAno_publicacao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                livro.setId(rs.getInt(1));
            }

            return livro;


        } catch (SQLException e){
            e.printStackTrace();
        }
        return livro;
    }

    public List<Livro> buscarTodos(){
        String sql = """
                SELECT
                id, titulo, autor, ano_publicacao
                FROM livro
                """;
        List<Livro> livros = new ArrayList<>();

        try(Connection conn = Conexao.conexao();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setAno_publicacao(rs.getInt("ano_publicacao"));
                livros.add(livro);
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao buscar livro!", e);
        }
        return livros;
    }

    public Livro buscarPorId(int id) {
        String sql = "SELECT id, titulo, autor, ano_publicacao FROM livro WHERE id = ?";

        try(Connection conn = Conexao.conexao();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setAno_publicacao(rs.getInt("ano_publicacao"));
                return livro;
            }
            return null;
        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar livro por ID", e);
        }

    }

    public void atualizar(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, autor=?, ano_publicacao=? WHERE id=?";

        try (Connection conn = Conexao.conexao();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAno_publicacao());
            stmt.setInt(4, livro.getId());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar livro", e);
        }
    }

    public void deletar(int id){
        String sql = "DELETE FROM livro WHERE id = ?";

        try(Connection conn = Conexao.conexao();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Erro ao deletar livro", e);
        }
    }

}
