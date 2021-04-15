package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;
import org.jetbrains.skija.shaper.*;

public class TextLine extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public TextLine(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @NotNull @Contract("_, _ -> new")
    public static TextLine make(String text, Font font) {
        return make(text, font, null, true);
    }

    @NotNull @Contract("_, _, _, _ -> new")
    public static TextLine make(String text, Font font, @Nullable FontFeature[] features, boolean leftToRight) {
        try (Shaper shaper = Shaper.makeShapeDontWrapOrReorder();) {
            return shaper.shapeLine(text, font, features, leftToRight);
        }
    }

    public float getAscent() {
        Stats.onNativeCall();
        try {
            return _nGetAscent(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public float getCapHeight() {
        Stats.onNativeCall();
        try {
            return _nGetCapHeight(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public float getDescent() {
        Stats.onNativeCall();
        try {
            return _nGetDescent(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public float getLeading() {
        Stats.onNativeCall();
        try {
            return _nGetLeading(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public float getWidth() {
        Stats.onNativeCall();
        try {
            return _nGetWidth(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public float getHeight() {
        Stats.onNativeCall();
        try {
            return _nGetHeight(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    @Nullable
    public TextBlob getTextBlob() {
        Stats.onNativeCall();
        try {
            long res = _nGetTextBlob(_ptr);
            return res == 0 ? null : new TextBlob(res);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public short[] getGlyphs() {
        Stats.onNativeCall();
        try {
            return _nGetGlyphs(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }    

    /**
     * @return  [x0, y0, x1, y1, ...]
     */
    public float[] getPositions() {
        Stats.onNativeCall();
        try {
            return _nGetPositions(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    public int[] getClusters() {
        Stats.onNativeCall();
        try {
            return _nGetClusters(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }    

    /**
     * @param x  coordinate in px
     * @return   UTF-16 offset of glyph
     */
    public int getOffsetAtCoord(float x) {
        try {
            Stats.onNativeCall();
            return _nGetOffsetAtCoord(_ptr, x);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    /**
     * @param offset  UTF-16 character offset
     * @return        glyph coordinate
     */
    public float getCoordAtOffset(int offset) {
        try {
            Stats.onNativeCall();
            return _nGetCoordAtOffset(_ptr, offset);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal public static native long  _nGetFinalizer();
    @ApiStatus.Internal public static native float _nGetAscent(long ptr);
    @ApiStatus.Internal public static native float _nGetCapHeight(long ptr);
    @ApiStatus.Internal public static native float _nGetDescent(long ptr);
    @ApiStatus.Internal public static native float _nGetLeading(long ptr);
    @ApiStatus.Internal public static native float _nGetWidth(long ptr);
    @ApiStatus.Internal public static native float _nGetHeight(long ptr);
    @ApiStatus.Internal public static native long  _nGetTextBlob(long ptr);
    @ApiStatus.Internal public static native short[] _nGetGlyphs(long ptr);
    @ApiStatus.Internal public static native float[] _nGetPositions(long ptr);
    @ApiStatus.Internal public static native int[]   _nGetClusters(long ptr);
    @ApiStatus.Internal public static native int   _nGetOffsetAtCoord(long ptr, float x);
    @ApiStatus.Internal public static native float _nGetCoordAtOffset(long ptr, int offset);
}