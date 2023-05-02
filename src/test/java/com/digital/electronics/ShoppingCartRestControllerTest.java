package com.digital.electronics;

import com.digital.electronics.model.Product;
import com.digital.electronics.model.ShoppingCart;
import com.digital.electronics.repo.ProductRepository;
import com.digital.electronics.repo.ShoppingCartRepository;
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
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronicsStoreApplication.class)
@WebAppConfiguration
public class ShoppingCartRestControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartRestControllerTest.class);

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<ShoppingCart> cartList = new ArrayList<ShoppingCart>();
    private List<Product> productList = new ArrayList<Product>();
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

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

        this.shoppingCartRepository.deleteAll();
        this.productRepository.deleteAll();

        Date prodOneRelease, prodTwoRelease, orderDate;

        prodOneRelease = this.ft.parse("2023-04-21T12:15:55.570Z");

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

        prodTwoRelease = this.ft.parse("2023-04-15T12:15:55.570Z");
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

        productList = productRepository.findAll();
        orderDate = this.ft.parse("2023-04-15T12:15:55.570Z");
        HashMap<String, Product> productHashMap = new HashMap<String, Product>();
        HashMap<String, Integer> productQuantities = new HashMap<String, Integer>();
        int amount = 1;
        for(Product prod : productList) {
            productHashMap.put(prod.getId(), prod);
            productQuantities.put(prod.getId(), amount);
            amount++;
        }

        this.cartList.add(shoppingCartRepository.save(
                new ShoppingCart(
                        "pending",
                        "testuser",
                        productHashMap,
                        productQuantities,
                        orderDate,
                        orderDate,
                        0
                )));
    }

    @Test
    public void shoppingCartNotFound() throws Exception {
        logger.debug("Testing shopping cart not found response ");
        mockMvc.perform(get("/carts/0")
                .content(this.json(new ShoppingCart()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createCart() throws Exception {
        logger.debug("Testing product creation ");

        Date date = this.ft.parse("2023-04-04T20:15:55.570Z");
        List<ShoppingCart> previousTest = this.shoppingCartRepository.findByUserName("testuser");

        for(ShoppingCart cart : previousTest) this.shoppingCartRepository.delete(cart);

        List<Product> previousProdTest = this.productRepository.findByProductCode("GE-test");
        for(Product prod : previousProdTest) this.productRepository.delete(prod);


        productRepository.save(
                new Product(
                        "Camera",
                        "C-0022",
                        21155,
                        ft.parse("2023-04-15T12:15:55.570Z"),
                        "Camera Mirrorless changeable lens Camera ",
                        3.7f,
                        "https://openclipart.org/detail/168562/camera-no-filters",
                        1200
                ));
        productRepository.save(
                new Product(
                        "Video Game Controller",
                        "VG-0042",
                        3595,
                        ft.parse("2023-02-15T14:15:55.570Z"),
                        "Wireless gaming Controler with high quality sound and dual vibrate plug",
                        4.6f,
                        "http://openclipart.org/image/300px/svg_to_png/120337/xbox-controller_01.png",
                        300
                ));


        List<Product> prodTest = this.productRepository.findByProductCode("C-0022");
        HashMap<String, Product> productHashMap = new HashMap<String, Product>();
        HashMap<String, Integer> productQuantities = new HashMap<String, Integer>();
        int amount = 1;

        for(Product prod : prodTest) {
            productHashMap.put(prod.getId(), prod);
            productQuantities.put(prod.getId(), amount);
            amount++;
        }

        String cartJson = this.json(
                new ShoppingCart(
                        "pending",
                        "testuser",
                        productHashMap,
                        productQuantities,
                        date,
                        date,
                        0
                        ));

        this.mockMvc.perform(post("/carts")
                .contentType(contentType).content(cartJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
