package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository <Login,Long> {
    Login findByEmail(String email);

}
