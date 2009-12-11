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
#pragma stateVertex(PVBackground)
#pragma stateRaster(parent)
#pragma stateFragment(PFBackground)

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

float xshift[5];
float rotation[5];
float scale[5];
float alphafactor;
int currentpreset;
int lastuptime;
float timedelta;

void drawCloud(float *ident, int id, int idx) {
    float mat1[16];
    float z = -8.f * idx;
    matrixLoadMat(mat1,ident);
    matrixTranslate(mat1, -State->mXOffset * 8.f * idx, -State->mTilt * idx / 3.f, 0.f);
    matrixRotate(mat1, rotation[idx], 0.f, 0.f, 1.f);
    vpLoadModelMatrix(mat1);

    bindTexture(NAMED_PFBackground, 0, id);
    drawQuadTexCoords(
            -1200.0f, -1200.0f, z,        // space
                0.f + xshift[idx], 0.f,        // texture
            1200, -1200.0f, z,            // space
                scale[idx] + xshift[idx], 0.f,         // texture
            1200, 1200.0f, z,            // space
                scale[idx] + xshift[idx], scale[idx],         // texture
            -1200.0f, 1200.0f, z,        // space
                0.f + xshift[idx], scale[idx]);       // texture
}

void drawClouds(float* ident) {

    int i;

    float mat1[16];

    matrixLoadMat(mat1,ident);

    if (State->mRotate != 0) {
        rotation[0] += 0.10 * timedelta;
        rotation[1] += 0.102f * timedelta;
        rotation[2] += 0.106f * timedelta;
        rotation[3] += 0.114f * timedelta;
        rotation[4] += 0.123f * timedelta;
    }

    int mask = State->mTextureMask;
    if (mask & 1) {
        xshift[0] += 0.0010f * timedelta;
        if (State->mTextureSwap != 0) {
            drawCloud(mat1, NAMED_Tnoise5, 0);
        } else {
            drawCloud(mat1, NAMED_Tnoise1, 0);
        }
    }

    if (mask & 2) {
        xshift[1] += 0.00106 * timedelta;
        drawCloud(mat1, NAMED_Tnoise2, 1);
    }

    if (mask & 4) {
        xshift[2] += 0.00114f * timedelta;
        drawCloud(mat1, NAMED_Tnoise3, 2);
    }

    if (mask & 8) {
        xshift[3] += 0.00118f * timedelta;
        drawCloud(mat1, NAMED_Tnoise4, 3);
    }

    if (mask & 16) {
        xshift[4] += 0.00127f * timedelta;
        drawCloud(mat1, NAMED_Tnoise5, 4);
    }

    // Make sure the texture coordinates don't continuously increase
    for(i = 0; i < 5; i++) {
        while (xshift[i] >= 1.f) {
            xshift[i] -= 1.f;
        }
    }
    // Make sure the rotation angles don't continuously increase
    for(i = 0; i < 5; i++) {
        while (rotation[i] >= 360.f) {
            rotation[i] -= 360.f;
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

void makeTexture(int *src, int *dst, int rsid) {
    
    int x;
    int y;
    int pm = State->mPreMul;

    if (State->mProcessTextureMode == 1) {
        int lowcol = State->mLowCol;
        int highcol = State->mHighCol;
        
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
        alphafactor *= State->mAlphaMul;
    } else if (State->mProcessTextureMode == 2) {
        int lowcol = State->mLowCol;
        int highcol = State->mHighCol;
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
        alphafactor *= State->mAlphaMul;
    } else if (State->mProcessTextureMode == 3) {
        int lowcol = State->mLowCol;
        int highcol = State->mHighCol;
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
        alphafactor *= State->mAlphaMul;
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
    uploadToTexture(rsid, 0);
}

void makeTextures() {
    debugI32("makeTextures", State->mPreset);
    alphafactor = 1.f;
    makeTexture((int*)noisesrc1, (int*)noisedst1, NAMED_Tnoise1);
    makeTexture((int*)noisesrc2, (int*)noisedst2, NAMED_Tnoise2);
    makeTexture((int*)noisesrc3, (int*)noisedst3, NAMED_Tnoise3);
    makeTexture((int*)noisesrc4, (int*)noisedst4, NAMED_Tnoise4);
    makeTexture((int*)noisesrc5, (int*)noisedst5, NAMED_Tnoise5);
}



struct color {
    float r;
    float g;
    float b;
};

void init() {
    int i;
    for (i=0;i<4;i++) {
        xshift[i] = 0.f;
        rotation[i] = 360.f * i / 5.f;
    }
    scale[0] = 4.0f; // changed below based on preset
    scale[1] = 3.0f;
    scale[2] = 3.4f;
    scale[3] = 3.8f;
    scale[4] = 4.2f;

    currentpreset = -1;
    lastuptime = uptimeMillis();
    timedelta = 0;
}


int main(int launchID) {

    int i;
    float ident[16];
    float masterscale = 0.0041f;// / (State->mXOffset * 4.f + 1.f);
    matrixLoadIdentity(ident);
    matrixTranslate(ident, -State->mXOffset, 0.f, 0.f);
    matrixScale(ident, masterscale, masterscale, masterscale);
    //matrixRotate(ident, 0.f, 0.f, 0.f, 1.f);
    matrixRotate(ident, -State->mTilt, 1.f, 0.f, 0.f);

    if (State->mBlendFunc) {
        bindProgramStore(NAMED_PFSBackgroundOne);
    } else {
        bindProgramStore(NAMED_PFSBackgroundSrc);
    }

    int now = uptimeMillis();
    timedelta = ((float)(now - lastuptime)) / 44.f;
    lastuptime = now;
    if (timedelta > 3) {
        // Limit the step adjustment factor to 3, so we don't get a sudden jump
        // after coming back from sleep.
        timedelta = 3;
    }

    i = State->mPreset;
    if (i != currentpreset) {
        currentpreset = i;
        int rgb = State->mBackCol;
        pfClearColor(
            ((float)((rgb >> 16)  & 0xff)) / 255.0f,
            ((float)((rgb >> 8)  & 0xff)) / 255.0f,
            ((float)(rgb & 0xff)) / 255.0f,
            1.0f);
        makeTextures();
    }

    if (State->mTextureSwap != 0) {
        scale[0] = .25f;
    } else {
        scale[0] = 4.f;
    }
    drawClouds(ident);

    return 55;
}
