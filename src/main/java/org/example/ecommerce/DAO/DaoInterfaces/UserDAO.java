package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.User;

public interface UserDAO {
    User save(User user);

    User findByEmail(String email);

    User findById(int id);
}
