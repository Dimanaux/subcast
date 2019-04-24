package com.example.subcast.db.repositories;

import com.example.subcast.db.Account;
import com.example.subcast.db.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * loads Account object by username
     *
     * @param username query param
     * @return account object with given username
     */
    Account findByUsername(String username);

    @Query(
            value = "SELECT * FROM account_token t " +
                    "INNER JOIN account a ON " +
                    "t.account_id = a.id " +
                    "WHERE token = :token ",
            nativeQuery = true
    )
    Account findByToken(@Param("token") String token);

    default Account findByToken(Token token) {
        return findByToken(token.toString());
    }

    @Query(
            value = "INSERT INTO account_token (account_id, token)" +
                    " VALUES (:id, :token)",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void saveToken(@Param("id") long accountId,
                   @Param("token") String token);
}
