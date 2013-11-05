/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2012, Red Hat Inc. or third-party contributors as
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
package org.hibernate.loader.plan.spi;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.engine.FetchStrategy;
import org.hibernate.loader.PropertyPath;
import org.hibernate.loader.plan.exec.process.spi.ResultSetProcessingContext;
import org.hibernate.type.Type;

/**
 * Contract for associations that are being fetched.
 * <p/>
 * NOTE : can represent components/embeddables
 *
 * @author Steve Ebersole
 */
public interface Fetch extends CopyableFetch {
	/**
	 * Obtain the owner of this fetch.
	 *
	 * @return The fetch owner.
	 */
	public FetchOwner getOwner();

	/**
	 * Get the property path to this fetch
	 *
	 * @return The property path
	 */
	public PropertyPath getPropertyPath();

	public Type getFetchedType();

	/**
	 * Gets the fetch strategy for this fetch.
	 *
	 * @return the fetch strategy for this fetch.
	 */
	public FetchStrategy getFetchStrategy();

	/**
	 * Is this fetch nullable?
	 *
	 * @return true, if this fetch is nullable; false, otherwise.
	 */
	public boolean isNullable();

	public String getAdditionalJoinConditions();

	/**
	 * Generates the SQL select fragments for this fetch.  A select fragment is the column and formula references.
	 *
	 * @return the select fragments
	 */
	public String[] toSqlSelectFragments(String alias);

//	public void hydrate(ResultSet resultSet, ResultSetProcessingContext context) throws SQLException;
//
//	public Object resolve(ResultSet resultSet, ResultSetProcessingContext context) throws SQLException;
//
//	public void read(ResultSet resultSet, ResultSetProcessingContext context, Object owner) throws SQLException;

	@Override
	public Fetch makeCopy(CopyContext copyContext, FetchOwner fetchOwnerCopy);
}
