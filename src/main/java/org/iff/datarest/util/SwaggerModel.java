/*******************************************************************************
 * Copyright (c) 2019-03-07 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.datarest.util;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.links.LinkParameter;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import io.swagger.v3.oas.models.tags.Tag;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * SwaggerModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-07
 * auto generate by qdp.
 */
public class SwaggerModel {
    //"query", "header", "path" or "cookie".
    public static final String IN_query = "query";
    public static final String IN_header = "header";
    public static final String IN_path = "path";
    public static final String IN_cookie = "cookie";
    //primitive, array, object
    public static final String STYLE_primitive = "primitive";
    public static final String STYLE_array = "array";
    public static final String STYLE_object = "object";
    //Security type
    public static final SecurityScheme.Type SECURITY_http = SecurityScheme.Type.HTTP;
    public static final SecurityScheme.Type SECURITY_apiKey = SecurityScheme.Type.APIKEY;
    public static final SecurityScheme.Type SECURITY_oauth2 = SecurityScheme.Type.OAUTH2;
    public static final SecurityScheme.Type SECURITY_openId = SecurityScheme.Type.OPENIDCONNECT;
    //SecuritySchema
    public static final String SECURITY_http_basic = "basic";
    public static final String SECURITY_http_bearer = "bearer";
    //Encoding style
    public static final Encoding.StyleEnum ENCODING_form = Encoding.StyleEnum.FORM;
    public static final Encoding.StyleEnum ENCODING_spaceDelimited = Encoding.StyleEnum.SPACE_DELIMITED;
    public static final Encoding.StyleEnum ENCODING_pipeDelimited = Encoding.StyleEnum.PIPE_DELIMITED;
    public static final Encoding.StyleEnum ENCODING_deepObject = Encoding.StyleEnum.DEEP_OBJECT;
    //Content type
    public static final String CONTENT_TYPE_json = "application/json";
    public static final String CONTENT_TYPE_xml = "application/xml";
    public static final String CONTENT_TYPE_text = "text/plain";
    public static final String CONTENT_TYPE_binnary = "application/octet-stream";
    public static final String CONTENT_TYPE_media = "image/*";
    public static final String CONTENT_TYPE_form = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_multipart = "multipart/mixed";
    //
    private static final Map<String, PathItem> paths = new TreeMap<>();
    private static final Map<String, Schema> typeSchemas = new TreeMap<>();

    public static void main(String[] args) {

        OpenAPI api = create().openapi("3.0.0")
                .info(info()
                        .title("Sample API").description("Optional multiline").version("0.1.9")
                        .contact(contact()
                                .email("iffiff1@gmail.com").name("TylerChen"))
                        .license(license()
                                .name("Apache License 2.0")))
                .addServersItem(server()
                        .url("http://api.example.com/v1").description("Main (production) server"))
                .addServersItem(server()
                        .url("http://staging-api.example.com").description("Internal staging server for testing"))
                .path("/users", path()
                        .post(operation()
                                .summary("Returns a list of users.").description("Optional extended description in CommonMark or HTML")
                                .addParametersItem(parameterPath()
                                        .name("userId").description("Parameter description in CommonMark or HTML.")
                                        .schema(schemaLong()
                                                .minimum(BigDecimal.ONE)))
                                .requestBody(requestBody()
                                        .required(true).content(content()
                                                .addMediaType(CONTENT_TYPE_json, mediaType()
                                                        .schema(schemaObject()
                                                                .addProperties("username", schemaString())))))
                                .responses(responses()
                                        .addApiResponse("200", response()
                                                .description("A JSON array of user names")
                                                .content(content()
                                                        .addMediaType(CONTENT_TYPE_json, mediaType()
                                                                .schema(schemaArray()
                                                                        .items(schemaString())))))
                                        .addApiResponse("400", response()
                                                .description("The specified user ID is invalid (not a number)."))
                                        .addApiResponse("404", response()
                                                .description("A user with the specified ID was not found."))
                                        .addApiResponse("500", response().$ref("500"))
                                        ._default(response()
                                                .description("Unexpected error"))))
                )
                .components(components()
                        .addSchemas("User", schema()
                                .addProperties("id", schemaString())
                                .addProperties("name", schemaString())
                                .addRequiredItem("id")
                                .addRequiredItem("name"))
                        .addSchemas(TestModel.class.getSimpleName(), ModelConverters.getInstance().read(TestModel.class).values().iterator().next())
                        .addResponses("500", response()
                                .description("default 500"))
                        .addSecuritySchemes("BasicAuth", securityScheme()
                                .type(SECURITY_http).scheme(SECURITY_http_basic)))
                .addSecurityItem(securityRequirement().addList("BasicAuth", Arrays.asList()));
        String pretty = Json.pretty(api);
        System.out.println(pretty);
    }

    public static OpenAPI create() {
        return new OpenAPI();
    }

    public static OpenAPI addRegisterPathItems(OpenAPI openAPI) {
        for (Map.Entry<String, PathItem> entry : paths.entrySet()) {
            openAPI.path(entry.getKey(), entry.getValue());
        }
        return openAPI;
    }

    public static OpenAPI addRegisterTypeSchemas(OpenAPI openAPI) {
        if (openAPI.getComponents() == null) {
            openAPI.components(components());
        }
        for (Map.Entry<String, Schema> entry : typeSchemas.entrySet()) {
            openAPI.getComponents().addSchemas(entry.getKey(), entry.getValue());
        }
        return openAPI;
    }

    public static String toJson(OpenAPI openAPI) {
        return Json.pretty(openAPI);
    }

    public static void addPath(String path, PathItem pathItem) {
        paths.put(path, pathItem);
    }

    public static void addSchema(Type type) {
        if (type instanceof Class) {
            if (typeSchemas.containsKey(((Class) type).getSimpleName())) {
                return;
            }
        }
        Map<String, Schema> read = ModelConverters.getInstance().read(type);
        typeSchemas.putAll(read);
    }

    public static ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation();
    }

    public static OAuthFlow oAuthFlow() {
        return new OAuthFlow();
    }

    public static OAuthFlows oAuthFlows() {
        return new OAuthFlows();
    }

    public static XML xml() {
        return new XML();
    }

    public static Discriminator discriminator() {
        return new Discriminator();
    }


    public static Tag tag() {
        return new Tag();
    }

    public static Encoding encoding() {
        return new Encoding();
    }

    public static EncodingProperty encodingProperty() {
        return new EncodingProperty();
    }

    public static Callback callback() {
        return new Callback();
    }

    public static LinkParameter linkParameter() {
        return new LinkParameter();
    }

    public static Link link() {
        return new Link();
    }

    public static Example example() {
        return new Example();
    }

    public static Header header() {
        return new Header();
    }

    public static SecurityRequirement securityRequirement() {
        return new SecurityRequirement();
    }

    public static SecurityScheme securityScheme() {
        return new SecurityScheme();
    }

    public static Scopes scopes() {
        return new Scopes();
    }

    public static Components components() {
        return new Components();
    }

    public static RequestBody requestBody() {
        return new RequestBody();
    }

    public static CookieParameter parameterCookie() {
        return new CookieParameter();
    }

    public static HeaderParameter parameterHeader() {
        return new HeaderParameter();
    }

    public static PathParameter parameterPath() {
        return new PathParameter();
    }

    public static QueryParameter parameterQuery() {
        return new QueryParameter();
    }

    public static Parameter parameter() {
        return new Parameter();
    }

    public static ArraySchema schemaArray() {
        return new ArraySchema();
    }

    public static BinarySchema schemaBinary() {
        return new BinarySchema();
    }

    public static BooleanSchema schemaBoolean() {
        return new BooleanSchema();
    }

    public static ByteArraySchema schemaByteArray() {
        return new ByteArraySchema();
    }

    public static ComposedSchema schemaComposed() {
        return new ComposedSchema();
    }

    public static DateSchema schemaDate() {
        return new DateSchema();
    }

    public static DateTimeSchema schemaDateTime() {
        return new DateTimeSchema();
    }

    public static EmailSchema schemaEmail() {
        return new EmailSchema();
    }

    public static FileSchema schemaFile() {
        return new FileSchema();
    }

    public static IntegerSchema schemaInteger() {
        return new IntegerSchema();
    }

    public static IntegerSchema schemaLong() {
        return new IntegerSchema().format("int64");
    }

    public static MapSchema schemaMap() {
        return new MapSchema();
    }

    public static NumberSchema schemaNumber() {
        return new NumberSchema();
    }

    public static NumberSchema schemaDouble() {
        return (NumberSchema) new NumberSchema().format("double");
    }

    public static NumberSchema schemaFloat() {
        return (NumberSchema) new NumberSchema().format("float");
    }

    public static ObjectSchema schemaObject() {
        return new ObjectSchema();
    }

    public static PasswordSchema schemaPassword() {
        return new PasswordSchema();
    }

    public static StringSchema schemaString() {
        return new StringSchema();
    }

    public static UUIDSchema schemaUUID() {
        return new UUIDSchema();
    }

    public static Schema schema() {
        return new Schema();
    }

    public static MediaType mediaType() {
        return new MediaType();
    }

    public static Content content() {
        return new Content();
    }

    public static ApiResponse response() {
        return new ApiResponse();
    }

    public static ApiResponses responses() {
        return new ApiResponses();
    }

    public static Operation operation() {
        return new Operation();
    }

    public static Server server() {
        return new Server();
    }

    public static ServerVariables serverVariables() {
        return new ServerVariables();
    }

    public static ServerVariable serverVariable() {
        return new ServerVariable();
    }

    public static PathItem path() {
        return new PathItem();
    }

    public static Paths paths() {
        return new Paths();
    }

    public static Info info() {
        return new Info();
    }

    public static Contact contact() {
        return new Contact();
    }

    public static License license() {
        return new License();
    }
}
