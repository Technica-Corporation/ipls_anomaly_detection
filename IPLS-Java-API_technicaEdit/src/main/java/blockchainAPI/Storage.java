package blockchainAPI;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.6.1.
 */
@SuppressWarnings("rawtypes")
public class Storage extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610958806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c806368d54aa714610051578063b2f9fc951461016a578063ef2ea1b014610172578063f641090c14610216575b600080fd5b6100f56004803603602081101561006757600080fd5b810190602081018135600160201b81111561008157600080fd5b82018360208201111561009357600080fd5b803590602001918460018302840111600160201b831117156100b457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610341945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561012f578181015183820152602001610117565b50505050905090810190601f16801561015c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6100f56103e7565b6100f56004803603602081101561018857600080fd5b810190602081018135600160201b8111156101a257600080fd5b8201836020820111156101b457600080fd5b803590602001918460018302840111600160201b831117156101d557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610656945050505050565b61033f6004803603604081101561022c57600080fd5b810190602081018135600160201b81111561024657600080fd5b82018360208201111561025857600080fd5b803590602001918460018302840111600160201b8311171561027957600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156102cb57600080fd5b8201836020820111156102dd57600080fd5b803590602001918460018302840111600160201b831117156102fe57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061074a945050505050565b005b80516020818301810180516002808352938301948301949094209390528254604080516001831615610100026000190190921693909304601f81018390048302820183019093528281529291908301828280156103df5780601f106103b4576101008083540402835291602001916103df565b820191906000526020600020905b8154815290600101906020018083116103c257829003601f168201915b505050505081565b60608060018054905060405190808252806020026020018201604052801561042357816020015b606081526020019060019003908161040e5790505b5060408051602081019091526000808252919250905b60015460ff8216101561064e5760018160ff168154811061045657fe5b600091825260209182902001805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156104e45780601f106104b9576101008083540402835291602001916104e4565b820191906000526020600020905b8154815290600101906020018083116104c757829003601f168201915b5050505050838260ff16815181106104f857fe5b602002602001018190525081838260ff168151811061051357fe5b60200260200101516040516020018083805190602001908083835b6020831061054d5780518252601f19909201916020918201910161052e565b51815160209384036101000a600019018019909216911617905285519190930192850191508083835b602083106105955780518252601f199092019160209182019101610576565b6001836020036101000a038019825116818451168082178552505050505050905001925050506040516020818303038152906040529150816040516020018082805190602001908083835b602083106105ff5780518252601f1990920191602091820191016105e0565b6001836020036101000a03801982511681845116808217855250505050505090500180600160fd1b81525060010191505060405160208183030381529060405291508080600101915050610439565b509150505b90565b60606002826040518082805190602001908083835b6020831061068a5780518252601f19909201916020918201910161066b565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f600260018316159098029095011695909504928301829004820288018201905281875292945092505083018282801561073e5780601f106107135761010080835404028352916020019161073e565b820191906000526020600020905b81548152906001019060200180831161072157829003601f168201915b50505050509050919050565b6002826040518082805190602001908083835b6020831061077c5780518252601f19909201916020918201910161075d565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900490506000141561088757806002836040518082805190602001908083835b602083106107ff5780518252601f1990920191602091820191016107e0565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381019093208451610840959194919091019250905061088b565b50600180548082018083556000929092528351610884917fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60190602086019061088b565b50505b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106108cc57805160ff19168380011785556108f9565b828001600101855582156108f9579182015b828111156108f95782518255916020019190600101906108de565b50610905929150610909565b5090565b61065391905b80821115610905576000815560010161090f56fea265627a7a72315820d5e90c28c0507ffc99f7f41631058b3c358d2cdfcc64da48e4662e01b4b3cfb464736f6c63430005100032";

    public static final String FUNC_KEY = "key";

    public static final String FUNC_RETRIEVEHASH = "retrieveHash";

    public static final String FUNC_RETRIEVEPROJECTNAMES = "retrieveProjectNames";

    public static final String FUNC_STORE = "store";

    @Deprecated
    protected Storage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Storage(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Storage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Storage(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> key(String param0) {
        final Function function = new Function(FUNC_KEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> retrieveHash(String Pkeyname) {
        final Function function = new Function(FUNC_RETRIEVEHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(Pkeyname)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> retrieveProjectNames() {
        final Function function = new Function(FUNC_RETRIEVEPROJECTNAMES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> store(String Pkeyname, String hash) {
        final Function function = new Function(
                FUNC_STORE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(Pkeyname), 
                new org.web3j.abi.datatypes.Utf8String(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Storage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Storage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Storage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Storage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Storage load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Storage(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Storage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Storage(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Storage> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Storage.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Storage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Storage.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Storage> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Storage.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Storage> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Storage.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
