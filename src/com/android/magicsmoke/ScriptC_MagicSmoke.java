
package com.android.magicsmoke;

import android.content.res.Resources;
import android.renderscript.*;
import android.util.Log;

public class ScriptC_MagicSmoke
    extends android.renderscript.ScriptC
{

    public ScriptC_MagicSmoke(RenderScript rs, Resources resources, int id, boolean isRoot) {
        super(rs, resources, id, isRoot);
    }

    private float mField_gXOffset;
    public void set_gXOffset(float v) {
        mField_gXOffset = v;
        setVar(0, v);
    }
    private float mField_gTilt;
    public void set_gTilt(float v) {
        mField_gTilt = v;
        setVar(1, v);
    }
    private int   mField_gPreset;
    public void set_gPreset(int v) {
        mField_gPreset = v;
        setVar(2, v);
    }
    private int   mField_gTextureMask;
    public void set_gTextureMask(int v) {
        mField_gTextureMask = v;
        setVar(3, v);
    }
    private int   mField_gRotate;
    public void set_gRotate(int v) {
        mField_gRotate = v;
        setVar(4, v);
    }
    private int   mField_gTextureSwap;
    public void set_gTextureSwap(int v) {
        mField_gTextureSwap = v;
        setVar(5, v);
    }
    private int   mField_gProcessTextureMode;
    public void set_gProcessTextureMode(int v) {
        mField_gProcessTextureMode = v;
        setVar(6, v);
    }
    private int   mField_gBackCol;
    public void set_gBackCol(int v) {
        mField_gBackCol = v;
        setVar(7, v);
    }
    private int   mField_gLowCol;
    public void set_gLowCol(int v) {
        mField_gLowCol = v;
        setVar(8, v);
    }
    private int   mField_gHighCol;
    public void set_gHighCol(int v) {
        mField_gHighCol = v;
        setVar(9, v);
    }
    private float mField_gAlphaMul;
    public void set_gAlphaMul(float v) {
        mField_gAlphaMul = v;
        setVar(10, v);
    }
    private int   mField_gPreMul;
    public void set_gPreMul(int v) {
        mField_gPreMul = v;
        setVar(11, v);
    }
    private int   mField_gBlendFunc;
    public void set_gBlendFunc(int v) {
        mField_gBlendFunc = v;
        setVar(12, v);
    }

    private ProgramVertex mField_gPVBackground;
    public void set_gPVBackground(ProgramVertex v) {
        mField_gPVBackground = v;
        setVar(13, v.getID());
    }
    private ProgramFragment mField_gPFBackground;
    public void set_gPFBackground(ProgramFragment v) {
        mField_gPFBackground = v;
        setVar(14, v.getID());
    }
    private ProgramStore mField_gPFSBackgroundOne;
    public void set_gPFSBackgroundOne(ProgramStore v) {
        mField_gPFSBackgroundOne = v;
        setVar(15, v.getID());
    }
    private ProgramStore mField_gPFSBackgroundSrc;
    public void set_gPFSBackgroundSrc(ProgramStore v) {
        mField_gPFSBackgroundSrc = v;
        setVar(16, v.getID());
    }

    private Allocation mField_gTnoise1;
    public void set_gTnoise1(Allocation v) {
        mField_gTnoise1 = v;
        setVar(17, v.getID());
    }
    private Allocation mField_gTnoise2;
    public void set_gTnoise2(Allocation v) {
        mField_gTnoise2 = v;
        setVar(18, v.getID());
    }
    private Allocation mField_gTnoise3;
    public void set_gTnoise3(Allocation v) {
        mField_gTnoise3 = v;
        setVar(19, v.getID());
    }
    private Allocation mField_gTnoise4;
    public void set_gTnoise4(Allocation v) {
        mField_gTnoise4 = v;
        setVar(20, v.getID());
    }
    private Allocation mField_gTnoise5;
    public void set_gTnoise5(Allocation v) {
        mField_gTnoise5 = v;
        setVar(21, v.getID());
    }

    // Pointers
    public void bind_gNoisesrc1(Allocation v) {
        if (v == null) {
            bindAllocation(null, 22);
        } else {
            bindAllocation(v, 22);
        }
    }
    public void bind_gNoisesrc2(Allocation v) {
        if (v == null) {
            bindAllocation(null, 23);
        } else {
            bindAllocation(v, 23);
        }
    }
    public void bind_gNoisesrc3(Allocation v) {
        if (v == null) {
            bindAllocation(null, 24);
        } else {
            bindAllocation(v, 24);
        }
    }
    public void bind_gNoisesrc4(Allocation v) {
        if (v == null) {
            bindAllocation(null, 25);
        } else {
            bindAllocation(v, 25);
        }
    }
    public void bind_gNoisesrc5(Allocation v) {
        if (v == null) {
            bindAllocation(null, 26);
        } else {
            bindAllocation(v, 26);
        }
    }

    public void bind_gNoisedst1(Allocation v) {
        if (v == null) {
            bindAllocation(null, 27);
        } else {
            bindAllocation(v, 27);
        }
    }
    public void bind_gNoisedst2(Allocation v) {
        if (v == null) {
            bindAllocation(null, 28);
        } else {
            bindAllocation(v, 28);
        }
    }
    public void bind_gNoisedst3(Allocation v) {
        if (v == null) {
            bindAllocation(null, 29);
        } else {
            bindAllocation(v, 29);
        }
    }
    public void bind_gNoisedst4(Allocation v) {
        if (v == null) {
            bindAllocation(null, 30);
        } else {
            bindAllocation(v, 30);
        }
    }
    public void bind_gNoisedst5(Allocation v) {
        if (v == null) {
            bindAllocation(null, 31);
        } else {
            bindAllocation(v, 31);
        }
    }

}

