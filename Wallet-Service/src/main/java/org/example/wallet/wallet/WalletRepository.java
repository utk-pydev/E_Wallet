package org.example.wallet.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findByPhoneNumber(String number);

    @Modifying
    @Query("update Wallet w set w.balance=w.balance+?2 where w.phoneNumber=?1")
    void updateWallet(String phoneNumber, double amount);
}
