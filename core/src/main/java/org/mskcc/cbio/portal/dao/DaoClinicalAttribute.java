/*
 * Copyright (c) 2015 Memorial Sloan-Kettering Cancer Center.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS
 * FOR A PARTICULAR PURPOSE. The software and documentation provided hereunder
 * is on an "as is" basis, and Memorial Sloan-Kettering Cancer Center has no
 * obligations to provide maintenance, support, updates, enhancements or
 * modifications. In no event shall Memorial Sloan-Kettering Cancer Center be
 * liable to any party for direct, indirect, special, incidental or
 * consequential damages, including lost profits, arising out of the use of this
 * software and its documentation, even if Memorial Sloan-Kettering Cancer
 * Center has been advised of the possibility of such damage.
 */

/*
 * This file is part of cBioPortal.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.mskcc.cbio.portal.dao;

import org.mskcc.cbio.portal.model.*;

import com.google.inject.internal.Join;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.*;
import org.mskcc.cbio.portal.util.InternalIdUtil;

/**
 * Data Access Object for `clinical_attribute` table
 *
 * @author Gideon Dresdner
 */
public class DaoClinicalAttribute {

    public static int addDatum(ClinicalAttribute attr)  throws DaoException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = JdbcUtil.getDbConnection(DaoClinicalAttribute.class);
            pstmt = con.prepareStatement
                    ("INSERT INTO clinical_attribute(" +
                            "`ATTR_ID`," +
                            "`DISPLAY_NAME`," +
                            "`DESCRIPTION`," +
                            "`DATATYPE`," +
                            "`PATIENT_ATTRIBUTE`," +
                            "`PRIORITY`)" +
                            " VALUES(?,?,?,?,?,?)");
            pstmt.setString(1, attr.getAttrId());
            pstmt.setString(2, attr.getDisplayName());
            pstmt.setString(3, attr.getDescription());
            pstmt.setString(4, attr.getDatatype());
            pstmt.setBoolean(5, attr.isPatientAttribute());
            pstmt.setString(6, attr.getPriority());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            JdbcUtil.closeAll(DaoClinicalAttribute.class, con, pstmt, rs);
        }
    }

    private static ClinicalAttribute unpack(ResultSet rs) throws SQLException {
        return new ClinicalAttribute(rs.getString("ATTR_ID"),
                                     rs.getString("DISPLAY_NAME"),
                                     rs.getString("DESCRIPTION"),
                                     rs.getString("DATATYPE"),
                                     rs.getBoolean("PATIENT_ATTRIBUTE"),
                                     rs.getString("PRIORITY"));
    }
    
    public static ClinicalAttribute getDatum(String attrId) throws DaoException {
        List<ClinicalAttribute> attrs = getDatum(Arrays.asList(attrId));
        if (attrs.isEmpty()) {
            return null;
        }
        
        return attrs.get(0);
    }

    public static List<ClinicalAttribute> getDatum(Collection<String> attrIds) throws DaoException {
        if(attrIds == null || attrIds.isEmpty() ) {
            return Collections.emptyList();
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = JdbcUtil.getDbConnection(DaoClinicalAttribute.class);

            pstmt = con.prepareStatement("SELECT * FROM clinical_attribute WHERE ATTR_ID IN ('"
                    + StringUtils.join(attrIds,"','")+"') ");

            rs = pstmt.executeQuery();

            List<ClinicalAttribute> list = new ArrayList<ClinicalAttribute>();
            while (rs.next()) {
                list.add(unpack(rs));
            }
            
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            JdbcUtil.closeAll(DaoClinicalAttribute.class, con, pstmt, rs);
        }
    }

    public static List<ClinicalAttribute> getDataByStudy(int cancerStudyId) throws DaoException
    {
        List<ClinicalAttribute> attrs = new ArrayList<ClinicalAttribute>();
        List<Integer> patientIds = InternalIdUtil.getInternalPatientIds(cancerStudyId);
        attrs.addAll(getDataByInternalIds(patientIds, "clinical_patient"));
        
        List<Integer> sampleIds = InternalIdUtil.getInternalNonNormalSampleIds(cancerStudyId);
        attrs.addAll(getDataByInternalIds(sampleIds, "clinical_sample"));
        
        return attrs;
    }

    /**
     * Gets all the clinical attributes for a particular set of samples
     * Looks in the clinical table for all records associated with any of the samples, extracts and uniques
     * the attribute ids, then finally uses the attribute ids to fetch the clinical attributes from the db.
     *
     * @param sampleIdSet
     * @return
     * @throws DaoException
     */
    private static List<ClinicalAttribute> getDataByInternalIds(List<Integer> internalIds, String table) throws DaoException {
        
        Connection con = null;
        ResultSet rs = null;
		PreparedStatement pstmt = null;

        String sql = ("SELECT DISTINCT ATTR_ID FROM " + table
                + " WHERE INTERNAL_ID IN " +
                      "(" + StringUtils.join(internalIds, ",") + ")");

        Set<String> attrIds = new HashSet<String>();
        try {
            con = JdbcUtil.getDbConnection(DaoClinicalAttribute.class);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
             while(rs.next()) {
                attrIds.add(rs.getString("ATTR_ID"));
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            JdbcUtil.closeAll(DaoClinicalAttribute.class, con, pstmt, rs);
        }

        return getDatum(attrIds);
    }

    private static Collection<ClinicalAttribute> getAll() throws DaoException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Collection<ClinicalAttribute> all = new ArrayList<ClinicalAttribute>();

        try {
            con = JdbcUtil.getDbConnection(DaoClinicalAttribute.class);
            pstmt = con.prepareStatement("SELECT * FROM clinical_attribute");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                all.add(unpack(rs));
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            JdbcUtil.closeAll(DaoClinicalAttribute.class, con, pstmt, rs);
        }
        return all;
    }

	public static Map<String, String> getAllMap() throws DaoException {

		HashMap<String, String> toReturn = new HashMap<String, String>();
		for (ClinicalAttribute clinicalAttribute : DaoClinicalAttribute.getAll()) {
			toReturn.put(clinicalAttribute.getAttrId(), clinicalAttribute.getDisplayName());
		}
		return toReturn;
	}

    /**
     * Deletes all Records.
     * @throws DaoException DAO Error.
     */
    public static void deleteAllRecords() throws DaoException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = JdbcUtil.getDbConnection(DaoClinicalAttribute.class);
            pstmt = con.prepareStatement("TRUNCATE TABLE clinical_attribute");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            JdbcUtil.closeAll(DaoClinicalAttribute.class, con, pstmt, rs);
        }
    }
}
