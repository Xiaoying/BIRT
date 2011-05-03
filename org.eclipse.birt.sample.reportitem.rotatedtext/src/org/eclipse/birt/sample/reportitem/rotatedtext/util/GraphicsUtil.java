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

package org.eclipse.birt.sample.reportitem.rotatedtext.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class to draw the image.
 */
public class GraphicsUtil { 
	
	  private Display display = null;
	  private GC gc = null;
	  
	/**
	 * @param display
	 * @param gc
	 */
	private GraphicsUtil(Display display, GC gc) {
		super();
		this.display = display;
		this.gc = gc;
	}
	
	
	/**
	 * 
	 */
	public GraphicsUtil() {
		this.display = new Display();
		this.gc = new GC(display);

	}


	/**
	 * @return Returns the display.
	 */
	public Display getDisplay() {
		return display;
	}
	/**
	 * @param display The display to set.
	 */
	public void setDisplay(Display display) {
		this.display = display;
	}
	/**
	 * @return Returns the gc.
	 */
	public GC getGc() {
		return gc;
	}
	/**
	 * @param gc The gc to set.
	 */
	public void setGc(GC gc) {
		this.gc = gc;
	}
	  
	  /** 
	   * Creates an image containing the specified text, rotated either plus or minus 
	   * 90 degrees. 
	   * <dl> 
	   * <dt><b>Styles: </b></dt> 
	   * <dd>UP, DOWN</dd> 
	   * </dl> 
	   * 
	   * @param text the text to rotate 
	   * @param font the font to use 
	   * @param foreground the color for the text 
	   * @param background the background color 
	   * @param style direction to rotate (up or down) 
	   * @return Image 
	   *         <p> 
	   *         Note: Only one of the style UP or DOWN may be specified. 
	   *         </p> 
	   */ 
	  public Image createRotatedText( String text, Integer angle, boolean rtl ) 
	  {
	  	

	  	Image stringImage = null;

	  	try
		{
	  		// TO DO REMOVE before checkin
	  		
	  		if ( text == null || text.trim().length() == 0)
	  		{
	  			text = new String("Rotated Text Sample");
	  		}
	  		
		    //Display display = Display.getCurrent(); 
		    if (display == null) SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS); 
	
	
		    // Determine string's dimensions 
		    FontMetrics fm = gc.getFontMetrics(); 
		    Point pt = gc.textExtent(text); 
	
		    // Dispose that gc 
		    gc.dispose(); 
	
		    // Create an image the same size as the string 
		    stringImage = new Image(display, pt.x, pt.y); 
	
		    // Create a gc for the image 
		    if ( rtl )
				gc = new GC( stringImage, SWT.RIGHT_TO_LEFT );
			else
				gc = new GC( stringImage, SWT.LEFT_TO_RIGHT ); 
		    
		    // Draw the text onto the image 
		    gc.drawText(text, 0, 0); 
	
		    // Draw the image vertically onto the original GC 
		    Image image = createRotatedImage(stringImage, SWT.DOWN); 
	
		    // Dispose the new GC 
		    gc.dispose(); 
	
		    // Dispose the horizontal image 
		    stringImage.dispose(); 
	
		    // Return the rotated image 
		    return image;
		}
	  	catch(Exception e)
		{
	  		e.printStackTrace();
		}
	  	finally
		{
	  		//gc.dispose();
	  		//stringImage.dispose();
	  		//display.dispose();
		}
	  	
	  	return null;
	  } 


	  
	  /** 
	   * Creates a rotated image (plus or minus 90 degrees) 
	   * <dl> 
	   * <dt><b>Styles: </b></dt> 
	   * <dd>UP, DOWN</dd> 
	   * </dl> 
	   * 
	   * @param image the image to rotate 
	   * @param style direction to rotate (up or down) 
	   * @return Image 
	   *         <p> 
	   *         Note: Only one of the style UP or DOWN may be specified. 
	   *         </p> 
	   */ 
	  private  Image createRotatedImage(Image image, int style) 
	  { 
	   
	    if (display == null) SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS); 

	    // Use the image's data to create a rotated image's data 
	    ImageData sd = image.getImageData(); 
	    ImageData dd = new ImageData(sd.height, sd.width, sd.depth, sd.palette); 

	    // Determine which way to rotate, depending on up or down 
	    boolean up = (style & SWT.UP) == SWT.UP; 

	    // Run through the horizontal pixels 
	    for (int sx = 0; sx < sd.width; sx++) { 
	      // Run through the vertical pixels 
	      for (int sy = 0; sy < sd.height; sy++) {    // Determine where to move pixel to in destination image data 
	        int dx = up ? sy : sd.height - sy - 1; 
	        int dy = up ? sd.width - sx - 1 : sx; 

	        // Swap the x, y source data to y, x in the destination 
	        dd.setPixel(dx, dy, sd.getPixel(sx, sy)); 
	      } 
	    } 

	    // Create the vertical image 
	    return new Image(this.display, dd); 
	  } 
 
	
	public void cleanUp()
	{
		if ( gc != null && !gc.isDisposed())
		{
			gc.dispose();
		}

		if ( display != null && ! display.isDisposed())
		{
			display.dispose();
		}
		
	}

}