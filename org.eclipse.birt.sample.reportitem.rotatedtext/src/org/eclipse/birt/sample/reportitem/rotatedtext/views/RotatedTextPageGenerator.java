/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext.views;

import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.swt.widgets.Composite;

/**
 * This class implements the IPageGenerator interface, which creates specific
 * property pages for the extended item.
 */
public class RotatedTextPageGenerator extends AbstractPageGenerator
{

	public void createControl( Composite parent, Object input )
	{
		setCategoryProvider( RotatedTextCategoryProviderFactory.getInstance( )
				.getCategoryProvider( input ) );
		super.createControl( parent, input );
	}
}
