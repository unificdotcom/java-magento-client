package com.github.chen0040.magento;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.magento.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 */
public class MagentoClientProductUnitTest {

   private static final Logger logger = LoggerFactory.getLogger(MagentoClientProductUnitTest.class);
   @Test
   public void test_login_client(){
      MagentoClient client = new MagentoClient(Mediator.url);
      String token = client.loginAsClient(Mediator.customerUsername, Mediator.customerPassword);
      logger.info("my account:\r\n{}", JSON.toJSONString(client.getMyAccount(), SerializerFeature.PrettyFormat));
      logger.info("product types:\r\n{}", JSON.toJSONString(client.products().listProductTypes(), SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_list_product(){
      MagentoClient client = new MagentoClient(Mediator.url);
      String token = client.loginAsAdmin(Mediator.adminUsername, Mediator.adminPassword);
      logger.info("account with id = 1: {}", client.getAccountById(1));
      logger.info("product types: \r\n{}", JSON.toJSONString(client.products().listProductTypes(), SerializerFeature.PrettyFormat));

      ProductPage page  = client.products().page(0, 10);
      logger.info("product page: \r\n{}", JSON.toJSONString(page, SerializerFeature.PrettyFormat));
      Product p1 = page.getItems().get(0);
      Product p2 = client.products().getProductBySku(p1.getSku());
      logger.info("product:\r\n{}", JSON.toJSONString(p2, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_get_product(){
      MagentoClient client = new MagentoClient(Mediator.url);
      client.loginAsAdmin(Mediator.adminUsername, Mediator.adminPassword);

      Product p1 = client.products().getProductBySku("B201-SKU");
      logger.info("product:\r\n{}", JSON.toJSONString(p1, SerializerFeature.PrettyFormat));
      Product p2 = client.products().getProductBySku("B202-SKU");
      logger.info("product:\r\n{}", JSON.toJSONString(p2, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_delete_product(){
      MagentoClient client = new MagentoClient(Mediator.url);
      client.loginAsAdmin(Mediator.adminUsername, Mediator.adminPassword);

      String sku = "B203-SKU";
      logger.info("product exists ? {}", client.products().hasProduct(sku));
      logger.info("client.deleteProduct(sku): {}", client.products().deleteProduct(sku));
      logger.info("product exists ? {}", client.products().hasProduct(sku));
   }

   @Test
   public void test_list_product_attribute_types() {
      MagentoClient client = new MagentoClient(Mediator.url);
      client.loginAsAdmin(Mediator.adminUsername, Mediator.adminPassword);

      List<MagentoAttributeType> attributeTypes = client.products().getProductAttributeTypes();
      logger.info("product attribute types:\r\n{}", JSON.toJSONString(attributeTypes, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_list_product_attributes() {
      MagentoClient client = new MagentoClient(Mediator.url);
      client.loginAsAdmin(Mediator.adminUsername, Mediator.adminPassword);

      ProductAttributePage page = client.products().getProductAttributes(0,10);
      logger.info("product attribute types:\r\n{}", JSON.toJSONString(page, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_add_product() {
      MagentoClient client = new MagentoClient(Mediator.url);
      client.loginAsAdmin(Mediator.adminUsername, Mediator.adminPassword);

      String sku = "B203-SKU";
      if(client.products().hasProduct(sku)) {
         logger.info("Deleting {}", sku);
         client.products().deleteProduct(sku);
         try {
            Thread.sleep(3000L);
         }
         catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

      Product product = new Product();
      product.setSku(sku);
      product.setName("B203");
      product.setPrice(30.00);
      product.setStatus(1);
      product.setType_id("simple");
      product.setAttribute_set_id(4);
      product.setWeight(1);
      product.setVisibility(Product.VISIBILITY_BOTH);
      product.setStatus(Product.STATUS_ENABLED);

      logger.info("add product result: {}", JSON.toJSONString(client.products().saveProduct(product), SerializerFeature.PrettyFormat));
   }


}
