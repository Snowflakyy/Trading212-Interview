# Trading212 Interview

## Overview
A **Spring Boot** application handling **WebSocket real-time updates** and running on:

- **Backend API:** `http://localhost:8080/api/v1/`
- **Frontend WebSocket:** `ws://localhost:8080/api/v1/ws/crypto`

All business logic resides in the **backend**.
### Future update: Junit tests
---

## Endpoints
| Controller              | Method | Endpoint             | Description             |
|-------------------------|--------|----------------------|-------------------------|
| **WalletController**    | POST   | `/wallet`           | Create a new wallet    |
|                         | GET    | `/wallet/{id}`      | Retrieve wallet by ID  |
|                         | PUT    | `/wallet/reset/{id}` | Reset wallet balance   |
| **CryptoWebSocketController** | GET | `/price/top20`     | Get top 20 crypto prices |
|                         | GET    | `/price/{symbol}`   | Get price by symbol    |
| **TransactionController** | POST | `/transaction`      | Create a transaction   |
|                         | GET    | `/transaction`      | Get all transactions   |
|                         | GET    | `/transaction/{id}` | Get transaction by ID  |
|                         | GET    | `/transaction/holding` | Get all holdings |

---

## Core Components

### **Transaction Service (`TransactionServiceImpl`)**
Handles transaction operations with key responsibilities:

#### **1. Transaction Creation**
- Supports **buy and sell** transactions.
- Ensures:
  - **Buy transactions:** Wallet has sufficient funds.
  - **Sell transactions:** Sufficient cryptocurrency quantity.
- Updates wallet balances **automatically**.
- Calculates amounts using `quantity × price`.

#### **2. Transaction Validation**
- Checks:
  - Wallet existence.
  - Sufficient balance for purchases.
  - Sufficient crypto for sales.
- Throws exceptions:
  - `InsufficientFundsException`
  - `NoSuchWalletException`
  - `NoSuchTransactionException`
  - `InsufficientQuantityException`
  - `NoSuchCryptoException`

#### **3. Wallet Balance Management**
- Implements **debit/credit** operations:
  ```java
  public void debit(BigDecimal amount) {
      walletBalance = walletBalance.subtract(amount);
  }

  public void credit(BigDecimal amount) {
      walletBalance = walletBalance.add(amount);
  }
### **4. Performance Enhancements**
- ⚡ **Batch Processing**:  
  ```java
  @Transactional
  public void processTransactionsInBatch(List<Transaction> transactions) {
      transactionRepository.saveAll(transactions);
  }
Maintains wallet balance integrity

## 🔹 Holdings Management
Manages **cryptocurrency holdings** across wallets and ensures accurate portfolio tracking.

### ✅ Features
- 📊 Tracks all cryptocurrency holdings.
- 🔄 Calculates **current portfolio value** using **real-time prices**.
- 📈 Provides **profit/loss calculations** based on transactions.

Transaction Mapper (TransactionMapper)
The mapper handles data transformation between different layers of the application with these key functions:
### 📌 1. Entity-DTO Conversion
Converts between:
- **Transaction entities** ↔ **DTOs**
- **Transaction requests** ↔ **Entities**
- **Transaction entities** ↔ **Response DTOs**


### 📌 2. Holdings Calculation
Processes **transactions** to compute:
- **Current holding quantities**
- **Total cost basis**
- **Profit/Loss metrics**
- **Performance percentages**

### 📌 3. Complex Calculations
Implements **financial computations**:
- **Holding Quantity Determination**  
  ```java
  BigDecimal calculateHoldingQuantity(List<Transaction> transactions) {
      return transactions.stream()
                         .map(Transaction::getQuantity)
                         .reduce(BigDecimal.ZERO, BigDecimal::add);
  }


### 📌 4. Custom Error Handling

- **InsufficientFundsException: Insufficient wallet balance
- **NoSuchWalletException: Invalid wallet reference
- **NoSuchTransactionException: Invalid transaction reference
- **InsufficientQuantityException: Insufficient cryptocurrency for sale

# 📊 Trading212 - Crypto Transaction & Portfolio Management

## 🔹 Profit/Loss Calculation

✔ Uses **FIFO (First In, First Out)** method for cost basis  
✔ Considers **current market prices**  
✔ Computes **percentage gains/losses**  

### 📌 FIFO Calculation Example:
```java
public BigDecimal calculateProfitLoss(List<Transaction> transactions, BigDecimal currentPrice) {
    BigDecimal costBasis = BigDecimal.ZERO;
    BigDecimal totalQuantity = BigDecimal.ZERO;

    for (Transaction txn : transactions) {
        costBasis = costBasis.add(txn.getQuantity().multiply(txn.getPrice()));
        totalQuantity = totalQuantity.add(txn.getQuantity());
    }

    BigDecimal averageCost = costBasis.divide(totalQuantity, 4, RoundingMode.HALF_UP);
    return (currentPrice.subtract(averageCost))
            .divide(averageCost, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
}

Integration Points
Kraken WebSocket Integration

Receives real-time cryptocurrency price updates
Updates current prices for holdings calculations
Ensures accurate portfolio valuation

Performance Considerations
Calculation Optimization

Uses efficient streaming operations for calculations
Implements BigDecimal for precise financial calculations
Maintains transaction ordering for accurate FIFO calculations

Data Consistency

Uses transactional operations for wallet updates
Ensures atomic updates for critical operations
Maintains referential integrity across entities
