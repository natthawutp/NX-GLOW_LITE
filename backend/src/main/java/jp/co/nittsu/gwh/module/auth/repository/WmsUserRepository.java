package jp.co.nittsu.gwh.module.auth.repository;

import jp.co.nittsu.gwh.module.auth.entity.WmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WmsUserRepository extends JpaRepository<WmsUser, Long> {

    Optional<WmsUser> findByEmailAndDelFlg(String email, Integer delFlg);

    boolean existsByEmail(String email);
}
