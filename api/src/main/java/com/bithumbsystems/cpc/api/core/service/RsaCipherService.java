package com.bithumbsystems.cpc.api.core.service;

import com.bithumbsystems.persistence.mongodb.cipher.entity.RsaCipherInfo;
import com.bithumbsystems.persistence.mongodb.cipher.service.RsaCipherInfoDomainService;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RsaCipherService {
  private final RsaCipherInfoDomainService rsaCipherInfoDomainService;

  private final static String RSA_ALGORITHM = "RSA";
  private static String RSA_CIPHER_KEY = "rsa-cipher-key";

  private PublicKey getPublicKeyFromBase64Encrypted(String base64PublicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);

    return KeyFactory.getInstance(RSA_ALGORITHM)
        .generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
  }

  private PrivateKey getPrivateKeyFromBase64Encrypted(String base64PrivateKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);

    return KeyFactory.getInstance(RSA_ALGORITHM)
        .generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
  }


  public String encryptRSA(String plainText, String publicKey) {
    String encryptedText = "";

    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKeyFromBase64Encrypted(publicKey));

      byte[] bytePlain = cipher.doFinal(plainText.getBytes());
      encryptedText = Base64.getEncoder().encodeToString(bytePlain);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return encryptedText;
  }

  public String decryptRSA(String encryptedText, String privateKey) {
    String plainText = "";

    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKeyFromBase64Encrypted(privateKey));
      byte[] byteEncrypted = Base64.getDecoder().decode(encryptedText.getBytes());

      byte[] bytePlain = cipher.doFinal(byteEncrypted);
      plainText = new String(bytePlain, StandardCharsets.UTF_8);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return plainText;
  }

  public Mono<String> getRsaPublicKey() {
    return rsaCipherInfoDomainService.findById(RSA_CIPHER_KEY)
        .map(RsaCipherInfo::getServerPublicKey);
  }

  public Mono<String> getRsaPrivateKey() {
    return rsaCipherInfoDomainService.findById(RSA_CIPHER_KEY)
        .map(RsaCipherInfo::getServerPrivateKey);
  }
}
