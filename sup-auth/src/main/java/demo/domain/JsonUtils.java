package demo.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();

        OBJECT_MAPPER
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .getSerializerProvider()
                .setNullKeySerializer(new NullKeySerialize());

        OBJECT_MAPPER.setVisibility(OBJECT_MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE))
                .enable(SerializationFeature.INDENT_OUTPUT);

    }

    public static <T> String toJson(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("An error occur when parse json.", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.<T>readValue(json, type);
        } catch (Exception e) {
            throw new JsonSerializationException("An error occur when parse json.", e);
        }
    }

    public static <T> Map toMap(T object) {
        try {
            return OBJECT_MAPPER.convertValue(object, LinkedHashMap.class);
        } catch (IllegalArgumentException e) {
            throw new JsonSerializationException("An error occur when parse json.", e);
        }
    }

    public static <T> byte[] toBytes(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new JsonSerializationException("An error occur when parse json.", e);
        }
    }

    public static <E, T extends Collection<E>> T toCollection(String json, Class<T> collectionType, Class<E> elementType)
            throws JsonSerializationException {
        try {
            return OBJECT_MAPPER.readValue(json, constructCollectionType(collectionType, elementType));
        } catch (Exception e) {
            throw new JsonSerializationException("Cannot convert Json to Object type "
                    + constructCollectionType(collectionType, elementType).toString(), e);
        }
    }

    public static JavaType constructCollectionType(Class<? extends Collection<?>> collectionClass,
                                                   Class<?> elementType) {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementType);
    }
}

class NullKeySerialize extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName("null");
    }
}

class JsonSerializationException extends RuntimeException {

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
