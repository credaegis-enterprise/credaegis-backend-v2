package com.credaegis.backend.utility;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Web3Utility {


    public static BigDecimal covertToAVAX(TransactionReceipt transactionReceipt) {
        BigInteger effectiveGasPrice = new BigInteger(transactionReceipt.getEffectiveGasPrice().substring(2),16);
        BigInteger gasUsed = transactionReceipt.getGasUsed();
        BigInteger totalCost = effectiveGasPrice.multiply(gasUsed);
        BigDecimal totalFeeAvax = new BigDecimal(totalCost).divide(BigDecimal.TEN.pow(18));
        return totalFeeAvax;
    }
}
