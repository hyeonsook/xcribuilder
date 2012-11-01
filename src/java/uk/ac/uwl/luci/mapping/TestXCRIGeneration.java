package uk.ac.uwl.luci.mapping;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.xcri.profiles.x12.catalog.*;
import org.xcri.profiles.x12.catalog.CatalogDocument.Catalog;
public class TestXCRIGeneration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CatalogDocument catalog = new Hb2Xcri().buildCatalog();
			//catalog.save(new File("/Applications/xcri.xml"));
			System.out.println(catalog.xmlText());
                        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static CatalogDocument createCatalog()
	{
	    CatalogDocument newCatalogDoc = CatalogDocument.Factory.newInstance();
	    Catalog newCatalog = Catalog.Factory.newInstance();
	    newCatalog.setGenerated(Calendar.getInstance());
	    newCatalogDoc.setCatalog(newCatalog);

	    return newCatalogDoc;
	}

}
