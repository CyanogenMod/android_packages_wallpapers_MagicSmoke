/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.magicsmoke;

import android.renderscript.*;
import android.content.res.Resources;
import android.util.Log;

public class ScriptC_Clouds extends ScriptC {
    // Constructor
    public  ScriptC_Clouds(RenderScript rs, Resources resources, int id, boolean isRoot) {
        super(rs, resources, id, isRoot);
    }

    private final static int mExportVarIdx_gXOffset = 0;
    private float mExportVar_gXOffset;
    public void set_gXOffset(float v) {
        mExportVar_gXOffset = v;
        setVar(mExportVarIdx_gXOffset, v);
    }

    public float get_gXOffset() {
        return mExportVar_gXOffset;
    }

    private final static int mExportVarIdx_gTilt = 1;
    private float mExportVar_gTilt;
    public void set_gTilt(float v) {
        mExportVar_gTilt = v;
        setVar(mExportVarIdx_gTilt, v);
    }

    public float get_gTilt() {
        return mExportVar_gTilt;
    }

    private final static int mExportVarIdx_gPreset = 2;
    private int mExportVar_gPreset;
    public void set_gPreset(int v) {
        mExportVar_gPreset = v;
        setVar(mExportVarIdx_gPreset, v);
    }

    public int get_gPreset() {
        return mExportVar_gPreset;
    }

    private final static int mExportVarIdx_gTextureMask = 3;
    private int mExportVar_gTextureMask;
    public void set_gTextureMask(int v) {
        mExportVar_gTextureMask = v;
        setVar(mExportVarIdx_gTextureMask, v);
    }

    public int get_gTextureMask() {
        return mExportVar_gTextureMask;
    }

    private final static int mExportVarIdx_gRotate = 4;
    private int mExportVar_gRotate;
    public void set_gRotate(int v) {
        mExportVar_gRotate = v;
        setVar(mExportVarIdx_gRotate, v);
    }

    public int get_gRotate() {
        return mExportVar_gRotate;
    }

    private final static int mExportVarIdx_gTextureSwap = 5;
    private int mExportVar_gTextureSwap;
    public void set_gTextureSwap(int v) {
        mExportVar_gTextureSwap = v;
        setVar(mExportVarIdx_gTextureSwap, v);
    }

    public int get_gTextureSwap() {
        return mExportVar_gTextureSwap;
    }

    private final static int mExportVarIdx_gProcessTextureMode = 6;
    private int mExportVar_gProcessTextureMode;
    public void set_gProcessTextureMode(int v) {
        mExportVar_gProcessTextureMode = v;
        setVar(mExportVarIdx_gProcessTextureMode, v);
    }

    public int get_gProcessTextureMode() {
        return mExportVar_gProcessTextureMode;
    }

    private final static int mExportVarIdx_gBackCol = 7;
    private int mExportVar_gBackCol;
    public void set_gBackCol(int v) {
        mExportVar_gBackCol = v;
        setVar(mExportVarIdx_gBackCol, v);
    }

    public int get_gBackCol() {
        return mExportVar_gBackCol;
    }

    private final static int mExportVarIdx_gLowCol = 8;
    private int mExportVar_gLowCol;
    public void set_gLowCol(int v) {
        mExportVar_gLowCol = v;
        setVar(mExportVarIdx_gLowCol, v);
    }

    public int get_gLowCol() {
        return mExportVar_gLowCol;
    }

    private final static int mExportVarIdx_gHighCol = 9;
    private int mExportVar_gHighCol;
    public void set_gHighCol(int v) {
        mExportVar_gHighCol = v;
        setVar(mExportVarIdx_gHighCol, v);
    }

    public int get_gHighCol() {
        return mExportVar_gHighCol;
    }

    private final static int mExportVarIdx_gAlphaMul = 10;
    private float mExportVar_gAlphaMul;
    public void set_gAlphaMul(float v) {
        mExportVar_gAlphaMul = v;
        setVar(mExportVarIdx_gAlphaMul, v);
    }

    public float get_gAlphaMul() {
        return mExportVar_gAlphaMul;
    }

    private final static int mExportVarIdx_gPreMul = 11;
    private int mExportVar_gPreMul;
    public void set_gPreMul(int v) {
        mExportVar_gPreMul = v;
        setVar(mExportVarIdx_gPreMul, v);
    }

    public int get_gPreMul() {
        return mExportVar_gPreMul;
    }

    private final static int mExportVarIdx_gBlendFunc = 12;
    private int mExportVar_gBlendFunc;
    public void set_gBlendFunc(int v) {
        mExportVar_gBlendFunc = v;
        setVar(mExportVarIdx_gBlendFunc, v);
    }

    public int get_gBlendFunc() {
        return mExportVar_gBlendFunc;
    }

    private final static int mExportVarIdx_gPVBackground = 13;
    private ProgramVertex mExportVar_gPVBackground;
    public void set_gPVBackground(ProgramVertex v) {
        mExportVar_gPVBackground = v;
        setVar(mExportVarIdx_gPVBackground, (v == null) ? 0 : v.getID());
    }

    public ProgramVertex get_gPVBackground() {
        return mExportVar_gPVBackground;
    }

    private final static int mExportVarIdx_gPFBackground = 14;
    private ProgramFragment mExportVar_gPFBackground;
    public void set_gPFBackground(ProgramFragment v) {
        mExportVar_gPFBackground = v;
        setVar(mExportVarIdx_gPFBackground, (v == null) ? 0 : v.getID());
    }

    public ProgramFragment get_gPFBackground() {
        return mExportVar_gPFBackground;
    }

    private final static int mExportVarIdx_gPFSBackgroundOne = 15;
    private ProgramStore mExportVar_gPFSBackgroundOne;
    public void set_gPFSBackgroundOne(ProgramStore v) {
        mExportVar_gPFSBackgroundOne = v;
        setVar(mExportVarIdx_gPFSBackgroundOne, (v == null) ? 0 : v.getID());
    }

    public ProgramStore get_gPFSBackgroundOne() {
        return mExportVar_gPFSBackgroundOne;
    }

    private final static int mExportVarIdx_gPFSBackgroundSrc = 16;
    private ProgramStore mExportVar_gPFSBackgroundSrc;
    public void set_gPFSBackgroundSrc(ProgramStore v) {
        mExportVar_gPFSBackgroundSrc = v;
        setVar(mExportVarIdx_gPFSBackgroundSrc, (v == null) ? 0 : v.getID());
    }

    public ProgramStore get_gPFSBackgroundSrc() {
        return mExportVar_gPFSBackgroundSrc;
    }

    private final static int mExportVarIdx_gTnoise1 = 17;
    private Allocation mExportVar_gTnoise1;
    public void set_gTnoise1(Allocation v) {
        mExportVar_gTnoise1 = v;
        setVar(mExportVarIdx_gTnoise1, (v == null) ? 0 : v.getID());
    }

    public Allocation get_gTnoise1() {
        return mExportVar_gTnoise1;
    }

    private final static int mExportVarIdx_gTnoise2 = 18;
    private Allocation mExportVar_gTnoise2;
    public void set_gTnoise2(Allocation v) {
        mExportVar_gTnoise2 = v;
        setVar(mExportVarIdx_gTnoise2, (v == null) ? 0 : v.getID());
    }

    public Allocation get_gTnoise2() {
        return mExportVar_gTnoise2;
    }

    private final static int mExportVarIdx_gTnoise3 = 19;
    private Allocation mExportVar_gTnoise3;
    public void set_gTnoise3(Allocation v) {
        mExportVar_gTnoise3 = v;
        setVar(mExportVarIdx_gTnoise3, (v == null) ? 0 : v.getID());
    }

    public Allocation get_gTnoise3() {
        return mExportVar_gTnoise3;
    }

    private final static int mExportVarIdx_gTnoise4 = 20;
    private Allocation mExportVar_gTnoise4;
    public void set_gTnoise4(Allocation v) {
        mExportVar_gTnoise4 = v;
        setVar(mExportVarIdx_gTnoise4, (v == null) ? 0 : v.getID());
    }

    public Allocation get_gTnoise4() {
        return mExportVar_gTnoise4;
    }

    private final static int mExportVarIdx_gTnoise5 = 21;
    private Allocation mExportVar_gTnoise5;
    public void set_gTnoise5(Allocation v) {
        mExportVar_gTnoise5 = v;
        setVar(mExportVarIdx_gTnoise5, (v == null) ? 0 : v.getID());
    }

    public Allocation get_gTnoise5() {
        return mExportVar_gTnoise5;
    }

    private final static int mExportVarIdx_gNoisesrc1 = 22;
    private Allocation mExportVar_gNoisesrc1;
    public void bind_gNoisesrc1(Allocation v) {
        mExportVar_gNoisesrc1 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisesrc1);
        else bindAllocation(v, mExportVarIdx_gNoisesrc1);
    }

    public Allocation get_gNoisesrc1() {
        return mExportVar_gNoisesrc1;
    }

    private final static int mExportVarIdx_gNoisesrc2 = 23;
    private Allocation mExportVar_gNoisesrc2;
    public void bind_gNoisesrc2(Allocation v) {
        mExportVar_gNoisesrc2 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisesrc2);
        else bindAllocation(v, mExportVarIdx_gNoisesrc2);
    }

    public Allocation get_gNoisesrc2() {
        return mExportVar_gNoisesrc2;
    }

    private final static int mExportVarIdx_gNoisesrc3 = 24;
    private Allocation mExportVar_gNoisesrc3;
    public void bind_gNoisesrc3(Allocation v) {
        mExportVar_gNoisesrc3 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisesrc3);
        else bindAllocation(v, mExportVarIdx_gNoisesrc3);
    }

    public Allocation get_gNoisesrc3() {
        return mExportVar_gNoisesrc3;
    }

    private final static int mExportVarIdx_gNoisesrc4 = 25;
    private Allocation mExportVar_gNoisesrc4;
    public void bind_gNoisesrc4(Allocation v) {
        mExportVar_gNoisesrc4 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisesrc4);
        else bindAllocation(v, mExportVarIdx_gNoisesrc4);
    }

    public Allocation get_gNoisesrc4() {
        return mExportVar_gNoisesrc4;
    }

    private final static int mExportVarIdx_gNoisesrc5 = 26;
    private Allocation mExportVar_gNoisesrc5;
    public void bind_gNoisesrc5(Allocation v) {
        mExportVar_gNoisesrc5 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisesrc5);
        else bindAllocation(v, mExportVarIdx_gNoisesrc5);
    }

    public Allocation get_gNoisesrc5() {
        return mExportVar_gNoisesrc5;
    }

    private final static int mExportVarIdx_gNoisedst1 = 27;
    private Allocation mExportVar_gNoisedst1;
    public void bind_gNoisedst1(Allocation v) {
        mExportVar_gNoisedst1 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisedst1);
        else bindAllocation(v, mExportVarIdx_gNoisedst1);
    }

    public Allocation get_gNoisedst1() {
        return mExportVar_gNoisedst1;
    }

    private final static int mExportVarIdx_gNoisedst2 = 28;
    private Allocation mExportVar_gNoisedst2;
    public void bind_gNoisedst2(Allocation v) {
        mExportVar_gNoisedst2 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisedst2);
        else bindAllocation(v, mExportVarIdx_gNoisedst2);
    }

    public Allocation get_gNoisedst2() {
        return mExportVar_gNoisedst2;
    }

    private final static int mExportVarIdx_gNoisedst3 = 29;
    private Allocation mExportVar_gNoisedst3;
    public void bind_gNoisedst3(Allocation v) {
        mExportVar_gNoisedst3 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisedst3);
        else bindAllocation(v, mExportVarIdx_gNoisedst3);
    }

    public Allocation get_gNoisedst3() {
        return mExportVar_gNoisedst3;
    }

    private final static int mExportVarIdx_gNoisedst4 = 30;
    private Allocation mExportVar_gNoisedst4;
    public void bind_gNoisedst4(Allocation v) {
        mExportVar_gNoisedst4 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisedst4);
        else bindAllocation(v, mExportVarIdx_gNoisedst4);
    }

    public Allocation get_gNoisedst4() {
        return mExportVar_gNoisedst4;
    }

    private final static int mExportVarIdx_gNoisedst5 = 31;
    private Allocation mExportVar_gNoisedst5;
    public void bind_gNoisedst5(Allocation v) {
        mExportVar_gNoisedst5 = v;
        if(v == null) bindAllocation(null, mExportVarIdx_gNoisedst5);
        else bindAllocation(v, mExportVarIdx_gNoisedst5);
    }

    public Allocation get_gNoisedst5() {
        return mExportVar_gNoisedst5;
    }

}

