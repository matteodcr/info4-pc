package Main;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import prodcons.v2.ProdConsSync;
import prodcons.v3.ProdConsBuffer3;
import prodcons.v5.ProdConsBuffer5;

public class TestProdCons {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
    	
    	IProdConsBuffer buff = new ProdConsBuffer5(bufSz);
    	
    	ProdConsSync synchronizer2 = new ProdConsSync(properties, buff);
    	synchronizer2.start();
    }
}