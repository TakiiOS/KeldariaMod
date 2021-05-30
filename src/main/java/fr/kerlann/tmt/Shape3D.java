/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.kerlann.tmt;

public class Shape3D
{
	public Shape3D(PositionTransformVertex[] verts, TexturedPolygon[] poly)
	{
		vertices = verts;
		faces = poly;
	}
	
	public PositionTransformVertex[] vertices;
	public TexturedPolygon[] faces;
}
