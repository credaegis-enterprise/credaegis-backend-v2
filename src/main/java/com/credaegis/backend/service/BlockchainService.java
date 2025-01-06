package com.credaegis.backend.service;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class BlockchainService {


}


//const Web3 = require('web3');
//
//// Set up Web3 instance with Avalanche C-Chain RPC URL
//const web3 = new Web3('https://api.avax-test.network/ext/bc/C/rpc'); // Fuji Testnet RPC URL
//
//
//// Specify the address to check the balance of
//const address = '0x34DaF71a339BE6774AeB83eb2cea8AC5A35c1461'; // Replace with your address
//        const transactionHash = '0xd44d3955d3b1b4fe5536555c15287f26a7229791d6447262139f06f4e0f25b6e';
//
//// Check if connected to the network
//        web3.eth.net.isListening()
//    .then(() => {
//        console.log('Successfully connected to Avalanche C-Chain');
//    })
//            .catch(console.error);
//
//// Function to get the balance
//async function getBalance() {
//    try {
//        // Get the balance of the address (in Wei)
//        const balance = await web3.eth.getBalance(address);
//        console.log(`Balance in Wei: ${balance}`);
//
//        // Convert balance from Wei to AVAX (1 AVAX = 10^18 Wei)
//        const balanceInAVAX = web3.utils.fromWei(balance, 'ether');
//
//        console.log(`Balance of ${address}: ${parseFloat(balanceInAVAX).toFixed(6)} AVAX`);
//
//    } catch (error) {
//        console.error('Error fetching balance:', error);
//    }
//}
//
//async function getTransactionCost() {
//    try {
//        // Get the transaction receipt
//        const receipt = await web3.eth.getTransactionReceipt(transactionHash);
//
//        if (!receipt) {
//            console.log("Transaction not found or still pending.");
//            return;
//        }
//
//        // Get the gas used for the transaction
//        const gasUsed = receipt.gasUsed;
//
//        // Get the current gas price
//        const gasPrice = await web3.eth.getGasPrice(); // Gas price in Wei
//
//        // Calculate the cost in Wei (gasUsed * gasPrice)
//        const costInWei = gasUsed * gasPrice;
//
//        // Convert the cost from Wei to AVAX (1 AVAX = 10^18 Wei)
//        const costInAVAX = web3.utils.fromWei(costInWei.toString(), 'ether');
//
//        console.log(`Transaction Cost for ${transactionHash}: ${costInAVAX} AVAX`);
//    } catch (error) {
//        console.error('Error fetching transaction cost:', error);
//    }
//}
//
//// Call the functions
//getBalance();
//getTransactionCost();