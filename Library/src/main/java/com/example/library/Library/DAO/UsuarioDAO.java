package com.example.library.Library.DAO;


import com.example.library.Library.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDAO {

    @Autowired
    private DataSource dataSource;

    public Usuario salvar(Usuario usuario){
        String sql = "INSERT INTO usuario (id, nome, email) VALUES (?,?,?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                usuario.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usuario;
    }


    public List<Usuario> buscarTodos() throws SQLException {
        String sql = """
                SELECT
                id, nome, email
                FROM
                usuario
                """;
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuarios.add(usuario);
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao buscar usuario!", e);
        }
        return usuarios;
    }


    public Usuario buscarPorId(int id) throws SQLException{
        String sql = "SELECT id, nome, email WHERE id = ?";

        try(Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                return usuario;
            }
            return null;

        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar livro por ID", e);
        }
    }

    public void atualizar(Usuario usuario) throws SQLException{
        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar usuario", e);
        }
    }

    public void deletar(int id){
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Erro ao deletar usuario", e);
        }
    }
}
