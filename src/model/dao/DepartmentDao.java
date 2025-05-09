package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {
    void insert(Department o);
    void update(Department o);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
}
