package com.genka.orderservice.infra.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genka.orderservice.application.messaging.dtos.OrderPlacedEvent;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemInput;
import com.genka.orderservice.application.usecases.order.dtos.PlaceOrderInput;
import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.OrderItem;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;
import com.genka.orderservice.infra.repositories.order.mysql.OrderDatabaseGatewayMysql;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.awaitility.Durations.*;

@ActiveProfiles("test")
@Testcontainers
@AutoConfigureWireMock(port = 8088)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerImplIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OrderDatabaseGatewayMysql orderDatabaseGatewayMysql;
    @Container
    @ServiceConnection
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    @Container
    @ServiceConnection
    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
    private OrderPlacedEvent publishedOrderPlacedEvent;

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    public void placeOrderEndpoint() {
        String buyerEmailAddress = "buyer@email.address.com";
        OrderItemInput orderItemInput = OrderItemInput.builder()
                .name("item name")
                .productId(UUID.randomUUID())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100))
                .build();
        PlaceOrderInput placeOrderInput = PlaceOrderInput.builder()
                .items(Collections.singletonList(orderItemInput))
                .buyerEmailAddress(buyerEmailAddress)
                .build();
        OrderItem expectedOrderItem = OrderItem.builder()
                .name(orderItemInput.getName())
                .productId(orderItemInput.getProductId())
                .quantity(orderItemInput.getQuantity())
                .unitPrice(orderItemInput.getUnitPrice())
                .totalPrice(orderItemInput.getUnitPrice().multiply(BigDecimal.valueOf(orderItemInput.getQuantity())))
                .build();
        Order expectedOrder = Order.builder()
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(expectedOrderItem))
                .buyerEmailAddress("buyer@email.address.com")
                .build();
        OrderDTO expectedPlacedOrderDTO = OrderDTO.mapFromEntity(expectedOrder);
        String inventoryStockAvailabilitiesMockedResponse = "[{\"productId\": \"" + orderItemInput.getProductId() + "\",\"availability\": true}]".trim();
        stubFor(post(urlPathMatching("/inventories/availabilities"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(inventoryStockAvailabilitiesMockedResponse)));
        stubFor(post(urlPathMatching("/inventories/stocks/decrement"))
                .willReturn(aResponse()
                        .withStatus(200)));
        ResponseEntity<OrderDTO> placedOrderDTOResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                placeOrderInput,
                OrderDTO.class
        );
        Order savedOrder = orderDatabaseGatewayMysql.findOrdersByBuyerEmailAddress(buyerEmailAddress).get(0);
        System.out.println(savedOrder);
        System.out.println(placedOrderDTOResponseEntity.getBody());
        OrderPlacedEvent expectedOrderPlacedEvent = OrderPlacedEvent.mapFromEntity(savedOrder);
        //Assert that no error was thrown
        assertEquals(HttpStatus.CREATED, placedOrderDTOResponseEntity.getStatusCode());
        //Compare the Order saved in the database with the expected Order
        assertThat(savedOrder)
                .usingRecursiveComparison()
                .ignoringFields("id", "items")
                .isEqualTo(expectedOrder);
        //Compare the OrderItem saved in the database with the expected OrderItem
        assertThat(savedOrder.getItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedOrder.getItems().get(0));
        //Compare the placed OrderDTO response with the expected OrderDTO
        assertNotNull(placedOrderDTOResponseEntity.getBody());
        assertThat(placedOrderDTOResponseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id", "items")
                .isEqualTo(expectedPlacedOrderDTO);
        //Compare the placed OrderItemDTO response with the expected OrderItemDTO
        assertThat(placedOrderDTOResponseEntity.getBody().getItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedPlacedOrderDTO.getItems().get(0));
        //Compare the published OrderPlacedEvent with the expected OrderPlacedEvent
        await().atMost(ONE_MINUTE).untilAsserted(() -> {
            assertThat(publishedOrderPlacedEvent)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedOrderPlacedEvent);
        });
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    public void getBuyerOrdersEndpoint() {
        String buyerEmailAddress = "buyer@email.address.com";
        OrderItem orderItem = OrderItem.builder()
                .name("item name")
                .productId(UUID.randomUUID())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(200))
                .totalPrice(BigDecimal.valueOf(200))
                .build();
        Order orderToSave = Order.builder()
                .price(BigDecimal.valueOf(200))
                .items(Collections.singletonList(orderItem))
                .buyerEmailAddress(buyerEmailAddress)
                .build();
        Order savedOrder = this.orderDatabaseGatewayMysql.saveOrder(orderToSave);
        OrderDTO expectedOrderDTO = OrderDTO.mapFromEntity(savedOrder);
        ResponseEntity<List<OrderDTO>> buyerOrdersDTOResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/orders/buyers/" + buyerEmailAddress,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        System.out.println(buyerOrdersDTOResponseEntity.getBody());
        assertNotNull(buyerOrdersDTOResponseEntity.getBody());
        assertFalse(buyerOrdersDTOResponseEntity.getBody().isEmpty());
        assertThat(buyerOrdersDTOResponseEntity.getBody())
                .usingRecursiveComparison()
                .isEqualTo(Collections.singletonList(expectedOrderDTO));
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    public void getOneOrderEndpoint() {
        String buyerEmailAddress = "buyer@email.address.com";
        OrderItem orderItem = OrderItem.builder()
                .name("item name")
                .productId(UUID.randomUUID())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(300))
                .totalPrice(BigDecimal.valueOf(300))
                .build();
        Order orderToSave = Order.builder()
                .price(BigDecimal.valueOf(300))
                .items(Collections.singletonList(orderItem))
                .buyerEmailAddress(buyerEmailAddress)
                .build();
        Order savedOrder = this.orderDatabaseGatewayMysql.saveOrder(orderToSave);
        OrderDTO expectedOrderDTO = OrderDTO.mapFromEntity(savedOrder);
        ResponseEntity<OrderDTO> orderDTOResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/orders/" + savedOrder.getId(),
                OrderDTO.class
        );
        System.out.println(orderDTOResponseEntity);
        assertNotNull(orderDTOResponseEntity.getBody());
        assertThat(orderDTOResponseEntity.getBody())
                .usingRecursiveComparison()
                .isEqualTo(expectedOrderDTO);
    }

    @KafkaListener(topics = "order_placed", groupId = "test-group")
    public void listenOrderPlacedEvent(String message) throws JsonProcessingException {
        this.publishedOrderPlacedEvent = mapper.readValue(message, OrderPlacedEvent.class);
    }
}