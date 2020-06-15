package io.tolar.api;

import com.google.protobuf.ByteString;
import io.tolar.config.TolarConfig;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import tolar.proto.Blockchain;
import tolar.proto.Common;
import tolar.proto.tx.TransactionOuterClass;

import java.math.BigInteger;
import java.util.Arrays;

@Ignore("manual")
public class TolarApiImplTest extends TestCase {

    @Test
    public void testGetTransaction() {
        TolarConfig config = new TolarConfig();
        config.setHost("116.203.236.134");
        config.setPort(9200);
        TolarApiImpl tolarApi = new TolarApiImpl(new ChannelUtils(config));

        //Blockchain.GetTransactionResponse transaction = tolarApi.getTransaction(ByteString.EMPTY);
        Blockchain.GetTransactionResponse where = tolarApi.getTransaction(ByteString.copyFromUtf8(
                "4a67850a409d231226121365ebb47fc6557ec42551f5a359309d989b1a7f9999"));

        Assert.assertNull(where);
    }

    public void sendTransactionCheckStatus() {

        TolarConfig config = new TolarConfig();
        config.setHost("116.203.236.134");
        config.setPort(9200);
        TolarApiImpl tolarApi = new TolarApiImpl(new ChannelUtils(config));
        TransactionApi api = new TransactionApiImpl(new ChannelUtils(config));

        //Blockchain.GetTransactionResponse transaction = tolarApi.getTransaction(ByteString.EMPTY);
        Blockchain.GetTransactionResponse where = tolarApi.getTransaction(ByteString.copyFromUtf8(
                "4a67850a409d231226121365ebb47fc6557ec42551f5a359309d989b1a7f9999"));
        Credentials credentials = Credentials.create(
                "78e36e1756542e69eed2fe60b5a4a788e20f519bfac38d428275e868f9d84baa");
        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(ByteString.copyFromUtf8(
                        createTolarAddress(credentials)))
                .setReceiverAddress(ByteString.copyFromUtf8(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb"))
                .setValue(BalanceConverter.toByteString(BigInteger.ZERO))
                .setGas(BalanceConverter.toByteString(BigInteger.valueOf(240000)))
                .setGasPrice(BalanceConverter.toByteString(BigInteger.ONE))
                .setData("test")
                .setNonce(BalanceConverter.toByteString(BigInteger.valueOf(59)))
                .build();

        String privKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        byte[] hashed = Hash.sha3(transaction.toByteString().toByteArray());
        System.out.println(Arrays.toString(hashed));
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

        ByteString bytes = api.sendSignedTransaction(signedTransaction);
        // jesi siguran. ne.
        Blockchain.GetTransactionResponse transaction1 = tolarApi.getTransaction(bytes);

        assertNotNull(transaction1);
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