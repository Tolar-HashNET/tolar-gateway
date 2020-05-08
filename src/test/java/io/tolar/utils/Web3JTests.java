package io.tolar.utils;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.*;

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
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.ONE, //nonce
                BigInteger.valueOf(21000L), //gasPrice
                BigInteger.ONE, //gasLimit
                "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb", //toAddress -> tolar address
                BigInteger.ZERO, //value
                ""//data (empty for now)
        );

        //from (tolar address): 540dc971237be2361e04c1643d57b572709db15e449a870fef
        //from ethereum: T (54) + 0dc971237be2361e04c1643d57b572709db15e44 + checksum (9a870fef)
        //to (tolar address): 5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb

        //todo: need hash, signature (sign data), signed_id (maybe address? or private key?)


        //todo: convert signature data tu signature!
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        Sign.SignatureData signatureData =
                Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        byte[] hashedMessage = Hash.sha3(encodedTransaction);

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

        //web3.js uses concat (r, s, v) for signature, maybe this is the case?

        //if it doesnt work, remove the last byte! (recover id)

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

        //todo: try to send a regular transaction then to send the signed one!!
    }

}