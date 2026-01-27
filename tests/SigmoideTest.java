import mlp.Sigmoide;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SigmoideTest {

    @Test
    public void testSigmoide(){
        var sigmoide = new Sigmoide();
        assertEquals(0.26894, sigmoide.evaluate(-1.0),1e-5);
        assertEquals(0.37754, sigmoide.evaluate(-0.5),1e-5);
        assertEquals(0.5, sigmoide.evaluate(0));
        assertEquals(0.62246, sigmoide.evaluate(0.5),1e-5);
        assertEquals(0.73106, sigmoide.evaluate(1.0),1e-5);

        assertEquals(0.19661, sigmoide.evaluateDer(sigmoide.evaluate(-1.0)),1e-5);
        assertEquals(0.235, sigmoide.evaluateDer(sigmoide.evaluate(-0.5)),1e-5);
        assertEquals(0.25, sigmoide.evaluateDer(sigmoide.evaluate(0)),1e-5);
        assertEquals(0.235, sigmoide.evaluateDer(sigmoide.evaluate(0.5)),1e-5);
        assertEquals(0.19661, sigmoide.evaluateDer(sigmoide.evaluate(1.0)),1e-5);
    }
}