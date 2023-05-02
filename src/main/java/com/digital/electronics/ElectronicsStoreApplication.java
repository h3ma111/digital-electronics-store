package com.digital.electronics;

import com.digital.electronics.model.CartUser;
import com.digital.electronics.model.Product;
import com.digital.electronics.repo.CartUserRepository;
import com.digital.electronics.repo.ProductRepository;
import com.digital.electronics.repo.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class ElectronicsStoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(ElectronicsStoreApplication.class);
    @Autowired
    private CartUserRepository cartUserRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public ElectronicsStoreApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(ElectronicsStoreApplication.class, args);
        logger.info("Electronics App Started Successfully");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            logger.info("Reset data");
            cartUserRepository.deleteAll();
            productRepository.deleteAll();
            shoppingCartRepository.deleteAll();

            //Added Dummy Users
            cartUserRepository.save(new CartUser("Kevin", "Chung", "kevinc", bCryptPasswordEncoder.encode("kevin"), "kevin@gmail.com"));
            cartUserRepository.save(new CartUser("John", "Walter", "johnw", bCryptPasswordEncoder.encode("john"), "john@gmail.com"));

            //Initialize the default products
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


            productRepository.save(
                    new Product(
                            "Sound System",
                            "S-0011",
                            10995,
                            ft.parse("2023-03-19T15:15:55.570Z"),
                            "Sound System with 2 way bass reflex active speakers",
                            3.2f,
                            "https://openclipart.org/detail/315898/sound-system",
                            400
                    ));
            productRepository.save(
                    new Product(
                            "Television",
                            "T-0023",
                            13295,
                            ft.parse("2023-03-18T08:15:55.570Z"),
                            "Color Old Funky Tv",
                            4.2f,
                            "https://openclipart.org/detail/90871/funky-old-tv",
                            20
                    ));
            productRepository.save(
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

            for (CartUser cartUser : cartUserRepository.findAll()) {
                logger.debug("Cart users: {}", cartUser);
            }
            for (Product product : productRepository.findAll()) {
                logger.debug("Cart product: {}", product);
            }
        };
    }

}
