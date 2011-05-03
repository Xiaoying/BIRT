/*
 * Created on Jan 17, 2005
 */

package com.eclipse.birt.converters.jasper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * @author Steve
 */
public class Jrxml2Rom
{
	private final JasperDesign jasperDesign;
	private ReportDesignHandle designHandle = null;
	private boolean generateFreeForm = false;

	private Jrxml2Rom(String jrxmlFileName) throws ConvertException
	{
		File sourceFile = new File(jrxmlFileName);
		try
		{
			jasperDesign = JRXmlLoader.load(sourceFile);
		}
		catch (Exception e)
		{
			throw new ConvertException("Unable to parse jrxml file", e);
		}
	}

	public void createROM(String romFileName) throws ConvertException
	{
		try
		{
			SessionHandle session = DesignEngine.newSession(null);
			designHandle = session.createDesign();
			addParameters();
			DataSetHandle dataSetHandle = addDataSet();
			addBody(dataSetHandle);
			SlotHandle componentSlot = designHandle.getComponents();
			SlotHandle dataSourceSlot = designHandle.getDataSources();
			SlotHandle masterPageSlot = designHandle.getMasterPages();
			SlotHandle styleSlot = designHandle.getStyles();
			designHandle.saveAs(romFileName);
		}
		catch (Exception e)
		{
			throw new ConvertException("Unable to create ROM", e);
		}
	}

	private void addBody(DataSetHandle dataSetHandle) throws SemanticException
	{
		SlotHandle slot = designHandle.getBody();
		ReportDesign design = designHandle.getDesign();
		JRBand jrBackgroundBand = jasperDesign.getBackground();
		JRBand jrColumnFooterBand = jasperDesign.getColumnFooter();
		JRBand jrColumnHeaderBand = jasperDesign.getColumnHeader();
		if (generateFreeForm)
		{
			ListItem list = new ListItem();
			ListHandle listHandle = new ListHandle(design, list);
			listHandle.setDataSet(dataSetHandle);
			addPageHeader(listHandle);
			addPageFooter(listHandle);
			addTitle(listHandle);
			addSummary(listHandle);
			addGroups(listHandle);
			addDetail(listHandle);
			slot.add(list);
		}
		else
		{
			TableItem table = new TableItem();
			TableHandle tableHandle = new TableHandle(design, table);
			// TODO sort elements by X position
			tableHandle.setDataSet(dataSetHandle);
			addColumns(tableHandle);
			addPageHeader(tableHandle);
			addPageFooter(tableHandle);
			addTitle(tableHandle);
			addSummary(tableHandle);
			addGroups(tableHandle);
			addDetail(tableHandle);
			slot.add(table);
			/*
			 * for (int i = 0; i < jrElements.length; i++) { TableColumn column =
			 * new TableColumn(); columnsSlot.add(column); }
			 * addDetail(tableHandle.getDetail());
			 */
		}
	}

	private void addPageHeader(ListHandle listHandle)
	{
		JRBand jrPageHeaderBand = jasperDesign.getPageHeader();
		// TODO finish
	}

	private void addPageHeader(TableHandle tableHandle)
	{
		JRBand jrPageHeaderBand = jasperDesign.getPageHeader();
		// TODO finish
	}

	private void addPageFooter(ListHandle listHandle)
	{
		JRBand jrPageFooterBand = jasperDesign.getPageFooter();
		// TODO finish
	}

	private void addPageFooter(TableHandle tableHandle)
	{
		JRBand jrPageFooterBand = jasperDesign.getPageFooter();
		// TODO finish
	}

	private void addTitle(ListHandle listHandle) throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		SlotHandle headerSlot = listHandle.getHeader();
		FreeForm freeForm = new FreeForm();
		FreeFormHandle freeFormHandle = new FreeFormHandle(design, freeForm);
		addListElements(
			jasperDesign.getTitle(),
			freeFormHandle.getReportItems());
		headerSlot.add(freeForm);
	}

	private void addTitle(TableHandle tableHandle) throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		SlotHandle headerSlot = tableHandle.getHeader();
		TableRow row = new TableRow();
		RowHandle rowHandle = new RowHandle(design, row);
		addTableElements(jasperDesign.getTitle(), rowHandle.getCells());
		headerSlot.add(row);
	}

	private void addSummary(ListHandle listHandle) throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		SlotHandle footerSlot = listHandle.getFooter();
		FreeForm freeForm = new FreeForm();
		FreeFormHandle freeFormHandle = new FreeFormHandle(design, freeForm);
		addListElements(
			jasperDesign.getSummary(),
			freeFormHandle.getReportItems());
		footerSlot.add(freeForm);
	}

	private void addSummary(TableHandle tableHandle) throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		SlotHandle footerSlot = tableHandle.getFooter();
		TableRow row = new TableRow();
		RowHandle rowHandle = new RowHandle(design, row);
		addTableElements(jasperDesign.getSummary(), rowHandle.getCells());
		footerSlot.add(row);
	}

	private void addGroups(ListHandle listHandle) throws SemanticException
	{
		JRGroup[] jrGroups = jasperDesign.getGroups();
		SlotHandle slot = listHandle.getGroups();
		for (int i = 0; i < jrGroups.length; i++)
			addListGroup(jrGroups[i], slot);
	}

	private void addListGroup(JRGroup jrGroup, SlotHandle slot)
		throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		ListGroup group = new ListGroup();
		ListGroupHandle groupHandle = new ListGroupHandle(design, group);
		SlotHandle headerSlot = groupHandle.getHeader();
		FreeForm headerFreeForm = new FreeForm();
		headerSlot.add(headerFreeForm);
		FreeFormHandle headerFreeFormHandle = new FreeFormHandle(design,
			headerFreeForm);
		addTableElements(
			jrGroup.getGroupHeader(),
			headerFreeFormHandle.getReportItems());
		SlotHandle footerSlot = groupHandle.getFooter();
		FreeForm footerFreeForm = new FreeForm();
		footerSlot.add(footerFreeForm);
		FreeFormHandle footerFreeFormHandle = new FreeFormHandle(design,
			footerFreeForm);
		addTableElements(
			jrGroup.getGroupFooter(),
			footerFreeFormHandle.getReportItems());
		slot.add(group);
	}

	private void addGroups(TableHandle tableHandle) throws SemanticException
	{
		JRGroup[] jrGroups = jasperDesign.getGroups();
		SlotHandle slot = tableHandle.getGroups();
		for (int i = 0; i < jrGroups.length; i++)
			addTableGroup(jrGroups[i], slot);
	}

	private void addTableGroup(JRGroup jrGroup, SlotHandle slot)
		throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		TableGroup group = new TableGroup();
		TableGroupHandle groupHandle = new TableGroupHandle(design, group);
		SlotHandle headerSlot = groupHandle.getHeader();
		TableRow headerRow = new TableRow();
		headerSlot.add(headerRow);
		RowHandle headerRowHandle = new RowHandle(design, headerRow);
		addTableElements(jrGroup.getGroupHeader(), headerRowHandle.getCells());
		SlotHandle footerSlot = groupHandle.getFooter();
		TableRow footerRow = new TableRow();
		footerSlot.add(footerRow);
		RowHandle footerRowHandle = new RowHandle(design, footerRow);
		addTableElements(jrGroup.getGroupFooter(), footerRowHandle.getCells());
		slot.add(group);
	}

	private void addDetail(ListHandle listHandle) throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		SlotHandle detailSlot = listHandle.getDetail();
		FreeForm freeForm = new FreeForm();
		FreeFormHandle freeFormHandle = new FreeFormHandle(design, freeForm);
		addListElements(
			jasperDesign.getDetail(),
			freeFormHandle.getReportItems());
		detailSlot.add(freeForm);
	}

	private void addColumns(TableHandle tableHandle) throws SemanticException
	{
		JRBand jrDetailBand = jasperDesign.getDetail();
		JRElement[] jrElements = jrDetailBand.getElements();
		SlotHandle slot = tableHandle.getColumns();
		for (int i = 0; i < jrElements.length; i++)
		{
			TableColumn column = new TableColumn();
			slot.add(column);
		}
	}

	private void addDetail(TableHandle tableHandle) throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		SlotHandle detailSlot = tableHandle.getDetail();
		TableRow row = new TableRow();
		RowHandle rowHandle = new RowHandle(design, row);
		addTableElements(jasperDesign.getDetail(), rowHandle.getCells());
		detailSlot.add(row);
	}

	private JRElement[] sortJrElements(JRElement[] jrElements)
	{
		ArrayList list = new ArrayList();
		for (int i = 0; i < jrElements.length; i++)
			list.add(jrElements[i]);
		Collections.sort(list, new JrElementComparator());
		JRElement[] result = new JRElement[list.size()];
		for (int i = 0; i < list.size(); i++)
			result[i] = (JRElement) list.get(i);
		return result;
	}

	private static class JrElementComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			if (arg0 instanceof JRElement && arg1 instanceof JRElement)
			{
				JRElement e0 = (JRElement) arg0;
				JRElement e1 = (JRElement) arg1;
				int i0 = e0.getX();
				int i1 = e1.getX();
				if (i0 > i1)
					return 1;
				if (i0 < i1)
					return -1;
			}
			return 0;
		}
	}

	private void addListElements(JRBand jrBand, SlotHandle slot)
		throws SemanticException
	{
		addListElements(jrBand.getElements(), slot);
	}

	private void addTableElements(JRBand jrBand, SlotHandle slot)
		throws SemanticException
	{
		addTableElements(sortJrElements(jrBand.getElements()), slot);
	}

	private void addListElements(JRElement[] jrElements, SlotHandle slot)
		throws SemanticException
	{
		for (int i = 0; i < jrElements.length; i++)
			addElement(jrElements[i], slot);
	}

	private void addTableElements(JRElement[] jrElements, SlotHandle slot)
		throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		for (int i = 0; i < jrElements.length; i++)
		{
			Cell cell = new Cell();
			CellHandle cellHandle = new CellHandle(design, cell);
			SlotHandle contentSlot = cellHandle.getContent();
			addElement(jrElements[i], contentSlot);
			slot.add(cell);
		}
	}

	private void addElement(JRElement jrElement, SlotHandle slot)
		throws SemanticException
	{
		ReportDesign design = designHandle.getDesign();
		ReportItemHandle reportItemHandle = null;
		if (jrElement instanceof JRDesignRectangle)
		{
		}
		else if (jrElement instanceof JRDesignEllipse)
		{
		}
		else if (jrElement instanceof JRDesignImage)
		{
		}
		else if (jrElement instanceof JRDesignLine)
		{
		}
		else if (jrElement instanceof JRDesignStaticText)
		{
			JRDesignStaticText jrDesignStaticText = (JRDesignStaticText) jrElement;
			String text = jrDesignStaticText.getText();
			Label label = new Label();
			LabelHandle labelHandle = new LabelHandle(design, label);
			reportItemHandle = labelHandle;
			labelHandle.setText(text);
			slot.add(label);
		}
		else if (jrElement instanceof JRDesignTextField)
		{
			JRDesignTextField jrDesignTextField = (JRDesignTextField) jrElement;
			JRExpression jrExpression = jrDesignTextField.getExpression();
			String romScript = jasperExpr2RomScript(jrExpression.getText());
			DataItem dataItem = new DataItem();
			DataItemHandle dataHandle = new DataItemHandle(design, dataItem);
			reportItemHandle = dataHandle;
			dataHandle.setValueExpr(romScript);
			slot.add(dataItem);
		}
		if (reportItemHandle != null)
		{
			reportItemHandle.setX(jrElement.getX() + "px");
			reportItemHandle.setY(jrElement.getY() + "px");
			reportItemHandle.setWidth(jrElement.getWidth() + "px");
			reportItemHandle.setHeight(jrElement.getHeight() + "px");
		}
	}

	private DataSetHandle addDataSet() throws Exception
	{
		SlotHandle slot = designHandle.getDataSets();
		ReportDesign design = designHandle.getDesign();
		JRQuery jrQuery = jasperDesign.getQuery();
		String queryText = jrQuery.getText();
		OdaDataSetHandle handle = null;
		if (queryText != null && queryText.length() > 0)
		{
			OdaDataSet dataSet = new OdaDataSet();
			handle = new OdaDataSetHandle(design, dataSet);
			handle.setProperty("name", "jdbc");
			handle.setProperty("queryType", "JdbcSelectDataSet");
			slot.add(dataSet);
			String queryScript = jasperString2RomScript(queryText);
			handle.setProperty("queryScript", queryScript);
		}
		return handle;
	}

	private String jasperExpr2RomScript(String source)
	{
		// TODO - handle java expessions
		source = source.replaceAll("'", "\\'");
		ArrayList partList = new ArrayList();
		int dollarLoc = source.indexOf("$");
		int startLoc = 0;
		while (dollarLoc >= 0)
		{
			String part = source.substring(startLoc, dollarLoc);
			partList.add(part);
			startLoc = dollarLoc + 1;
			if (dollarLoc == 0 || source.charAt(startLoc - 1) != '\\')
			{
				int keyLoc = startLoc;
				while (keyLoc < source.length() && source.charAt(keyLoc) == ' ')
					keyLoc++;
				if (keyLoc < source.length())
				{
					char subsType;
					switch (source.charAt(keyLoc))
					{
					case 'P' :
					case 'p' :
						subsType = 'P';
						break;
					case 'V' :
					case 'v' :
						subsType = 'V';
						break;
					case 'F' :
					case 'f' :
						subsType = 'F';
						break;
					default :
						subsType = '?';
					}
					if (subsType != '?')
					{
						int beginBracketLoc = keyLoc + 1;
						while (beginBracketLoc < source.length()
							&& source.charAt(beginBracketLoc) == ' ')
							beginBracketLoc++;
						if (beginBracketLoc < source.length())
						{
							if (source.charAt(beginBracketLoc) == '{')
							{
								beginBracketLoc += 1;
								int endBracketLoc = source.indexOf(
									'}',
									beginBracketLoc);
								if (endBracketLoc >= beginBracketLoc)
								{
									String identifier = source.substring(
										beginBracketLoc,
										endBracketLoc);
									switch (subsType)
									{
									case 'P' :
										partList.add("report.params['"
											+ identifier + "']");
										break;
									case 'V' :
										partList.add(identifier);
										break;
									case 'F' :
										partList.add("row['" + identifier
											+ "']");
										break;
									}
									startLoc = endBracketLoc + 1;
								}
							}
						}
					}
				}
			}
			dollarLoc = source.indexOf("$", startLoc);
		}
		String part = source.substring(startLoc);
		partList.add(part);
		StringBuffer buf = new StringBuffer();
		Iterator iterator = partList.iterator();
		while (iterator.hasNext())
		{
			part = (String) iterator.next();
			buf.append(part);
		}
		return buf.toString();
	}

	private String jasperString2RomScript(String source)
	{
		source = source.replaceAll("'", "\\'");
		ExprPartList partList = new ExprPartList();
		int dollarLoc = source.indexOf("$");
		int startLoc = 0;
		while (dollarLoc >= 0)
		{
			String part = source.substring(startLoc, dollarLoc);
			partList.addLiteral(part);
			startLoc = dollarLoc + 1;
			if (dollarLoc == 0 || source.charAt(startLoc - 1) != '\\')
			{
				int keyLoc = startLoc;
				while (keyLoc < source.length() && source.charAt(keyLoc) == ' ')
					keyLoc++;
				if (keyLoc < source.length())
				{
					char subsType;
					switch (source.charAt(keyLoc))
					{
					case 'P' :
					case 'p' :
						subsType = 'P';
						break;
					case 'V' :
					case 'v' :
						subsType = 'V';
						break;
					case 'F' :
					case 'f' :
						subsType = 'F';
						break;
					default :
						subsType = '?';
					}
					if (subsType != '?')
					{
						int beginBracketLoc = keyLoc + 1;
						while (beginBracketLoc < source.length()
							&& source.charAt(beginBracketLoc) == ' ')
							beginBracketLoc++;
						if (beginBracketLoc < source.length())
						{
							if (source.charAt(beginBracketLoc) == '{')
							{
								beginBracketLoc += 1;
								int endBracketLoc = source.indexOf(
									'}',
									beginBracketLoc);
								if (endBracketLoc >= beginBracketLoc)
								{
									String identifier = source.substring(
										beginBracketLoc,
										endBracketLoc);
									switch (subsType)
									{
									case 'P' :
										partList.addExpression("report.params['"
											+ identifier + "']");
										break;
									case 'V' :
										partList.addExpression(identifier);
										break;
									case 'F' :
										partList.addExpression("row['"
											+ identifier + "']");
										break;
									}
									startLoc = endBracketLoc + 1;
								}
							}
						}
					}
				}
			}
			dollarLoc = source.indexOf("$", startLoc);
		}
		String part = source.substring(startLoc);
		partList.addLiteral(part);
		return partList.toString();
	}

	private static class ExprPart
	{
		public final boolean literal;
		public String part;

		public ExprPart(boolean literal, String part)
		{
			this.literal = literal;
			this.part = part;
		}
	}

	private static class ExprPartList
	{
		public final ArrayList list = new ArrayList();

		public void addLiteral(String part)
		{
			int size = list.size();
			ExprPart lastExprPart = size == 0
				? null
				: (ExprPart) list.get(size - 1);
			if (lastExprPart != null && lastExprPart.literal)
				lastExprPart.part += part;
			else
			{
				lastExprPart = new ExprPart(true, part);
				list.add(lastExprPart);
			}
		}

		public void addExpression(String part)
		{
			ExprPart exprPart = new ExprPart(false, part);
			list.add(exprPart);
		}

		public String toString()
		{
			StringBuffer buf = new StringBuffer();
			String sep = "";
			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				ExprPart exprPart = (ExprPart) iterator.next();
				if (exprPart.part.length() > 0)
				{
					buf.append(sep);
					sep = "+";
					if (exprPart.literal)
						buf.append("'");
					buf.append(exprPart.part);
					if (exprPart.literal)
						buf.append("'");
				}
			}
			return buf.toString();
		}
	}

	private void addParameters() throws Exception
	{
		SlotHandle slot = designHandle.getParameters();
		ReportDesign design = designHandle.getDesign();
		JRParameter[] jrParameters = jasperDesign.getParameters();
		for (int i = 0; i < jrParameters.length; i++)
		{
			JRParameter jrParameter = jrParameters[i];
			String name = jrParameter.getName();
			JRExpression defaultValueExpr = jrParameter.getDefaultValueExpression();
			String description = jrParameter.getDescription();
			String valueClassName = jrParameter.getValueClassName();
			String jrDataType = null;
			if ("java.lang.String".equals(valueClassName))
				jrDataType = "string";
			else if ("java.lang.Integer".equals(valueClassName))
				jrDataType = "decimal";
			if (jrDataType != null)
			{
				ScalarParameter param = new ScalarParameter();
				ScalarParameterHandle handle = new ScalarParameterHandle(
					design, param);
				handle.setProperty("name", name);
				handle.setProperty("dataType", "string");
				handle.setProperty("displayName", description);
				slot.add(param);
			}
		}
	}

	public static class ConvertException extends Exception
	{
		private static final long serialVersionUID = 3257284712639640887L;

		public ConvertException()
		{
			super();
		}

		public ConvertException(String arg0)
		{
			super(arg0);
		}

		public ConvertException(String arg0, Throwable arg1)
		{
			super(arg0, arg1);
		}

		public ConvertException(Throwable arg0)
		{
			super(arg0);
		}
	}

	public static void main(String args[])
	{
		if (args.length < 1)
		{
			showUsage();
			return;
		}
		String jrxmlFileName = args[0];
		String romFileName;
		if (args.length == 1)
		{
			romFileName = jrxmlFileName;
			int i = romFileName.lastIndexOf(".");
			if (i >= 0)
				romFileName = romFileName.substring(0, i);
			romFileName += ".rom.xml";
		}
		else if (args.length == 2)
			romFileName = args[1];
		else
		{
			showUsage();
			return;
		}
		System.out.println("jrxml file name = " + jrxmlFileName);
		System.out.println("ROM file name = " + romFileName);
		Jrxml2Rom converter;
		try
		{
			converter = new Jrxml2Rom(jrxmlFileName);
		}
		catch (Exception e)
		{
			System.out.println("Unable to parse jrxml: " + e);
			return;
		}
		try
		{
			converter.createROM(romFileName);
		}
		catch (Exception e)
		{
			System.out.println("Unable to create ROM: " + e);
			return;
		}
	}

	public static void showUsage()
	{
		System.out.println("Usage:");
		System.out.println("  Jrxml2Rom <jrxmlFileName> [<romFileName>]");
	}

	public final boolean isGenerateFreeForm()
	{
		return generateFreeForm;
	}

	public final void setGenerateFreeForm(boolean generateFreeForm)
	{
		this.generateFreeForm = generateFreeForm;
	}
}
