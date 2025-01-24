package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile contactImage,String fileName);

    String getUrlFromPublicId(String publicId);

}
