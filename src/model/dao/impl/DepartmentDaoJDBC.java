package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection connect;

    public DepartmentDaoJDBC(Connection connect) { this.connect = connect; }

    @Override
    public void insert(Department o) {
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(
                    "INSERT INTO department "
                    + "(Name) "
                    + "VALUES "
                    + "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, o.getName());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    o.setId(id);
                }
                DB.closeResultSet(resultSet);
            }
            else {
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
    public void update(Department o) {
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE Id = ? "
            );
            statement.setInt(2, o.getId());
            statement.setString(1, o.getName());

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
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(
                    "DELETE FROM department WHERE Id = ? "
            );
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new DbException("Error: this id does not exist.");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(
                    "SELECT * FROM department WHERE Id = ? "
            );
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Department dep = new Department();
                dep.setId(resultSet.getInt("Id"));
                dep.setName(resultSet.getString("Name"));
                return dep;
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
    public List<Department> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(
                    "SELECT * FROM department ORDER BY Id"
            );
            resultSet = statement.executeQuery();
            List<Department> list = new ArrayList<>();

            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                list.add(department);
            }
            return list;
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
