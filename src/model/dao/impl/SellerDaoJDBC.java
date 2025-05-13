package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection connect;

    public SellerDaoJDBC(Connection connect) {
        this.connect = connect;
    }

    @Override
    public void insert(Seller o) {

    }

    @Override
    public void update(Seller o) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ? "
            );

            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Department dep  = new Department();
                dep.setId(resultSet.getInt("DepartmentId"));
                dep.setName(resultSet.getString("DepName"));
                Seller o = new Seller();
                o.setId(resultSet.getInt("Id"));
                o.setName(resultSet.getString("Name"));
                o.setEmail(resultSet.getString("Email"));
                o.setBaseSalary(resultSet.getDouble("BaseSalary"));
                o.setBirthDate(resultSet.getDate("BirthDate"));
                o.setDepartment(dep);
                return o;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
