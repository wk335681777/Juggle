package net.somta.juggle.core.model.node;

public class YingDaEncryptNode  extends FlowNode{
    private String sm2PrivateKey;//sm2密钥
    private String sm2PublicKey;//sm2公钥
    private String encodingType;//加密数据格式
    private String sm4EncryptType;//sm4加密模式
    private String encryptMode;//加密类型

    public String getSm2PrivateKey() {
        return sm2PrivateKey;
    }

    public void setSm2PrivateKey(String sm2PrivateKey) {
        this.sm2PrivateKey = sm2PrivateKey;
    }

    public String getSm2PublicKey() {
        return sm2PublicKey;
    }

    public void setSm2PublicKey(String sm2PublicKey) {
        this.sm2PublicKey = sm2PublicKey;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public String getSm4EncryptType() {
        return sm4EncryptType;
    }

    public void setSm4EncryptType(String sm4EncryptType) {
        this.sm4EncryptType = sm4EncryptType;
    }

    public String getEncryptMode() {
        return encryptMode;
    }

    public void setEncryptMode(String encryptMode) {
        this.encryptMode = encryptMode;
    }
}
