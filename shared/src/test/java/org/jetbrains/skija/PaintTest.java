package org.jetbrains.skija;

import org.jetbrains.skija.test.*;
import static org.jetbrains.skija.test.TestRunner.*;

public class PaintTest implements Executable {
    @Override
    public void execute() throws Exception {
        assertEquals(new Paint().setColor(0x12345678), new Paint().setColor(0x12345678));
        assertEquals(new Paint().setColor(0x12345678).hashCode(), new Paint().setColor(0x12345678).hashCode());
        assertNotEquals(new Paint().setColor(0x12345678), new Paint().setColor(0xFF345678));
        assertNotEquals(new Paint().setColor(0x12345678).hashCode(), new Paint().setColor(0xFF345678).hashCode());
        assertNotEquals(new Paint(), new Paint().setAntiAlias(false));
        assertNotEquals(new Paint(), new Paint().setDither(true));

        try (Paint p = new Paint().setColor(0x12345678);) {
            assertEquals(false, p == p.makeClone());
            assertEquals(p, p.makeClone());
            assertEquals(p.hashCode(), p.makeClone().hashCode());
        }

        try (Paint p = new Paint();) {
            assertEquals(false, p.hasNothingToDraw());

            p.setBlendMode(BlendMode.DST);
            assertEquals(true, p.hasNothingToDraw());

            p.setBlendMode(BlendMode.SRC_OVER);
            assertEquals(false, p.hasNothingToDraw());

            p.setAlpha(0);
            assertEquals(true, p.hasNothingToDraw());
        }
    }
}