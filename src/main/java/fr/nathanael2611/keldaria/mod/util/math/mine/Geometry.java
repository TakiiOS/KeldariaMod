package fr.nathanael2611.keldaria.mod.util.math.mine;

import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3d;
import org.joml.Matrix4d;
import org.joml.Vector3d;

import java.nio.DoubleBuffer;

public class Geometry
{

    public static Vec3d transformPosition(Matrix4d matrix, Vec3d vec)
    {
        Vector3d nv = new Vector3d(vec.x, vec.y, vec.z);
        nv = matrix.transformPosition(nv);
        return new Vec3d(nv.x, nv.y, nv.z);
    }

    public static boolean isPointInOBB(Vec3d point, OBB obb)
    {
        Vec3d dir = transformPosition(obb.orientation, point.subtract(obb.center));
        //Vec3d dir = point.subtract(obb.center);
        double[] all = (new Matrix3d(obb.orientation)).get(new double[3 * 3]);
        for (int i = 0; i < 3; ++i)
        {
            double[] orientation = new double[3];
            orientation[0] = all[i * 3];
            orientation[1] = all[i * 3 + 1];
            orientation[2] = all[i * 3 + 2];
            Vec3d axis = new Vec3d(orientation[0], orientation[1], orientation[2]);
            double distance = dir.dotProduct(axis);
            double size = i == 0 ? obb.size.x : i == 1 ? obb.size.y : obb.size.z;
            if (distance > size)
            {
                return false;
            }
            if (distance < -size)
            {
                return false;
            }
        }
        return true;
    }

    public static Vec3d getClosestPoint(OBB obb, Vec3d point)
    {
        Vec3d result = transformPosition(obb.orientation, obb.center);
        Vec3d dir = transformPosition(obb.orientation, point.subtract(obb.center));
        double[] all = (new Matrix3d(obb.orientation)).get(new double[3 * 3]);
        for (int i = 0; i < 3; i++) //YEET ? ++i?
        {
            double[] orientation = new double[3];
            orientation[0] = all[i * 3];
            orientation[1] = all[i * 3 + 1];
            orientation[2] = all[i * 3 + 2];
            Vec3d axis = new Vec3d(orientation[0], orientation[1], orientation[2]);
            double distance = dir.dotProduct(axis);
            double size = i == 0 ? obb.size.x : i == 1 ? obb.size.y : obb.size.z;
            if (distance > size)
            {
                distance = size;
            }
            if (distance < -size)
            {
                distance = -size;
            }
            result = mult(result, distance);
        }
        return result;
    }

    public static Vec3d getMin(AABB aabb)
    {
        Vec3d p1 = aabb.origin.add(aabb.size);
        Vec3d p2 = aabb.origin.subtract(aabb.size);

        return new Vec3d(
                Math.min(p1.x, p2.x),
                Math.min(p1.y, p2.y),
                Math.min(p1.z, p2.z)
        );
    }

    public static Vec3d getMax(AABB aabb)
    {
        Vec3d p1 = aabb.origin.add(aabb.size);
        Vec3d p2 = aabb.origin.subtract(aabb.size);

        return new Vec3d(
                Math.max(p1.x, p2.x),
                Math.max(p1.y, p2.y),
                Math.max(p1.z, p2.z)
        );
    }

    public static AABB fromMinmax(Vec3d min, Vec3d max)
    {
        Vec3d added = min.add(max);
        Vec3d sub = max.subtract(min);
        return new AABB(
                mult(added, 0.5),
                mult(sub, 0.5)
        );
    }

    public static Vec3d mult(Vec3d vec, double val)
    {
        return (new Vec3d(vec.x * val, vec.y * val, vec.z * val));
    }

    public static Interval getInterval(AABB aabb, Vec3d axis)
    {
        Vec3d i = getMin(aabb);
        Vec3d a = getMax(aabb);

        Vec3d[] vertex = new Vec3d[8];
        vertex[0] = new Vec3d(i.x, a.y, a.z);
        vertex[1] = new Vec3d(i.x, a.y, i.z);
        vertex[2] = new Vec3d(i.x, i.y, a.z);
        vertex[3] = new Vec3d(i.x, i.y, i.z);
        vertex[4] = new Vec3d(a.x, a.y, a.z);
        vertex[5] = new Vec3d(a.x, a.y, i.z);
        vertex[6] = new Vec3d(a.x, i.y, a.z);
        vertex[7] = new Vec3d(a.x, i.y, i.z);

        Interval result = new Interval();
        result.min = result.max = axis.dotProduct(vertex[0]);

        for (int k = 1; k < 8; ++k)
        {
            double projection = axis.dotProduct(vertex[k]);
            result.min = Math.min(projection, result.min);
            result.max = Math.max(projection, result.max);
        }
        return result;
    }

    public static Interval getInterval(OBB obb, Vec3d axis)
    {
        Vec3d[] vertex = new Vec3d[8];

        Vec3d C = transformPosition(obb.orientation, obb.center);
        Vec3d E = obb.size;
        double[] all = (new Matrix3d(obb.orientation)).get(new double[3 * 3]);
        Vec3d[] A = new Vec3d[]{
                new Vec3d(all[0], all[1], all[2]),
                new Vec3d(all[3], all[4], all[5]),
                new Vec3d(all[6], all[7], all[8])
        };

        vertex[0] = C.add(mult(A[0], E.x)).add(mult(A[1], E.y)).add(mult(A[2], E.z));
        vertex[1] = C.subtract(mult(A[0], E.x)).add(mult(A[1], E.y)).add(mult(A[2], E.z));
        vertex[2] = C.add(mult(A[0], E.x)).subtract(mult(A[1], E.y)).add(mult(A[2], E.z));
        vertex[3] = C.add(mult(A[0], E.x)).add(mult(A[1], E.y)).subtract(mult(A[2], E.z));
        vertex[4] = C.subtract(mult(A[0], E.x)).subtract(mult(A[1], E.y)).subtract(mult(A[2], E.z));
        vertex[5] = C.add(mult(A[0], E.x)).subtract(mult(A[1], E.y)).subtract(mult(A[2], E.z));
        vertex[6] = C.subtract(mult(A[0], E.x)).add(mult(A[1], E.y)).subtract(mult(A[2], E.z));
        vertex[7] = C.subtract(mult(A[0], E.x)).subtract(mult(A[1], E.y)).add(mult(A[2], E.z));

        Interval result = new Interval();
        result.min = result.max = axis.dotProduct(vertex[0]);

        for (int i = 1; i < 8; i++)
        {
            double projection = axis.dotProduct(vertex[i]);
            result.min = Math.min(projection, result.min);
            result.max = Math.max(projection, result.max);
        }
        return result;

    }

    public static boolean overlapOnAxis(AABB aabb, OBB obb, Vec3d axis)
    {
        Interval a = getInterval(aabb, axis);
        Interval b = getInterval(obb, axis);
        return ((b.min <= a.max) && (a.min <= b.max));
    }

    public static boolean AABOBB(AABB aabb, OBB obb)
    {
        double[] o = (new Matrix3d(obb.orientation)).get(new double[3 * 3]);

        Vec3d[] test = new Vec3d[15];
        test[0] = new Vec3d(1, 0, 0);
        test[1] = new Vec3d(0, 1, 0);
        test[2] = new Vec3d(0, 0, 1);
        test[3] = new Vec3d(o[0], o[1], o[2]);
        test[4] = new Vec3d(o[3], o[4], o[5]);
        test[5] = new Vec3d(o[6], o[7], o[8]);

        for (int i = 0; i < 3; ++i)
        {
            test[6 + i * 3 + 0] = test[i].crossProduct(test[0]);
            test[6 + i * 3 + 1] = test[i].crossProduct(test[1]);
            test[6 + i * 3 + 2] = test[i].crossProduct(test[2]);
        }

        for (int i = 0; i < 15; ++i)
        {
            if (!overlapOnAxis(aabb, obb, test[i]))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean overlapOnAxis(OBB obb1, OBB obb2, Vec3d axis)
    {
        Interval a = getInterval(obb1, axis);
        Interval b = getInterval(obb2, axis);
        return ((b.min <= a.max) && (a.min <= b.max));
    }

    public static boolean OBBOBB(OBB obb1, OBB obb2)
    {
        double[] o1 = (new Matrix3d(obb1.orientation)).get(new double[3 * 3]);
        double[] o2 = (new Matrix3d(obb1.orientation)).get(new double[3 * 3]);

        Vec3d[] test = new Vec3d[15];
        test[0] = new Vec3d(o1[0], o1[1], o1[2]);
        test[1] = new Vec3d(o1[3], o1[4], o1[5]);
        test[2] = new Vec3d(o1[6], o1[7], o1[8]);
        test[3] = new Vec3d(o2[0], o2[1], o2[2]);
        test[4] = new Vec3d(o2[3], o2[4], o2[5]);
        test[5] = new Vec3d(o2[6], o2[7], o2[8]);

        for (int i = 0; i < 3; ++i)
        {
            test[6 + i * 3 + 0] = test[i].crossProduct(test[0]);
            test[6 + i * 3 + 1] = test[i].crossProduct(test[1]);
            test[6 + i * 3 + 2] = test[i].crossProduct(test[2]);
        }

        for(int i = 0; i < 15; i ++)
        {
            if(!overlapOnAxis(obb1, obb2, test[i]))
            {
                return false;
            }
        }
        return true;
    }


    public static DoubleBuffer store(DoubleBuffer buf, Matrix4d matrix4d)
    {
        buf.put(matrix4d.m00());
        buf.put(matrix4d.m01());
        buf.put(matrix4d.m02());
        buf.put(matrix4d.m03());
        buf.put(matrix4d.m10());
        buf.put(matrix4d.m11());
        buf.put(matrix4d.m12());
        buf.put(matrix4d.m13());
        buf.put(matrix4d.m20());
        buf.put(matrix4d.m21());
        buf.put(matrix4d.m22());
        buf.put(matrix4d.m23());
        buf.put(matrix4d.m30());
        buf.put(matrix4d.m31());
        buf.put(matrix4d.m32());
        buf.put(matrix4d.m33());
        buf.flip();
        return buf;
    }


    public static Vec3d getRotatedPoint(Vec3d pos, float pitch, float yaw, float roll)
    {
        double a,b,c,d,e,f; //thread-safe
        float x,y,z;

        a = Math.cos(pitch * 0.017453292F);//A
        b = Math.sin(pitch * 0.017453292F);//B
        c = Math.cos(yaw * 0.017453292F);//C
        d = Math.sin(yaw * 0.017453292F);//D
        e = Math.cos(roll * 0.017453292F);//E
        f = Math.sin(roll * 0.017453292F);//F

        x = (float) (pos.x*(c*e-b*d*f) + pos.y*(-b*d*e-c*f) + pos.z*(-a*d));
        y = (float) (pos.x*(a*f)          + pos.y*(a*e)           + pos.z*(-b));
        z = (float) (pos.x*(d*e+b*c*f) + pos.y*(b*c*e-d*f)  + pos.z*(a*c));

        return new Vec3d(x, y, z);
    }

}
