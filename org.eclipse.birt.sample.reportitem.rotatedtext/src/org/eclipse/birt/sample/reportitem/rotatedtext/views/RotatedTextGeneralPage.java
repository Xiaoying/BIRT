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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provide the general page content.
 */
public class RotatedTextGeneralPage extends AttributePage
{

	private TextSection librarySection;
	private SeperatorSection seperatorSection;

	public void refresh( )
	{
		if ( input instanceof List
				&& DEUtil.getMultiSelectionHandle( (List) input )
						.isExtendedElements( ) )
		{
			librarySection.setHidden( false );
			seperatorSection.setHidden( false );
			librarySection.load( );
		}
		else
		{
			librarySection.setHidden( true );
			seperatorSection.setHidden( true );
		}
		container.layout( true );
		container.redraw( );
		super.refresh( );
	}

	public void buildUI( Composite parent )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 5, 15 ) );

		LibraryDescriptorProvider provider = new LibraryDescriptorProvider( );
		librarySection = new TextSection( provider.getDisplayName( ),
				container,
				true );
		librarySection.setProvider( provider );
		librarySection.setGridPlaceholder( 2, true );
		addSection( RotatedTextPageSectionID.LIBRARY, librarySection );

		seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( RotatedTextPageSectionID.SEPERATOR, seperatorSection );

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( "displayText", //$NON-NLS-1$
				ReportDesignConstants.EXTENDED_ITEM );
		TextSection nameSection = new TextSection( "Display text:", //$NON-NLS-1$
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setGridPlaceholder( 3, true );
		nameSection.setWidth( 200 );
		addSection( RotatedTextPageSectionID.DISPLAY_TEXT, nameSection );

		TextPropertyDescriptorProvider angleProvider = new TextPropertyDescriptorProvider( "rotationAngle", //$NON-NLS-1$
				ReportDesignConstants.EXTENDED_ITEM );
		TextSection angleSection = new TextSection( "Rotation Angle:", //$NON-NLS-1$
				container,
				true );
		angleSection.setProvider( angleProvider );
		angleSection.setGridPlaceholder( 3, true );
		angleSection.setWidth( 200 );
		addSection( RotatedTextPageSectionID.ROTATION_ANGLE, angleSection );

		createSections( );
		layoutSections( );
	}
}
