varying vec2 varTex0;
varying vec2 varTex1;
varying vec2 varTex2;
varying vec2 varTex3;
varying vec2 varTex4;

vec2 mul(vec4 uni, vec2 attr, float idx)
{
    float invz = 0.5 + idx*0.05;
    return vec2(
        0.5 + 0.5 * invz * (uni.z * ( uni.y * attr.x + uni.x * attr.y)) + uni.w,
        0.5 + 0.5 * invz * (uni.z * (-uni.x * attr.x + uni.y * attr.y)));
}

void main() {
    varTex0 = mul(UNI_layer0, ATTRIB_position.xy, 1.0);
    varTex1 = mul(UNI_layer1, ATTRIB_position.xy, 2.0);
    varTex2 = mul(UNI_layer2, ATTRIB_position.xy, 3.0);
    varTex3 = mul(UNI_layer3, ATTRIB_position.xy, 4.0);
    varTex4 = mul(UNI_layer4, ATTRIB_position.xy, 5.0);
    gl_Position = ATTRIB_position;
}
