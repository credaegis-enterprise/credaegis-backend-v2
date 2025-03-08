package com.credaegis.backend.configuration.web3;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
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
    public static final String BINARY = "608060405260016002553480156013575f80fd5b506109c5806100215f395ff3fe608060405234801561000f575f80fd5b506004361061003f575f3560e01c8063922f0f331461004357806395df4b401461006c578063a0b9bd291461008c575b5f80fd5b610056610051366004610520565b6100a1565b60405161006391906106a0565b60405180910390f35b61007f61007a366004610520565b6101a1565b6040516100639190610703565b610094610407565b604051610063919061075a565b80516060905f8167ffffffffffffffff8111156100c0576100c06104db565b60405190808252806020026020018201604052801561010557816020015b60408051808201909152606081525f60208201528152602001906001900390816100de5790505b5090505f5b82811015610199575f858281518110610125576101256107b1565b602002602001015190505f808260405161013f91906107c5565b9081526040519081900360200190205460ff161561015b575060015b6040518060400160405280838152602001821515815250848481518110610184576101846107b1565b6020908102919091010152505060010161010a565b509392505050565b80516060905f8167ffffffffffffffff8111156101c0576101c06104db565b60405190808252806020026020018201604052801561020557816020015b60408051808201909152606081525f60208201528152602001906001900390816101de5790505b5090505f5b82811015610199575f858281518110610225576102256107b1565b602002602001015190505f808260405161023f91906107c5565b9081526040519081900360200190205460ff161561025b575060015b80156102b557604051806040016040528088858151811061027e5761027e6107b1565b602002602001015181526020015f15158152508484815181106102a3576102a36107b1565b602002602001018190525050506103ff565b600380546001810182555f919091527fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b016102f0838261085f565b5060015f8360405161030291906107c5565b908152604051908190036020018120805492151560ff1990931692909217909155600254906001906103359085906107c5565b9081526020016040518091039020819055506002547f962fb7a924159dc0966b10da109020686f23e058ede690e3b46058d486099dee88858151811061037d5761037d6107b1565b6020026020010151604051610392919061091a565b60405180910390a260405180604001604052808885815181106103b7576103b76107b1565b60200260200101518152602001600115158152508484815181106103dd576103dd6107b1565b602090810291909101015260028054905f6103f78361096b565b919050555050505b60010161020a565b60606003805480602002602001604051908101604052809291908181526020015f905b828210156104d2578382905f5260205f20018054610447906107db565b80601f0160208091040260200160405190810160405280929190818152602001828054610473906107db565b80156104be5780601f10610495576101008083540402835291602001916104be565b820191905f5260205f20905b8154815290600101906020018083116104a157829003601f168201915b50505050508152602001906001019061042a565b50505050905090565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f1916810167ffffffffffffffff81118282101715610518576105186104db565b604052919050565b5f60208284031215610530575f80fd5b813567ffffffffffffffff811115610546575f80fd5b8201601f81018413610556575f80fd5b803567ffffffffffffffff811115610570576105706104db565b8060051b610580602082016104ef565b9182526020818401810192908101908784111561059b575f80fd5b6020850192505b8383101561063d57823567ffffffffffffffff8111156105c0575f80fd5b8501603f810189136105d0575f80fd5b602081013567ffffffffffffffff8111156105ed576105ed6104db565b610600601f8201601f19166020016104ef565b8181526040838301018b1015610614575f80fd5b816040840160208301375f602083830101528085525050506020820191506020830192506105a2565b979650505050505050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b5f81516040845261068a6040850182610648565b6020938401511515949093019390935250919050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b828110156106f757603f198786030184526106e2858351610676565b945060209384019391909101906001016106c6565b50929695505050505050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b828110156106f757603f19878603018452610745858351610676565b94506020938401939190910190600101610729565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b828110156106f757603f1987860301845261079c858351610648565b94506020938401939190910190600101610780565b634e487b7160e01b5f52603260045260245ffd5b5f82518060208501845e5f920191825250919050565b600181811c908216806107ef57607f821691505b60208210810361080d57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111561085a57805f5260205f20601f840160051c810160208510156108385750805b601f840160051c820191505b81811015610857575f8155600101610844565b50505b505050565b815167ffffffffffffffff811115610879576108796104db565b61088d8161088784546107db565b84610813565b6020601f8211600181146108bf575f83156108a85750848201515b5f19600385901b1c1916600184901b178455610857565b5f84815260208120601f198516915b828110156108ee57878501518255602094850194600190920191016108ce565b508482101561090b57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b604081525f61092c6040830184610648565b8281036020840152601981527f486173682073746f726564207375636365737366756c6c792e0000000000000060208201526040810191505092915050565b5f6001820161098857634e487b7160e01b5f52601160045260245ffd5b506001019056fea2646970667358221220ca824180f58a35c06fa6862bbfb17c5b5b0f783f0ffe05e1153e19b90f514da564736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_GETSTOREDHASHES = "getStoredHashes";

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

    public RemoteFunctionCall<List> getStoredHashes() {
        final Function function = new Function(FUNC_GETSTOREDHASHES,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
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