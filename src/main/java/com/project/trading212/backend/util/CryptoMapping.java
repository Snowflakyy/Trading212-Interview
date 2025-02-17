package com.project.trading212.backend.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CryptoMapping {
    private static final Map<String,String> symbolToName = new HashMap<>();

    static{
        symbolToName.put("BTC/USD", "Bitcoin");
        symbolToName.put("WBTC/USD", "Wrapped Bitcoin");
        symbolToName.put("tBTC/USD", "tBitcoin");
        symbolToName.put("YFI/USD", "Yearn Finance");
        symbolToName.put("PAXG/USD", "PAX Gold");
        symbolToName.put("ETH/USD", "Ethereum");
        symbolToName.put("MKR/USD", "Maker");
        symbolToName.put("TAO/USD", "Tao");
        symbolToName.put("BCH/USD", "Bitcoin Cash");
        symbolToName.put("AAVE/USD", "Aave");
        symbolToName.put("MSOL/USD", "Marinade Staked SOL");
        symbolToName.put("XMR/USD", "Monero");
        symbolToName.put("SOL/USD", "Solana");
        symbolToName.put("GNO/USD", "Gnosis");
        symbolToName.put("LTC/USD", "Litecoin");
        symbolToName.put("QNT/USD", "Quant");
        symbolToName.put("COMP/USD", "Compound");
        symbolToName.put("FARM/USD", "Harvest Finance");
        symbolToName.put("ZEC/USD", "Zcash");
    }

    public static String getFullName(String symbol) {
        return symbolToName.getOrDefault(symbol,symbol);
    }

    public static Set<String> getAllSymbols() {
        return symbolToName.keySet();
    }
}
