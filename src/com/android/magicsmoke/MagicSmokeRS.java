/*
 * Copyright (C) 2009 The Android Open Source Project
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

import static android.renderscript.Sampler.Value.LINEAR;
import static android.renderscript.Sampler.Value.WRAP;

import com.android.magicsmoke.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Dimension;
import android.renderscript.Element;
import android.renderscript.Primitive;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramVertex;
import android.renderscript.Sampler;
import android.renderscript.ScriptC;
import android.renderscript.SimpleMesh;
import android.renderscript.Type;
import android.renderscript.Element.Builder;
import android.renderscript.ProgramStore.BlendDstFunc;
import android.renderscript.ProgramStore.BlendSrcFunc;
import android.util.Log;
import android.view.MotionEvent;

import java.util.TimeZone;

class MagicSmokeRS extends RenderScriptScene implements OnSharedPreferenceChangeListener {

    static class WorldState {
        public float mXOffset;
        public float mTilt;
        public int   mPreset;
        public int   mTextureMask;
        public int   mRotate;
        public int   mTextureSwap;
        public int   mProcessTextureMode;
        public int   mBackCol;
        public int   mLowCol;
        public int   mHighCol;
        public float mAlphaMul;
        public int   mPreMul;
        public int   mBlendFunc;
    }
    WorldState mWorldState = new WorldState();
    private Type mStateType;
    private Allocation mState;

    private ProgramStore mPfsBackgroundOne;
    private ProgramStore mPfsBackgroundSrc;
    private ProgramFragment mPfBackground;
    private Sampler mSampler;
    private Allocation[] mSourceTextures;
    private Allocation[] mRealTextures;

    private ProgramVertex mPVBackground;
    private ProgramVertex.MatrixAllocation mPVAlloc;

    private static final int RSID_STATE = 0;
    //private static final int RSID_PROGRAMVERTEX = 3;
    private static final int RSID_NOISESRC1 = 1;
    private static final int RSID_NOISESRC2 = 2;
    private static final int RSID_NOISESRC3 = 3;
    private static final int RSID_NOISESRC4 = 4;
    private static final int RSID_NOISESRC5 = 5;
    private static final int RSID_NOISEDST1 = 6;
    private static final int RSID_NOISEDST2 = 7;
    private static final int RSID_NOISEDST3 = 8;
    private static final int RSID_NOISEDST4 = 9;
    private static final int RSID_NOISEDST5 = 10;

    private Context mContext;
    private SharedPreferences mSharedPref;

    static class Preset {
        Preset(int processmode, int backcol, int locol, int hicol, float mul, int mask,
                 boolean rot, int blend, boolean texswap, boolean premul) {
            mProcessTextureMode = processmode;
            mBackColor = backcol;
            mLowColor = locol;
            mHighColor = hicol;
            mAlphaMul = mul;
            mTextureMask = mask;
            mRotate = rot;
            mBlendFunc = blend;
            mTextureSwap = texswap;
            mPreMul = premul;
        }
        public int mProcessTextureMode;
        public int mBackColor;
        public int mLowColor;
        public int mHighColor;
        public float mAlphaMul;
        public int mTextureMask;
        public boolean mRotate;
        public int mBlendFunc;
        public boolean mTextureSwap;
        public boolean mPreMul;
    }

    public static final int DEFAULT_PRESET = 4;
    public static final Preset [] mPreset = new Preset[] {
        //       proc    back     low       high     alph  mask  rot  blend  swap premul
        new Preset(1,  0x000000, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, false),
        new Preset(1,  0x0000ff, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, false),
        new Preset(1,  0x00ff00, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, false),
        new Preset(1,  0x00ff00, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, true),
        new Preset(1,  0x00ff00, 0x00ff00, 0xffffff, 2.5f, 0x1f, true,  0, true, true),
        new Preset(1,  0x800000, 0xff0000, 0xffffff, 2.5f, 0x1f, true,  0, true,  false),
        new Preset(0,  0x000000, 0x000000, 0xffffff, 0.0f, 0x1f, true,  0, false, false),
        new Preset(1,  0x0000ff, 0x00ff00, 0xffff00, 2.0f, 0x1f, true,  0, true,  false),
        new Preset(1,  0x008000, 0x00ff00, 0xffffff, 2.5f, 0x1f, true,  0, true,  false),
        new Preset(1,  0x800000, 0xff0000, 0xffffff, 2.5f, 0x1f, true,  0, true, true),
        new Preset(1,  0x808080, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, true),
        new Preset(1,  0x0000ff, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, true),
        new Preset(1,  0x0000ff, 0x00ff00, 0xffff00, 1.5f, 0x1f, false, 0, false, true),
        new Preset(1,  0x0000ff, 0x00ff00, 0xffff00, 2.0f, 0x1f, true,  0, true, true),
        new Preset(1,  0x0000ff, 0x00ff00, 0xffff00, 1.5f, 0x1f, true,  0, true, true),
        new Preset(1,  0x808080, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, false, false),
        new Preset(1,  0x000000, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, true, false),
        new Preset(2,  0x000000, 0x000070, 0xff2020, 2.5f, 0x1f, true,  0, false, false),
        new Preset(2,  0x6060ff, 0x000070, 0xffffff, 2.5f, 0x1f, true,  0, false, false),
        new Preset(3,  0x0000f0, 0x000000, 0xffffff, 2.0f, 0x0f, true,  0, true, false),
    };

    private float mTouchY;

    MagicSmokeRS(Context context, int width, int height) {
        super(width, height);
        mWidth = width;
        mHeight = height;
        mWorldState.mTilt = 0;
        mContext = context;
        mSharedPref = mContext.getSharedPreferences("magicsmoke", Context.MODE_PRIVATE);
        makeNewState();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        makeNewState();
        mState.data(mWorldState);
    }

    void makeNewState() {
        int p = mSharedPref.getInt("preset", DEFAULT_PRESET);
        if (p >= mPreset.length) {
            p = 0;
        }
        mWorldState.mPreset = p;
        mWorldState.mTextureMask = mPreset[p].mTextureMask;
        mWorldState.mRotate = mPreset[p].mRotate ? 1 : 0;
        mWorldState.mTextureSwap = mPreset[p].mTextureSwap ? 1 : 0;
        mWorldState.mProcessTextureMode = mPreset[p].mProcessTextureMode;
        mWorldState.mBackCol = mPreset[p].mBackColor;
        mWorldState.mLowCol = mPreset[p].mLowColor;
        mWorldState.mHighCol = mPreset[p].mHighColor;
        mWorldState.mAlphaMul = mPreset[p].mAlphaMul;
        mWorldState.mPreMul = mPreset[p].mPreMul ? 1 : 0;
        mWorldState.mBlendFunc = mPreset[p].mBlendFunc;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (mPVAlloc != null) {
            mPVAlloc.setupProjectionNormalized(width, height);
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mTouchY;
                mTouchY += dy;
                dy /= 20;
                dy += mWorldState.mTilt;
                if (dy > 4) {
                    dy = 4;
                } else if (dy < -4) {
                    dy = -4;
                }
                mWorldState.mTilt = dy;
                mState.data(mWorldState);
        }
    }

    @Override
    public void setOffset(float xOffset, float yOffset, float xStep, float yStep,
            int xPixels, int yPixels) {
        // update our state, then push it to the renderscript
        mWorldState.mXOffset = xOffset;
        mState.data(mWorldState);
    }

    @Override
    public void stop() {
        mSharedPref.unregisterOnSharedPreferenceChangeListener(this);
        super.stop();
    }

    @Override
    public void start() {
        super.start();
        mSharedPref.registerOnSharedPreferenceChangeListener(this);
        makeNewState();
        mState.data(mWorldState);
    }

    float alphafactor;
    Type mTextureType;

    void loadBitmap(int id, int index, String name, float alphamul, int lowcol, int highcol) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap in = BitmapFactory.decodeResource(mResources, id, opts);

        int pixels[] = new int[65536];
        in.getPixels(pixels, 0, 256, 0, 0, 256, 256);
        mRealTextures[index] = Allocation.createTyped(mRS, mTextureType);
        mSourceTextures[index] = Allocation.createTyped(mRS, mTextureType);
        mSourceTextures[index].setName(name+"_src");
        mSourceTextures[index].data(pixels);
        mRealTextures[index].setName(name);
        in.recycle();
    }

    void loadBitmaps() {
        alphafactor = 1f;
        float alphamul = mPreset[mWorldState.mPreset].mAlphaMul;
        int lowcol = mPreset[mWorldState.mPreset].mLowColor;
        int highcol = mPreset[mWorldState.mPreset].mHighColor;
        //Log.i("@@@@", "preset " + mWorldState.mPreset + ", mul: " + alphamul +
        //        ", colors: " + Integer.toHexString(lowcol) + "/" + Integer.toHexString(highcol));

        // TODO: using different high and low colors for each layer offers some cool effects too
        loadBitmap(R.drawable.noise1, 0, "Tnoise1", alphamul, lowcol, highcol);
        loadBitmap(R.drawable.noise2, 1, "Tnoise2", alphamul, lowcol, highcol);
        loadBitmap(R.drawable.noise3, 2, "Tnoise3", alphamul, lowcol, highcol);
        loadBitmap(R.drawable.noise4, 3, "Tnoise4", alphamul, lowcol, highcol);
        loadBitmap(R.drawable.noise5, 4, "Tnoise5", alphamul, lowcol, highcol);
    }

    @Override
    protected ScriptC createScript() {

        // Create a renderscript type from a java class. The specified name doesn't
        // really matter; the name by which we refer to the object in RenderScript
        // will be specified later.
        mStateType = Type.createFromClass(mRS, WorldState.class, 1, "WorldState");
        // Create an allocation from the type we just created.
        mState = Allocation.createTyped(mRS, mStateType);
        mState.data(mWorldState);

        // First set up the coordinate system and such
        ProgramVertex.Builder pvb = new ProgramVertex.Builder(mRS, null, null);
        mPVBackground = pvb.create();
        mPVBackground.setName("PVBackground");
        mPVAlloc = new ProgramVertex.MatrixAllocation(mRS);
        mPVBackground.bindAllocation(mPVAlloc);
        mPVAlloc.setupProjectionNormalized(mWidth, mHeight);

        mSourceTextures = new Allocation[5];
        mRealTextures = new Allocation[5];

        Type.Builder tb = new Type.Builder(mRS, Element.RGBA_8888(mRS));
        tb.add(Dimension.X, 256);
        tb.add(Dimension.Y, 256);
        mTextureType = tb.create();
        loadBitmaps();

        Sampler.Builder samplerBuilder = new Sampler.Builder(mRS);
        samplerBuilder.setMin(LINEAR);
        samplerBuilder.setMag(LINEAR);
        samplerBuilder.setWrapS(WRAP);
        samplerBuilder.setWrapT(WRAP);
        mSampler = samplerBuilder.create();

        {
            ProgramFragment.Builder builder = new ProgramFragment.Builder(mRS);
            builder.setTexture(ProgramFragment.Builder.EnvMode.REPLACE,
                               ProgramFragment.Builder.Format.RGBA, 0);
            mPfBackground = builder.create();
            mPfBackground.setName("PFBackground");
            mPfBackground.bindSampler(mSampler, 0);
        }

        {
            ProgramStore.Builder builder = new ProgramStore.Builder(mRS, null, null);
            builder.setDepthFunc(ProgramStore.DepthFunc.EQUAL);
            builder.setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ONE_MINUS_SRC_ALPHA);
            builder.setDitherEnable(true); // without dithering there is severe banding
            builder.setDepthMask(false);
            mPfsBackgroundOne = builder.create();
            mPfsBackgroundOne.setName("PFSBackgroundOne");
            builder.setBlendFunc(BlendSrcFunc.SRC_ALPHA, BlendDstFunc.ONE_MINUS_SRC_ALPHA);
            mPfsBackgroundSrc = builder.create();
            mPfsBackgroundSrc.setName("PFSBackgroundSrc");
        }

        // Time to create the script
        ScriptC.Builder sb = new ScriptC.Builder(mRS);
        // Specify the name by which to refer to the WorldState object in the
        // renderscript.
        sb.setType(mStateType, "State", RSID_STATE);
        sb.setType(mTextureType, "noisesrc1", RSID_NOISESRC1);
        sb.setType(mTextureType, "noisedst1", RSID_NOISEDST1);
        sb.setType(mTextureType, "noisesrc2", RSID_NOISESRC2);
        sb.setType(mTextureType, "noisedst2", RSID_NOISEDST2);
        sb.setType(mTextureType, "noisesrc3", RSID_NOISESRC3);
        sb.setType(mTextureType, "noisedst3", RSID_NOISEDST3);
        sb.setType(mTextureType, "noisesrc4", RSID_NOISESRC4);
        sb.setType(mTextureType, "noisedst4", RSID_NOISEDST4);
        sb.setType(mTextureType, "noisesrc5", RSID_NOISESRC5);
        sb.setType(mTextureType, "noisedst5", RSID_NOISEDST5);
        sb.setScript(mResources, R.raw.clouds);
        sb.setRoot(true);

        ScriptC script = sb.create();
        script.setClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        script.setTimeZone(TimeZone.getDefault().getID());

        script.bindAllocation(mState, RSID_STATE);
        script.bindAllocation(mSourceTextures[0], RSID_NOISESRC1);
        script.bindAllocation(mRealTextures[0], RSID_NOISEDST1);
        script.bindAllocation(mSourceTextures[1], RSID_NOISESRC2);
        script.bindAllocation(mRealTextures[1], RSID_NOISEDST2);
        script.bindAllocation(mSourceTextures[2], RSID_NOISESRC3);
        script.bindAllocation(mRealTextures[2], RSID_NOISEDST3);
        script.bindAllocation(mSourceTextures[3], RSID_NOISESRC4);
        script.bindAllocation(mRealTextures[3], RSID_NOISEDST4);
        script.bindAllocation(mSourceTextures[4], RSID_NOISESRC5);
        script.bindAllocation(mRealTextures[4], RSID_NOISEDST5);
        //script.bindAllocation(mPVAlloc.mAlloc, RSID_PROGRAMVERTEX);


        return script;
    }
}
