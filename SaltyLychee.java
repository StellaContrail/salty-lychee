import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class SaltyLychee {
    private static final String ALGORITHM = "SHA-256";
    private static final int STRETCHING_COUNT = 10;
    private static final String SEPARATOR = "$";
    private static final SecureRandom RAND = new SecureRandom();

    public static String hash(String passStr) throws NoSuchAlgorithmException {
        return hash(passStr, genRandStr());
    }
    
    public static String hash(String passStr, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        
        byte[] passBytes = (passStr + salt).getBytes();
        byte[] passHash  = md.digest(passBytes);
        
        for (int i=0; i<(int)Math.pow(2, STRETCHING_COUNT); i++) {
            passHash = md.digest(passHash);
        }
        
        StringBuilder sb = new StringBuilder(passHash.length * 2);
        for (byte b : passHash) {
            sb.append(String.format("%02x", b));
        }

        return salt + SEPARATOR + sb.toString();
    }
    
    private static String genRandStr() {
        return RAND.ints(48, 123)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(8)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
    }
    
    public static boolean verify(String str, String hash) throws NoSuchAlgorithmException {
        if (str == null || hash == null) { return false; }
        if (str.isEmpty() && hash.isEmpty()) { return true; }
        
        String[] items = hash.split(Pattern.quote(SEPARATOR));
        return hash(str, items[0]).equals(hash);
    }
}
