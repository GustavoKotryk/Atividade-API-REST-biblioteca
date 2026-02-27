package com.example.library.Library.DAO;

import com.example.library.Library.Model.Emprestimo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmprestimoDAO {

    @Autowired
    private DataSource dataSource;

    public Emprestimo salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (livro_id, usuario_id, data_emprestimo, data_devolucao) VALUES (?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, emprestimo.getLivroId());
            stmt.setLong(2, emprestimo.getUsuarioId());
            stmt.setObject(3, emprestimo.getDataEmprestimo());
            stmt.setObject(4, emprestimo.getDataDevolucao());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar emprestimo" + e.getMessage(), e);
        }
        return emprestimo;
    }


    public List<Emprestimo> buscarTodos() throws SQLException {
        String sql = """
                SELECT
                id, livro_id, usuario_id, data_emprestimo, data_devolucao
                FROM emprestimo;
                """;
        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getLong("id"));
                emprestimo.setLivroId(rs.getLong("livro_id"));
                emprestimo.setUsuarioId(rs.getLong("usuario_id"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                Date dataDevolucao = rs.getDate("data_devolucao");


                if (dataDevolucao != null) {
                    emprestimo.setDataDevolucao(dataDevolucao.toLocalDate());
                }

                emprestimos.add(emprestimo);
            }
            return emprestimos;
        }
    }

    public Emprestimo buscarPorId(Long id) {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getLong("id"));
                emprestimo.setLivroId(rs.getLong("livro_id"));
                emprestimo.setUsuarioId(rs.getLong("usuario_id"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());

                Date dataDevolucao = rs.getDate("data_devolucao");
                if (dataDevolucao != null) {
                    emprestimo.setDataDevolucao(dataDevolucao.toLocalDate());
                }

                return emprestimo;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimo por ID", e);
        }
    }

    public void atualizar(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimo SET livro_id = ?, usuario_id = ?, data_emprestimo = ?, data_devolucao = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, emprestimo.getLivroId());
            stmt.setLong(2, emprestimo.getUsuarioId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));

            if (emprestimo.getDataDevolucao() != null) {
                stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setLong(5, emprestimo.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar empréstimo", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM emprestimo WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar empréstimo", e);
        }
    }
}
