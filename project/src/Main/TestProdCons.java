package Main;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import prodcons.v1.ProdConsBuffer;

public class TestProdCons {
    public static void main(String[] args) {
        /*Properties properties = new Properties();
        try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        int nProd = Integer.parseInt(properties.getProperty("nProd"));
        int nCons = Integer.parseInt(properties.getProperty("nCons"));
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
    	int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
    	int consTime = Integer.parseInt(properties.getProperty("consTime"));
    	int minProd = Integer.parseInt(properties.getProperty("minProd"));
    	int maxProd = Integer.parseInt(properties.getProperty("maxProd"));*/
        int nProd = 15;
        int nCons = 10;
        int bufSz = 2;
    	int prodTime = 10;
    	int consTime = 10;
    	int minProd = 5;
    	int maxProd = 500;
    	
    	IProdConsBuffer buff = new ProdConsBuffer(bufSz);
    	
    	Producer[] prod = new Producer[nProd];
    	for(int i=0; i<nProd; i++) {
    		prod[i] = new Producer(buff, prodTime, minProd, maxProd);
    	}
    	
    	Consumer[] cons = new Consumer[nCons];
    	for(int i=0; i<nCons; i++) {
    		cons[i] = new Consumer(buff, consTime);
    	}
    	
    	for(Producer p : prod)
    		p.start();
    	for(Consumer c : cons)
    		c.start();
    }
}