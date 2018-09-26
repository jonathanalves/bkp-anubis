package anubis.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {

	public static <T> T convertXmlToObject(File file, Class<T> classe) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classe);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return classe.cast(jaxbUnmarshaller.unmarshal(file));
    }

	@SuppressWarnings("unchecked")
	public static <T> T convertXmlToObject(String xml, Class<T> classe) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classe);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return (T) jaxbUnmarshaller.unmarshal(reader);
    }

	public static String convertObjectToXml(Object obj) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		marshaller.marshal(obj, sw);
        return sw.toString();
	}

}