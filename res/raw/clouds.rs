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


void drawCloud(float *ident, int id, int idx) {
    float mat1[16];
    float z = -8.f * idx;
    matrixLoadMat(mat1,ident);
    matrixTranslate(mat1, -State->mXOffset * 8.f * idx, -State->mTilt * idx / 3.f, 0.f);
    matrixRotate(mat1, rotation[idx], 0.f, 0.f, 1.f);
    vpLoadModelMatrix(mat1);

    bindTexture(NAMED_PFBackground, 0, id);
    drawQuadTexCoords(
            -600.0f, -600.0f, z,        // space
                0.f + xshift[idx], 0.f,        // texture
            600, -600.0f, z,            // space
                scale[idx] + xshift[idx], 0.f,         // texture
            600, 600.0f, z,            // space
                scale[idx] + xshift[idx], scale[idx],         // texture
            -600.0f, 600.0f, z,        // space
                0.f + xshift[idx], scale[idx]);       // texture
}

void drawClouds(float* ident) {

    int i;

    float mat1[16];

    matrixLoadMat(mat1,ident);


    int mask = State->mTextureMask;
    if (mask & 1) {
        if (State->mTextureSwap != 0) {
            drawCloud(mat1, NAMED_Tnoise5, 0);
        } else {
            drawCloud(mat1, NAMED_Tnoise1, 0);
        }
        xshift[0] += 0.0010f;
    }

    if (mask & 2) {
        drawCloud(mat1, NAMED_Tnoise2, 1);
        xshift[1] += 0.00106;
    }

    if (mask & 4) {
        drawCloud(mat1, NAMED_Tnoise3, 2);
        xshift[2] += 0.00114f;
    }

    if (mask & 8) {
        drawCloud(mat1, NAMED_Tnoise4, 3);
        xshift[3] += 0.00118f;
    }

    if (mask & 16) {
        drawCloud(mat1, NAMED_Tnoise5, 4);
        xshift[4] += 0.00127f;
    }

    if (State->mRotate != 0) {
        rotation[0] += 0.10;
        rotation[1] += 0.102f;
        rotation[2] += 0.106f;
        rotation[3] += 0.114f;
        rotation[4] += 0.123f;
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

    if (State->mProcessTexture) {
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
    scale[0] = 2.f; // changed below based on preset
    scale[1] = 1.5f;
    scale[2] = 1.7f;
    scale[3] = 1.9f;
    scale[4] = 2.1f;

    currentpreset = -1;
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
        scale[0] = .125f;
    } else {
        scale[0] = 2.f;
    }
    drawClouds(ident);

    return 1;
}
