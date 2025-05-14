package app;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TESTE 1 department findById ===");
        Department department = departmentDao.findById(2);
        System.out.println(department.getName());

        System.out.println("\n=== TEST 2. department findAll ===");
        List<Department> list = departmentDao.findAll();
        list.forEach(System.out::println);

        System.out.println("\n=== TEST 3. department insert ===");
        department = new Department(null, "Teste");
        departmentDao.insert(department);
        System.out.println("Inserted: new id = " + department.getId());

        System.out.println("\n=== TEST 4. department update ===");
        department = departmentDao.findById(1);
        department.setName("Teste supremo");
        departmentDao.update(department);
        System.out.println("Update completed: \n" + department);

        System.out.println("\n=== TEST 5. department DeleteById ===");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter id for delete test: ");
        int deleteId = sc.nextInt();
        departmentDao.deleteById(deleteId);
        System.out.println("Delete completed");
        sc.close();
    }
}
