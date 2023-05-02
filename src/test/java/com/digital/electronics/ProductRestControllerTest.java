package com.digital.electronics;

import com.digital.electronics.model.Product;
import com.digital.electronics.repo.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronicsStoreApplication.class)
@WebAppConfiguration
public class ProductRestControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestControllerTest.class);

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Product> productList = new ArrayList<>();
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.productRepository.deleteAll();

        Date prodOneRelease, prodTwoRelease;

        prodOneRelease = new Date(1682079355570L);

        this.productList.add(productRepository.save(
                new Product(
                        "Smart phone",
                        "M-0048",
                        8890,
                        ft.parse("2023-04-21T12:15:55.570Z"),
                        "Smart phone with super fast charge 50Mp AI Dual Camera",
                        4.8f,
                        "https://openclipart.org/detail/18694/mobile-phone-with-big-screen",
                        400
                )));

        prodTwoRelease = new Date(1681560955570L);
        this.productList.add(productRepository.save(
                new Product(
                        "Camera",
                        "C-0022",
                        21155,
                        ft.parse("2023-04-15T12:15:55.570Z"),
                        "Camera Mirrorless changeable lens Camera ",
                        3.7f,
                        "https://openclipart.org/detail/168562/camera-no-filters",
                        1200
                )));
    }

    @Test
    public void productNotFound() throws Exception {
        logger.debug("Testing product not found response: ");
        mockMvc.perform(get("/products/0/")
                .content(this.json(new Product()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createProduct() throws Exception {
        logger.debug("Testing product creation");

        Date prodRelease = this.ft.parse("2023-04-21T12:15:55.570Z");

        String productJson = this.json(
                new Product(
                        "Smart phone",
                        "M-0048",
                        8890,
                        ft.parse("2023-04-21T12:15:55.570Z"),
                        "Smart phone with super fast charge 50Mp AI Dual Camera",
                        4.8f,
                        "https://openclipart.org/detail/18694/mobile-phone-with-big-screen",
                        400
                        ));

        this.mockMvc.perform(post("/products")
                .contentType(contentType).content(productJson))
        .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
