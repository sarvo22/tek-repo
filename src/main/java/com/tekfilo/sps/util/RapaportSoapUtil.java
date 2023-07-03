package com.tekfilo.sps.util;

import com.tekfilo.sps.ibot.entities.Rapaport;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public final class RapaportSoapUtil {
    private static final String TECHNET_NAMESPACE_PREFIX = "technet";
    private static final String WEBSERVICE_SECURE_URL = "https://technet.rapaport.com/webservices/prices/rapaportprices.asmx";
    //private static final String WEBSERVICE_INSECURE_URL = "http://technet.rapaport.com/webservices/prices/rapaportprices.asmx";

//    public static void main(String[] args) throws Exception {
//        final RapaportSoapUtil webServiceCodeSample = new RapaportSoapUtil();
//        final String authenticationTicket = webServiceCodeSample.login("om83mv8vo1teomidjqu1xexd3apfdy", "SoqK9vwM");
//        //webServiceCodeSample.getPrice(authenticationTicket, "Round", 0.4F, "D", "VS1");
//        webServiceCodeSample.getPriceSheet(authenticationTicket, Shapes.ROUND);
//    }


    public List<Rapaport> getRapaportPriceSheet(String shapeName,
                                                String apiEndPoint,
                                                String userName,
                                                String password) throws SOAPException, TransformerException {
        List<Rapaport> rapaportList = new ArrayList<>();
        // Login into Rapaport
        final String authenticationTicket = this.login(userName, password);
        rapaportList = this.getPriceSheet(authenticationTicket, shapeName);
        return rapaportList;
    }


    /**
     * Get the login token
     *
     * @param username
     * @param password
     * @return The authentication ticket
     * @throws SOAPException
     */
    private String login(final String username, final String password) throws SOAPException {
        final SOAPMessage soapMessage = getSoapMessage();
        final SOAPBody soapBody = soapMessage.getSOAPBody();
        final SOAPElement loginElement = soapBody.addChildElement("Login", TECHNET_NAMESPACE_PREFIX);
        loginElement.addChildElement("Username", TECHNET_NAMESPACE_PREFIX).addTextNode(username);
        loginElement.addChildElement("Password", TECHNET_NAMESPACE_PREFIX).addTextNode(password);
        soapMessage.saveChanges();
        final SOAPConnection soapConnection = getSoapConnection();
        final SOAPMessage soapMessageReply = soapConnection.call(soapMessage, WEBSERVICE_SECURE_URL);
        final String textContent = soapMessageReply.getSOAPHeader().getFirstChild().getTextContent();
        soapConnection.close();
        return textContent;
    }

    /**
     * Returns the price
     *
     * @param authenticationTicket
     * @param shape
     * @param size
     * @param color
     * @param clarity
     * @throws SOAPException
     */
    private void getPrice(final String authenticationTicket, final String shape, final float size, final String color,
                          final String clarity) throws SOAPException {
        final SOAPMessage soapMessage = getSoapMessage();

        addAuthenticationTicket(authenticationTicket, soapMessage);

        final SOAPBody soapBody = soapMessage.getSOAPBody();
        final SOAPElement getPriceElement = soapBody.addChildElement("GetPrice", TECHNET_NAMESPACE_PREFIX);
        getPriceElement.addChildElement("shape", TECHNET_NAMESPACE_PREFIX).addTextNode(shape);
        getPriceElement.addChildElement("size", TECHNET_NAMESPACE_PREFIX).addTextNode(String.valueOf(size));
        getPriceElement.addChildElement("color", TECHNET_NAMESPACE_PREFIX).addTextNode(color);
        getPriceElement.addChildElement("clarity", TECHNET_NAMESPACE_PREFIX).addTextNode(clarity);

        soapMessage.saveChanges();

        final SOAPConnection soapConnection = getSoapConnection();

        final SOAPMessage soapMessageReply = soapConnection.call(soapMessage, WEBSERVICE_SECURE_URL);

        final SOAPBody replyBody = soapMessageReply.getSOAPBody();
        final Node getPriceResponse = replyBody.getFirstChild();
        final Node getPriceResult = getPriceResponse.getFirstChild();

        final NodeList childNodes = getPriceResult.getChildNodes();
        final String replyShape = childNodes.item(0).getTextContent();
        final String lowSize = childNodes.item(1).getTextContent();


// ... etc etc
// You can create a bean that will encompass all elements

        soapConnection.close();
    }

    /**
     * Gets the price sheet
     *
     * @param authenticationTicket
     * @param shape
     * @throws SOAPException
     * @throws TransformerException
     */
    public List<Rapaport> getPriceSheet(final String authenticationTicket, final String shape)
            throws SOAPException, TransformerException {

        String rapaportDate = "";
        String shapeName = "";

        final SOAPMessage soapMessage = getSoapMessage();
        addAuthenticationTicket(authenticationTicket, soapMessage);
        final SOAPBody soapBody = soapMessage.getSOAPBody();
        final SOAPElement getPriceSheetElement =
                soapBody.addChildElement("GetPriceSheet", TECHNET_NAMESPACE_PREFIX);
        getPriceSheetElement.addChildElement(
                "shape", TECHNET_NAMESPACE_PREFIX).addTextNode(shape);
        soapMessage.saveChanges();
        final SOAPConnection soapConnection = getSoapConnection();
        final SOAPMessage soapMessageReply = soapConnection.call(soapMessage, WEBSERVICE_SECURE_URL);


        final SOAPBody replyBody = soapMessageReply.getSOAPBody();
//        for (int j = 0; j < childNodes.getLength(); j++) {
//            System.out.println(childNodes.item(j).getTextContent());
//        }

        NodeList result = replyBody.getElementsByTagName("GetPriceSheetResult");

        for (int i = 0; i < result.getLength(); i++) {
            NamedNodeMap map = result.item(i).getAttributes();
            for (int k = 0; k < map.getLength(); k++) {
                //System.out.println(map.item(0).getTextContent());
//                System.out.println(
//                        " Index = " + k +
//                                map.item(k).getNodeName() + "=" +
//                                map.item(k).getLocalName() + "=" +
//                                map.item(k).getTextContent());
                if (map.item(k).getNodeName().equalsIgnoreCase("Shape")) {
                    shapeName = map.item(k).getTextContent();
                }

                if (map.item(k).getNodeName().equalsIgnoreCase("Date")) {
                    rapaportDate = map.item(k).getTextContent();
                }
            }
        }

        List<Rapaport> rapaportList = new ArrayList<>();

        NodeList list = replyBody.getElementsByTagName("Table");
        //System.out.println("Table List {} " + "=" + list.getLength());
        //System.out.println("Color   Clarity     HighSize    Lowsize     Price");
        for (int i = 0; i < list.getLength(); i++) {
            final NodeList tableChildNodes = list.item(i).getChildNodes();
            Rapaport rapaport = new Rapaport();
            rapaport.setShape(shapeName);
            rapaport.setDate(convert2SqlDate(rapaportDate));
            rapaport.setColor(tableChildNodes.item(0).getTextContent());
            rapaport.setClarity(tableChildNodes.item(1).getTextContent());
            rapaport.setHighSize(Double.valueOf(tableChildNodes.item(2).getTextContent()));
            rapaport.setLowSize(Double.valueOf(tableChildNodes.item(3).getTextContent()));
            rapaport.setCurrency("USD");
            rapaport.setPrice(Double.valueOf(tableChildNodes.item(4).getTextContent()));
            rapaportList.add(rapaport);
//            System.out.println(tableChildNodes.item(0).getTextContent() + " " +
//                    tableChildNodes.item(1).getTextContent() + " " +
//                    tableChildNodes.item(2).getTextContent() + " " +
//                    tableChildNodes.item(3).getTextContent() + " " +
//                    tableChildNodes.item(4).getTextContent() + " "
//            );
        }

        soapConnection.close();
        return rapaportList;
    }

    private Date convert2SqlDate(String rapaportDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            java.util.Date dts = formatter.parse(rapaportDate);
            java.sql.Date sqlDate = new Date(dts.getTime());
            return sqlDate;
        } catch (ParseException ex) {
            log.error("Date Parser exception occur");
        }
        return null;
    }

    /**
     * Create a SOAP Connection
     *
     * @return
     * @throws UnsupportedOperationException
     * @throws SOAPException
     */
    private SOAPConnection getSoapConnection() throws UnsupportedOperationException, SOAPException {
        final SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        final SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        return soapConnection;
    }

    /**
     * Create the SOAP Message
     *
     * @return
     * @throws SOAPException
     */
    private SOAPMessage getSoapMessage() throws SOAPException {
        final MessageFactory messageFactory = MessageFactory.newInstance();
        final SOAPMessage soapMessage = messageFactory.createMessage();

// Object for message parts
        final SOAPPart soapPart = soapMessage.getSOAPPart();
        final SOAPEnvelope envelope = soapPart.getEnvelope();

        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("enc", "http://schemas.xmlsoap.org/soap/encoding/");
        envelope.addNamespaceDeclaration("env", "http://schemas.xmlsoap.org/soap/envelop/");

// add the technet namespace as "technet"
        envelope.addNamespaceDeclaration(TECHNET_NAMESPACE_PREFIX, "http://technet.rapaport.com/");

        envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        return soapMessage;
    }

    private void addAuthenticationTicket(final String authenticationTicket, final SOAPMessage soapMessage)
            throws SOAPException {
        final SOAPHeader header = soapMessage.getSOAPHeader();
        final SOAPElement authenticationTicketHeader =
                header.addChildElement("AuthenticationTicketHeader", TECHNET_NAMESPACE_PREFIX);
        authenticationTicketHeader.addChildElement(
                "Ticket", TECHNET_NAMESPACE_PREFIX).addTextNode(authenticationTicket);
    }

//    private enum Shapes {
//        ROUND("Round"), PEAR("Pear"),
//        OVAL("Oval");
//
//        private final String enumString;
//
//        private Shapes(final String enumString) {
//            this.enumString = enumString;
//        }
//    }
}
