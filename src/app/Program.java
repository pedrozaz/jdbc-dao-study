package app;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1. seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n=== TEST 2. seller findByDepartment ===");
        Department dept = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(dept);
        list.forEach(System.out::println);

        System.out.println("\n=== TEST 3. seller findAll ===");
        list = sellerDao.findAll();
        list.forEach(System.out::println);

        System.out.println("\n=== TEST 4. seller insert ===");
        seller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000., dept);
        sellerDao.insert(seller);
        System.out.println("Inserted: new id = " + seller.getId());

        System.out.println("\n=== TEST 5. seller update ===");
        seller = sellerDao.findById(1);
        seller.setName("Marta Wayne");
        sellerDao.update(seller);
        System.out.println("Update completed: \n" + seller);

        System.out.println("\n=== TEST 6. seller DeleteById ===");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter id for delete test: ");
        int deleteId = sc.nextInt();
        sellerDao.deleteById(deleteId);
        System.out.println("Delete completed");
        sc.close();

    }
}
