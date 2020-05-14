package io.tolar.utils;

import com.google.protobuf.ByteString;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import tolar.proto.Common;
import tolar.proto.tx.TransactionOuterClass;

import java.io.File;
import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class Web3JTests {

    @Test
    public void extractPrivateKey() throws Exception {
        String test = "Hello world!";
        String hextTets = Hex.toHexString(test.getBytes());
        String sha3 = Hash.sha3(hextTets);

        Function function = new Function("greet",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {
                })
        );

        String encodedFunction = FunctionEncoder.encode(function);

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
                .setData("b3de648b0000000000000000000000000000000000000000000000000000000000000001")
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
        File file = new File("/Users/frane/.tolar/keystore/Thin_node/keys/630c1867-9a42-eb26-6488-8dfcbeafd0c9.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("supersifra", file);

        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "544004697faada9c548250b5f497909755a8adcde65492ab1b"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "540dc971237be2361e04c1643d57b572709db15e449a870fef"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(21463)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData("")
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
                .setData("kitula")
                .setNonce(BalanceConverter.toByteString(BigInteger.ZERO))//check nonce if needed
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

        Common.SignatureData signatureTx = Common.SignatureData.newBuilder()
                .setHash(ByteString.copyFromUtf8(hexHash))
                .setSignature(ByteString.copyFromUtf8(signatureField))
                .setSignerId(ByteString.copyFromUtf8(pubKey))
                .build();

        TransactionOuterClass.SignedTransaction signedTransaction =
                TransactionOuterClass.SignedTransaction.newBuilder()
                .setBody(transaction)
                .setSigData(signatureTx)
                .build();
        assertTrue(true);
    }

}