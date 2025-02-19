package com.project.trading212.backend.model;

import com.project.trading212.backend.exception.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "wallet")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="wallet_name" , unique = true,nullable = false)
    private String walletName;
    @Column(name = "account_balance")
    private BigDecimal accountBalance;
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.REMOVE)
    private List<TransactionEntity> accountMovements;
    @Version
    private Long version;
    public void debit(BigDecimal amount) {
        if(accountBalance.compareTo(amount)<0){
           throw new InsufficientFundsException(INSUFFICIENT_FUNDS_EXCEPTION_DESCRIPTION);
        }
        this.accountBalance = this.accountBalance.subtract(amount);
    }
    public void credit(BigDecimal amount){
        this.accountBalance = this.accountBalance.add(amount);
    }
}
