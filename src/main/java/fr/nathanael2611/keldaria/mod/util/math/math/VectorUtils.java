/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.util.math.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VectorUtils {

    public static String vec3iListToString(Collection<Vector3i> vectors){
        StringBuilder builder = new StringBuilder();
        for(Vector3i vec : vectors){
            builder.append("/");
            builder.append(vec.x).append(",").append(vec.y).append(",").append(vec.z);
        }
        return builder.toString();
    }
    public static List<Vector3i> getVec3ListFromString(String vec3list){
        String[] vecs = vec3list.split("/");
        List<Vector3i> vectorList = new ArrayList<>();
        for(String v : vecs){
            String[] vals = v.split(",");
            if(vals.length==3){
                Vector3i vec = new Vector3i(
                        Integer.valueOf(vals[0]),
                        Integer.valueOf(vals[1]),
                        Integer.valueOf(vals[2])
                );
                vectorList.add(vec);
            }
        }
        return vectorList;
    }

}
