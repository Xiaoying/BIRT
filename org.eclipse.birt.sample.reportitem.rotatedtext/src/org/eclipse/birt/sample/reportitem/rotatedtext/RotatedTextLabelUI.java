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

import org.eclipse.birt.report.designer.ui.extensions.IReportItemLabelProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * This class provides the representation of this element in the designer.
 *
 */
public class RotatedTextLabelUI implements IReportItemLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemLabelProvider#getLabel(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public String getLabel(ExtendedItemHandle handle) {
		return "Rotated Text";
	}

}
