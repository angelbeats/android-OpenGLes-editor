package com.editor.common;

import java.util.Set;
public class Normal
{
   public static final float DIFF=0.0000001f;
   float _nx;
   float _ny;
   float _nz;
   
   public Normal(float nx,float ny,float nz)
   {
	   this._nx=nx;
	   this._ny=ny;
	   this._nz=nz;
   }
   
   @Override 
   public boolean equals(Object o)
   {
	   if(o instanceof  Normal)
	   {
		   Normal tn=(Normal)o;
		   if(Math.abs(_nx-tn._nx)<DIFF&&
			  Math.abs(_ny-tn._ny)<DIFF&&
			  Math.abs(_ny-tn._ny)<DIFF
             )
		   {
			   return true;
		   }
		   else
		   {
			   return false;
		   }
	   }
	   else
	   {
		   return false;
	   }
   }
   
   @Override
   public int hashCode()
   {
	   return 1;
   }
   
   public static float[] getAverage(Set<Normal> sn)
   {
	   float[] result=new float[3];
	   for(Normal n:sn)
	   {
		   result[0]+=n._nx;
		   result[1]+=n._ny;
		   result[2]+=n._nz;
	   }	   
	   return LoadUtil.vectorNormal(result);
   }
}
