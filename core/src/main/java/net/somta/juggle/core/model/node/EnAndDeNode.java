package net.somta.juggle.core.model.node;

public class EnAndDeNode extends FlowNode {
    private String encrypt_type;//加密类型
    private String decrypt_type;//解密类型
    private String en_decrypt;//加解密
    private String public_key;//公钥
    private String private_key;//私钥
    private String countersign;//是否加签
    private String encrypt_code;//加密字段
    private String decrypt_code;//解密字段
    private String sign_code;//加签字段
    private String secret;//密钥

    public String getEncrypt_type() {
        return encrypt_type;
    }

    public void setEncrypt_type(String encrypt_type) {
        this.encrypt_type = encrypt_type;
    }

    public String getDecrypt_type() {
        return decrypt_type;
    }

    public void setDecrypt_type(String decrypt_type) {
        this.decrypt_type = decrypt_type;
    }

    public String getEn_decrypt() {
        return en_decrypt;
    }

    public void setEn_decrypt(String en_decrypt) {
        this.en_decrypt = en_decrypt;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getCountersign() {
        return countersign;
    }

    public void setCountersign(String countersign) {
        this.countersign = countersign;
    }

    public String getEncrypt_code() {
        return encrypt_code;
    }

    public void setEncrypt_code(String encrypt_code) {
        this.encrypt_code = encrypt_code;
    }

    public String getDecrypt_code() {
        return decrypt_code;
    }

    public void setDecrypt_code(String decrypt_code) {
        this.decrypt_code = decrypt_code;
    }

    public String getSign_code() {
        return sign_code;
    }

    public void setSign_code(String sign_code) {
        this.sign_code = sign_code;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
