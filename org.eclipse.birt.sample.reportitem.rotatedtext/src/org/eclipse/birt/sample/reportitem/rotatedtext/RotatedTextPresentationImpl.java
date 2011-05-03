/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  IBM Corporation  - Bidi direction of text
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.sample.reportitem.rotatedtext.util.GraphicsUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 *  This class provides the rendering capabilities for the sample report item
 *   When the Sample Report Item is used in a report design , and teh report is
 *   viewed, the methods in this class are invoked to perform the actal
 *   rendering  of the user defined report item.  
 */
public class RotatedTextPresentationImpl extends ReportItemPresentationBase 
{
	private File fRotatedTextImage = null;

	/**
	 *  
	 */
	private FileInputStream fis = null;
	private FileOutputStream fos = null;

	/**
	 *  
	 */
	private String sExtension = null;

	/**
	 *  
	 */
	//private RotatedText rotatedText  = null;
	
	private ReportItem rotatedText = null;
	private ExtendedItemHandle modelHandle = null;
	GraphicsUtil graphicsUtil = null;


	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject(ExtendedItemHandle modelHandle) 
	{
		
		IReportItem item = null;
/*		
		try
		{
			item = modelHandle.getReportItem( );
		}
		catch ( Exception e )
		{
		}
		
		if ( item == null )
		{
			try
			{
				modelHandle.loadExtendedElement( );
			}
			catch ( Exception eeex )
			{
				
			}
			item = ( (ExtendedItem) modelHandle.getElement( ) ).getExtendedElement( );
			if ( item == null )
			{
				return;
			}
		}
		
	*/	
		
		this.modelHandle = modelHandle;
		
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat(String outputFormat)
	{
		if ( outputFormat.equalsIgnoreCase( "HTML" ) )
		{
			sExtension = "PNG";
		}
		else if ( outputFormat.equalsIgnoreCase( "PDF" ) )
		{
			sExtension = "JPEG";
		}
		else
		{
			sExtension = outputFormat;
		}
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getOutputType()
	 */
	public int getOutputType() {
		return OUTPUT_AS_IMAGE;

	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets(IRowSet[] rowSets) throws BirtException 
	{
		
		if ( modelHandle == null )
		{
			return null;
		}

		Integer angle = new Integer(90);
		if ( modelHandle.getProperty("rotationAngle") != null )
		{
		 angle =  (Integer) modelHandle.getProperty("rotationAngle");
		}
		
		
		
		String text = "Rotated Text";
		if ( modelHandle.getProperty("displayText") != null )
		{
			text = (String)(modelHandle.getProperty("displayText"));	
		}
		
		
		
		graphicsUtil = new GraphicsUtil();
		org.eclipse.swt.graphics.Image rotatedImage = graphicsUtil
				.createRotatedText( text, angle, modelHandle.isDirectionRTL( ) );
		
		// Test
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] {rotatedImage.getImageData()};

		try
		{
			fRotatedTextImage = File.createTempFile( "test", "." + sExtension );
			fos = new FileOutputStream(fRotatedTextImage.getPath());
			fis = new FileInputStream( fRotatedTextImage.getPath( ) );
			 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		imageLoader.save(fos,SWT.IMAGE_JPEG);
		
		return fis;
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#finish()
	 */
	public void finish() 
	{
		super.finish();
		
		if ( graphicsUtil != null )
		{
			graphicsUtil.cleanUp();
		}
		
		try {
			fos.close();
			fis.close();
			fRotatedTextImage.delete();
			
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
		
		}
	
		
	}
	
	

}
