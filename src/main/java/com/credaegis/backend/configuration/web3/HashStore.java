package com.credaegis.backend.configuration.web3;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.12.3.
 */
@SuppressWarnings("rawtypes")

public class HashStore extends Contract {
    public static final String BINARY = "608060405260016002553480156013575f80fd5b506106ff806100215f395ff3fe608060405234801561000f575f80fd5b5060043610610034575f3560e01c8063922f0f331461003857806395df4b4014610061575b5f80fd5b61004b6100463660046103f0565b610081565b6040516100589190610570565b60405180910390f35b61007461006f3660046103f0565b610181565b60405161005891906105d3565b80516060905f8167ffffffffffffffff8111156100a0576100a06103ab565b6040519080825280602002602001820160405280156100e557816020015b60408051808201909152606081525f60208201528152602001906001900390816100be5790505b5090505f5b82811015610179575f8582815181106101055761010561062a565b602002602001015190505f808260405161011f919061063e565b9081526040519081900360200190205460ff161561013b575060015b60405180604001604052808381526020018215158152508484815181106101645761016461062a565b602090810291909101015250506001016100ea565b509392505050565b80516060905f8167ffffffffffffffff8111156101a0576101a06103ab565b6040519080825280602002602001820160405280156101e557816020015b60408051808201909152606081525f60208201528152602001906001900390816101be5790505b5090505f5b82811015610179575f8582815181106102055761020561062a565b602002602001015190505f808260405161021f919061063e565b9081526040519081900360200190205460ff161561023b575060015b801561029557604051806040016040528088858151811061025e5761025e61062a565b602002602001015181526020015f15158152508484815181106102835761028361062a565b602002602001018190525050506103a3565b60015f836040516102a6919061063e565b908152604051908190036020018120805492151560ff1990931692909217909155600254906001906102d990859061063e565b9081526020016040518091039020819055506002547f962fb7a924159dc0966b10da109020686f23e058ede690e3b46058d486099dee8885815181106103215761032161062a565b60200260200101516040516103369190610654565b60405180910390a2604051806040016040528088858151811061035b5761035b61062a565b60200260200101518152602001600115158152508484815181106103815761038161062a565b602090810291909101015260028054905f61039b836106a5565b919050555050505b6001016101ea565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f1916810167ffffffffffffffff811182821017156103e8576103e86103ab565b604052919050565b5f60208284031215610400575f80fd5b813567ffffffffffffffff811115610416575f80fd5b8201601f81018413610426575f80fd5b803567ffffffffffffffff811115610440576104406103ab565b8060051b610450602082016103bf565b9182526020818401810192908101908784111561046b575f80fd5b6020850192505b8383101561050d57823567ffffffffffffffff811115610490575f80fd5b8501603f810189136104a0575f80fd5b602081013567ffffffffffffffff8111156104bd576104bd6103ab565b6104d0601f8201601f19166020016103bf565b8181526040838301018b10156104e4575f80fd5b816040840160208301375f60208383010152808552505050602082019150602083019250610472565b979650505050505050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b5f81516040845261055a6040850182610518565b6020938401511515949093019390935250919050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b828110156105c757603f198786030184526105b2858351610546565b94506020938401939190910190600101610596565b50929695505050505050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b828110156105c757603f19878603018452610615858351610546565b945060209384019391909101906001016105f9565b634e487b7160e01b5f52603260045260245ffd5b5f82518060208501845e5f920191825250919050565b604081525f6106666040830184610518565b8281036020840152601981527f486173682073746f726564207375636365737366756c6c792e0000000000000060208201526040810191505092915050565b5f600182016106c257634e487b7160e01b5f52601160045260245ffd5b506001019056fea264697066735822122035f2c1fa8ab6e12b11d4c9848effe8b5a4459bd3bf0ad9e83ee3948aab3af1c464736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_STOREHASH = "storeHash";

    public static final String FUNC_VERIFYHASHESBYVALUE = "verifyHashesByValue";

    public static final Event HASHSTORED_EVENT = new Event("HashStored", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected HashStore(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected HashStore(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected HashStore(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected HashStore(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<HashStoredEventResponse> getHashStoredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(HASHSTORED_EVENT, transactionReceipt);
        ArrayList<HashStoredEventResponse> responses = new ArrayList<HashStoredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            HashStoredEventResponse typedResponse = new HashStoredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.hash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static HashStoredEventResponse getHashStoredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(HASHSTORED_EVENT, log);
        HashStoredEventResponse typedResponse = new HashStoredEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.hash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.message = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<HashStoredEventResponse> hashStoredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getHashStoredEventFromLog(log));
    }

    public Flowable<HashStoredEventResponse> hashStoredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(HASHSTORED_EVENT));
        return hashStoredEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> storeHash(List<String> hashes) {
        final Function function = new Function(
                FUNC_STOREHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(hashes, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> verifyHashesByValue(List<String> hashesToVerify) {
        final Function function = new Function(FUNC_VERIFYHASHESBYVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(hashesToVerify, org.web3j.abi.datatypes.Utf8String.class))), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<VerificationResult>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @Deprecated
    public static HashStore load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new HashStore(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static HashStore load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HashStore(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static HashStore load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new HashStore(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static HashStore load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new HashStore(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<HashStore> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(HashStore.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<HashStore> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HashStore.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<HashStore> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(HashStore.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<HashStore> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HashStore.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class StoreResult extends DynamicStruct {
        public String Hash;

        public Boolean stored;

        public StoreResult(String Hash, Boolean stored) {
            super(new org.web3j.abi.datatypes.Utf8String(Hash), 
                    new org.web3j.abi.datatypes.Bool(stored));
            this.Hash = Hash;
            this.stored = stored;
        }

        public StoreResult(Utf8String Hash, Bool stored) {
            super(Hash, stored);
            this.Hash = Hash.getValue();
            this.stored = stored.getValue();
        }
    }

    public static class VerificationResult extends DynamicStruct {
        public String verifiedHashes;

        public Boolean verificationResults;

        public VerificationResult(String verifiedHashes, Boolean verificationResults) {
            super(new org.web3j.abi.datatypes.Utf8String(verifiedHashes), 
                    new org.web3j.abi.datatypes.Bool(verificationResults));
            this.verifiedHashes = verifiedHashes;
            this.verificationResults = verificationResults;
        }

        public VerificationResult(Utf8String verifiedHashes, Bool verificationResults) {
            super(verifiedHashes, verificationResults);
            this.verifiedHashes = verifiedHashes.getValue();
            this.verificationResults = verificationResults.getValue();
        }
    }

    public static class HashStoredEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String hash;

        public String message;
    }
}
