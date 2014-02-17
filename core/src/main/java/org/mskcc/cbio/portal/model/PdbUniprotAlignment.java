/** Copyright (c) 2013 Memorial Sloan-Kettering Cancer Center.
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 **
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and
 ** Memorial Sloan-Kettering Cancer Center
 ** has no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall
 ** Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if
 ** Memorial Sloan-Kettering Cancer Center
 ** has been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/

package org.mskcc.cbio.portal.model;

/**
 * Class designed to represent a single row in the pdb_uniprot_alignment table.
 *
 * @author Selcuk Onur Sumer
 */
public class PdbUniprotAlignment
{
	private Integer alignmentId;
	private String pdbId;
	private String chain;
	private String uniprotAcc;
	private Integer pdbFrom;
	private Integer pdbTo;
	private Integer uniprotFrom;
	private Integer uniprotTo;

	public Integer getAlignmentId()
	{
		return alignmentId;
	}

	public void setAlignmentId(Integer alignmentId)
	{
		this.alignmentId = alignmentId;
	}

	public String getPdbId()
	{
		return pdbId;
	}

	public void setPdbId(String pdbId)
	{
		this.pdbId = pdbId;
	}

	public String getChain()
	{
		return chain;
	}

	public void setChain(String chain)
	{
		this.chain = chain;
	}

	public String getUniprotAcc()
	{
		return uniprotAcc;
	}

	public void setUniprotAcc(String uniprotAcc)
	{
		this.uniprotAcc = uniprotAcc;
	}

	public Integer getPdbFrom()
	{
		return pdbFrom;
	}

	public void setPdbFrom(Integer pdbFrom)
	{
		this.pdbFrom = pdbFrom;
	}

	public Integer getPdbTo()
	{
		return pdbTo;
	}

	public void setPdbTo(Integer pdbTo)
	{
		this.pdbTo = pdbTo;
	}

	public Integer getUniprotFrom()
	{
		return uniprotFrom;
	}

	public void setUniprotFrom(Integer uniprotFrom)
	{
		this.uniprotFrom = uniprotFrom;
	}

	public Integer getUniprotTo()
	{
		return uniprotTo;
	}

	public void setUniprotTo(Integer uniprotTo)
	{
		this.uniprotTo = uniprotTo;
	}
}
