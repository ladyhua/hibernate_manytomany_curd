package com.hibernate.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestORM_CURD {
	public static SessionFactory sf=null;
	@BeforeClass
	public static void beforeClass(){
		//ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().configure().build();
		//MetadataImplementor metadata=(MetadataImplementor)new MetadataSources(serviceRegistry).buildMetadata();
		//new SchemaExport(metadata).create(false,true);
		sf=new Configuration().configure().buildSessionFactory();
	}
	@Test
	public void testUserSave() {
		User u1=new User();
		u1.setName("u1");
		User u2=new User();
		u2.setName("u2");
		Group g=new Group();
		g.setName("g1");
		u1.setGroup(g);

		
		
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		s.save(u1);
		//s.save(g);
		s.getTransaction().commit();
		
	}
	@Test
	public void testGroupSave() {
		User u1=new User();
		u1.setName("u1");
		User u2=new User();
		u2.setName("u2");
		Group g=new Group();
		g.setName("g1");
		g.getUsers().add(u1);
		g.getUsers().add(u2);
		u1.setGroup(g);
		u2.setGroup(g);
		
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		//s.save(u);
		s.save(g);
		s.getTransaction().commit();
		
	}
	@Test
	public void testGroupDelete(){
		testGroupSave();
		
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		Group g=s.get(Group.class, 1);
		s.delete(g);
		s.getTransaction().commit();
	}
	
	@Test
	public void testSaveTree(){
		Org o1=new Org();
		o1.setParent(null);
		o1.setName("总公司");
		Org o2=new Org();
		o2.setName("第一层分公司1");
		o2.setParent(o1);
		Org o3=new Org();
		o3.setName("第一层分公司2");
		o3.setParent(o1);
		Org o4=new Org();
		o4.setParent(o2);
		o4.setName("第二层分公司1");
		o1.getOrgs().add(o2);
		o1.getOrgs().add(o3);
		o2.getOrgs().add(o4);
		
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		s.save(o1);
		s.getTransaction().commit();
	}
	
	@Test
	public void testLodeTree(){
		testSaveTree();
		
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		Org o=s.load(Org.class, 1);
		print(o,0);
		s.getTransaction().commit();
	}
	private void print(Org o,int level) {
		String prestr="";
		for(int i=0;i<level;i++){
			prestr="----"+prestr;
		}
		System.out.println(prestr+o.getName());
		for(Org child:o.getOrgs()){
			print(child,level+1);
		}
		
	}
	@Test
	public void testSaveStudentCourse(){
		Student s1=new Student();
		s1.setName("张三");
		Course c1=new Course();
		
		c1.setName("计算机网络");
		Score score=new Score();
		score.setStudent(s1);
		score.setCourse(c1);
		score.setScore(100);
		
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		s.save(s1);
		s.save(c1);
		s.save(score);
		s.getTransaction().commit();
	}
	@Test
	public void testLodeStudentCourse(){
		testSaveStudentCourse();
		Session s=sf.getCurrentSession();
		s.beginTransaction();
		Student s1=s.load(Student.class, 7);
		for(Course c:s1.getCourses()){
			System.out.println(c.getName());
		}
	}
	public static void main(String[] args){
		beforeClass();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
