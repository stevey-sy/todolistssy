package com.example.todolistssy.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoManager @Inject constructor() {
    
    companion object {
        private const val KEY_ALIAS = "TodoAppSecretKey"
        private const val KEYSTORE_TYPE = "AndroidKeyStore"
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
    }
    
    private val keyStore = KeyStore.getInstance(KEYSTORE_TYPE).apply {
        load(null)
    }
    
    init {
        createOrGetSecretKey()
    }
    
    private fun createOrGetSecretKey(): SecretKey {
        return if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_TYPE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }
    
    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val secretKey = createOrGetSecretKey()
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        
        return EncryptedData(
            encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT),
            iv = Base64.encodeToString(iv, Base64.DEFAULT)
        )
    }
    
    fun decrypt(encryptedData: EncryptedData): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val secretKey = createOrGetSecretKey()
        
        val iv = Base64.decode(encryptedData.iv, Base64.DEFAULT)
        val encryptedBytes = Base64.decode(encryptedData.encryptedText, Base64.DEFAULT)
        
        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
        
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}

data class EncryptedData(
    val encryptedText: String,
    val iv: String
) 