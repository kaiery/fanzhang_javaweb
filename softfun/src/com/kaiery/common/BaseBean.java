package com.kaiery.common;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class BaseBean {
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	//将list装进本bean内
	public void initBean(Object[] obj){
		Field[] preName=this.getClass().getDeclaredFields();
		if(preName.length==obj.length){			
			for(int i=0;i<obj.length;i++){
				setValueByPropName(preName[i].getName(),obj[i]);				
			}
		}else{
			System.out.println("属性与字段不吻合:结果集与类熟悉长度不一致");
		}		
	}	
	
	public void initBean(String[] parm,Object[] obj){		
		if(parm.length==obj.length){			
			for(int i=0;i<obj.length;i++){
				setValueByPropName(parm[i],obj[i]);
			}
		}else{
			System.out.println("属性与字段不吻合");
		}		
	}
	
	public void initBean(String parm,Object obj){		
		setValueByPropName(parm,obj);				
	}	
	
	public void setValueByPropName(String propName,Object obj){
		try {
			//获得此参数的引用类型CLASS
			Class<?> clazz=getSetterType(propName);			
			//反射继承了本类的某个实体中某个属性set方法 一个参数是setXxx的方法名，一个参数是传入此方法的参数类型CLASS		
			//System.out.println(getSetterName(propName));
			Method method=this.getClass().getDeclaredMethod(getSetterName(propName), clazz);
			Constructor<?> con=null;
			//System.out.println(clazz);
			if(clazz==Date.class){				
				method.invoke(this,obj==null?null:obj);
			}else if(clazz==byte[].class){		
				method.invoke(this,obj==null?null:obj);
			}else{		
				//取得传入参数类型根据字符串实例化的构造函数
				//System.err.println(clazz);
				con = clazz.getDeclaredConstructor(String.class);
				//加入传入的是空,则传入空,否则传入实例化了的值
				if(clazz==String.class && obj==null){
					obj = "";
				}
				if(clazz==Integer.class && obj==null){
					obj = 0;
				}
				method.invoke(this,obj==null?null:con.newInstance(obj.toString()));	
			}
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (NoSuchMethodException e) {			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (InvocationTargetException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {						
			e.printStackTrace();
		}
	}
	
	public String getValueByPropName(String propName){
		try {
			//获得此参数的引用类型CLASS
			Class<?> clazz=getSetterType(propName);			
			//反射继承了本类的某个实体中某个属性set方法 一个参数是setXxx的方法名，一个参数是传入此方法的参数类型CLASS		
			Method method=this.getClass().getDeclaredMethod(getGetterName(propName));
			if(clazz==String.class){
				return method.invoke(this)==null?null:method.invoke(this).toString();
			}
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (NoSuchMethodException e) {			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (InvocationTargetException e) {			
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getGetterName(String propName) {
		return "get" + propName.substring(0, 1).toUpperCase()
				+ propName.substring(1, propName.length());
	}
	
	public String getSetterName(String propName) {
		return "set" + propName.substring(0, 1).toUpperCase()
				+ propName.substring(1, propName.length());
	}
	
	public Class<?> getSetterType(String propName) {
		Class<?> clazz=null;
		try {
//			System.out.println(propName);
			clazz = this.getClass().getDeclaredField(propName).getType();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		 return clazz;
	}
	
	//继承此类将具有格式化属性能力
	public static String getPreName(Class<?> clazz){		
		String str="";
		Field[] preName=clazz.getDeclaredFields();
		for(Field fie:preName){
			str += fie.getName()+",";
		}		
		return str.substring(0, str.length()-1)+" ";
	}
	/**
	 * @param tableName 表名
	 * @param idName 主键名
	 * @return update串
	 */
	public String getModifyStr(String tableName,String idName){		
		StringBuffer str=new StringBuffer("update "+tableName+" set ");
		Field[] preName=this.getClass().getDeclaredFields();
		try {
			for(Field fie:preName){
				if(!fie.getName().equals(idName)){
					if(this.getClass().getDeclaredMethod(getGetterName(fie.getName())).invoke(this)!=null){				
						if(fie.getType()==Date.class){
							str.append(fie.getName()+"=");
							str.append("to_date('"+sdf.format((Date)(this.getClass().getDeclaredMethod(getGetterName(fie.getName())).invoke(this)))+"','YYYYMMDDHH24MISS'),");
						}else{
							str.append(fie.getName()+"='"+this.getClass().getDeclaredMethod(getGetterName(fie.getName())).invoke(this).toString()+"',");
						}
					}else{
						str.append(fie.getName()+"='',");
					}
				}
				
			}		
			str.delete(str.toString().length()-1, str.toString().length());	
			str.append(" where "+idName+"='"+(this.getClass().getDeclaredMethod(getGetterName(idName)).invoke(this).toString())+"'");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
	
	/**
	 * @param tableName 表名
	 * @param idName 主键名
	 * @return update串
	 */
	public String getModifyUnNullStr(String tableName,String idName){		
		StringBuffer str=new StringBuffer("update "+tableName+" set ");
		Field[] preName=this.getClass().getDeclaredFields();
		try {
			for(Field fie:preName){				
				if(!fie.getName().equals(idName)&&this.getClass().getDeclaredMethod(getGetterName(fie.getName())).invoke(this)!=null){
					if(fie.getType()==Date.class){
						
						str.append(" "+fie.getName()+"=to_date('"+(sdf.format((Date)(this.getClass().getDeclaredMethod(getGetterName(fie.getName())).invoke(this))))+"','YYYYMMDDHH24MISS'),");
					}else{
						str.append(" "+fie.getName()+"='"+(this.getClass().getDeclaredMethod(getGetterName(fie.getName())).invoke(this))+"',");
					}
										
				}
						
			}
			str.delete(str.toString().length()-1, str.toString().length());		
			str.append(" where "+idName+"='"+(this.getClass().getDeclaredMethod(getGetterName(idName)).invoke(this).toString())+"'");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
	
	
	public void initBean(Map<String, Object> obj){		
		Field[] fields=this.getClass().getDeclaredFields();
		for(Field field:fields){
			if(obj.containsKey(field.getName())){
				setValueByPropName(field.getName(),obj.get(field.getName()));
			}
		}
	}	
	
	
}

