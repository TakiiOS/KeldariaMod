package fr.nathanael2611.keldaria.mod.util.math;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public enum BoxCorner
{
	BottomNorthEast
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.maxX, box.minY, box.maxZ);
		}
	},
	BottomNorthWest
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.maxX, box.minY, box.minZ);
		}
	},
	BottomSouthWest
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.minX, box.minY, box.minZ);
		}
	},
	BottomSouthEast
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.minX, box.minY, box.maxZ);
		}
	},
	TopNorthEast
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.maxX, box.maxY, box.maxZ);
		}
	},
	TopNorthWest
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.maxX, box.maxY, box.minZ);
		}
	},
	TopSouthWest
	{
		@Override
		public Vec3d getPoint(AxisAlignedBB box )
		{
			return new Vec3d(box.minX, box.maxY, box.minZ);
		}
	},
	TopSouthEast
	{
		@Override
		public Vec3d getPoint( AxisAlignedBB box )
		{
			return new Vec3d(box.minX, box.maxY, box.maxZ);
		}
	};
	
	public abstract Vec3d getPoint(AxisAlignedBB box );
}
