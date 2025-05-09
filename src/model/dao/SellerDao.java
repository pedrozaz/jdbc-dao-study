package model.dao;

import model.entities.Seller;

import java.util.List;

public interface SellerDao {
        void insert(Seller o);
        void update(Seller o);
        void deleteById(Seller id);
        Seller findById(Seller id);
        List<Seller> findAll();
}
