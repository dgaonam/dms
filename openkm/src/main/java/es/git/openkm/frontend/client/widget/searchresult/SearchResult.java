/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (C) 2006  GIT Consultors
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package es.git.openkm.frontend.client.widget.searchresult;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.SelectionGrid;
import com.google.gwt.widgetideas.table.client.ScrollTable.ScrollTableImages;

import es.git.openkm.frontend.client.Main;
import es.git.openkm.frontend.client.bean.GWTDocument;
import es.git.openkm.frontend.client.bean.GWTFolder;
import es.git.openkm.frontend.client.bean.GWTMail;
import es.git.openkm.frontend.client.bean.GWTQueryParams;
import es.git.openkm.frontend.client.bean.GWTQueryResult;
import es.git.openkm.frontend.client.bean.GWTResultSet;
import es.git.openkm.frontend.client.config.Config;
import es.git.openkm.frontend.client.service.OKMSearchService;
import es.git.openkm.frontend.client.service.OKMSearchServiceAsync;
import es.git.openkm.frontend.client.util.CommonUI;

/**
 * SearchResult
 * 
 * @author jllort
 *
 */
public class SearchResult extends Composite {
	
	private final OKMSearchServiceAsync searchService = (OKMSearchServiceAsync) GWT.create(OKMSearchService.class);
	
	public ExtendedScrollTable table;
	private FixedWidthFlexTable headerTable;
	private FixedWidthGrid dataTable;
	public MenuPopup menuPopup;
	public Status status;
	
	/**
	 * SearchResult
	 */
	public SearchResult() {
		menuPopup = new MenuPopup();
		menuPopup.setStyleName("okm-SearchResult-MenuPopup");	
		status = new Status();
		status.setStyleName("okm-StatusPopup");
		
		ScrollTableImages scrollTableImages = new ScrollTableImages(){
			public AbstractImagePrototype fillWidth() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/fill_width.gif");
					}
					public Image createImage() {
						return  new Image("img/fill_width.gif");
					}
					public String getHTML(){
						return "<img/>";
					}
				};
			}
			
			public AbstractImagePrototype scrollTableAscending() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/sort_asc.gif");
					}
					public Image createImage() {
						return  new Image("img/sort_asc.gif");
					}
					public String getHTML(){
						return "<img/>";
					}
				};
			}
			
			public AbstractImagePrototype scrollTableDescending() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/sort_desc.gif");
					}
					public Image createImage() {
						return  new Image("img/sort_desc.gif");
					}
					public String getHTML(){
						return "<img/>";
					}
				};
			}

			public AbstractImagePrototype scrollTableFillWidth() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/fill_width.gif");
					}
					public Image createImage() {
						return  new Image("img/fill_width.gif");
					}
					public String getHTML(){
						return "<img/>";
					}
				};
			}
		};
		
		headerTable = new FixedWidthFlexTable();
		dataTable = new FixedWidthGrid();
		table = new ExtendedScrollTable(dataTable,headerTable,scrollTableImages);
		table.setCellSpacing(0);
		table.setCellPadding(2);
		table.setSize("540","140");
		
		// Level 1 headers
	    headerTable.setHTML(0, 0, Main.i18n("search.result.score"));
	    headerTable.setHTML(0, 1, "&nbsp;");
	    headerTable.setHTML(0, 2, Main.i18n("search.result.name"));
	    headerTable.setHTML(0, 3, Main.i18n("search.result.size"));
	    headerTable.setHTML(0, 4, Main.i18n("search.result.date.update"));
	    headerTable.setHTML(0, 5, Main.i18n("search.result.author"));
	    headerTable.setHTML(0, 6, Main.i18n("search.result.version"));
		
		// Format    
	    table.setColumnWidth(0,70);
	    table.setColumnWidth(1,25);
	    table.setColumnWidth(2,150);
	    table.setColumnWidth(3,100);
	    table.setColumnWidth(4,150);
	    table.setColumnWidth(5,110);
	    table.setColumnWidth(6,90);
	    
	    // Table data
	    //dataTable.setHoveringPolicy(SortableFixedWidthGrid.HOVERING_POLICY_CELL);
	    dataTable.setSelectionPolicy(SelectionGrid.SelectionPolicy.ONE_ROW);
	    //dataTable.setMinHoverRow(0);
		
		table.addStyleName("okm-DisableSelect");
		table.addStyleName("okm-Input");
		
		initWidget(table);
	}
	
	/**
	 * Refreshing lang
	 */
	public void langRefresh() {
		headerTable.setHTML(0, 0, Main.i18n("search.result.score"));
		headerTable.setHTML(0, 2, Main.i18n("search.result.name"));
		headerTable.setHTML(0, 3, Main.i18n("search.result.size"));
		headerTable.setHTML(0, 4, Main.i18n("search.result.date.update"));
		headerTable.setHTML(0, 5, Main.i18n("search.result.author"));
		headerTable.setHTML(0, 6, Main.i18n("search.result.version"));
		menuPopup.langRefresh();
	}
	
	/**
	 * Removes all rows except the first
	 */
	public void removeAllRows() {
		// Purge all rows 
		while (dataTable.getRowCount() > 0) {
			dataTable.removeRow(0);
		}
		
		table.reset();
	}
	
	/**
	 * Adds a document to the panel
	 * 
	 * @param doc The doc to add
	 */
	private void addRow(GWTQueryResult gwtQueryResult) {
		 table.addRow(gwtQueryResult);
	 }
	
	/**
	 * Show the browser menu
	 */
	public void showMenu() {
		// The browser menu depends on actual view
		// Must substract top position from Y Screen Position
		menuPopup.setPopupPosition(table.getMouseX(), table.getMouseY());
		menuPopup.show();		
	}
	
	/**
	 * Download document
	 */
	public void downloadDocument() {
		if (!dataTable.getSelectedRows().isEmpty()) {
			Main.get().redirect = true;
			if (table.isDocumentSelected()) {
				Window.open(Config.OKMDownloadServlet +"?id=" + URL.encodeComponent(getDocument().getPath()), "_self", "");
			} else if (table.isAttachmentSelected()) {
				Window.open(Config.OKMDownloadServlet +"?id=" + URL.encodeComponent(getAttachment().getPath()), "_self", "");
			}
			Main.get().redirect = false;
		}
	}
	
	/**
	 * Open all folder path
	 */
	public void openAllFolderPath() {
		String docPath = "";
		String path = "";
		if (table.isDocumentSelected() || table.isAttachmentSelected()) {
			if (table.isAttachmentSelected()) {
				docPath = getAttachment().getParent();
			} else {
				docPath = getDocument().getPath();
			}
			path = docPath.substring(0,docPath.lastIndexOf("/"));
			
		} else if (table.isFolderSelected()) {
			path = getFolder().getPath();
			
		} else if (table.isMailSelected()) {
			docPath = getMail().getPath();
			path = docPath.substring(0,docPath.lastIndexOf("/"));
		}
		CommonUI.openAllFolderPath(path, docPath);
		menuPopup.hide();
	}
	
	/**
	 * Gets a actual document object row
	 * 
	 * @return The Document object value
	 */
	public GWTDocument getDocument() {
		//Row selected must be on table documents
		return table.getDocument();
	}
	
	/**
	 * Gets a actual attachment object row
	 * 
	 * @return The Attachment object value
	 */
	public GWTDocument getAttachment() {
		//Row selected must be on table documents
		return table.getAttachment();
	}
	
	/**
	 * Gets a actual folder object row
	 * 
	 * @return The folder object value
	 */
	public GWTFolder getFolder() {
		//Row selected must be on table documents
		return table.getFolder();
	}
	
	/**
	 * Gets a actual mail object row
	 * 
	 * @return The mail object value
	 */
	public GWTMail getMail() {
		//Row selected must be on table documents
		return table.getMail();
	}

	/**
	 * Call Back find paginated
	 */
	final AsyncCallback<GWTResultSet> callbackFindPaginated = new AsyncCallback<GWTResultSet>() {
		public void onSuccess(GWTResultSet result){
			GWTResultSet resultSet = result;	
			Main.get().mainPanel.search.searchIn.controlSearch.refreshControl(resultSet.getTotal());
			
			removeAllRows();
			int size = 0;
			
			for (Iterator<GWTQueryResult> it = resultSet.getResults().iterator(); it.hasNext();){
				GWTQueryResult gwtQueryResult = it.next();
				
				addRow(gwtQueryResult);
				size++;
			}
			
			// Only sets column auto size if rows are returned
			if (size>0) {
				// Sets the columns that mus auto fit column size
			    //table.autoFitColumnWidth(2);
			    //table.autoFitColumnWidth(3);
			    //table.autoFitColumnWidth(5);
			    //table.autoFitColumnWidth(7);
			}
			
			status.unsetFlag_findPaginated(); // TODO : falta modificar el nombre del flag
		}
		
		public void onFailure(Throwable caught) {
			status.unsetFlag_findPaginated();
			Main.get().showError("FindPaginated", caught);
		}
	};
	
	/**
	 * Call Back get search
	 */
	final AsyncCallback<GWTQueryParams> callbackGetSearch = new AsyncCallback<GWTQueryParams>() {
		public void onSuccess(GWTQueryParams result){
			GWTQueryParams gWTParams = result;	
			Main.get().mainPanel.search.searchIn.setSavedSearch(gWTParams);
			removeAllRows();
		}
		
		public void onFailure(Throwable caught) {
			Main.get().showError("getSearch", caught);
		}
	};
	
	/**
	 * Get the saved search
	 * 
	 * @param name The name of the search
	 */
	public void getSearch(String name) {
		ServiceDefTarget endPoint = (ServiceDefTarget) searchService;
		endPoint.setServiceEntryPoint(Config.OKMSearchService);
		searchService.getSearch(name, callbackGetSearch);
	}
	
	/**
	 * Find paginated
	 * 
	 * @param words The path id
	 */
	public void findPaginated(GWTQueryParams params, int offset, int limit) {
		status.setFlag_findPaginated();
		ServiceDefTarget endPoint = (ServiceDefTarget) searchService;
		endPoint.setServiceEntryPoint(Config.OKMSearchService);
		searchService.findPaginated(params, offset, limit, callbackFindPaginated);
	}
	
	/**
	 * Indicates if panel is selected
	 * 
	 * @return The value of panel ( selected )
	 */
	public boolean isPanelSelected(){
		return table.isPanelSelected();
	}
	
	/**
	 * Sets the selected panel value
	 * 
	 * @param selected The select panel value
	 */
	public void setSelectedPanel(boolean selected) {
		table.setSelectedPanel(selected);
	}
	
	/**
	 * Fix width
	 */
	public void fixWidth() {
		table.fillWidth();
	}
}