package com.astrovis.astrovisbackend.Controller;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class TileProxyController {




    private RestTemplate restTemplate = new RestTemplate();
    private static final String IMAGE_FORMAT_PNG = "png";
    private static final String IMAGE_FORMAT_JPG = "jpg";

    private Pair<Integer,BufferedImage> downloadImage(String url){


        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
                return Pair.of(HttpStatus.OK.value(),ImageIO.read(bis));
            }
            return Pair.of(response.getStatusCode().value(),null);
        } catch (Exception e) {
            //System.err.println("Failed to download image: " + url);
        }

        return Pair.of(null,null);
    }


    private byte[] mergeTiles(BufferedImage left,BufferedImage right,String format){
        BufferedImage merged = new BufferedImage(right.getWidth() + left.getWidth(),
                right.getHeight(),
                right.getType());
        Graphics2D graphics2D = merged.createGraphics();
        graphics2D.drawImage(left,0,0,null);
        graphics2D.drawImage(right,left.getWidth(),0,null);
        graphics2D.dispose();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            ImageIO.write(merged,format,buffer);
            return buffer.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }




    @GetMapping("/tileProxy/getTile")
    public ResponseEntity getTile(@RequestParam String proxyUrl,@RequestParam int z,@RequestParam int y,@RequestParam int x){

        String url = proxyUrl;

        if (StringUtils.isBlank(url)){
            // bad request
            return ResponseEntity.badRequest().body(null);
        }
        String format = url.substring(url.lastIndexOf(".")+1);
        Pair<Integer,BufferedImage> left = downloadImage(url.replace("{z}/{y}/{x}",String.format("%d/%d/%d",z,y,2*x)));
        Integer status = left.getLeft();
        if (status == null){
            return ResponseEntity.internalServerError().build();
        }
        if (status!=HttpStatus.OK.value()){
            return ResponseEntity.status(status).build();
        }
        Pair<Integer,BufferedImage> right = downloadImage(url.replace("{z}/{y}/{x}",String.format("%d/%d/%d",z,y,2*x+1)));
        status = right.getLeft();
        if (status == null){
            return ResponseEntity.internalServerError().build();
        }
        if (status!=HttpStatus.OK.value()){
            return ResponseEntity.status(status).build();
        }

        byte[] mergedTileBuffer = mergeTiles(left.getRight(),right.getRight(),format);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mergedTileBuffer);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = format.equalsIgnoreCase(IMAGE_FORMAT_PNG)?MediaType.IMAGE_PNG:MediaType.IMAGE_JPEG;
        headers.setContentType(mediaType);
        return new ResponseEntity<>(new InputStreamResource(byteArrayInputStream), headers, HttpStatus.OK);
    }
}
