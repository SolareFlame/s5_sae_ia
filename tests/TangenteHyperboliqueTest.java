import mlp.TangenteHyperbolique;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TangenteHyperboliqueTest {

    @Test
    public void testTangente(){
        var tangente = new TangenteHyperbolique();
        assertEquals(-0.76159, tangente.evaluate(-1.0),1e-5);
        assertEquals(-0.46212, tangente.evaluate(-0.5),1e-5);
        assertEquals(0, tangente.evaluate(0));
        assertEquals(0.46212, tangente.evaluate(0.5),1e-5);
        assertEquals(0.76159, tangente.evaluate(1.0),1e-5);

        assertEquals(0.41997, tangente.evaluateDer(tangente.evaluate(-1.0)),1e-5);
        assertEquals(0.78645, tangente.evaluateDer(tangente.evaluate(-0.5)),1e-5);
        assertEquals(1, tangente.evaluateDer(tangente.evaluate(0)),1e-5);
        assertEquals(0.78645, tangente.evaluateDer(tangente.evaluate(0.5)),1e-5);
        assertEquals(0.41997, tangente.evaluateDer(tangente.evaluate(1.0)),1e-5);
    }
}