package com.credaegis.backend.utility;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MerkleTreeUtility {

    public static String calculateMerkleRoot(List<String> hashes) {

        try {
            if (hashes.size() % 2 != 0) {
                hashes.add(sha256("padding_value"));
            }
            return computeMerkleRoot(hashes);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private static String computeMerkleRoot(List<String> layer) throws NoSuchAlgorithmException {
        if (layer.size() == 1) {
            return layer.get(0);
        }

        List<String> nextLayer = new ArrayList<>();
        for (int i = 0; i < layer.size(); i += 2) {
            String left = layer.get(i);
            String right = (i + 1 < layer.size()) ? layer.get(i + 1) : left;
            nextLayer.add(sha256(left + right));
        }

        return computeMerkleRoot(nextLayer);
    }

    private static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

}
