/**
* Salty Lychee: a simple password hashing library
* 
* @author	StellaContrail
* 
* Copyright (c) 2022, StellaContrail
*
* This library is released under the MIT License.
* See https://opensource.org/licenses/mit-license.php for more details.
*/

package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class SaltyLychee {
    private static final String ALGORITHM = "SHA-256";
    private static final int STRETCHING_COUNT = 10;
    private static final String SEPARATOR = "$";
    private static final Random RAND = new Random();

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
        
        return salt + SEPARATOR + DatatypeConverter.printHexBinary(passHash).toLowerCase();
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
