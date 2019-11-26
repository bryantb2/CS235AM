package com.example.tideprediction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;

/************** ParseHandler **********************************************
 * This class contains helper methods for the SAX event-based parser
 * framework. These methods are tailored to parse the XML tide prediction
 * data files from https://tidesandcurrents.noaa.gov/noaacurrents/Regions
 *
 * This class is used by the Dal (Data Access Layer) class.
 * This class depends on the TideItem class.
 **************************************************************************/

public class ParseHandler extends DefaultHandler {

    private ArrayList<TideItem> tideItems; // Will hold the parser output
    private TideItem item;           // Holds data for one tide prediction
    private boolean isData = false;  // This and the other booleans are used by SAX
    private boolean isDay = false;
    private boolean isTime = false;
    private boolean isPredInFt = false;
    private boolean isPredInCm = false;
    private boolean isHighLow = false;

    public ArrayList<TideItem> getItems() {
        return tideItems;
    }

    @Override
    public void startDocument() throws SAXException {
        tideItems = new ArrayList<TideItem>();
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (qName.equals("item")) {
            item = new TideItem();
            //item.setForecastDate(atts.getValue(0));
            return;
        }
        else if (qName.equals("date")) {
            isData = true;
            return;
        }
        else if (qName.equals("day")) {
            //item.setLowTemp(atts.getValue(2));
            //item.setHighTemp(atts.getValue(3));
            isDay = true;
            return;
        }
        else if (qName.equals("time")) {
            //item.setPrecipitation(atts.getValue(0));
            isTime = true;
            return;
        }
        else if (qName.equals("pred_in_ft")) {
            //item.setSymbol(atts.getValue(2));
            isPredInFt = true;
            return;
        }
        else if (qName.equals("pred_in_cm")) {
            //item.setSymbol(atts.getValue(2));
            isPredInCm = true;
            return;
        }
        else if (qName.equals("highlow")) {
            //item.setSymbol(atts.getValue(2));
            isHighLow = true;
            return;
        }
    }

    @Override
    public void characters(char ch[], int start , int length){
        //print out the attributes' value
        String valueString = new String(ch, start, length);
        if (isData){
            item.setDate(valueString);
            isData = false;
        }
        else if (isDay){
            item.setDay(valueString);
            System.out.println(valueString);
            isDay = false;
        }
        else if (isTime){
            item.setTime(valueString);
            System.out.println(valueString);
            isTime = false;
        }
        else if (isPredInFt){
            item.setPredInFt(valueString);
            System.out.println(valueString);
            isPredInFt = false;
        }
        else if (isPredInCm){
            item.setPredInCm(valueString);
            System.out.println(valueString);
            isPredInCm = false;
        }
        else if (isHighLow){
            if (valueString.equals("H")){item.setHighlow("High");}
            else if (valueString.equals("L")){item.setHighlow("Low");}
            System.out.println(valueString);
            isHighLow = false;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            tideItems.add(item);
        }
        return;
    }
}
