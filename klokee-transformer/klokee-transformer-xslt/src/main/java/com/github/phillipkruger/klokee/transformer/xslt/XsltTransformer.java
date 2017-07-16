package com.github.phillipkruger.klokee.transformer.xslt;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.Transformer;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.java.Log;
import org.xml.sax.InputSource;

/**
 * Transformer that can push a message through XSLT
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 * TODO: support other protocols for the stylesheet
 * TODO: support other transformers (like XALAN)
 */
@Log
@Named("xslt")
public class XsltTransformer implements Transformer {
    private static final String NAME = "xslt";
    //private static final String XALAN = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
    private final TransformerFactory transFactory = TransformerFactory.newInstance();//XALAN, null);

    @Inject @KlokeeProperties
    protected Properties properties;
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public byte[] transform(byte[] input) {
        
        if(input!=null && input.length>0){
            String uri = properties.getProperty(XSL,null);
            if(uri!=null){
                try (InputStream xslStream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(uri));
                        InputStream xmlStream = new BufferedInputStream(new ByteArrayInputStream(input))
                     ){
                    log.log(Level.SEVERE,"Now transforming with [" + NAME + "] using [{0}]", uri);
                    return transform(xmlStream, xslStream, null, null);
                } catch (IOException | TransformerException ex) {
                     log.log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return input;
    }
    
    private byte[] transform(InputStream xmlStream, InputStream xslStream, Map<String, String> params, String stylesheetURI) throws TransformerException {
        StreamSource stylesheetStream = new StreamSource(xslStream);
        if (stylesheetURI != null && !stylesheetURI.isEmpty()) {
            stylesheetStream.setSystemId(stylesheetURI);
        }

        Templates templates = transFactory.newTemplates(stylesheetStream);
        javax.xml.transform.Transformer transformer = templates.newTransformer();

        // If there is any, pass on the parameters
        if (params != null && !params.isEmpty()) {
            params.keySet().forEach((key) -> {
                transformer.setParameter(key, params.get(key));
            });
        }
        InputSource inputSource = new InputSource(xmlStream);

        SAXSource saxSource = new SAXSource(inputSource);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            StreamResult streamResult = new StreamResult(baos);
            transformer.transform(saxSource, streamResult);
            baos.flush();
            if (log.isLoggable(Level.FINEST)) {
                String string = new String(baos.toByteArray(), "UTF-8");
                log.log(Level.FINEST, "XML file: {0}", string);
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new TransformerException(ex);
        }
    }
    
    private static final String XSL = "xsltUri";
}