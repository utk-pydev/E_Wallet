package org.example.wallet.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Modifying
    @Query("update Transaction t set t.transactionStatus=?2 where t.transactionUUID=?1")
    void updateTxn(String txnId, TransactionStatus transactionStatus);

}
