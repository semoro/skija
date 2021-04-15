package org.jetbrains.skija;

import org.jetbrains.skija.paragraph.FontCollection;
import org.jetbrains.skija.paragraph.TypefaceFontProvider;
import org.jetbrains.skija.test.Executable;
import org.jetbrains.skija.test.TestRunner;

import static org.jetbrains.skija.test.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.TestRunner.assertEquals;

public class FontMgrTest implements Executable {
    @Override
    public void execute() throws Exception {
        // FontManager
        TypefaceFontProvider fm = new TypefaceFontProvider();
        Typeface jbMono = Typeface.makeFromFile("src/test/resources/fonts/JetBrainsMono-Regular.ttf", 0);
        fm.registerTypeface(jbMono);
        Typeface jbMonoBold = Typeface.makeFromFile("src/test/resources/fonts/JetBrainsMono-Bold.ttf", 0);
        fm.registerTypeface(jbMonoBold);
        Typeface inter = Typeface.makeFromFile("src/test/resources/fonts/InterHinted-Regular.ttf", 0);
        fm.registerTypeface(inter, "Interface");

        assertEquals(2, fm.getFamiliesCount());
        assertEquals("JetBrains Mono", fm.getFamilyName(0));
        assertEquals("Interface", fm.getFamilyName(1));

        try (FontStyleSet ss = fm.makeStyleSet(0)) {
            assertEquals(0, ss.count()); // ?
        }

        try (FontStyleSet ss = fm.makeStyleSet(1)) {
            assertEquals(0, ss.count()); // ?
        }

        // FontStyleSet
        try (FontStyleSet ss = fm.matchFamily("JetBrains Mono")) {
            assertEquals(2, ss.count());
            assertEquals(FontStyle.NORMAL, ss.getStyle(0));
            assertEquals("JetBrains Mono", ss.getStyleName(0));

            assertEquals(FontStyle.BOLD, ss.getStyle(1));
            assertEquals("JetBrains Mono", ss.getStyleName(1));

            assertEquals(2, jbMono.getRefCount());
            try (Typeface face = ss.getTypeface(0)) {
                assertEquals(3, jbMono.getRefCount());
                assertEquals(jbMono, face);
            }
            assertEquals(2, jbMono.getRefCount());

            assertEquals(2, jbMonoBold.getRefCount());
            try (Typeface face = ss.getTypeface(1)) {
                assertEquals(3, jbMonoBold.getRefCount());
                assertEquals(jbMonoBold, face);
            }
            assertEquals(2, jbMonoBold.getRefCount());

            assertEquals(2, jbMono.getRefCount());
            try (Typeface face = ss.matchStyle(FontStyle.NORMAL)) {
                assertEquals(3, jbMono.getRefCount());
                assertEquals(jbMono, face);
            }
            assertEquals(2, jbMono.getRefCount());            

            assertEquals(2, jbMonoBold.getRefCount());
            try (Typeface face = ss.matchStyle(FontStyle.BOLD)) {
                assertEquals(3, jbMonoBold.getRefCount());
                assertEquals(jbMonoBold, face);
            }
            assertEquals(2, jbMonoBold.getRefCount());

            assertEquals(jbMono, ss.matchStyle(FontStyle.ITALIC)); // ?
        }

        assertEquals(null, fm.matchFamilyStyle("JetBrains Mono", FontStyle.BOLD)); // ?
        assertEquals(null, fm.matchFamilyStyle("Interface", FontStyle.NORMAL)); // ?

        assertEquals(null, fm.matchFamilyStyleCharacter("JetBrains Mono", FontStyle.BOLD, new String[] {"en-US"}, 65 /* A */)); // ?

        try (Data data = Data.makeFromFileName("src/test/resources/fonts/JetBrainsMono-Italic.ttf");
             Typeface face = fm.makeFromData(data);
             FontStyleSet ss = fm.matchFamily("JetBrains Mono"); )
        {
            assertEquals(2, fm.getFamiliesCount());
            assertEquals(2, ss.count()); // ?
        }

        fm.close();
    }
}