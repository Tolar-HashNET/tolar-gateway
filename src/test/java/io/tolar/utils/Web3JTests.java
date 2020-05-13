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
import tolar.proto.tx.TransactionOuterClass;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class Web3JTests {

    @Test
    public void extractPrivateKey() throws IOException, CipherException {
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
    public void signMessage() throws IOException, CipherException {
        File file = new File("/Users/frane/.tolar/keystore/Thin_node/keys/d334ca47-9ca2-1ab2-7d1f-800ab3953911.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("test2", file);
        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        "540dc971237be2361e04c1643d57b572709db15e449a870fef"))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(21463)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData("b3de648b0000000000000000000000000000000000000000000000000000000000000001")
                .setNonce(BalanceConverter.toByteString(BigInteger.ONE))
                .build();//finish the transaction
        String correctProtoString = transaction.toByteString().toStringUtf8();

        byte[] txHash = Hash.sha3(correctProtoString.getBytes()); //this is the hash field
        String toHexString = Numeric.toHexString(txHash);

        //from (tolar address): 540dc971237be2361e04c1643d57b572709db15e449a870fef
        //from ethereum: T (54) + 0dc971237be2361e04c1643d57b572709db15e44 + checksum (9a870fef)
        //to (tolar address): 5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb

        Sign.SignatureData signatureData =
                Sign.signMessage(txHash, credentials.getEcKeyPair(), false);

        byte[] concatSignatureLikeWeb3js = new byte[signatureData.getR().length +
                signatureData.getS().length +
                signatureData.getV().length];

        System.arraycopy(signatureData.getR(), 0, concatSignatureLikeWeb3js,
                0,
                signatureData.getR().length);
        System.arraycopy(signatureData.getS(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length,
                signatureData.getS().length);
        System.arraycopy(signatureData.getV(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length + signatureData.getS().length,
                signatureData.getV().length);

        String notHexSignature = new String(concatSignatureLikeWeb3js);
        String possibleHexSignature = Numeric.toHexString(concatSignatureLikeWeb3js); //signature?
        String signerId = credentials.getEcKeyPair().getPublicKey().toString(16);
        String notHexSignerId = credentials.getEcKeyPair().getPublicKey().toString();

        //web3.js uses concat (r, s, v) for signature, maybe this is the case?
        //if it doesnt work, remove the last byte! (recover id)

        assertTrue(true);
    }

    @Test
    public void createProperSignedTx() throws Exception {
        File file = new File("/Users/frane/.tolar/keystore/Thin_node/keys/da13bcb1-fb8d-3cbc-9ab0-07d9304366df.json");

        assertTrue(file.exists());
        Credentials credentials = WalletUtils.loadCredentials("test3", file);

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
        String hexHash = Numeric.toHexStringNoPrefix(hashed);
        String hexString = Numeric.toHexStringNoPrefix(transaction.toByteString().toByteArray());

        Sign.SignatureData signatureData =
                Sign.signMessage(hashed, credentials.getEcKeyPair(), false);

        byte[] concatSignatureLikeWeb3js = new byte[signatureData.getR().length +
                signatureData.getS().length +
                signatureData.getV().length];

        System.arraycopy(signatureData.getR(), 0, concatSignatureLikeWeb3js,
                0,
                signatureData.getR().length);
        System.arraycopy(signatureData.getS(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length,
                signatureData.getS().length);
        System.arraycopy(signatureData.getV(), 0, concatSignatureLikeWeb3js,
                signatureData.getR().length + signatureData.getS().length,
                signatureData.getV().length);

        String shouldBeSignature = Numeric.toHexStringNoPrefix(concatSignatureLikeWeb3js);
        String privKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        String pubKey = credentials.getEcKeyPair().getPublicKey().toString(16);
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

}