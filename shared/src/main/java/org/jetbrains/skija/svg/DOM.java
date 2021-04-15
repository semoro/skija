package org.jetbrains.skija.svg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class DOM extends RefCnt {
    static { Library.staticLoad(); }

    public DOM(@NotNull Data data) {
        this(_nMakeFromData(Native.getPtr(data)));
        Stats.onNativeCall();
        RefExt.reachabilityFence(data);
    }

    @NotNull
    public Point getContainerSize() {
        try {
            return _nGetContainerSize(_ptr);
        } finally {
            RefExt.reachabilityFence(this);
        }
    }

    @NotNull @Contract("-> this")
    public DOM setContainerSize(float width, float height) {
        Stats.onNativeCall();
        _nSetContainerSize(_ptr, width, height);
        return this;
    }

    @NotNull @Contract("-> this")
    public DOM setContainerSize(Point size) {
        Stats.onNativeCall();
        _nSetContainerSize(_ptr, size._x, size._y);
        return this;
    }

    // sk_sp<SkSVGNode>* findNodeById(const char* id);

    @NotNull @Contract("-> this")
    public DOM render(@NotNull Canvas canvas) {
        try {
            Stats.onNativeCall();
            _nRender(_ptr, Native.getPtr(canvas));
            return this;
        } finally {
            RefExt.reachabilityFence(canvas);
        }
    }

    @ApiStatus.Internal
    public DOM(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal public static native long  _nMakeFromData(long dataPtr);
    @ApiStatus.Internal public static native Point _nGetContainerSize(long ptr);
    @ApiStatus.Internal public static native void  _nSetContainerSize(long ptr, float width, float height);
    @ApiStatus.Internal public static native void  _nRender(long ptr, long canvasPtr);
}