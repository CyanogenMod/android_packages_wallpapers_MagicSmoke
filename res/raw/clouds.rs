// Copyright (C) 2009 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#pragma version(1)

#pragma rs java_package_name(com.android.magicsmoke)

#include "../../../../../frameworks/base/libs/rs/scriptc/rs_types.rsh"
#include "../../../../../frameworks/base/libs/rs/scriptc/rs_math.rsh"
#include "../../../../../frameworks/base/libs/rs/scriptc/rs_graphics.rsh"

#define RSID_NOISESRC1 1
#define RSID_NOISESRC2 2
#define RSID_NOISESRC3 3
#define RSID_NOISESRC4 4
#define RSID_NOISESRC5 5
#define RSID_NOISEDST1 6
#define RSID_NOISEDST2 7
#define RSID_NOISEDST3 8
#define RSID_NOISEDST4 9
#define RSID_NOISEDST5 10

// State set from java
float gXOffset;
float gTilt;
int   gPreset;
int   gTextureMask;
int   gRotate;
int   gTextureSwap;
int   gProcessTextureMode;
int   gBackCol;
int   gLowCol;
int   gHighCol;
float gAlphaMul;
int   gPreMul;
int   gBlendFunc;

rs_program_vertex gPVBackground;
rs_program_fragment gPFBackground;
rs_program_store gPFSBackgroundOne;
rs_program_store gPFSBackgroundSrc;

rs_allocation gTnoise1;
rs_allocation gTnoise2;
rs_allocation gTnoise3;
rs_allocation gTnoise4;
rs_allocation gTnoise5;

int *gNoisesrc1;
int *gNoisesrc2;
int *gNoisesrc3;
int *gNoisesrc4;
int *gNoisesrc5;

int *gNoisedst1;
int *gNoisedst2;
int *gNoisedst3;
int *gNoisedst4;
int *gNoisedst5;

#pragma rs export_var(gXOffset, gTilt, gPreset, gTextureMask, gRotate, gTextureSwap, gProcessTextureMode, gBackCol, gLowCol, gHighCol, gAlphaMul, gPreMul, gBlendFunc, gPVBackground, gPFBackground, gPFSBackgroundOne, gPFSBackgroundSrc, gTnoise1, gTnoise2, gTnoise3, gTnoise4, gTnoise5, gNoisesrc1, gNoisesrc2, gNoisesrc3, gNoisesrc4, gNoisesrc5, gNoisedst1, gNoisedst2, gNoisedst3, gNoisedst4, gNoisedst5)
#pragma rs_export_func()

// Local script variables
float xshift[5];
float rotation[5];
float scale[5];
float alphafactor;
int currentpreset;
int lastuptime;
float timedelta;
static float4 clearColor = {0.5f, 0.0f, 0.0f, 1.0f};

void drawCloud(rs_matrix4x4 *ident, rs_allocation allocat, int idx) {
    rs_matrix4x4 mat1;
    float z = -8.f * idx;
    rsMatrixLoadMat(&mat1, ident);
    rsMatrixTranslate(&mat1, -gXOffset * 8.f * idx, -gTilt * idx / 3.f, 0.f);
    rsMatrixRotate(&mat1, rotation[idx], 0.f, 0.f, 1.f);
    rsgProgramVertexLoadModelMatrix(&mat1);

    rsgBindTexture(gPFBackground, 0, allocat);
    rsgDrawQuadTexCoords(
            -1200.0f, -1200.0f, z,        // space
                0.f + xshift[idx], 0.f,        // texture
            1200, -1200.0f, z,            // space
                scale[idx] + xshift[idx], 0.f,         // texture
            1200, 1200.0f, z,            // space
                scale[idx] + xshift[idx], scale[idx],         // texture
            -1200.0f, 1200.0f, z,        // space
                0.f + xshift[idx], scale[idx]);       // texture
}

void drawClouds(rs_matrix4x4 *ident) {
    rs_matrix4x4 mat1;
    rsMatrixLoadMat(&mat1, ident);

    if (gRotate != 0) {
        rotation[0] += 0.10 * timedelta;
        rotation[1] += 0.102f * timedelta;
        rotation[2] += 0.106f * timedelta;
        rotation[3] += 0.114f * timedelta;
        rotation[4] += 0.123f * timedelta;
    }

    int mask = gTextureMask;
    if (mask & 1) {
        xshift[0] += 0.0010f * timedelta;
        if (gTextureSwap != 0) {
            drawCloud(&mat1, gTnoise5, 0);
        } else {
            drawCloud(&mat1, gTnoise1, 0);
        }
    }

    if (mask & 2) {
        xshift[1] += 0.00106 * timedelta;
        drawCloud(&mat1, gTnoise2, 1);
    }

    if (mask & 4) {
        xshift[2] += 0.00114f * timedelta;
        drawCloud(&mat1, gTnoise3, 2);
    }

    if (mask & 8) {
        xshift[3] += 0.00118f * timedelta;
        drawCloud(&mat1, gTnoise4, 3);
    }

    if (mask & 16) {
        xshift[4] += 0.00127f * timedelta;
        drawCloud(&mat1, gTnoise5, 4);
    }

    // Make sure the texture coordinates don't continuously increase
    int i;
    for(i = 0; i < 5; i++) {
        if (xshift[i] > 1.f) {
            xshift[i] -= floor(xshift[i]);
        }
    }
    // Make sure the rotation angles don't continuously increase
    for(i = 0; i < 5; i++) {
        if (rotation[i] > 360.f) {
            float multiplier = floor(rotation[i]/360.f);
            rotation[i] -= 360.f * multiplier;
        }
    }
}

int premul(int rgb, int a) {
    int r = (rgb >> 16) * a + 1;
    r = (r + (r >> 8)) >> 8;
    int g = ((rgb >> 8) & 0xff) * a + 1;
    g = (g + (g >> 8)) >> 8;
    int b = (rgb & 0xff) * a + 1;
    b = (b + (b >> 8)) >> 8;
    return r << 16 | g << 8 | b;
}


void makeTexture(int *src, int *dst, rs_allocation rsid) {

    int x;
    int y;
    int pm = gPreMul;

    if (gProcessTextureMode == 1) {
        int lowcol = gLowCol;
        int highcol = gHighCol;

        for (y=0;y<256;y++) {
            for (x=0;x<256;x++) {
                int pix = src[y*256+x];
                int lum = pix & 0x00ff;
                int newpix;
                if (lum < 128) {
                    newpix = lowcol;
                    int newalpha = 255 - (lum * 2);
                    newalpha /= alphafactor;
                    if (pm) newpix = premul(newpix, newalpha);
                    newpix = newpix | (newalpha << 24);
                } else {
                    newpix = highcol;
                    int newalpha = (lum - 128) * 2;
                    newalpha /= alphafactor;
                    if (pm) newpix = premul(newpix, newalpha);
                    newpix = newpix | (newalpha << 24);
                }
                // have ARGB, need ABGR
                newpix = (newpix & 0xff00ff00) | ((newpix & 0xff) << 16) | ((newpix >> 16) & 0xff);
                dst[y*256+x] = newpix;
            }
        }
        alphafactor *= gAlphaMul;
    } else if (gProcessTextureMode == 2) {
        int lowcol = gLowCol;
        int highcol = gHighCol;
        float scale = 255.f / (255.f - lowcol);

        for (y=0;y<256;y++) {
            for (x=0;x<256;x++) {
                int pix = src[y*256+x];
                int alpha = pix & 0x00ff;
                if (alpha < lowcol) {
                    alpha = 0;
                } else {
                    alpha = (alpha - lowcol) * scale;
                }
                alpha /= alphafactor;
                int newpix = highcol;
                if (pm) newpix = premul(newpix, alpha);
                newpix = newpix | (alpha << 24);
                // have ARGB, need ABGR
                newpix = (newpix & 0xff00ff00) | ((newpix & 0xff) << 16) | ((newpix >> 16) & 0xff);
                dst[y*256+x] = newpix;
            }
        }
        alphafactor *= gAlphaMul;
    } else if (gProcessTextureMode == 3) {
        int lowcol = gLowCol;
        int highcol = gHighCol;
        float scale = 255.f / (255.f - lowcol);

        for (y=0;y<256;y++) {
            for (x=0;x<256;x++) {
                int pix = src[y*256+x];
                int lum = pix & 0x00ff;
                int newpix;
                if (lum < 128) lum *= 2;
                else lum = (255 - (lum - 128) * 2);
                if (lum < 128) {
                    newpix = lowcol;
                    int newalpha = 255 - (lum * 2);
                    newalpha /= alphafactor;
                    if (pm) newpix = premul(newpix, newalpha);
                    newpix = newpix | (newalpha << 24);
                } else {
                    newpix = highcol;
                    int newalpha = (lum - 128) * 2;
                    newalpha /= alphafactor;
                    if (pm) newpix = premul(newpix, newalpha);
                    newpix = newpix | (newalpha << 24);
                }
                // have ARGB, need ABGR
                newpix = (newpix & 0xff00ff00) | ((newpix & 0xff) << 16) | ((newpix >> 16) & 0xff);
                dst[y*256+x] = newpix;
            }
        }
        alphafactor *= gAlphaMul;
    } else {
        for (y=0;y<256;y++) {
            for (x=0;x<256;x++) {
                int rgb = *src++;
                int a = (rgb >> 24) & 0xff;
                rgb &= 0x00ffffff;
                rgb = premul(rgb, a);
                int newpix = (a << 24) | rgb;
                newpix = (newpix & 0xff00ff00) | ((newpix & 0xff) << 16) | ((newpix >> 16) & 0xff);
                *dst++ = newpix;
            }
        }
    }

    rsgUploadToTexture(rsid, 0);
}

void makeTextures() {
    alphafactor = 1.f;
    makeTexture((int*)gNoisesrc1, (int*)gNoisedst1, gTnoise1);
    makeTexture((int*)gNoisesrc2, (int*)gNoisedst2, gTnoise2);
    makeTexture((int*)gNoisesrc3, (int*)gNoisedst3, gTnoise3);
    makeTexture((int*)gNoisesrc4, (int*)gNoisedst4, gTnoise4);
    makeTexture((int*)gNoisesrc5, (int*)gNoisedst5, gTnoise5);
}

void init() {
    for (int i=0;i<5;i++) {
        xshift[i] = 0.f;
        rotation[i] = 360.f * i / 5.f;
    }

    scale[0] = 4.0f; // changed below based on preset
    scale[1] = 3.0f;
    scale[2] = 3.4f;
    scale[3] = 3.8f;
    scale[4] = 4.2f;

    currentpreset = -1;
    lastuptime = (int)rsUptimeMillis();
    timedelta = 0;
}


int root(int launchID) {
    int i;
    rs_matrix4x4 ident;
    float masterscale = 0.0041f;// / (gXOffset * 4.f + 1.f);

    rsgBindProgramVertex(gPVBackground);
    rsgBindProgramFragment(gPFBackground);

    rsMatrixLoadIdentity(&ident);
    rsMatrixTranslate(&ident, -gXOffset, 0.f, 0.f);
    rsMatrixScale(&ident, masterscale, masterscale, masterscale);
    //rsMatrixRotate(&ident, 0.f, 0.f, 0.f, 1.f);
    rsMatrixRotate(&ident, -gTilt, 1.f, 0.f, 0.f);

    if (gBlendFunc) {
        rsgBindProgramStore(gPFSBackgroundOne);
    } else {
        rsgBindProgramStore(gPFSBackgroundSrc);
    }

    int now = (int)rsUptimeMillis();
    timedelta = ((float)(now - lastuptime)) / 44.f;
    lastuptime = now;
    if (timedelta > 3) {
        // Limit the step adjustment factor to 3, so we don't get a sudden jump
        // after coming back from sleep.
        timedelta = 3;
    }

    i = gPreset;
    if (i != currentpreset) {
        currentpreset = i;
        clearColor.x = ((float)((gBackCol >> 16)  & 0xff)) / 255.0f;
        clearColor.y = ((float)((gBackCol >> 8)  & 0xff)) / 255.0f;
        clearColor.z = ((float)(gBackCol & 0xff)) / 255.0f;
        makeTextures();
    }
    rsgClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);

    if (gTextureSwap != 0) {
        scale[0] = .25f;
    } else {
        scale[0] = 4.f;
    }
    drawClouds(&ident);

    return 55;
}
