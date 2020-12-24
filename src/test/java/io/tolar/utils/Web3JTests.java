package io.tolar.utils;

import com.google.protobuf.ByteString;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import tolar.proto.Common;
import tolar.proto.tx.TransactionOuterClass;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class Web3JTests {

    @Test
    public void extractPrivateKey() throws Exception {
        File file = new File("/Users/frane/.tolar/keystore/Thin_node/keys/d334ca47-9ca2-1ab2-7d1f-800ab3953911.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("test2", file);
        String privateKeyHex = credentials.getEcKeyPair().getPrivateKey().toString(16);
        String publicKeyHex = credentials.getEcKeyPair().getPublicKey().toString(16);
    }

    @Test
    public void createTolarAddress() throws Exception {
        File file = new File("/Users/frane/.tolar/keystore/Thin_node/keys/d334ca47-9ca2-1ab2-7d1f-800ab3953911.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("test2", file);

        String prefix = "T";
        String prefixHex = Numeric.toHexStringNoPrefix(prefix.getBytes());
        String address = credentials.getAddress();
        String addressHash = Hash.sha3(address);
        String hashOfHash = Hash.sha3(addressHash);

        String tolarAddress = prefixHex +
                Numeric.cleanHexPrefix(address) +
                hashOfHash.substring(hashOfHash.length() - 8);

        assertEquals("540dc971237be2361e04c1643d57b572709db15e449a870fef",
                tolarAddress);

        String zeroAddress = "0000000000000000000000000000000000000000";
        String tolarZero = prefixHex + zeroAddress +
                Hash.sha3(Hash.sha3(zeroAddress)).substring(hashOfHash.length() - 8);

        assertEquals("54000000000000000000000000000000000000000023199e2b",
                tolarZero);
    }

    @Test
    public void createNewCreds() throws Exception {
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());

        String prefix = "T";
        String prefixHex = Numeric.toHexStringNoPrefix(prefix.getBytes());
        String address = credentials.getAddress();
        String addressHash = Hash.sha3(address);
        String hashOfHash = Hash.sha3(addressHash);

        String tolarAddress = prefixHex +
                Numeric.cleanHexPrefix(address) +
                hashOfHash.substring(hashOfHash.length() - 8);

        System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));
        System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));
        System.out.println(tolarAddress);

        assertTrue(true);
    }

    @Test
    public void exampleHashedTx() throws Exception {
        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "5412c347d6570bcdde3a89fca489f679b8b0ca22a5d4e3b6ca"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "5456a09d5c06e23ec6a71a7db606549ec4bde1788c71a9552b"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(21463)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8("b3de648b0000000000000000000000000000000000000000000000000000000000000001"))
                .setNonce(BalanceConverter.toByteString(BigInteger.ZERO))
                .build();
        ByteString correctByteString = transaction.toByteString();
        String correctProtoString = correctByteString.toStringUtf8();
        byte[] hashed = Hash.sha3(correctByteString.toByteArray());
        String hexHash = Numeric.toHexStringNoPrefix(hashed);
        String hexString = Numeric.toHexStringNoPrefix(correctByteString.toByteArray());

        assertEquals("f2652d9b8b649c86163e3654e731c0e75a7efc92526acf05567f737debf2792b",
                hexHash);
        assertEquals("0a323534313263333437643635373062636464653361383966636134383966363" +
                        "7396238623063613232613564346533623663611232353435366130396435633036653233656336" +
                        "61373161376462363036353439656334626465313738386337316139353532621a0100220253d72" +
                        "a010132486233646536343862303030303030303030303030303030303030303030303030303030" +
                        "303030303030303030303030303030303030303030303030303030303030303030303030313a0100",
                hexString);
        assertEquals("\n25412c347d6570bcdde3a89fca489f679b8b0ca22a5d4e3b6ca\u001225456a" +
                        "09d5c06e23ec6a71a7db606549ec4bde1788c71a9552b\u001A\u0001\u0000\"\u0002S�*\u0001" +
                        "\u00012Hb3de648b0000000000000000000000000000000000000000000000000000000000000001" +
                        ":\u0001\u0000",
                correctProtoString);
    }

    @Test
    public void createProperSignedTx() throws Exception {
        File file = new File("/Users/frane/Documents/Tolar/keys/630c1867-9a42-eb26-6488-8dfcbeafd0c9.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("supersifra", file);
        String tolarAddress = createTolarAddress(credentials);

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "544004697faada9c548250b5f497909755a8adcde65492ab1b"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "540dc971237be2361e04c1643d57b572709db15e449a870fef"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(21463)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8(""))
                .setNonce(BalanceConverter.toByteString(BigInteger.ZERO))
                .build();

        byte[] hashed = Hash.sha3(transaction.toByteString().toByteArray());
        //the hash field in signedTx
        String hexHash = Numeric.toHexStringNoPrefix(hashed);
        //used for verification
        String hexString = Numeric.toHexStringNoPrefix(transaction.toByteString().toByteArray());

        Sign.SignatureData signatureData =
                Sign.signMessage(hashed, credentials.getEcKeyPair(), false);

        byte[] concatSignatureLikeWeb3js = new byte[signatureData.getR().length +
                signatureData.getS().length +
                signatureData.getV().length];

        //this reduces the recId of a signature to be same as tolar (prone to changes)
        signatureData.getV()[0] = (byte) ((int) signatureData.getV()[0] - 27);

        System.arraycopy(signatureData.getR(), 0, concatSignatureLikeWeb3js,
                0,
                signatureData.getR().length);
        System.arraycopy(signatureData.getS(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length,
                signatureData.getS().length);
        System.arraycopy(signatureData.getV(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length + signatureData.getS().length,
                signatureData.getV().length);

        //signature in signedTx
        String signatureField = Numeric.toHexStringNoPrefix(concatSignatureLikeWeb3js);
        //for verification
        String privKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        //sender_id in signedTx
        String pubKey = credentials.getEcKeyPair().getPublicKey().toString(16);
        assertTrue(true);
    }

    @Test
    public void createSignedTxFromCreatedAccount() throws Exception {
        Credentials credentials = Credentials
                .create("d7ce009203c5d16d6b5daafa1efb1167a9e4558e88dff0bc14ebd65f3f0fc116");

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "547ec363f4d32b1fb3c67b8bf91aacf689943e6e87ae4ae600"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "540dc971237be2361e04c1643d57b572709db15e449a870fef"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(21463)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8("kitula"))
                .setNonce(BalanceConverter.toByteString(BigInteger.ZERO))//check nonce if needed
                .build();

        String hexHash = createTxHash(transaction);
        String signature = createSignature(transaction, credentials);
        String signerId = createSignerId(credentials);

        Common.SignatureData signatureTx = Common.SignatureData.newBuilder()
                .setHash(ByteString.copyFromUtf8(hexHash))
                .setSignature(ByteString.copyFromUtf8(signature))
                .setSignerId(ByteString.copyFromUtf8(signerId))
                .build();

        TransactionOuterClass.SignedTransaction signedTransaction =
                TransactionOuterClass.SignedTransaction.newBuilder()
                .setBody(transaction)
                .setSigData(signatureTx)
                .build();
        assertTrue(true);
    }

    @Test
    public void createSignedTxFromCreatedAccountTest() throws Exception {
        Credentials credentials = Credentials
                .create("d7ce009203c5d16d6b5daafa1efb1167a9e4558e88dff0bc14ebd65f3f0fc116");

        String tolarAddress = createTolarAddress(credentials);

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "547ec363f4d32b1fb3c67b8bf91aacf689943e6e87ae4ae600"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "5456a09d5c06e23ec6a71a7db606549ec4bde1788c71a9552b"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(21463)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8(""))
                .setNonce(BalanceConverter.toByteString(BigInteger.ZERO))//check nonce if needed
                .build();

        byte[] hashed = Hash.sha3(transaction.toByteString().toByteArray());
        System.out.println(Arrays.toString(hashed));
        String test = new String(hashed);
        String hexHash = createTxHash(transaction);
        byte[] bytes = Numeric.hexStringToByteArray(hexHash);
        String signature = createSignature(transaction, credentials);
        String signerId = createSignerId(credentials);

        Common.SignatureData signatureTx = Common.SignatureData.newBuilder()
                .setHash(ByteString.copyFromUtf8(hexHash))
                .setSignature(ByteString.copyFromUtf8(signature))
                .setSignerId(ByteString.copyFromUtf8(signerId))
                .build();

        TransactionOuterClass.SignedTransaction signedTransaction =
                TransactionOuterClass.SignedTransaction.newBuilder()
                        .setBody(transaction)
                        .setSigData(signatureTx)
                        .build();
        assertTrue(true);
    }

    @Test
    public void createSignedTxFromCreatedAccountTest_deb() throws Exception {
        //File file = new File("/Users/frane/Documents/Tolar/keys/d90f9e3d-9b1c-cd85-99b7-5161379c97b1.json");

        //assertTrue(file.exists());
        Credentials credentials = Credentials.create("78e36e1756542e69eed2fe60b5a4a788e20f519bfac38d428275e868f9d84baa");//WalletUtils.loadCredentials("supersifra", file);
        String tolarAddress = createTolarAddress(credentials);
        assertEquals("5493b8597964a2a7f0c93c49f9e4c4a170e0c42a5eb3beda0d", tolarAddress);

        BigInteger pare = new BigInteger("100");

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "5493b8597964a2a7f0c93c49f9e4c4a170e0c42a5eb3beda0d"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "5408fc50bf0ae1a9d842507271a308b653c368af367d41ba4c"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(24000)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8("something other happened here!"))
                .setNonce(BalanceConverter.toByteString(BigInteger.valueOf(25)))//check nonce if needed
                .build();

        String privKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        String hexHash = createTxHash(transaction);
        String signature = createSignature(transaction, credentials);
        String signerId = createSignerId(credentials);

        Common.SignatureData signatureTx = Common.SignatureData.newBuilder()
                .setHash(ByteString.copyFromUtf8(hexHash))
                .setSignature(ByteString.copyFromUtf8(signature))
                .setSignerId(ByteString.copyFromUtf8(signerId))
                .build();

        TransactionOuterClass.SignedTransaction signedTransaction =
                TransactionOuterClass.SignedTransaction.newBuilder()
                        .setBody(transaction)
                        .setSigData(signatureTx)
                        .build();



        assertTrue(true);
    }

    @Test
    public void deployContact() throws Exception {
        String deployData = "608060405234801561001057600080fd5b5060408051808201909152600c8082527f48656c6c6" +
                "f20776f726c642100000000000000000000000000000000000000006020909201918252610055916000916100" +
                "5b565b506100f6565b828054600181600116156101000203166002900490600052602060002090601f0160209" +
                "00481019282601f1061009c57805160ff19168380011785556100c9565b828001600101855582156100c95791" +
                "82015b828111156100c95782518255916020019190600101906100ae565b506100d59291506100d9565b50905" +
                "65b6100f391905b808211156100d557600081556001016100df565b90565b6102a7806101056000396000f300" +
                "60806040526004361061004b5763ffffffff7c010000000000000000000000000000000000000000000000000" +
                "00000006000350416639698086b8114610050578063cfae3217146100ab575b600080fd5b34801561005c5760" +
                "0080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100a99436949" +
                "293602493928401919081908401838280828437509497506101359650505050505050565b005b3480156100b7" +
                "57600080fd5b506100c061014c565b60408051602080825283518183015283519192839290830191850190808" +
                "38360005b838110156100fa5781810151838201526020016100e2565b50505050905090810190601f16801561" +
                "01275780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b805" +
                "16101489060009060208401906101e3565b5050565b60008054604080516020601f6002600019610100600188" +
                "1615020190951694909404938401819004810282018101909252828152606093909290918301828280156101d" +
                "85780601f106101ad576101008083540402835291602001916101d8565b820191906000526020600020905b81" +
                "54815290600101906020018083116101bb57829003601f168201915b505050505090505b90565b82805460018" +
                "1600116156101000203166002900490600052602060002090601f016020900481019282601f10610224578051" +
                "60ff1916838001178555610251565b82800160010185558215610251579182015b82811115610251578251825" +
                "591602001919060010190610236565b5061025d929150610261565b5090565b6101e091905b8082111561025d" +
                "57600081556001016102675600a165627a7a72305820add192b2a76849720e9c4b9bce9b99169437d07653e6b" +
                "0abbca3bc2c32aa4bcd0029";

        File file = new File("/Users/frane/.tolar/keystore/Thin_node/keys/d334ca47-9ca2-1ab2-7d1f-800ab3953911.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("test2", file);

        String sender = createTolarAddress(credentials);
        String zeroAddress = "54000000000000000000000000000000000000000023199e2b";
        int gasLimit = 4700000;

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(sender))
                .setReceiverAddress(ByteString.copyFromUtf8(zeroAddress))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(gasLimit)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8(deployData))
                .setNonce(BalanceConverter.toByteString(BigInteger.valueOf(2)))//check nonce if needed
                .build();


    }

    @Test
    public void stagingContractSolidity4() {
        String data = "0x608060405234801561001057600080fd5b506040805190810160405280600c81526020017f48656c6c6" +
                "f20776f726c642100000000000000000000000000000000000000008152506000908051906020019061005c9291" +
                "90610062565b50610107565b828054600181600116156101000203166002900490600052602060002090601f016" +
                "020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d157" +
                "9182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5" +
                "090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b6102d78061" +
                "01166000396000f30060806040526004361061004c576000357c010000000000000000000000000000000000000" +
                "0000000000000000000900463ffffffff1680639698086b14610051578063cfae3217146100ba575b600080fd5b" +
                "34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f0" +
                "1602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091" +
                "9291929050505061014a565b005b3480156100c657600080fd5b506100cf610164565b604051808060200182810" +
                "3825283818151815260200191508051906020019080838360005b8381101561010f578082015181840152602081" +
                "0190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a0319168" +
                "15260200191505b509250505060405180910390f35b8060009080519060200190610160929190610206565b5050" +
                "565b606060008054600181600116156101000203166002900480601f01602080910402602001604051908101604" +
                "05280929190818152602001828054600181600116156101000203166002900480156101fc5780601f106101d157" +
                "6101008083540402835291602001916101fc565b820191906000526020600020905b81548152906001019060200" +
                "18083116101df57829003601f168201915b5050505050905090565b828054600181600116156101000203166002" +
                "900490600052602060002090601f016020900481019282601f1061024757805160ff19168380011785556102755" +
                "65b82800160010185558215610275579182015b8281111561027457825182559160200191906001019061025956" +
                "5b5b5090506102829190610286565b5090565b6102a891905b808211156102a4576000816000905550600101610" +
                "28c565b5090565b905600a165627a7a72305820a2a5a81298e37becfaa27e9a786121e953a8ecc46cef0cad5c39" +
                "8cb22278370a0029";

        Credentials credentials = Credentials.create("78e36e1756542e69eed2fe60b5a4a788e20f519bfac38d428275e868f9d84baa");
        String tolarAddress = createTolarAddress(credentials);
        String zeroAddress = "54000000000000000000000000000000000000000023199e2b";
        int gasLimit = 4700000;

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(tolarAddress))
                .setReceiverAddress(ByteString.copyFromUtf8(zeroAddress))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(gasLimit)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8(data))
                .setNonce(BalanceConverter.toByteString(BigInteger.valueOf(23)))//check nonce if needed
                .build();

        String txHash = createTxHash(transaction);
        String signature = createSignature(transaction, credentials);
        String signerId = createSignerId(credentials);
        assertTrue(true);
    }

    @Test
    public void stagingContractSolidity6() {
        String data = "608060405234801561001057600080fd5b506040518060400160405280600c81526020017f48656c6c6f20776f726c642100000000000000000000000000000000000000008152506000908051906020019061005c929190610062565b506100ff565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b5b808211156100fb5760008160009055506001016100e3565b5090565b6103088061010e6000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80639698086b1461003b578063cfae3217146100f6575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610179565b005b6100fe610193565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561013e578082015181840152602081019050610123565b50505050905090810190601f16801561016b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b806000908051906020019061018f929190610235565b5050565b606060008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561022b5780601f106102005761010080835404028352916020019161022b565b820191906000526020600020905b81548152906001019060200180831161020e57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061027657805160ff19168380011785556102a4565b828001600101855582156102a4579182015b828111156102a3578251825591602001919060010190610288565b5b5090506102b191906102b5565b5090565b5b808211156102ce5760008160009055506001016102b6565b509056fea26469706673582212201dd0b541c76e4234b37a99932323ccdaf91cd84ce6418e6486159c15d373b61d64736f6c634300060c0033";

        Credentials credentials = Credentials.create("78e36e1756542e69eed2fe60b5a4a788e20f519bfac38d428275e868f9d84baa");
        String tolarAddress = createTolarAddress(credentials);
        String zeroAddress = "54000000000000000000000000000000000000000023199e2b";
        int gasLimit = 4700000;

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(tolarAddress))
                .setReceiverAddress(ByteString.copyFromUtf8(zeroAddress))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(gasLimit)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData(ByteString.copyFromUtf8(data))
                .setNonce(BalanceConverter.toByteString(BigInteger.valueOf(27)))//check nonce if needed
                .build();

        String txHash = createTxHash(transaction);
        String signature = createSignature(transaction, credentials);
        String signerId = createSignerId(credentials);
        assertTrue(true);
    }

    private String createTolarAddress(Credentials credentials) {
        String prefix = "T";
        String prefixHex = Numeric.toHexStringNoPrefix(prefix.getBytes());
        String address = credentials.getAddress();
        String addressHash = Hash.sha3(address);
        String hashOfHash = Hash.sha3(addressHash);

        return prefixHex +
                Numeric.cleanHexPrefix(address) +
                hashOfHash.substring(hashOfHash.length() - 8);
    }

    private String createTxHash(TransactionOuterClass.Transaction tx) {
        byte[] hashed = Hash.sha3(tx.toByteString().toByteArray());
        return Numeric.toHexStringNoPrefix(hashed);
    }

    private String createSignature(TransactionOuterClass.Transaction tx, Credentials creds) {
        byte[] hashed = Hash.sha3(tx.toByteString().toByteArray());

        Sign.SignatureData signatureData =
                Sign.signMessage(hashed, creds.getEcKeyPair(), false);

        Sign.SignatureData withPrefix = Sign.signPrefixedMessage(hashed, creds.getEcKeyPair());

        byte[] concatSignatureLikeWeb3js = new byte[signatureData.getR().length +
                signatureData.getS().length +
                signatureData.getV().length];

        String rHex = Numeric.toHexStringNoPrefix(signatureData.getR());
        String sHex = Numeric.toHexStringNoPrefix(signatureData.getS());

        String rHexPrefix = Numeric.toHexStringNoPrefix(withPrefix.getR());
        String sHexPrefix = Numeric.toHexStringNoPrefix(withPrefix.getS());

        //this reduces the recId of a signature to be same as tolar (prone to changes)
        signatureData.getV()[0] = (byte) ((int) signatureData.getV()[0] - 27);

        System.arraycopy(signatureData.getR(), 0, concatSignatureLikeWeb3js,
                0, signatureData.getR().length);
        System.arraycopy(signatureData.getS(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length, signatureData.getS().length);
        System.arraycopy(signatureData.getV(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length + signatureData.getS().length,
                signatureData.getV().length);

        return Numeric.toHexStringNoPrefix(concatSignatureLikeWeb3js);
    }

    private String createSignerId(Credentials credentials) {
        return credentials.getEcKeyPair().getPublicKey().toString(16);
    }

}