package com.editor.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.content.res.Resources;
import com.editor.EditorModel.Model;
import com.editor.EditorModel.ObjModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadUtil {
    private static final Logger _logger = LoggerFactory.getLogger(LoadUtil.class);
    public static float[] getCrossProduct(float arg_x1, float arg_y1, float arg_z1, float arg_x2, float arg_y2, float arg_z2) {
        float A = arg_y1 * arg_z2 - arg_y2 * arg_z1;
        float B = arg_z1 * arg_x2 - arg_z2 * arg_x1;
        float C = arg_x1 * arg_y2 - arg_x2 * arg_y1;

        return new float[]{A, B, C};
    }

    public static float[] vectorNormal(float[] arg_vector) {
        float module = (float) Math.sqrt(arg_vector[0] * arg_vector[0] + arg_vector[1] * arg_vector[1] + arg_vector[2] * arg_vector[2]);
        return new float[]{arg_vector[0] / module, arg_vector[1] / module, arg_vector[2] / module};
    }

    public static Model loadObjFile(String arg_fname, Resources arg_resources) {
        Model model = null;
        ArrayList<Float> alv = new ArrayList<Float>();
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();
        ArrayList<Float> alvResult = new ArrayList<Float>();
        HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();
        ArrayList<Float> alt = new ArrayList<Float>();
        ArrayList<Float> altResult = new ArrayList<Float>();

        try {
            InputStream in = arg_resources.getAssets().open(arg_fname);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;

            while ((temps = br.readLine()) != null) {
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("vt")) {
                    alt.add(Float.parseFloat(tempsa[1]) / 2.0f);
                    alt.add(Float.parseFloat(tempsa[2]) / 2.0f);
                } else if (tempsa[0].trim().equals("f")) {

                    int[] index = new int[3];

                    index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index[0]);
                    float y0 = alv.get(3 * index[0] + 1);
                    float z0 = alv.get(3 * index[0] + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index[1]);
                    float y1 = alv.get(3 * index[1] + 1);
                    float z1 = alv.get(3 * index[1] + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index[2]);
                    float y2 = alv.get(3 * index[2] + 1);
                    float z2 = alv.get(3 * index[2] + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);

                    float vxa = x1 - x0;
                    float vya = y1 - y0;
                    float vza = z1 - z0;
                    float vxb = x2 - x0;
                    float vyb = y2 - y0;
                    float vzb = z2 - z0;
                    float[] vNormal = vectorNormal(getCrossProduct
                            (
                                    vxa, vya, vza, vxb, vyb, vzb
                            ));
                    for (int tempInxex : index) {
                        HashSet<Normal> hsn = hmn.get(tempInxex);
                        if (hsn == null) {
                            hsn = new HashSet<Normal>();
                        }
                        hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
                        hmn.put(tempInxex, hsn);
                    }

                    int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                }
            }

            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            float[] nXYZ = new float[alFaceIndex.size() * 3];
            int c = 0;
            for (Integer i : alFaceIndex) {
                HashSet<Normal> hsn = hmn.get(i);
                float[] tn = Normal.getAverage(hsn);
                nXYZ[c++] = tn[0];
                nXYZ[c++] = tn[1];
                nXYZ[c++] = tn[2];
            }

            size = altResult.size();
            float[] tST = new float[size];
            for (int i = 0; i < size; i++) {
                tST[i] = altResult.get(i);
            }

            model = new ObjModel(arg_resources, vXYZ, nXYZ, tST);
        } catch (Exception e) {
            _logger.error("err! info : "+e.getMessage());
        }
        return model;
    }
}
