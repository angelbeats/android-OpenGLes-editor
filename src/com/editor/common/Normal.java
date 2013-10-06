package com.editor.common;

import java.util.Set;
//��ʾ���������࣬�����һ�������ʾһ��������
public class Normal 
{
   public static final float DIFF=0.0000001f;//�ж������������Ƿ���ͬ����ֵ
   //��������XYZ���ϵķ���
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
	   {//������������XYZ���������ĲС��ָ������ֵ����Ϊ���������������
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
   
   //����Ҫ�õ�HashSet�����һ��Ҫ��дhashCode����
   @Override
   public int hashCode()
   {
	   return 1;
   }
   
   //������ƽ��ֵ�Ĺ��߷���
   public static float[] getAverage(Set<Normal> sn)
   {
	   //��ŷ������͵�����
	   float[] result=new float[3];
	   //�Ѽ��������еķ��������
	   for(Normal n:sn)
	   {
		   result[0]+=n._nx;
		   result[1]+=n._ny;
		   result[2]+=n._nz;
	   }	   
	   //����ͺ�ķ��������
	   return LoadUtil.vectorNormal(result);
   }
}
