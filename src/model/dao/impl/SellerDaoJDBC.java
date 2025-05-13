package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connect;

    public SellerDaoJDBC(Connection connect) {
        this.connect = connect;
    }

    @Override
    public void insert(Seller o) {
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    +" (?, ?, ?, ?, ?) ",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, o.getName());
            statement.setString(2, o.getEmail());
            statement.setDate(3, new java.sql.Date(o.getBirthDate().getTime()));
            statement.setDouble(4, o.getBaseSalary());
            statement.setInt(5, o.getDepartment().getId());

            int rowsAffect = statement.executeUpdate();

            if (rowsAffect > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    o.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected.");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Seller o) {
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(
                    "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?"
            );

            statement.setString(1, o.getName());
            statement.setString(2, o.getEmail());
            statement.setDate(3, new java.sql.Date(o.getBirthDate().getTime()));
            statement.setDouble(4, o.getBaseSalary());
            statement.setInt(5, o.getDepartment().getId());
            statement.setInt(6, o.getId());

            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement("DELETE FROM seller WHERE Id = ?");
            statement.setInt(1, id);
            int rows = statement.executeUpdate();

            if(rows == 0) {
                throw new DbException("This id does not exist");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
        }
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
                Department dep  = instantiateDepartment(resultSet);
                return instantiateSeller(resultSet, dep);
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name "
            );

            statement.setInt(1, department.getId());
            resultSet = statement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> depMap = new HashMap<>();

            while (resultSet.next()) {
                Department dep = depMap.get(resultSet.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    depMap.put(resultSet.getInt("DepartmentId"), dep);
                }
                Seller seller = instantiateSeller(resultSet, dep);
                sellerList.add(seller);
            }
            return sellerList;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    private Seller instantiateSeller(ResultSet resultSet, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department dep = new Department();
        dep.setId(resultSet.getInt("DepartmentId"));
        dep.setName(resultSet.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name "
            );

            resultSet = statement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> depMap = new HashMap<>();

            while (resultSet.next()) {
                Department dep = depMap.get(resultSet.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    depMap.put(resultSet.getInt("DepartmentId"), dep);
                }
                Seller seller = instantiateSeller(resultSet, dep);
                sellerList.add(seller);
            }
            return sellerList;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }
}



