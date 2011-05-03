/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItemFactory;

/**
 *  This class provides methods which are called to create a new report
 *  Item , when the user drags and drops the Item from the Palette View
 *  to the report design.
 */
public class RotatedTextItemFactoryImpl extends ReportItemFactory
{

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.extension.ReportItemFactory#newReportItem(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public IReportItem newReportItem(DesignElementHandle extendedItemHandle) {
		return new ReportItem();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.extension.ReportItemFactory#getMessages()
	 */
	public IMessages getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

}
