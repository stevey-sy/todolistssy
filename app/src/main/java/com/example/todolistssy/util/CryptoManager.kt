package com.example.todolistssy.util

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

/**
 * Android Keystore를 사용한 AES-256-GCM 암호화 관리자
 * - 할 일 데이터를 안전하게 암호화/복호화
 * - 하드웨어 보안 모듈 활용으로 키 보호
 * - 각 암호화마다 고유한 IV 생성
 */
@Singleton
class CryptoManager @Inject constructor() {
    
    companion object {
        // Android Keystore에서 사용할 키 별칭
        private const val KEY_ALIAS = "TodoAppSecretKey"
        // Android Keystore 타입
        private const val KEYSTORE_TYPE = "AndroidKeyStore"
        // AES-256-GCM 암호화 방식
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        // GCM 모드 IV 길이 (12바이트)
        private const val GCM_IV_LENGTH = 12
        // GCM 인증 태그 길이 (16바이트)
        private const val GCM_TAG_LENGTH = 16
    }
    
    // Android Keystore 인스턴스 초기화
    private val keyStore = KeyStore.getInstance(KEYSTORE_TYPE).apply {
        load(null)
    }
    
    init {
        // 앱 시작 시 암호화 키 생성 또는 로드
        createOrGetSecretKey()
    }
    
    /**
     * 암호화 키 생성 또는 기존 키 로드
     * - 이미 키가 존재하면 기존 키 사용
     * - 없으면 새로운 AES-256 키 생성
     */
    private fun createOrGetSecretKey(): SecretKey {
        return if (keyStore.containsAlias(KEY_ALIAS)) {
            // 기존 키 로드
            keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } else {
            // 새로운 키 생성
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_TYPE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256) // 256-bit 키 크기
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }
    
    /**
     * 텍스트를 AES-256-GCM으로 암호화
     * @param plainText 암호화할 평문
     * @return 암호화된 텍스트와 IV를 포함한 EncryptedData
     */
    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val secretKey = createOrGetSecretKey()
        
        // 암호화 모드로 초기화 (IV 자동 생성)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        
        val iv = cipher.iv // 생성된 IV 획득
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        
        return EncryptedData(
            encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT),
            iv = Base64.encodeToString(iv, Base64.DEFAULT)
        )
    }
    
    /**
     * 암호화된 데이터를 복호화하여 원본 텍스트로 변환
     * @param encryptedData 암호화된 텍스트와 IV
     * @return 복호화된 원본 텍스트
     */
    fun decrypt(encryptedData: EncryptedData): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val secretKey = createOrGetSecretKey()
        
        // Base64 디코딩
        val iv = Base64.decode(encryptedData.iv, Base64.DEFAULT)
        val encryptedBytes = Base64.decode(encryptedData.encryptedText, Base64.DEFAULT)
        
        // GCM 파라미터 설정 (IV 사용)
        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
        
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}

/**
 * 암호화된 데이터 모델
 * @param encryptedText Base64 인코딩된 암호화 텍스트
 * @param iv Base64 인코딩된 초기화 벡터
 */
data class EncryptedData(
    val encryptedText: String,
    val iv: String
)