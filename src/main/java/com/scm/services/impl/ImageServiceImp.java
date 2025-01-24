package com.scm.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstant;
import com.scm.services.ImageService;


@Service
public class ImageServiceImp implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImp(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage,String fileName) {
        // write code who is upload the image to the server and return a url


        try {
            byte[] data=new byte[contactImage.getInputStream().available()];

            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id",fileName
            ));
            

            return this.getUrlFromPublicId(fileName);
        } catch (IOException e) {
            
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url().transformation(
            new Transformation<>()
            .width(AppConstant.Contact_Image_Width)
            .height(AppConstant.Contact_Image_Height)
            .crop(AppConstant.Conatct_Image_Fill)
        ).generate(publicId);
    }
    
}
