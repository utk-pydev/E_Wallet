package org.example.wallet.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
