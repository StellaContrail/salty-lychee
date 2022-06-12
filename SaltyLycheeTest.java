import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

class SaltyLycheeTest {

    @Test
    void test() throws NoSuchAlgorithmException {
        assertTrue(SaltyLychee.verify("テスト", SaltyLychee.hash("テスト")));
        assertTrue(SaltyLychee.verify("Hello, World!", SaltyLychee.hash("Hello, World!")));
    }

}
