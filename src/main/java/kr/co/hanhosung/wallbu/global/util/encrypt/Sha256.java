package kr.co.hanhosung.wallbu.global.util.encrypt;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Sha256 implements IHashService {


    @Override
    public String encode(String data) {

        String result = "";
        try {
            //1. SHA256 적용
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            result = bytesToHex(md.digest());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert (!result.isEmpty());

        return result;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
