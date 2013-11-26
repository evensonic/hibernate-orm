/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2006-2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.test.manytomany.mapunique;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

public class ManyToManyUniqueDelOrphanTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "manytomany/mapunique/UserGroup.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "false");
	}

	@Test
	public void testManyToManyWithCascadeDeleteOrphan() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User gavin = new User("gavin", "jboss");
		Group seam = new Group("seam", "jboss");
		seam.setGroupType(1);
		Group hb = new Group("hibernate", "jboss");
		hb.setGroupType(2);
		gavin.getGroups().put(seam.getGroupType(), seam);
		gavin.getGroups().put(hb.getGroupType(), hb);
		s.persist(gavin);
		s.persist(seam);
		s.persist(hb);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.get(User.class, "gavin");
		assertEquals( 2, gavin.getGroups().size() );
		seam = (Group) s.get(Group.class, "seam");
		assertEquals((Integer) 1, seam.getGroupType() );
		hb = (Group) s.get(Group.class, "hibernate");
		assertEquals((Integer) 2, hb.getGroupType() );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.get(User.class, "gavin");
		assertEquals(2, gavin.getGroups().size());
		hb = (Group) s.get(Group.class, "hibernate");
		gavin.getGroups().remove(hb.getGroupType());
		assertEquals(1, gavin.getGroups().size());
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.get(User.class, "gavin");
		assertEquals(1, gavin.getGroups().size());
		t.commit();
		s.close();
		
		//Verify orphan group was deleted
		s = openSession();
		t = s.beginTransaction();
		List<Group> groups = s.createCriteria(Group.class).list();
		assertEquals( 1, groups.size() );
		assertEquals( "seam", groups.get(0).getName() );
		t.commit();
		s.close();
	}
}

